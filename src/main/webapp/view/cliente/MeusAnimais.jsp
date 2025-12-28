<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, model.FichaMedica" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Meus Animais</title>
<style>
    body { font-family: Arial, sans-serif; background-color: #f9f9f9; }
    .container { max-width: 1000px; margin: 0 auto; text-align: center; }
    .card-container { display: flex; justify-content: center; gap: 20px; flex-wrap: wrap; margin-top: 30px; }
    .animal-card { 
        background: white; border: 1px solid #ddd; padding: 20px; width: 220px; 
        text-align: center; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); 
        transition: transform 0.2s; 
    }
    .animal-card:hover { transform: translateY(-5px); border-color: #ff9800; }
    .foto-preview { 
        width: 120px; height: 120px; object-fit: cover; border-radius: 50%; 
        margin-bottom: 15px; border: 3px solid #eee; 
    }
    .btn-ver { 
        display: inline-block; margin-top: 15px; background-color: #4CAF50; 
        color: white; padding: 10px 20px; text-decoration: none; border-radius: 4px; 
    }
    .btn-ver:hover { background-color: #45a049; }
</style>
</head>
<body>
    <jsp:include page="/view/menu/Menu.jsp" />

    <div class="container">
        <h1>Os Meus Animais 🐾</h1>
        
        <div class="card-container">
        <%
            List<FichaMedica> lista = (List<FichaMedica>) request.getAttribute("minhasFichas");
            
            if (lista == null || lista.isEmpty()) {
                out.println("<p>Não existem animais registados para este NIF (ou ainda não têm ficha médica criada).</p>");
            } else {
                for (FichaMedica fm : lista) {
                    // Lógica para garantir que a imagem não falha
                    String foto = request.getContextPath() + "/uploads/animais/default.png";
                    if(fm.getFoto() != null && !fm.getFoto().trim().isEmpty()) {
                        foto = request.getContextPath() + "/" + fm.getFoto();
                    }
        %>
            <div class="animal-card">
                <img src="<%= foto %>" class="foto-preview">
                <h3><%= fm.getNome() %></h3>
                <p>Cor: <%= fm.getCor() == null ? "--" : fm.getCor() %></p>
                
                <a href="portalCliente?acao=detalhes&idAnimal=<%= fm.getIdPaciente() %>" class="btn-ver">
                    Ver Histórico
                </a>
            </div>
        <%
                }
            }
        %>
        </div>
        
        <br><br>
        <a href="view/cliente/LoginCliente.jsp" style="color:#555;">&larr; Sair / Mudar de NIF</a>
    </div>
</body>
</html>