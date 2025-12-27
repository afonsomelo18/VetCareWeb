package dao;

import model.Med_Prev;
import db.DBConnection;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class Med_PrevDAO {

	public void insert(Med_Prev m) throws SQLException{
		
		String sql =
	            "INSERT INTO med_prev (id_servico, tipo, produto_utilizado, fabricante, proxima_aplicacao) " +
	            "VALUES (?, ?, ?, ?, ?)";
		
		try(Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){
			ps.setInt(1, m.getId_servico());
			ps.setString(2, m.getTipo());
			ps.setString(3, m.getProduto());
			ps.setString(4, m.getFabricante());
			ps.setDate(5, m.getProx_aplicacao());
			
			ps.executeUpdate();
		}
	}
}
