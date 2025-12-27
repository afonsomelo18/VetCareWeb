package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
