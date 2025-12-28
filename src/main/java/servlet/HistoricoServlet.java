package servlet;

import dao.AgendamentoDAO;
import dao.PacienteDAO;
import model.Agendamento;
import model.Paciente;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/verHistorico")
public class HistoricoServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Receber o ID do animal
        String idStr = request.getParameter("idPaciente");
        
        if (idStr == null || idStr.isEmpty()) {
            response.getWriter().println("Erro: ID do paciente em falta.");
            return;
        }

        try {
            int idPaciente = Integer.parseInt(idStr);
            
            // 2. Buscar dados do Animal (para mostrar o nome no topo)
            PacienteDAO pacienteDAO = new PacienteDAO();
            Paciente p = pacienteDAO.findById(idPaciente);
            request.setAttribute("paciente", p);

            // 3. CHAMADA AO MÉTODO QUE PEDISTE (HISTÓRICO)
            AgendamentoDAO agendamentoDAO = new AgendamentoDAO();
            List<Agendamento> historico = agendamentoDAO.findHistoricoByPaciente(idPaciente);
            
            // 4. Guardar na memória para o JSP usar
            request.setAttribute("listaHistorico", historico);
            
            // 5. Enviar para a página de visualização
            request.getRequestDispatcher("view/vet/HistoricoClinico.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Erro ao carregar histórico: " + e.getMessage());
        }
    }
}