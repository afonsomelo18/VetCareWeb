<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Agenda do Veterinário</title>
</head>
<body>
<jsp:include page="/view/menu/Menu.jsp" />
	<h2>Serviços Agendados</h2>
	<button type="button" id="btnIniciarServico" disabled>Iniciar Serviço (1º da lista)</button>
	<button type="button" id="btnCancelar" disabled>Cancelar (1º da lista)</button>
	<button type="button" id="btnReagendar" disabled>Reagendar (1º da lista)</button>
	<table border="1" cellpadding="5" id="tabelaAgenda">
		<thead>
			<tr>
				<th>Data / Hora</th>
            	<th>Tipo de Serviço</th>
            	<th>Paciente</th>
			</tr>
		</thead>
		<tbody>
			<!-- As linhas da agenda serão inseridas aqui -->
		</tbody>
	</table>


	<script>
	function carregarAgenda() {
		const licenca = "COI555"; 
	    const baseUrl = "<%= request.getContextPath() %>/consultarAgenda?numLicenca=" + licenca;
	    fetch(baseUrl)
	      .then(r => r.text())
	      .then(html => {
	        const tbody = document.querySelector("#tabelaAgenda tbody");
	        tbody.innerHTML = html;

	        const primeiraLinha = tbody.querySelector("tr[data-idagendamento]");
	        const btn = document.getElementById("btnIniciarServico");

	        if (primeiraLinha) {
	        	const idS = primeiraLinha.dataset.idservico;
	            const idA = primeiraLinha.dataset.idagendamento;

	            // Ativa e guarda dados no botão Iniciar
	            btn.disabled = false;
	            btn.dataset.idservico = idS;
	            btn.dataset.idagendamento = idA;

	            // Ativa e guarda dados no botão Cancelar
	            const btnCan = document.getElementById("btnCancelar");
	            btnCan.disabled = false;
	            btnCan.dataset.idagendamento = idA;

	            // Ativa e guarda dados no botão Reagendar
	            const btnRea = document.getElementById("btnReagendar");
	            btnRea.disabled = false;
	            btnRea.dataset.idagendamento = idA;
	        }
	      })
	      .catch(console.error);
	  }

		document.getElementById("btnIniciarServico").addEventListener("click", function () {
		    const idServico = this.dataset.idservico;
		    const idAgendamento = this.dataset.idagendamento;
		    
		    // Verificação de segurança
		    if (!idServico || !idAgendamento) {
		        console.error("IDs em falta no botão");
		        return;
		    }
	
		    // CORREÇÃO AQUI: Usar o caminho completo desde a raiz do projeto
		    // Certifica-te que o ficheiro IniciarServico.jsp está na pasta view/vet/
		    window.location.href = 
		        "<%= request.getContextPath() %>/view/vet/IniciarServico.jsp?idServico=" + 
		        encodeURIComponent(idServico) + 
		        "&idAgendamento=" + 
		        encodeURIComponent(idAgendamento);
		});
	  
		// --- EVENTO CANCELAR ---
	  document.getElementById("btnCancelar").addEventListener("click", function() {
	      const idA = this.dataset.idagendamento;
	      if (!confirm("Deseja realmente cancelar este serviço?")) return;

	      executarAcao("cancelar", idA);
	  });
		
		// --- EVENTO REAGENDAR ---
	  document.getElementById("btnReagendar").addEventListener("click", function() {
	      const idA = this.dataset.idagendamento;
	      
	      // Simplificando: pede a data via prompt (ou podes usar um input tipo datetime-local)
	      const novaData = prompt("Insira a nova data e hora (AAAA-MM-DDTHH:MM):", "2024-01-01T10:00");
	      
	      if (novaData) {
	          executarAcao("reagendar", idA, novaData);
	      }
	  });
	
	// Função genérica para enviar ao Servlet
	  function executarAcao(acao, idA, data = "") {
	      const params = new URLSearchParams();
	      params.append("acao", acao);
	      params.append("idAgendamento", idA);
	      params.append("novaData", data);

	      fetch("<%= request.getContextPath() %>/agendamentoAcao", {
	          method: "POST",
	          body: params
	      })
	      .then(r => r.text())
	      .then(res => {
	          if (res === "ok") {
	              alert("Operação realizada com sucesso!");
	              carregarAgenda(); // Atualiza a tabela (o item vai desaparecer porque mudou de estado)
	          } else {
	              alert("Erro: " + res);
	          }
	      })
	      .catch(console.error);
	  }

	  window.addEventListener("load", carregarAgenda);
	</script>
</body>
</html>
