package servlet;

import dao.TutorDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Tutor;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;


/**
 * Servlet responsável por fornecer dados para o controlo de autocomplete do nome do tutor(2.1)
 * 
 * Recebe um parâmetro de pesquisa e devolve uma resposta em formato JSON.
 */
@WebServlet("/autocompleteTutor")
public class TutorAutocompleteServlet extends HttpServlet {
	
	
	/*
	 * Processa pedidos GET para pesquisa de tutores por nome.
	 * 
	 * @param request  pedido HTTP
     * @param response resposta HTTP
     * @throws ServletException em caso de erro do servlet
     * @throws IOException      em caso de erro de escrita na resposta
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String termo = request.getParameter("term");
        TutorDAO tutorDAO = new TutorDAO();
        PrintWriter out = response.getWriter();
        
        try {
        	List<Tutor> tutores = tutorDAO.findByName(termo);
        	out.print("[");
            for (int i = 0; i < tutores.size(); i++) {
                Tutor t = tutores.get(i);

                out.print("{");
                out.print("\"nif\":\"" + t.getNif() + "\",");
                out.print("\"nome\":\"" + t.getNome() + "\"");
                out.print("}");

                if (i < tutores.size() - 1) {
                    out.print(",");
                }
            }
            out.print("]");
        }
        catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"erro\":\"Erro ao aceder à base de dados\"}");
        }
        out.flush();
	}
}
