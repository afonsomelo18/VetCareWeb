package dao;

import db.DBConnection;
import model.FichaMedica;
import model.Paciente;
import model.Progenitor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * DAO responsável pelo acesso aos dados de Pacientes e respetivas Fichas Médicas.
 */
public class PacienteDAO {
	
	/*
	 * Obtém as fichas médicas dos animais associados a um determinado tutor.
     * A associação é feita através do NIF do tutor.(2.1)
     * 
     * @param nif: NIF do tutor
     * @return lista de fichas médicas dos animais do tutor
     * @throws SQLException erro de acesso à base de dados
	 */
	public List<FichaMedica> findFichaByTutor(String nif) throws SQLException{
		List<FichaMedica> fichas = new ArrayList<>();
		
		// O SQL faz a junção das duas tabelas através do idPaciente
		String sql = "SELECT f.* FROM FICHA_MEDICA f " +
                "JOIN PACIENTE p ON f.id_paciente = p.id_paciente " +
                "WHERE p.nif_tutor = ?";
		
		try (Connection conn = DBConnection.getConnection();
		         PreparedStatement ps = conn.prepareStatement(sql)) {
		        
		        ps.setString(1, nif);
		        
		        try (ResultSet rs = ps.executeQuery()) {
		            while (rs.next()) {
		                FichaMedica ficha = new FichaMedica(
		                		rs.getInt("id_paciente"),
			                    rs.getString("sexo"),
			                    rs.getString("nome"),
			                    rs.getDate("data_nascimento"),
			                    rs.getString("estado_reprodutivo"),
			                    rs.getString("alergias"),
			                    rs.getDouble("peso"),
			                    rs.getString("cor"),
			                    rs.getString("caract"),
			                    rs.getString("foto"),
			                    rs.getString("transponder")
		                );
		                fichas.add(ficha);
		            }
		        }
		    }
		    return fichas;
	}
	
	
	/**
	 *  Obtém a ficha médica do Paciente especifico.
	 *  
	 *  
	 * @param idPaciente: ID do paciente em questão
	 * @return o objeto Ficha_Medica em java.
	 * @throws SQLException erro de acesso à base de dados
	 */
	public FichaMedica findFichaById(int idPaciente) throws SQLException {

	    String sql =
	        "SELECT id_paciente, sexo, nome, data_nascimento, estado_reprodutivo, alergias, peso, cor, caract, foto, transponder " +
	        "FROM FICHA_MEDICA WHERE id_paciente = ?";

	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setInt(1, idPaciente);

	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                return new FichaMedica(
	                    rs.getInt("id_paciente"),
	                    rs.getString("sexo"),
	                    rs.getString("nome"),
	                    rs.getDate("data_nascimento"),
	                    rs.getString("estado_reprodutivo"),
	                    rs.getString("alergias"),
	                    rs.getDouble("peso"),
	                    rs.getString("cor"),
	                    rs.getString("caract"),
	                    rs.getString("foto"),
	                    rs.getString("transponder")
	                );
	            }
	        }
	    }

	    return null;
	}

	/**
     * Obtém um paciente pelo seu id.
     *
     * @param idPaciente id do paciente
     * @return Paciente ou null se não existir
     * @throws Exception erro de acesso à BD
     */
    public Paciente findById(int idPaciente) throws Exception {
        String sql = "SELECT id_paciente, nif_tutor, nome_comum_especie, nome_comum_raca, id_sistema, id_progenitor\r\n"
        		+ "            FROM PACIENTE\r\n"
        		+ "            WHERE id_paciente = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idPaciente);

            try (ResultSet rs = ps.executeQuery()) {

                if (!rs.next()) {
                    return null;
                }

                int idProgenitor = rs.getInt("id_progenitor");
                if (rs.wasNull()) {
                    idProgenitor = 0;
                }

                return new Paciente(
                    rs.getInt("id_paciente"),
                    rs.getString("nome_comum_especie"),
                    rs.getString("nome_comum_raca"),
                    rs.getString("nif_tutor"),
                    rs.getInt("id_sistema"),
                    idProgenitor
                );
            }
        }
    }
    
     
    /**
     * Este método utiliza a tabela PACIENTE_PROGENITOR para encontrar os pais. Para os avós, chamamos o mesmo método para cada pai encontrado.
     * 
     * @param idFilho
     * @return
     */
    public List<Progenitor> getProgenitoresPorFilho(int idFilho) {
        List<Progenitor> lista = new ArrayList<>();
        String sql = "SELECT p.id_paciente, p.nome_comum_especie, p.nome_comum_raca, "
                + "p.nif_tutor, p.id_sistema, pg.tipo "
                + "FROM PACIENTE p "
                + "JOIN PACIENTE_PROGENITOR pg ON p.id_paciente = pg.id_progenitor "
                + "WHERE pg.id_filho = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idFilho);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                // Cria o objeto Paciente (progenitor)
                Paciente progenitor = new Paciente(
                    rs.getInt("id_paciente"),
                    rs.getString("nome_comum_especie"),
                    rs.getString("nome_comum_raca"),
                    rs.getString("nif_tutor"),
                    rs.getInt("id_sistema"),
                    0 // id_progenitor antigo (pode ser ignorado)
                );
                
                // Adiciona à lista com o tipo (pai/mae)
                lista.add(new Progenitor(progenitor, rs.getString("tipo")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    public void insertCompleto(Paciente p, FichaMedica f) throws SQLException {
        String sqlPaciente = "INSERT INTO PACIENTE (nome_comum_especie, nome_comum_raca, nif_tutor, id_sistema) VALUES (?, ?, ?, ?)";
        String sqlFicha = "INSERT INTO FICHA_MEDICA (id_paciente, nome, sexo, data_nascimento, foto, estado_reprodutivo) VALUES (?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Iniciar Transação (tudo ou nada)

            // 1. Inserir na tabela PACIENTE e obter o ID gerado
            int idGerado = 0;
            try (PreparedStatement ps = conn.prepareStatement(sqlPaciente, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, p.getNomeEspecie());
                ps.setString(2, p.getNomeRaca());
                ps.setString(3, p.getNIF_tutor());
                ps.setInt(4, 1); // id_sistema default (assumindo 1 por agora)
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        idGerado = rs.getInt(1);
                    }
                }
            }

            // 2. Inserir na tabela FICHA_MEDICA usando o ID gerado
            if (idGerado > 0) {
                try (PreparedStatement ps2 = conn.prepareStatement(sqlFicha)) {
                    ps2.setInt(1, idGerado);
                    ps2.setString(2, f.getNome());
                    ps2.setString(3, f.getSexo());
                    ps2.setDate(4, f.getData_nasc());
                    ps2.setString(5, f.getFoto()); // Caminho da foto
                    ps2.setString(6, f.getEstadoReprodutivo());
                    ps2.executeUpdate();
                }
            }

            conn.commit(); // Confirma tudo
        } catch (SQLException e) {
            if (conn != null) conn.rollback(); // Desfaz se der erro
            throw e;
        } finally {
            if (conn != null) conn.setAutoCommit(true);
            if (conn != null) conn.close();
        }
    }

}
