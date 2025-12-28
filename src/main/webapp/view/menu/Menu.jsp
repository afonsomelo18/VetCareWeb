<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<style>
    /* Estilo da Barra */
    .navbar {
        background-color: #333;
        overflow: hidden;
        font-family: sans-serif;
        margin-bottom: 20px;
        border-radius: 4px;
    }

    /* Links da Barra */
    .navbar a {
        float: left;
        display: block;
        color: white;
        text-align: center;
        padding: 14px 20px;
        text-decoration: none;
        font-size: 16px;
    }

    /* Efeito ao passar o rato */
    .navbar a:hover {
        background-color: #ddd;
        color: black;
    }

    /* Destaque para a marca */
    .navbar .brand {
        background-color: #4CAF50; /* Verde VetCare */
        font-weight: bold;
    }
</style>

<div class="navbar">
    
    <a href="<%= request.getContextPath() %>/index.jsp" class="brand">🏥 VetCare</a>

    <a href="<%= request.getContextPath() %>/view/rececionista/Rececionista.jsp">👤 Rececionista</a>

    <a href="<%= request.getContextPath() %>/view/vet/Agenda.jsp">📅 Agenda</a>
    <a href="<%= request.getContextPath() %>/view/vet/FichaMedica.jsp">📋 Ficha Médica</a>

    <a href="<%= request.getContextPath() %>/index.jsp" style="float:right; background-color: #f44336;">🚪 Sair</a>

</div>