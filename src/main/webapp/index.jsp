<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>VetCare - Início</title>
<style>
    body { font-family: Arial, sans-serif; text-align: center; margin-top: 50px; }
    .btn-big {
        display: inline-block;
        padding: 20px 40px;
        margin: 10px;
        font-size: 20px;
        text-decoration: none;
        color: white;
        border-radius: 8px;
    }
    .rececao { background-color: #008CBA; } /* Azul */
    .vet { background-color: #4CAF50; } /* Verde */
</style>
</head>
<body>

    <img src="https://cdn-icons-png.flaticon.com/512/2171/2171991.png" width="100" alt="Logo">
    <h1>Bem-vindo ao VetCare</h1>
    <p>Selecione o seu perfil para entrar:</p>

    <br>

    <a href="<%= request.getContextPath() %>/view/rececionista/Rececionista.jsp" class="btn-big rececao">
        Sou Rececionista
    </a>

    <a href="<%= request.getContextPath() %>/view/vet/Agenda.jsp" class="btn-big vet">
        Sou Veterinário
    </a>
    <a href="<%= request.getContextPath() %>/view/cliente/LoginCliente.jsp" class="btn-big" style="background-color: #ff9800;">
    Sou Cliente
	</a>
</body>
</html>