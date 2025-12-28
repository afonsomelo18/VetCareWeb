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
        String nif = request.getParameter("nif");
        if (nif != null && !nif.isEmpty()) {
            request.getSession().setAttribute("nifCliente", nif);
            response.sendRedirect("portalCliente");
        } else {
            response.sendRedirect("view/cliente/LoginCliente.jsp?msg=NifInvalido");
        }
    }
}