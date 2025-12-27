<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Menu</title>
</head>
<body>
	<h1>Menu</h1>

	<label for="perfil">Perfil:</label>
	<select id="perfil" onchange="if(this.value) window.location.href=this.value;">
  		<option value="">-- Selecionar --</option>
  		<option value="../rececionista/Rececionista.jsp">Rececionista</option>
  	<!-- para mais tarde -->
  	 	<option value="../vet/Veterinario.jsp">Veterinário</option>
	</select>
</body>
</html>