package dao;

import db.DBConnection;
import model.Tutor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
* Data Access Object (DAO) responsável pelo acesso aos dados
* da entidade Tutor.
*/
public class TutorDAO {
	
	
	/**
	 * Pesquisa tutores cujo nome contenha o texto fornecido.
	 * Este método será utilizado pelo controlo de autocomplete pedido no ponto 2.1.
	 * 
	 * 
	 * @param termo: nome introduzido pelo utilizador
	 * @return lista de tutores cujo nome corresponde ao critério
	 * @throws SQLException em caso de erro de acesso à base de dados
	 */
	public List<Tutor> findByName(String termo) throws SQLException{
		List<Tutor> tutores = new ArrayList<>();
		
		String sql =
	            "SELECT nif, contacto, pref_linguistica, tipo, id_freguesia, id_concelho, nome " +
	            "FROM TUTOR " +
	            "WHERE nome LIKE ? " +
	            "ORDER BY nome";
		
		try(Connection conn = DBConnection.getConnection();
	             PreparedStatement ps = conn.prepareStatement(sql))
		{
			
			ps.setString(1, "%" + termo + "%");
			try(ResultSet rs = ps.executeQuery()){
				
				// transforma uma linha da base de dados num objeto Tutor
				while(rs.next()) {
					Tutor tutor = new Tutor(
	                        rs.getString("nif"),
	                        rs.getString("contacto"),
	                        rs.getString("pref_linguistica"),
	                        rs.getString("tipo"),
	                        rs.getInt("id_freguesia"),
	                        rs.getInt("id_concelho"),
	                        rs.getString("nome")
	                    );

	                    tutores.add(tutor);
				}
			}
		}
		return tutores;
	}
	
	public void save(Tutor t) throws SQLException {
	    String sql = "INSERT INTO TUTOR (nif, nome, contacto, pref_linguistica, tipo, id_freguesia, id_concelho) " +
	                 "VALUES (?, ?, ?, ?, ?, ?, ?)";
	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setString(1, t.getNif());
	        ps.setString(2, t.getNome()); // Nota: Adicionaste a coluna 'nome' no SQL ontem [cite: 137]
	        ps.setString(3, t.getContacto());
	        ps.setString(4, t.getLingua());
	        ps.setString(5, t.getTipo());
	        ps.setInt(6, t.getIdFreguesia());
	        ps.setInt(7, t.getIdConcelho());
	        ps.executeUpdate();
	    }
	}

	public void update(Tutor t) throws SQLException {
	    String sql = "UPDATE TUTOR SET nome=?, contacto=?, pref_linguistica=?, id_freguesia=?, id_concelho=? WHERE nif=?";
	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setString(1, t.getNome());
	        ps.setString(2, t.getContacto());
	        ps.setString(3, t.getLingua());
	        ps.setInt(4, t.getIdFreguesia());
	        ps.setInt(5, t.getIdConcelho());
	        ps.setString(6, t.getNif());
	        ps.executeUpdate();
	    }
	}

}
