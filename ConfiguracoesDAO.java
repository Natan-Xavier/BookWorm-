package biblioteca.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DAO para a entidade Configuracoes.
 */
public class ConfiguracoesDAO {

    /**
     * Busca o prazo padrão de dias para aluguel.
     * Busca na tabela 'Configuracoes' onde ID_Config = 1.
     * @return O número de dias (int) do prazo padrão.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public int buscarPrazoPadrao() throws SQLException {

        String sql = "SELECT prazo_padrao_dias FROM Configuracoes WHERE ID_Config = 1";
        //Valor padrão caso o banco falhe ou a tabela esteja vazia
        int prazoPadrao = 31;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                prazoPadrao = rs.getInt("prazo_padrao_dias");
            }
        }
        return prazoPadrao;
    }

    /**
     * Atualiza o prazo padrão de dias para aluguel.
     * Baseado no seu script SQL de 'Ajustar Prazo'.
     * @param novoPrazoEmDias O novo valor (int) para o prazo.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public void atualizarPrazoPadrao(int novoPrazoEmDias) throws SQLException {

        String sql = "UPDATE Configuracoes SET prazo_padrao_dias = ? WHERE ID_Config = 1";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, novoPrazoEmDias);
            pstmt.executeUpdate();
        }
    }
}