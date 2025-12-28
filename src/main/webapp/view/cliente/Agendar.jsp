<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Agendar Consulta</title>
<style>
    body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; }
    .box { background: white; padding: 30px; max-width: 500px; margin: 40px auto; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
    input, textarea { width: 100%; padding: 10px; margin: 10px 0; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; }
    button { background-color: #4CAF50; color: white; padding: 12px; border: none; width: 100%; cursor: pointer; font-size: 16px; border-radius: 4px; }
    button:hover { background-color: #45a049; }
    .btn-cancel { display:block; text-align:center; margin-top:15px; text-decoration:none; color:#555; }
    .aviso { font-size: 0.9em; color: #666; background: #fff3cd; padding: 10px; border-left: 4px solid #ffc107; margin-bottom: 15px; }
</style>
</head>
<body>
    <jsp:include page="/view/menu/Menu.jsp" />
    
    <div class="box">
        <% 
            String acao = request.getParameter("tipo"); // 'agendar' ou 'reagendar'
            String idPaciente = request.getParameter("idPaciente");
            String idAgendamento = request.getParameter("idAgendamento");
        %>

        <h2 style="text-align:center">
            <%= "reagendar".equals(acao) ? "🔄 Reagendar Consulta" : "📅 Nova Consulta" %>
        </h2>
        
        <div class="aviso">
            ⚠️ <strong>Atenção:</strong> Certifique-se que escolhe um dia/hora em que a clínica esteja aberta (verifique os Horários), caso contrário o agendamento falhará.
        </div>
        
        <form action="<%= request.getContextPath() %>/portalCliente" method="post">
            <input type="hidden" name="acao" value="<%= acao %>">
            <input type="hidden" name="idPaciente" value="<%= idPaciente %>">
            
            <% if("reagendar".equals(acao)) { %>
                <input type="hidden" name="idAgendamento" value="<%= idAgendamento %>">
                
                <label>Nova Data e Hora:</label>
                <input type="datetime-local" name="novaData" required>
            <% } else { %>
                <label>Data e Hora Preferencial:</label>
                <input type="datetime-local" name="dataHora" required>
                
                <label>Motivo / Sintomas:</label>
                <textarea name="motivo" rows="4" placeholder="Ex: Vacinação, Check-up anual..." required></textarea>
            <% } %>

            <br>
            <button type="submit">Confirmar</button>
        </form>
        
        <a href="<%= request.getContextPath() %>/portalCliente?acao=detalhes&idAnimal=<%= idPaciente %>" class="btn-cancel">
            Cancelar e Voltar
        </a>
    </div>
</body>
</html>