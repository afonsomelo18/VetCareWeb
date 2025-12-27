package dao;

import db.DBConnection;
import model.Terapeutico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * DAO responsável pela persistência dos dados clínicos
 * associados a serviços do tipo Tratamento Terapêutico.
 *
 * Os registos na tabela TERAPEUTICO são criados exclusivamente
 * no momento da prestação do serviço (requisito 2.5).
 */
public class TerapeuticoDAO {

    /**
     * Cria o registo clínico de um tratamento terapêutico.
     *
     * Este método é utilizado durante a prestação de um serviço
     * do tipo tratamento terapêutico, permitindo registar os
     * dados clínicos específicos associados ao tratamento.
     *
     * @param terapeutico objeto Terapeutico com os dados clínicos a persistir
     * @throws SQLException erro de acesso à base de dados
     */
    public void insert(Terapeutico terapeutico) throws SQLException {

        String sql =
            "INSERT INTO terapeutico (id_servico, tipo_ferida, cuidados) " +
            "VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, terapeutico.getId_servico());
            ps.setString(2, terapeutico.getTipo_ferida());
            ps.setString(3, terapeutico.getCuidados());

            ps.executeUpdate();
        }
    }
}
