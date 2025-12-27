package dao;

import db.DBConnection;
import model.Exame;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class ExameDAO {

	public void insert(Exame exame) throws SQLException{
		
		String sql =
	            "INSERT INTO exame (id_servico, tipo, notas) " +
	            "VALUES (?, ?, ?)";
		
		try(Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
			ps.setInt(1, exame.getId_servico());
			ps.setString(2, exame.getTipo());
			ps.setString(3, exame.getNotas());
			
			ps.executeUpdate();
		}
	}
}
