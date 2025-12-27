package servlet;

import dao.PacienteDAO;
import dao.TutorDAO;
import model.FichaMedica;
import model.Paciente;
import model.Tutor;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.nio.file.Paths;

@WebServlet("/gestaoCliente")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class GestaoClienteServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String acao = request.getParameter("acao"); // "novoTutor" ou "novoAnimal"
        
        try {
            if ("novoTutor".equals(acao)) {
                criarTutor(request, response);
            } else if ("novoAnimal".equals(acao)) {
                criarAnimal(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().write("Erro de BD: " + e.getMessage());
        }
    }

    private void criarTutor(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        // Recolher dados do form
        String nif = request.getParameter("nif");
        String nome = request.getParameter("nome");
        String contacto = request.getParameter("contacto");
        
        // Criar objeto e salvar
        Tutor t = new Tutor(nif, contacto, "Português", "pessoa", 1, 1, nome);
        new TutorDAO().save(t);
        
        // Redirecionar
        response.sendRedirect("view/rececionista/Rececionista.jsp?msg=TutorCriado");
    }

    private void criarAnimal(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException, ServletException {
        
        String nifTutor = request.getParameter("nifTutor");
        String nomeAnimal = request.getParameter("nomeAnimal");
        String especie = request.getParameter("especie");
        String raca = request.getParameter("raca");
        String sexo = request.getParameter("sexo");
        Date dataNasc = Date.valueOf(request.getParameter("dataNasc"));
        
        // --- TRATAMENTO DA FOTO ---
        Part filePart = request.getPart("foto"); // input type="file" name="foto"
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String caminhoFotoDB = "";

        if (fileName != null && !fileName.isEmpty()) {
            // Caminho para guardar: uploads/animais/
            String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads" + File.separator + "animais";
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdirs();

            // Guardar ficheiro no disco
            String filePath = uploadPath + File.separator + fileName;
            filePart.write(filePath);
            
            // Caminho relativo para guardar na BD
            caminhoFotoDB = "uploads/animais/" + fileName;
        }

        // Criar Modelos
        Paciente p = new Paciente(0, especie, raca, nifTutor, 1, 0);
        // Nota: FichaMedica precisa de construtor compatível ou usar setters
        FichaMedica f = new FichaMedica(0, sexo, nomeAnimal, dataNasc, "ativo", "Nenhuma", 0.0, "N/A", "N/A", caminhoFotoDB, "");

        // Salvar na BD (Transação)
        new PacienteDAO().insertCompleto(p, f);

        response.sendRedirect("view/rececionista/Rececionista.jsp?msg=AnimalCriado");
    }
}