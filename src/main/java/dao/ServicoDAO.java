package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import db.DBConnection;

public class ServicoDAO {

	public String findTipoServicoById(int idServico) throws SQLException {
	    String sql = "SELECT tipo_servico FROM SERVICO WHERE id_servico = ?";
	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setInt(1, idServico);
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                return rs.getString("tipo_servico");
	            }
	        }
	    }
	    return "Serviço Desconhecido";
	}
	
	public int insert(String tipo, String numLicenca, String localidade, int diaSemana) throws SQLException {
	    String sql = "INSERT INTO SERVICO (preco, estado, tipo_servico, num_licenca, localidade, dia_semana) " +
	                 "VALUES (0, 'ativo', ?, ?, ?, ?)";
	    
	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
	        
	        ps.setString(1, tipo);
	        ps.setString(2, numLicenca);
	        ps.setString(3, localidade);
	        ps.setInt(4, diaSemana);
	        
	        ps.executeUpdate();
	        
	        try (ResultSet rs = ps.getGeneratedKeys()) {
	            if (rs.next()) {
	                return rs.getInt(1); // Retorna o ID do serviço gerado
	            }
	        }
	    }
	    throw new SQLException("Falha ao criar serviço, nenhum ID obtido.");
	}
}
