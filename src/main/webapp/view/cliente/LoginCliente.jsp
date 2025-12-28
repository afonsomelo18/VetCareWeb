<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login Cliente</title>
<style>
    body { font-family: Arial, sans-serif; text-align: center; margin-top: 50px; background-color: #f4f4f4; }
    .login-box { 
        background: white; 
        padding: 40px; 
        border-radius: 8px; 
        box-shadow: 0 0 15px rgba(0,0,0,0.1); 
        display: inline-block; 
        width: 300px;
    }
    input[type="text"] { 
        width: 90%; padding: 10px; margin: 15px 0; border: 1px solid #ccc; border-radius: 4px; 
    }
    button { 
        width: 100%; padding: 10px; background-color: #008CBA; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 16px; 
    }
    button:hover { background-color: #007B9E; }
    .error { color: red; font-size: 14px; margin-top: 10px; }
</style>
</head>
<body>
    <jsp:include page="/view/menu/Menu.jsp" />
    
    <div class="login-box">
        <h2>🐾 Área do Cliente</h2>
        <p>Introduza o seu NIF para aceder.</p>
        
        <form action="<%= request.getContextPath() %>/portalCliente" method="post">
            <input type="text" name="nif" placeholder="NIF do Tutor" required pattern="[0-9]{9}" title="Deve ter 9 dígitos">
            <button type="submit">Entrar</button>
        </form>

        <% if (request.getParameter("msg") != null) { %>
            <div class="error">
                <% if(request.getParameter("msg").equals("NifInvalido")) { %>
                    ⚠️ NIF inválido ou não registado.
                <% } else if(request.getParameter("msg").equals("ErroBD")) { %>
                    ❌ Erro de ligação à Base de Dados.
                <% } %>
            </div>
        <% } %>
    </div>
</body>
</html>