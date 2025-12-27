package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.Duration;

import dao.*;
import model.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/iniciarServico")
public class IniciarServicoServlet extends HttpServlet{
	
	private ServicoDAO servicoDAO;
	private ConsultaDAO consultaDAO;
	private CirurgiaDAO cirurgiaDAO;
	private ExameDAO exameDAO;
	private Med_PrevDAO medDAO;
	private TerapeuticoDAO tDAO;
	private AgendamentoDAO agDAO;
	
	@Override
    public void init() {
        servicoDAO = new ServicoDAO();
        consultaDAO = new ConsultaDAO();
        cirurgiaDAO = new CirurgiaDAO();
        exameDAO = new ExameDAO();
        medDAO = new Med_PrevDAO();
        tDAO = new TerapeuticoDAO();
        agDAO = new AgendamentoDAO();
    }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		
		int idServico = Integer.parseInt(request.getParameter("idServico"));
        int idAgendamento = Integer.parseInt(request.getParameter("idAgendamento"));
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
        	// Procurar o tipo de serviço (consulta, cirurgia, exame, etc.)
        	String tipo = servicoDAO.findTipoServicoById(idServico);
        	
        	out.println("<h2>Registo Clínico: " + tipo.toUpperCase() + "</h2>");
            out.println("<form action='iniciarServico' method='POST'>");
            
            // Campos ocultos para manter a referência
            out.println("<input type='hidden' name='idServico' value='" + idServico + "'>");
            out.println("<input type='hidden' name='idAgendamento' value='" + idAgendamento + "'>");
            out.println("<input type='hidden' name='tipoServico' value='" + tipo + "'>");
            
            // Gerar campos dependendo do tipo de serviço
            switch (tipo.toLowerCase()) {
            case "consulta":
            	gerarCamposConsulta(out);
            	break;
            case "cirurgia":
                gerarCamposCirurgia(out);
                break;
            case "exame":
                gerarCamposExame(out);
                break;
            case "terapeutico":
                gerarCamposTerapeutico(out);
                break;
            case "med_prev":
                gerarCamposMedPrev(out);
                break;
            default:
                out.println("<p>Tipo de serviço não reconhecido.</p>");
            }
            out.println("<br><button type='submit'>Finalizar e Salvar</button>");
            out.println("</form>");
        }
        catch(SQLException e) {
        	out.println("<p>Erro ao carregar dados do serviço.</p>");
        }
	}
	
	
	// Métodos auxiliares para organizar o HTML de cada aba
	private void gerarCamposConsulta(PrintWriter out) {
        out.println("<label>Motivo:</label><br><textarea name='motivo'></textarea><br>");
        out.println("<label>Diagnóstico:</label><br><textarea name='diagnostico'></textarea><br>");
        out.println("<label>Medicação:</label><br><input type='text' name='medicacao'><br>");
    }
	
	private void gerarCamposCirurgia(PrintWriter out) {
        out.println("<label>Descrição:</label><br><textarea name='descricao'></textarea><br>");
        out.println("<label>Duração (minutos):</label><br><input type='number' name='duracaoMinutos'><br>");
        out.println("<label>Procedimento:</label><br><textarea name='procedimento'></textarea><br>");
    }

    private void gerarCamposExame(PrintWriter out) {
        out.println("<label>Tipo de Exame:</label><br><input type='text' name='tipoExame'><br>");
        out.println("<label>Notas:</label><br><textarea name='notas'></textarea><br>");
    }

    private void gerarCamposTerapeutico(PrintWriter out) {
        out.println("<label>Tipo de Ferida:</label><br><input type='text' name='tipoFerida'><br>");
        out.println("<label>Cuidados:</label><br><textarea name='cuidados'></textarea><br>");
    }

    private void gerarCamposMedPrev(PrintWriter out) {
        out.println("<label>Produto:</label><br><input type='text' name='produto'><br>");
        out.println("<label>Fabricante:</label><br><input type='text' name='fabricante'><br>");
        out.println("<label>Próxima Aplicação:</label><br><input type='date' name='proxAplicacao'><br>");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException{
    	String tipoServico = request.getParameter("tipoServico");
        int idServico = Integer.parseInt(request.getParameter("idServico"));
        int idAgendamento = Integer.parseInt(request.getParameter("idAgendamento"));
        
        try {
        	// 1. Decidir qual DAO usar baseado no tipo de serviço
        	switch(tipoServico.toLowerCase()) {
        	case "consulta":
        		Consulta c = new Consulta(idServico, null, "ativo", tipoServico, null, null, 0,
                        request.getParameter("motivo"), 
                        request.getParameter("diagnostico"), 
                        request.getParameter("medicacao"));
        		consultaDAO.insert(c);
        		break;
        	case "cirurgia":
                Duration duracao = Duration.ofMinutes(Long.parseLong(request.getParameter("duracaoMinutos")));
                Cirurgia cir = new Cirurgia(idServico, null, "ativo", tipoServico, null, null, 0,
                                   request.getParameter("descricao"), 
                                   duracao, 
                                   request.getParameter("procedimento"));
                cirurgiaDAO.insert(cir);
                break;

            case "terapeutico":
                Terapeutico t = new Terapeutico(idServico, null, "ativo", tipoServico, null, null, 0,
                                       request.getParameter("tipoFerida"), 
                                       request.getParameter("cuidados"));
                tDAO.insert(t);
                break;
                
            case "exame":
            	Exame e = new Exame(idServico, null, "ativo", tipoServico, null, null, 0,
            			request.getParameter("tipo"),
            			request.getParameter("notas"));
            	exameDAO.insert(e);
            	break;
            case "med_prev":
            	// Converter a String da data para java.sql.Date
            	String dataString = request.getParameter("proxAplicacao");
            	java.sql.Date proxAplicacao = null;
                if (dataString != null && !dataString.isEmpty()) {
                    proxAplicacao = java.sql.Date.valueOf(dataString);
                }
                
            	Med_Prev m = new Med_Prev(idServico, null, "ativo", tipoServico, null, null, 0, 
            			request.getParameter("tipo"),
            			request.getParameter("produto"),
            			request.getParameter("fabricante"),
            			proxAplicacao);
            	medDAO.insert(m);
            	break;
        	}
        	// Mudar o estado do agendamento para concluído para ele sair da agenda
        	agDAO.concluir(idAgendamento);
        	response.sendRedirect("agenda.jsp?msg=Servico finalizado com sucesso");
        }
        catch(SQLException e) {
        	e.printStackTrace();
            response.getWriter().println("Erro ao salvar: " + e.getMessage());
        }
    }
}
