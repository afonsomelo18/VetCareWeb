<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Area de Veterinário</title>
</head>
<body>
	<!-- AutoComplete -->
	<h2>Pesquisa Tutor</h2>
	<input type="text" id="tutorInput" list="resultados" placeholder="Nome do tutor">
	<datalist id="resultados"></datalist>
	
	
	<!-- Mostrar Ficha Médica -->
    <input type="hidden" id="nifTutor">
    <div id="tabelaAnimais"></div>
    
    <input type="hidden" id="idPacienteAG_123" value="123">
	<div id="tabelaAG_123"></div>
    
    
    <script>
    
    	const input = document.getElementById("tutorInput");
    	const resultados = document.getElementById("resultados");
    	const nifHidden = document.getElementById("nifTutor");
    	
    	// mapa nome -> nif
        const tutorMap = {};
    	
    	input.addEventListener("keyup", function () {
            const termo = input.value;

            if (termo.length < 2) {
                resultados.innerHTML = "";
                return;
            }

            
            const baseUrl = "<%= request.getContextPath() %>/autocompleteTutor";
            
            fetch(baseUrl + "?term=" + encodeURIComponent(termo))
                .then(response => response.json())
                .then(data => {
                    resultados.innerHTML = "";
                    for (let key in tutorMap) delete tutorMap[key];

                    for (let i = 0; i < data.length; i++) {
                        const tutor = data[i];
                        tutorMap[tutor.nome] = tutor.nif;
                        const option = document.createElement("option");
                        option.value = tutor.nome;
                        resultados.appendChild(option);
                    }
                })
                .catch(error => {
                    console.error("Erro no autocomplete:", error);
                });
        });
    	
    	// Após escolher o Tutor
    	input.addEventListener("change", function () {
    	    const nomeEscolhido = input.value;
    	    const nif = tutorMap[nomeEscolhido];

    	    // Verifica a existencia do NIF
    	    if (!nif) {
    	        console.warn("Tutor não encontrado:", nomeEscolhido);
    	        return;
    	    }

    	    console.log("NIF selecionado:", nif);

    	    const url = "<%= request.getContextPath() %>/listarAnimais?nif=" + encodeURIComponent(nif);

    	    // faz um HTTP GET para o servlet /listarAnimais e espera uma resposta
    	    fetch(url)
    	        .then(response => {
    	            if (!response.ok) {
    	                throw new Error("Erro HTTP: " + response.status);
    	            }
    	            // Se a resposta for 200 devolve o texto do servlet
    	            return response.text();
    	        })
    	        .then(html => {
    	            console.log("HTML recebido do servlet:", html);
    	            document.getElementById("tabelaAnimais").innerHTML = html;
    	        })
    	        .catch(error => {
    	            console.error("Erro ao chamar /listarAnimais:", error);
    	        });
    	});
    	
    	document.getElementById("tabelaAG_123").addEventListener("click", function (event) {
            // Verifica se o elemento clicado é o botão de árvore (podes definir a classe no Servlet)
            if (event.target && event.target.classList.contains("btn-mostrar-arvore")) {
                const idPaciente = event.target.getAttribute("data-id");
                const divDestino = document.getElementById("arvore_content_" + idPaciente);

                // Se a árvore já estiver visível, podemos apenas fechar (toggle)
                if (divDestino.innerHTML !== "") {
                    divDestino.innerHTML = "";
                    event.target.textContent = "Ver Árvore Genealógica";
                    return;
                }

                event.target.textContent = "⌛ A carregar...";

                // Chamada para o endpoint que devolve apenas o texto da árvore
                fetch("<%= request.getContextPath() %>/consultarArvore?id=" + idPaciente)
                    .then(response => response.text())
                    .then(textoArvore => {
                        divDestino.innerHTML = textoArvore;
                        event.target.textContent = "Esconder Árvore";
                    })
                    .catch(err => {
                        console.error("Erro ao carregar árvore:", err);
                        divDestino.innerHTML = "Erro ao carregar família.";
                    });
            }
        });
    </script>
	
</body>
</html>