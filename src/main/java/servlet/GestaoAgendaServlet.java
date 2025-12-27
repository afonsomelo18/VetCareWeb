package servlet;

import dao.AgendamentoDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/agendamentoAcao")
public class GestaoAgendaServlet extends HttpServlet {

    private AgendamentoDAO dao;

    @Override
    public void init() {
        dao = new AgendamentoDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String acao = request.getParameter("acao");
        String idStr = request.getParameter("idAgendamento");

        try {
            if (idStr == null || acao == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            int idAgendamento = Integer.parseInt(idStr);

            if ("cancelar".equals(acao)) {
                dao.cancelar(idAgendamento);
            } 
            else if ("reagendar".equals(acao)) {
                String novaDataStr = request.getParameter("novaData"); // Formato: 2023-12-31T15:30
                if (novaDataStr != null && !novaDataStr.isEmpty()) {
                    // Converte a string do input datetime-local para Timestamp
                    java.time.LocalDateTime ldt = java.time.LocalDateTime.parse(novaDataStr);
                    java.sql.Timestamp ts = java.sql.Timestamp.valueOf(ldt);
                    dao.reagendar(idAgendamento, ts);
                }
            }
            
            // Retorna sucesso para o JavaScript
            response.getWriter().write("ok");

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            response.getWriter().write("Erro: " + e.getMessage());
        }
    }
}
