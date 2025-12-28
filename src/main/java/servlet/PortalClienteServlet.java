package servlet;

import dao.AgendamentoDAO;
import dao.PacienteDAO;
import model.Agendamento;
import model.FichaMedica;
import model.Paciente;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import dao.ServicoDAO;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import java.io.IOException;
import java.util.List;

@WebServlet("/portalCliente")
public class PortalClienteServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String acao = request.getParameter("acao");
        HttpSession session = request.getSession();
        String nif = (String) session.getAttribute("nifCliente");

        // Verificação de Login
        if (nif == null && !"login".equals(acao)) {
            response.sendRedirect("view/cliente/LoginCliente.jsp");
            return;
        }

        try {
            PacienteDAO pacienteDAO = new PacienteDAO();
            AgendamentoDAO agendamentoDAO = new AgendamentoDAO();

            if ("detalhes".equals(acao)) {
                // --- CENÁRIO 1: VER DETALHES DO ANIMAL ---
                int idAnimal = Integer.parseInt(request.getParameter("idAnimal"));
                
                // 1. Dados do Paciente
                Paciente p = pacienteDAO.findById(idAnimal);
                request.setAttribute("paciente", p);
                
                // 2. Ficha Médica
                FichaMedica fm = pacienteDAO.findFichaById(idAnimal);
                request.setAttribute("ficha", fm);
                
                // 3. Agenda Geral (Mantemos esta para filtrar os Futuros/Marcados no JSP)
                List<Agendamento> agendamentos = agendamentoDAO.getByPaciente(idAnimal);
                request.setAttribute("agenda", agendamentos);
                
                // 4. HISTÓRICO CLÍNICO (NOVO)
                // Chamada específica ao método que retorna apenas os "concluídos" ordenados
                List<Agendamento> historico = agendamentoDAO.findHistoricoByPaciente(idAnimal);
                request.setAttribute("historico", historico);
                
                request.getRequestDispatcher("view/cliente/HistoricoAnimal.jsp").forward(request, response);

            } else {
                // --- CENÁRIO 2: LISTAR MEUS ANIMAIS ---
                List<FichaMedica> minhasFichas = pacienteDAO.findFichaByTutor(nif);
                request.setAttribute("minhasFichas", minhasFichas);
                request.getRequestDispatcher("view/cliente/MeusAnimais.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("view/cliente/LoginCliente.jsp?msg=ErroBD");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        String acao = request.getParameter("acao");
        HttpSession session = request.getSession();
        
        // --- 1. TENTATIVA DE LOGIN (PRIORIDADE MÁXIMA) ---
        // Verificamos primeiro se o formulário enviou um NIF
        String nifFormulario = request.getParameter("nif");
        
        if (nifFormulario != null && !nifFormulario.isEmpty()) {
            // É um login! Guardar na sessão e entrar.
            session.setAttribute("nifCliente", nifFormulario);
            response.sendRedirect("portalCliente"); // Vai para o doGet carregar a lista
            return; // Importante: Parar por aqui
        }

        // --- 2. VERIFICAÇÃO DE SEGURANÇA ---
        // Se não é login, o utilizador JÁ TEM de estar na sessão para fazer outras coisas (agendar, etc.)
        String nifSessao = (String) session.getAttribute("nifCliente");
        
        if (nifSessao == null) {
            // Se não tem sessão e não está a tentar logar, expulsa-o
            response.sendRedirect("view/cliente/LoginCliente.jsp");
            return;
        }

        // --- 3. PROCESSAR AÇÕES (Agendar, Reagendar, Cancelar) ---
        try {
            AgendamentoDAO agendamentoDAO = new AgendamentoDAO();
            ServicoDAO servicoDAO = new ServicoDAO();

            if ("agendar".equals(acao)) {
                int idPaciente = Integer.parseInt(request.getParameter("idPaciente"));
                String dataStr = request.getParameter("dataHora");
                String motivo = request.getParameter("motivo");
                
                LocalDateTime ldt = LocalDateTime.parse(dataStr);
                Timestamp dataHora = Timestamp.valueOf(ldt);
                int diaSemana = ldt.getDayOfWeek().getValue(); 

                // CORRETO (Tem de ser 'consulta' minúsculo):
                int idServico = servicoDAO.createServicoSolicitado("consulta", diaSemana);

                if (idServico > 0) {
                    Agendamento a = new Agendamento();
                    a.setId_paciente(idPaciente);
                    a.setId_servico(idServico);
                    a.setLocalidade("Lisboa-Arroios");
                    a.setDia_semana(diaSemana);
                    a.setNif_tutor(nifSessao); // Usa o NIF da sessão
                    a.setData_hora(dataHora);
                    a.setObs(motivo);
                    
                    agendamentoDAO.insert(a);
                }
                response.sendRedirect("portalCliente?acao=detalhes&idAnimal=" + idPaciente);

            } else if ("reagendar".equals(acao)) {
                int idAgendamento = Integer.parseInt(request.getParameter("idAgendamento"));
                int idPaciente = Integer.parseInt(request.getParameter("idPaciente"));
                String novaDataStr = request.getParameter("novaData");

                LocalDateTime ldt = LocalDateTime.parse(novaDataStr);
                Timestamp novaData = Timestamp.valueOf(ldt);

                agendamentoDAO.reagendar(idAgendamento, novaData);
                
                response.sendRedirect("portalCliente?acao=detalhes&idAnimal=" + idPaciente);

            } else if ("cancelar".equals(acao)) {
                int idAgendamento = Integer.parseInt(request.getParameter("idAgendamento"));
                int idPaciente = Integer.parseInt(request.getParameter("idPaciente"));

                agendamentoDAO.cancelar(idAgendamento);
                
                response.sendRedirect("portalCliente?acao=detalhes&idAnimal=" + idPaciente);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("view/cliente/MeusAnimais.jsp?msg=ErroOperacao");
        }
    }
}