package dao;

import db.DBConnection;
import model.Cirurgia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalTime;

/**
 * DAO responsável pela persistência dos dados clínicos
 * associados a serviços do tipo Cirurgia.
 *
 * Os registos na tabela CIRURGIA são criados exclusivamente
 * no momento da prestação do serviço (requisito 2.5).
 */
public class CirurgiaDAO {

    /**
     * Cria o registo clínico de uma cirurgia.
     *
     * Este método é utilizado durante a prestação de um serviço
     * do tipo cirurgia, permitindo registar os dados clínicos
     * específicos associados ao procedimento cirúrgico.
     *
     * @param cirurgia objeto Cirurgia com os dados clínicos a persistir
     * @throws SQLException erro de acesso à base de dados
     */
    public void insert(Cirurgia cirurgia) throws SQLException {

        String sql =
            "INSERT INTO cirurgia (id_servico, descricao, duracao, procedimento) " +
            "VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cirurgia.getId_servico());
            ps.setString(2, cirurgia.getDescricao());

            // Conversão Duration -> TIME (SQL)
            Time tempoDuracao = Time.valueOf(
                LocalTime.MIDNIGHT.plus(cirurgia.getDuracao())
            );
            ps.setTime(3, tempoDuracao);

            ps.setString(4, cirurgia.getProcedimento());

            ps.executeUpdate();
        }
    }
}
