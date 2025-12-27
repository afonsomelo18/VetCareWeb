package servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import dao.AgendamentoDAO;
import dao.PacienteDAO;
import dao.ServicoDAO;
import model.Agendamento;
import model.FichaMedica;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/consultarAgenda")
public class ConsultarAgendaServlet extends HttpServlet{
	private AgendamentoDAO agendamentoDAO;
    private PacienteDAO pacienteDAO;
    private ServicoDAO servicoDAO;
    
    @Override
    public void init() {
    	agendamentoDAO = new AgendamentoDAO();
        pacienteDAO = new PacienteDAO();
        servicoDAO = new ServicoDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
    	
    	String numLicenca = "COI555";
    	
    	response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
        	if(numLicenca == null || numLicenca.isEmpty()) {
        		out.println("<tr><td colspan='3'>Erro: Licença do veterinário não fornecida.</td></tr>");
                return;
        	}
        	
        	// Chama o teu método que faz o JOIN com a localidade do veterinário
            List<Agendamento> agenda = agendamentoDAO.findAgendaByVet(numLicenca);
            
            if(agenda.isEmpty()) {
            	out.println("<tr><td colspan='3'>Não existem agendamentos para este veterinário.</td></tr>");
            }
            else {
            	for(Agendamento ag: agenda) {
            		// Lógica para obter o nome do animal sem alterar o Model
            		FichaMedica ficha = pacienteDAO.findFichaById(ag.getId_paciente());
            		String nomePaciente = (ficha != null) ? ficha.getNome() : "ID: " + ag.getId_paciente();
            		String tipoServico = servicoDAO.findTipoServicoById(ag.getId_servico());
            		
            		// Formatação da data para algo mais legível (ex: dd/MM/yyyy HH:mm)
            		String dataFormatada = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm")
                            .format(ag.getData_hora());
            		
            		// Gerar as linhas da tabela 
                    out.println("<tr data-idagendamento='" + ag.getId_agendamento() + "' data-idservico='" + ag.getId_servico() + "'>");
                    out.println("  <td>" + dataFormatada + "</td>");
                    out.println("  <td>" + tipoServico + "</td>");
                    out.println("  <td>" + nomePaciente + "</td>");
                    out.println("</tr>");
            	}
            }
        }
        catch (SQLException e) {
        	out.println("<tr><td colspan='3'>Erro ao carregar agenda: " + e.getMessage() + "</td></tr>");
            e.printStackTrace();
        }
    }
}
