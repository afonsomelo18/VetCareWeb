package dao;

import db.DBConnection;
import model.Agendamento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO responsável pelo acesso aos dados de Agendamentos.
 * 
 * No contexto da aplicação, os agendamentos com estado "realizado"
 * representam os eventos do histórico clínico do animal.
 */
public class AgendamentoDAO {

	public List<Agendamento> findHistoricoByPaciente(int idPaciente) throws SQLException{
		
		List<Agendamento> historico = new ArrayList<>();
		
		String sql =
	            "SELECT id_agendamento, id_paciente, id_servico, localidade, dia_semana, " +
	            "       nif_tutor, data_hora, estado, observacoes " +
	            "FROM AGENDAMENTO " +
	            "WHERE id_paciente = ? " +
	            "  AND estado = 'concluído' " +
	            "ORDER BY data_hora DESC";
		
		 try (Connection conn = DBConnection.getConnection();
	             PreparedStatement ps = conn.prepareStatement(sql)) {

	            ps.setInt(1, idPaciente);

	            try (ResultSet rs = ps.executeQuery()) {
	                while (rs.next()) {

	                    Agendamento ag = new Agendamento(
	                        rs.getInt("id_agendamento"),
	                        rs.getInt("id_paciente"),
	                        rs.getInt("id_servico"),
	                        rs.getString("localidade"),
	                        rs.getInt("dia_semana"),
	                        rs.getString("nif_tutor"),
	                        rs.getTimestamp("data_hora"),
	                        rs.getString("estado"),
	                        rs.getString("observacoes")
	                    );

	                    historico.add(ag);
	                }
	            }
	        }

	        return historico;
	}
	
	
	/**
	 * Obtém a agenda de agendamentos associados a um veterinário.
	 * 
	 * Este método devolve todos os agendamentos realizados na localidade
	 * onde o veterinário exerce, filtrando pelo número de licença do veterinário.
	 * Os agendamentos são ordenados por data e hora de forma decrescente,
	 * apresentando primeiro os mais recentes.
	 * 
	 * A informação é obtida a partir da tabela AGENDAMENTO, sendo feita
	 * uma junção com a tabela VETERINARIO para identificar a localidade
	 * correspondente ao veterinário.
	 *
	 * @param numLicenca número de licença do veterinário
	 * @return lista de agendamentos da agenda do veterinário; lista vazia se não existirem registos
	 * @throws SQLException erro de acesso à base de dados
	 */
	public List<Agendamento> findAgendaByVet(String numLicenca) throws SQLException{
		List<Agendamento> agenda = new ArrayList<>();
		
		// SQL que une Agendamento (para os dados da consulta), 
	    // Ficha_Medica (para o nome do animal) 
	    // e Veterinario (para filtrar pela licença via localidade)
		String sql = "SELECT a.* FROM AGENDAMENTO a " +
                "JOIN VETERINARIO v ON a.localidade = v.localidade " +
                "WHERE v.num_licenca = ? " +
                "AND a.estado IN ('marcado', 'reagendado') " +
                "ORDER BY a.data_hora DESC";
		
		try(Connection conn = DBConnection.getConnection();
		         PreparedStatement ps = conn.prepareStatement(sql)){
			
			ps.setString(1, numLicenca);
			try(ResultSet rs = ps.executeQuery()){
				while(rs.next()){
					Agendamento ag = new Agendamento(
		                    rs.getInt("id_agendamento"),
		                    rs.getInt("id_paciente"),
		                    rs.getInt("id_servico"),
		                    rs.getString("localidade"),
		                    rs.getInt("dia_semana"),
		                    rs.getString("nif_tutor"),
		                    rs.getTimestamp("data_hora"),
		                    rs.getString("estado"),
		                    rs.getString("observacoes")
		                );
					agenda.add(ag);
					
				}
			}
		}
		return agenda;
	}
	
