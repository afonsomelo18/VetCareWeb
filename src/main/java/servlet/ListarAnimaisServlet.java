package servlet;

import dao.AgendamentoDAO;
import dao.PacienteDAO;
import model.Agendamento;
import model.FichaMedica;
import model.Progenitor;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Servlet responsável por listar os animais de um tutor,
 * apresentando a ficha médica e o respetivo histórico clínico.
 *
 * O histórico clínico é construído a partir dos agendamentos
 * realizados associados a cada paciente.
 */
@WebServlet("/listarAnimais")
public class ListarAnimaisServlet extends HttpServlet {

    private PacienteDAO pacienteDAO;
    private AgendamentoDAO agendamentoDAO;

    @Override
    public void init() {
        pacienteDAO = new PacienteDAO();
        agendamentoDAO = new AgendamentoDAO();
    }

    /**
     * Processa pedidos GET para listar os animais associados a um tutor.
     *
     * @param request  pedido HTTP
     * @param response resposta HTTP com HTML gerado dinamicamente
     * @throws ServletException erro do servlet
     * @throws IOException erro de escrita na resposta
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nif = request.getParameter("nif");

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            List<FichaMedica> fichas = pacienteDAO.findFichaByTutor(nif);

            if (fichas.isEmpty()) {
                out.println("<p>Este tutor não tem animais registados.</p>");
                return;
            }

            for (FichaMedica ficha : fichas) {

                int idPaciente = ficha.getIdPaciente();

                String foto = ficha.getFoto();
                String caminhoFoto;

                if (foto == null || foto.isEmpty()) {
                    caminhoFoto = request.getContextPath() + "/images/animais/default.png";
                } else {
                    caminhoFoto = request.getContextPath() + "/" + foto;
                }

                String idade = calcularIdade(ficha.getData_nasc());
                String escalao = determinarEscalaoEtario(ficha.getData_nasc());

                out.println("<div style='border:1px solid #ccc; padding:10px; margin-bottom:15px;'>");

                out.println("<div style='display:flex; align-items:center; gap:15px;'>");

                out.println("<img src='" + caminhoFoto +
                            "' width='100' height='100' " +
                            "style='object-fit:cover; border-radius:6px;'>");

                out.println("<div>");
                out.println("<h3 style='margin:0;'>🐾 " + ficha.getNome() + "</h3>");
                out.println("<p style='margin:4px 0;'>Idade: " + idade +
                            " | Escalão: <strong>" + escalao + "</strong></p>");
                out.println("</div>");

                out.println("</div>");


                // Ficha médica
                out.println("<table border='1' cellpadding='5'>");
                out.println("<tr><th>Sexo</th><td>" + ficha.getSexo() + "</td></tr>");
                out.println("<tr><th>Estado reprodutivo</th><td>" + ficha.getEstadoReprodutivo() + "</td></tr>");
                out.println("<tr><th>Alergias</th><td>" + ficha.getAlergias() + "</td></tr>");
                out.println("<tr><th>Peso</th><td>" + ficha.getPeso() + " kg</td></tr>");
                out.println("<tr><th>Cor</th><td>" + ficha.getCor() + "</td></tr>");
                out.println("<tr><th>Características</th><td>" + ficha.getCaract() + "</td></tr>");
                out.println("</table>");

                // Histórico clínico (colapsável)
                List<Agendamento> historico =
                        agendamentoDAO.findHistoricoByPaciente(idPaciente);

                out.println("<details style='margin-top:10px;'>");
                out.println("<summary><strong>Histórico clínico (" + historico.size() + " registos)</strong></summary>");

                if (historico.isEmpty()) {
                    out.println("<p>Sem registos clínicos.</p>");
                } else {
                    out.println("<table border='1' cellpadding='5'>");
                    out.println("<tr>");
                    out.println("<th>Data/Hora</th>");
                    out.println("<th>Estado</th>");
                    out.println("<th>Observações</th>");
                    out.println("</tr>");

                    for (Agendamento ag : historico) {
                        out.println("<tr>");
                        out.println("<td>" + ag.getData_hora() + "</td>");
                        out.println("<td>" + ag.getEstado() + "</td>");
                        out.println("<td>" + ag.getObs() + "</td>");
                        out.println("</tr>");
                    }

                    out.println("</table>");
                }

                out.println("</details>");
                
             // --- SECÇÃO DA ÁRVORE (CORREÇÃO DE LÓGICA) ---
                out.println("<div class='seccao-genealogica' style='margin-top:15px; padding-top:10px; border-top:1px dashed #eee;'>");
                out.println("<h4>🌳 Árvore Genealógica</h4>");
                
                // Chamada ao DAO (Isso funciona mesmo que o pai seja de outro tutor)
                List<Progenitor> pais = pacienteDAO.getProgenitoresPorFilho(idPaciente);

                if (pais.isEmpty()) {
                    out.println("<p style='color:gray;'><em>Sem progenitores registados para este animal.</em></p>");
                } else {
                    // Bloco dos Pais
                    out.print("<p><strong>Pais:</strong> ");
                    for (int i = 0; i < pais.size(); i++) {
                        Progenitor p = pais.get(i);
                        
                        // BUSCA A FICHA DO PAI PARA OBTER O NOME
                        FichaMedica fichaPai = pacienteDAO.findFichaById(p.getPaciente().getIdPaciente()); 
                        String nomePai = (fichaPai != null) ? fichaPai.getNome() : "ID " + p.getPaciente().getIdPaciente();
                        
                        out.print(nomePai + " (" + p.getTipo() + ")");
                        if (i < pais.size() - 1) out.print(", ");

                        // Procurar os AVÓS
                        List<Progenitor> avosDesteProgenitor = pacienteDAO.getProgenitoresPorFilho(p.getPaciente().getIdPaciente()); //
                        p.getPaciente().setProgenitores(avosDesteProgenitor);
                    }
                    out.println("</p>");

                    // Bloco dos Avós
                    out.print("<p><strong>Avós:</strong> ");
                    boolean temAvos = false;
                    for (Progenitor p : pais) {
                        for (Progenitor avo : p.getPaciente().getProgenitores()) {
                            
                        	// BUSCA A FICHA DO AVÔ PARA OBTER O NOME
                            FichaMedica fichaAvo = pacienteDAO.findFichaById(avo.getPaciente().getIdPaciente()); //
                            String nomeAvo = (fichaAvo != null) ? fichaAvo.getNome() : "ID " + avo.getPaciente().getIdPaciente();
                            
                            out.print(nomeAvo + " (via " + p.getTipo() + ") ");
                            temAvos = true;
                            
                        }
                    }
                    if (!temAvos) out.print("<span style='color:gray;'>Informação de avós não disponível</span>");
                    out.println("</p>");
                }
                out.println("</div>"); // Fecha seccao-genealogica
                out.println("</div>");
            }

        } catch (SQLException e) {
            out.println("<p>Erro ao aceder à base de dados.</p>");
            e.printStackTrace();
        }
    }
    
    
    /**
     * Calcula a idade do animal em função da data de nascimento.
     *
     * @param dataNascimento data de nascimento do animal
     * @return texto com a idade expressa na unidade mais adequada
     */
    private String calcularIdade(java.sql.Date dataNascimento) {

        LocalDate nascimento = dataNascimento.toLocalDate();
        LocalDate hoje = LocalDate.now();

        Period periodo = Period.between(nascimento, hoje);

        if (periodo.getYears() >= 1) {
            return periodo.getYears() + " anos";
        }

        if (periodo.getMonths() >= 1) {
            return periodo.getMonths() + " meses";
        }

        long dias = ChronoUnit.DAYS.between(nascimento, hoje);

        if (dias >= 7) {
            return (dias / 7) + " semanas";
        }

        return dias + " dias";
    }

    /**
     * Determina o escalão etário do animal com base na idade em dias.
     *
     * @param dataNascimento data de nascimento do animal
     * @return escalão etário (bebé, jovem, adulto ou idoso)
     */
    private String determinarEscalaoEtario(java.sql.Date dataNascimento) {

        long dias = ChronoUnit.DAYS.between(
                dataNascimento.toLocalDate(),
                LocalDate.now()
        );

        if (dias < 180) {          // < 6 meses
            return "Bebé";
        } else if (dias < 365 * 2) { // < 2 anos
            return "Jovem";
        } else if (dias < 365 * 7) { // < 7 anos
            return "Adulto";
        } else {
            return "Idoso";
        }
    }

}
