package servlet;

import dao.AgendamentoDAO;
import dao.ServicoDAO;
import model.Agendamento;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@WebServlet("/novoAgendamento")
public class NovoAgendamentoServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // 1. Recolher dados do formulário
            String nifTutor = request.getParameter("nifTutor");
            int idAnimal = Integer.parseInt(request.getParameter("idAnimal"));
            String tipoServico = request.getParameter("tipoServico");
            String dataHoraStr = request.getParameter("dataHora"); // ex: "2024-01-30T10:00"
            String numLicenca = request.getParameter("veterinario"); // Vem do select
            
            // --- CORREÇÃO: Definir a localidade certa baseada no médico ---
            String localidade = "Lisboa-Arroios"; // Valor por defeito

            if ("COI555".equals(numLicenca)) {
                localidade = "Coimbra-SeNova";
            } else if ("PRT789".equals(numLicenca)) {
                localidade = "Porto-Bonfim";
            } else if ("LIS123".equals(numLicenca)) {
                localidade = "Lisboa-Arroios";
            }
            // -------------------------------------------------------------
            
            // Tratamento da Data
            LocalDateTime ldt = LocalDateTime.parse(dataHoraStr);
            Timestamp dataHora = Timestamp.valueOf(ldt);
            int diaSemana = ldt.getDayOfWeek().getValue(); // 1 (Seg) a 7 (Dom)

            // 2. Criar o SERVICO primeiro (Tabela Pai)
            ServicoDAO servicoDAO = new ServicoDAO();
            int idServico = servicoDAO.insert(tipoServico, numLicenca, localidade, diaSemana);
            
            // 3. Criar o AGENDAMENTO (Tabela Filho)
            Agendamento ag = new Agendamento(0, idAnimal, idServico, localidade, diaSemana, nifTutor, dataHora, "marcado", "Agendado via Web");
            new AgendamentoDAO().insert(ag);
            
            // Sucesso
            response.sendRedirect("view/rececionista/Rececionista.jsp?msg=AgendadoComSucesso");

        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            response.getWriter().write("Erro ao agendar: " + e.getMessage());
        }
    }
}