	/**
	 * Atualiza a informação clínica de um agendamento no contexto da prestação
	 * de um serviço veterinário.
	 *
	 * Este método é utilizado quando um serviço previamente marcado é prestado,
	 * permitindo registar observações clínicas e marcar o agendamento como concluído,
	 * passando assim a integrar o histórico clínico do animal.(2.5)
	 *
	 * @param idAgendamento identificador do agendamento a atualizar
	 * @param observacoes  observações clínicas registadas durante a prestação do serviço
	 * @throws SQLException erro de acesso à base de dados
	 */
	public void update(int idAgendamento, String observacoes) throws SQLException{
		String sql =
		        "UPDATE AGENDAMENTO " +
		        "SET estado = 'concluído', observacoes = ? " +
		        "WHERE id_agendamento = ?";
		
		try (Connection conn = DBConnection.getConnection();
		         PreparedStatement ps = conn.prepareStatement(sql)) {

		        ps.setString(1, observacoes);
		        ps.setInt(2, idAgendamento);

		        ps.executeUpdate();
		    }
		
	}
	
	/**
     * Atualiza o estado de um agendamento para reagendado.
     *
     * Este método corresponde ao requisito 2.6 (agendar/reagendar
     * a prestação de um serviço veterinário).
     *
     * @param idAgendamento identificador do agendamento
     * @throws SQLException erro de acesso à base de dados
     */
    public void reagendar(int idAgendamento, Timestamp novaDataHora) throws SQLException {

        String sql =
            "UPDATE AGENDAMENTO " +
            "SET estado = 'reagendado', data_hora = ? " +
            "WHERE id_agendamento = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, novaDataHora);
            ps.setInt(2, idAgendamento);

            ps.executeUpdate();
        }
    }
    
    
    
    /**
     * Cancela a prestação de um serviço veterinário.
     *
     * Este método corresponde ao requisito 2.6 e implica:
     * - marcar o agendamento como cancelado
     * - marcar o serviço como inativo
     *
     * @param idAgendamento identificador do agendamento a cancelar
     * @throws SQLException erro de acesso à base de dados
     */
    public void cancelar(int idAgendamento) throws SQLException {

        String sqlAgendamento =
            "UPDATE AGENDAMENTO " +
            "SET estado = 'cancelado' " +
            "WHERE id_agendamento = ?";

        String sqlServico =
            "UPDATE SERVICO " +
            "SET estado = 'inativo' " +
            "WHERE id_servico = (" +
            "   SELECT id_servico FROM AGENDAMENTO WHERE id_agendamento = ?" +
            ")";

        try (Connection conn = DBConnection.getConnection()) {

            // cancelar agendamento
            try (PreparedStatement ps1 = conn.prepareStatement(sqlAgendamento)) {
                ps1.setInt(1, idAgendamento);
                ps1.executeUpdate();
            }

            // inativar serviço
            try (PreparedStatement ps2 = conn.prepareStatement(sqlServico)) {
                ps2.setInt(1, idAgendamento);
                ps2.executeUpdate();
            }
        }
    }
    
    
    /**
     * Atualiza o estado de um agendamento para "concluído".
     * * @param idAgendamento ID do agendamento a ser atualizado
     * @throws SQLException erro de acesso à base de dados
     */
    public void concluir(int idAgendamento) throws SQLException{
    	String sql = "UPDATE AGENDAMENTO SET estado = 'concluído' WHERE id_agendamento = ?";
    	
    	try (Connection conn = DBConnection.getConnection();
    	         PreparedStatement ps = conn.prepareStatement(sql)) {

    	        ps.setInt(1, idAgendamento);

    	        ps.executeUpdate();
    	    }
    }
    
    public void insert(Agendamento a) throws SQLException {
        String sql = "INSERT INTO AGENDAMENTO (id_paciente, id_servico, localidade, dia_semana, nif_tutor, data_hora, estado, observacoes) " +
                     "VALUES (?, ?, ?, ?, ?, ?, 'marcado', ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, a.getId_paciente());
            ps.setInt(2, a.getId_servico());
            ps.setString(3, a.getLocalidade());
            ps.setInt(4, a.getDia_semana());
            ps.setString(5, a.getNif_tutor());
            ps.setTimestamp(6, a.getData_hora());
            ps.setString(7, a.getObs() != null ? a.getObs() : ""); // Evitar null
            
            ps.executeUpdate();
        }
    }
    
}
