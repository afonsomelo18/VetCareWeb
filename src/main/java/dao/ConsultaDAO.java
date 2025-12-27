package dao;

import db.DBConnection;
import model.Consulta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * DAO responsável pela persistência dos dados clínicos
 * associados a serviços do tipo Consulta.
 *
 * Os registos na tabela CONSULTA são criados exclusivamente
 * no momento da prestação do serviço (requisito 2.5).
 */
public class ConsultaDAO {
	
	/**
     * Cria o registo clínico de uma consulta.
     *
     * Este método é utilizado durante a prestação de um serviço
     * do tipo consulta, permitindo registar os dados clínicos
     * específicos associados ao serviço.(2.5)
     *
     * @param consulta objeto Consulta com os dados clínicos a persistir
     * @throws SQLException erro de acesso à base de dados
     */
	public void insert(Consulta consulta) throws SQLException{
		String sql =
	            "INSERT INTO consulta (id_servico, motivo, diagnostico, medicacao) " +
	            "VALUES (?, ?, ?, ?)";
		
		try (Connection conn = DBConnection.getConnection();
	             PreparedStatement ps = conn.prepareStatement(sql)) {

	            ps.setInt(1, consulta.getId_servico());
	            ps.setString(2, consulta.getMotivo());
	            ps.setString(3, consulta.getDiagnostico());
	            ps.setString(4, consulta.getMedicacao());

	            ps.executeUpdate();
	        }
	}
}
