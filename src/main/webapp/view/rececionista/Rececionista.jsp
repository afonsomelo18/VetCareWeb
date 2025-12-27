<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Painel Rececionista</title>
</head>
<body>
    <h1>Área do Rececionista</h1>
    
    <fieldset>
        <legend>Novo Tutor</legend>
        <form action="<%= request.getContextPath() %>/gestaoCliente" method="post">
            <input type="hidden" name="acao" value="novoTutor">
            
            NIF: <input type="text" name="nif" required> <br>
            Nome: <input type="text" name="nome" required> <br>
            Contacto: <input type="text" name="contacto"> <br>
            
            <button type="submit">Criar Tutor</button>
        </form>
    </fieldset>

    <br><hr><br>

    <fieldset>
        <legend>Novo Animal</legend>
        <form action="<%= request.getContextPath() %>/gestaoCliente" method="post" enctype="multipart/form-data">
            <input type="hidden" name="acao" value="novoAnimal">
            
            NIF do Tutor: <input type="text" name="nifTutor" required placeholder="NIF do dono"> <br>
            
            Nome do Animal: <input type="text" name="nomeAnimal" required> <br>
            Espécie: 
            <select name="especie">
                <option value="Cão">Cão</option>
                <option value="Gato">Gato</option>
            </select> <br>
            Raça: <input type="text" name="raca" value="Rafeiro"> <br>
            
            Sexo: 
            <input type="radio" name="sexo" value="M" checked> Macho
            <input type="radio" name="sexo" value="F"> Fêmea <br>
            
            Data Nascimento: <input type="date" name="dataNasc" required> <br>
            
            Fotografia: <input type="file" name="foto" accept="image/*"> <br>
            
            <button type="submit">Registar Animal</button>
        </form>
        <hr>
<h1>Gestão de Agendamentos</h1>

<h3>1. Pesquisar Tutor e Animais</h3>
<input type="text" id="tutorInputAgendar" list="resultadosAgendar" placeholder="Pesquisar Tutor (Nome)">
<datalist id="resultadosAgendar"></datalist>

<div id="listaAnimaisArea"></div>

<div id="formAgendar" style="display:none; border: 2px solid #333; padding: 20px; background-color: #f9f9f9; margin-top: 20px;">
    <h3>Marcar Serviço para: <span id="nomeAnimalAgendar"></span></h3>
    
    <form action="<%= request.getContextPath() %>/novoAgendamento" method="post">
        <input type="hidden" name="nifTutor" id="inputNifHidden">
        <input type="hidden" name="idAnimal" id="inputIdAnimalHidden">
        
        <label>Data e Hora:</label><br>
        <input type="datetime-local" name="dataHora" required> <br><br>
        
        <label>Tipo de Serviço:</label><br>
        <select name="tipoServico">
            <option value="consulta">Consulta</option>
            <option value="exame">Exame</option>
            <option value="cirurgia">Cirurgia</option>
            <option value="med_prev">Vacinação/Desparasitação</option>
            <option value="terapeutico">Curativo/Tratamento</option>
        </select> <br><br>
        
        <label>Veterinário:</label><br>
        <select name="veterinario">
            <option value="COI555">Dr. Rui Neves (Coimbra)</option>
            <option value="LIS123">Dra. Sofia Mendes (Lisboa)</option>
            <option value="PRT789">Dra. Marta Silva (Porto)</option>
        </select> <br><br>
        
        <button type="submit">Confirmar Agendamento</button>
        <button type="button" onclick="document.getElementById('formAgendar').style.display='none'">Cancelar</button>
    </form>
</div>

<script>
    // --- Lógica de Autocomplete (Reutilizada) ---
    const inputAg = document.getElementById("tutorInputAgendar");
    const listAg = document.getElementById("resultadosAgendar");
    let mapTutores = {};

    inputAg.addEventListener("keyup", function() {
        if(this.value.length < 2) return;
        
        fetch("<%= request.getContextPath() %>/autocompleteTutor?term=" + encodeURIComponent(this.value))
            .then(r => r.json())
            .then(data => {
                listAg.innerHTML = "";
                mapTutores = {};
                data.forEach(t => {
                    const opt = document.createElement("option");
                    opt.value = t.nome;
                    listAg.appendChild(opt);
                    mapTutores[t.nome] = t.nif;
                });
            });
    });

    inputAg.addEventListener("change", function() {
        const nome = this.value;
        const nif = mapTutores[nome];
        if(!nif) return;

        // Buscar animais (Reutiliza o Servlet ListarAnimais que já tens!)
        // Mas atenção: O teu ListarAnimais atual desenha muito HTML.
        // Para simplificar, vou assumir que ele devolve HTML.
        // Vamos adicionar botões "Agendar" via Javascript depois de carregar.
        
        fetch("<%= request.getContextPath() %>/listarAnimais?nif=" + nif)
            .then(r => r.text())
            .then(html => {
                const div = document.getElementById("listaAnimaisArea");
                div.innerHTML = html;
                
                // --- TRUQUE: Adicionar botão "Agendar" a cada animal ---
                // O teu servlet ListarAnimais não mete botões de agendar.
                // Vamos injetar um botão em cada "card" de animal.
                
                // Vamos procurar os nomes dos animais nos H3 e meter um botão ao lado
                const h3s = div.querySelectorAll("h3");
                h3s.forEach(h3 => {
                    // Tenta encontrar o ID do animal. 
                    // Como o teu ListarAnimaisServlet atual não imprime o ID num atributo fácil,
                    // isto é complicado.
                    
                    // SOLUÇÃO RECOMENDADA:
                    // Cria um botão genérico aqui só para testar, mas o ideal
                    // seria alterares o ListarAnimaisServlet para incluir um 
                    // <button onclick="abrirAgendar(123, 'Bobby', 'NIF')">Agendar</button>
                });
                
                // Nota: Como o teu ListarAnimaisServlet é complexo, sugiro criar
                // uma versão simplificada ou injetar o botão manualmente.
                // Para este exemplo funcionar JÁ, precisas que o ListarAnimaisServlet
                // escreva o ID do animal algures no HTML (ex: data-id="1").
                
                // Se não quiseres mexer no Servlet agora, terás de escrever o ID manualmente no input.
            });
    });
    
    // Função para abrir o formulário (Tens de chamar isto quando clicares num animal)
    function prepararAgendamento(idAnimal, nomeAnimal, nifTutor) {
        document.getElementById("formAgendar").style.display = "block";
        document.getElementById("nomeAnimalAgendar").innerText = nomeAnimal;
        document.getElementById("inputNifHidden").value = nifTutor;
        document.getElementById("inputIdAnimalHidden").value = idAnimal;
    }
</script>
    </fieldset>

</body>
</html>