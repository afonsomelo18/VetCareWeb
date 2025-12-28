<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, model.Paciente, model.FichaMedica, model.Agendamento, java.util.Date" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Histórico Clínico</title>
<style>
    body { font-family: Arial, sans-serif; background-color: #fff; }
    .container { width: 80%; margin: auto; padding-bottom: 50px; }
    
    /* Cabeçalho do Animal */
    .header-box { 
        display: flex; background: #f4f4f4; padding: 30px; border-radius: 8px; 
        align-items: center; border-left: 5px solid #008CBA; 
    }
    .foto-grande { 
        width: 150px; height: 150px; object-fit: cover; border-radius: 8px; 
        margin-right: 30px; box-shadow: 0 0 10px rgba(0,0,0,0.1); 
    }
    
    /* Tabelas */
    h2 { margin-top: 40px; border-bottom: 2px solid #ddd; padding-bottom: 10px; }
    table { width: 100%; border-collapse: collapse; margin-top: 15px; }
    th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }
    th { background-color: #333; color: white; }
    tr:nth-child(even) { background-color: #f2f2f2; }
    
    .futuro-row { background-color: #e8f5e9 !important; } /* Verde claro */
    .tag-estado { padding: 4px 8px; border-radius: 4px; font-weight: bold; font-size: 0.9em; }
    .estado-marcado { background: #e3f2fd; color: #1565c0; }
    .estado-concluido { background: #e8f5e9; color: #2e7d32; }
    
    .btn-ver { 
        display: inline-block; padding: 10px 20px; text-decoration: none; 
        border-radius: 4px; font-weight: bold; color: white; 
    }
</style>
</head>
<body>
    <jsp:include page="/view/menu/Menu.jsp" />

    <div class="container">
        <%
            Paciente p = (Paciente) request.getAttribute("paciente");
            FichaMedica fm = (FichaMedica) request.getAttribute("ficha");
            
            // Lista Geral (usada para calcular Futuros)
            List<Agendamento> agenda = (List<Agendamento>) request.getAttribute("agenda");
            
            // Lista Específica de Histórico (Concluídos)
            List<Agendamento> historico = (List<Agendamento>) request.getAttribute("historico");
            
            Date hoje = new Date();
            
            // Tratamento da imagem
            String foto = request.getContextPath() + "/uploads/animais/default.png";
            if(fm != null && fm.getFoto() != null && !fm.getFoto().isEmpty()) {
                foto = request.getContextPath() + "/" + fm.getFoto();
            }
        %>

        <div class="header-box">
            <img src="<%= foto %>" class="foto-grande">
            <div>
                <h1 style="margin-top:0"><%= fm != null ? fm.getNome() : "Animal sem Nome" %></h1>
                <p><strong>Espécie:</strong> <%= p.getNomeEspecie() %> | <strong>Raça:</strong> <%= p.getNomeRaca() %></p>
                <p><strong>Sexo:</strong> <%= fm != null ? fm.getSexo() : "-" %> | <strong>Peso:</strong> <%= fm != null ? fm.getPeso() : "0" %> kg</p>
                
                <% if(fm != null && fm.getAlergias() != null && !fm.getAlergias().isEmpty()) { %>
                    <p style="color:red; font-weight:bold;">⚠️ Alergias: <%= fm.getAlergias() %></p>
                <% } %>
            </div>
        </div>

        <% if (fm != null) { %>
            <div style="margin: 20px 0; text-align: right;">
                <a href="view/cliente/Agendar.jsp?tipo=agendar&idPaciente=<%= p.getIdPaciente() %>" 
                   class="btn-ver" style="background-color: #008CBA;">
                   ➕ Marcar Nova Consulta
                </a>
            </div>
        <% } %>

        <h2>📅 Próximos Agendamentos</h2>
        <table>
            <tr>
                <th>Data/Hora</th>
                <th>Serviço</th>
                <th>Localidade</th>
                <th>Estado</th>
                <th>Ações</th> </tr>
            <% 
            boolean temFuturo = false;
            if(agenda != null) {
                for(Agendamento a : agenda) {
                    // Mostrar apenas agendamentos futuros e ativos (não cancelados/concluídos)
                    if(a.getData_hora().after(hoje) && 
                       !a.getEstado().equalsIgnoreCase("cancelado") && 
                       !a.getEstado().equalsIgnoreCase("concluido")) { 
                        temFuturo = true;
            %>
                <tr class="futuro-row">
                    <td><%= a.getData_hora() %></td>
                    <td><%= a.getObs() %></td> 
                    <td><%= a.getLocalidade() %></td>
                    <td><span class="tag-estado estado-marcado"><%= a.getEstado() %></span></td>
                    <td>
                        <a href="view/cliente/Agendar.jsp?tipo=reagendar&idPaciente=<%= p.getIdPaciente() %>&idAgendamento=<%= a.getId_agendamento() %>" 
                           style="color: blue; margin-right: 15px; font-weight:bold; text-decoration:none;">Reagendar</a>
                        
                        <form action="portalCliente" method="post" style="display:inline;" onsubmit="return confirm('Tem a certeza que deseja cancelar esta consulta?');">
                            <input type="hidden" name="acao" value="cancelar">
                            <input type="hidden" name="idPaciente" value="<%= p.getIdPaciente() %>">
                            <input type="hidden" name="idAgendamento" value="<%= a.getId_agendamento() %>">
                            <button type="submit" style="background:none; border:none; color:red; cursor:pointer; font-weight:bold; text-decoration:underline;">Cancelar</button>
                        </form>
                    </td>
                </tr>
            <%      }
                }
            } 
            if(!temFuturo) { %>
                <tr><td colspan="5" style="text-align:center; color:gray;">Sem consultas agendadas.</td></tr>
            <% } %>
        </table>

        <h2>📂 Histórico Clínico</h2>
        <table>
            <tr><th>Data</th><th>Procedimento</th><th>Observações / Diagnóstico</th></tr>
            <% 
            if(historico != null && !historico.isEmpty()) {
                for(Agendamento a : historico) {
                    // Formatação visual para separar [TIPO] das observações
                    String obsCompleta = a.getObs();
                    String tipo = obsCompleta; 
                    String detalhes = "";
                    
                    if(obsCompleta != null && obsCompleta.contains("]")) {
                        String[] parts = obsCompleta.split("]", 2);
                        tipo = parts[0] + "]";
                        detalhes = parts[1];
                    }
            %>
                <tr>
                    <td><%= a.getData_hora() %></td>
                    <td><b><%= tipo %></b></td>
                    <td><%= detalhes.isEmpty() ? a.getEstado() : detalhes %></td>
                </tr>
            <%      
                }
            } else { %>
                <tr><td colspan="3" style="text-align:center; color:gray;">Ainda não existe histórico clínico concluído.</td></tr>
            <% } %>
        </table>
        
        <br>
        <a href="portalCliente" class="btn-ver" style="background:#555; margin-top:20px;">&larr; Voltar aos meus animais</a>
    </div>
</body>
</html>