package biblioteca.dao;

import biblioteca.model.Aluguel;
import biblioteca.model.Livro;
import biblioteca.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para a entidade Aluguel.
 */
public class AluguelDAO {

    /**
     * Cria um novo registro de aluguel no banco de dados.
     * @param aluguel O objeto Aluguel contendo (no mínimo) o Usuário, o Livro e a data_prevista_dev.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public void alugar(Aluguel aluguel) throws SQLException {

        String sql = "INSERT INTO Aluguel (ID_Usuario, ID_livro, data_prevista_dev) VALUES (?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            //Pega os IDs
            pstmt.setInt(1, aluguel.getUsuario().getIdUsuario());
            pstmt.setInt(2, aluguel.getLivro().getIdLivro());
            pstmt.setDate(3, aluguel.getDataPrevistaDevolucao());

            pstmt.executeUpdate();
        }
    }
    //MÉTODO VISUALIZAR HISTÓRICO DO ALUGUEL

    /**
     * Busca o histórico de aluguéis de um usuário específico.
     * Baseado na sua query 'Visualizar Histórico do Aluguel'.
     * @param idUsuario O ID do usuário.
     * @return Uma lista de objetos Aluguel (preenchidos parcialmente para a view).
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public List<Aluguel> buscarHistoricoPorUsuario(int idUsuario) throws SQLException {

        List<Aluguel> historico = new ArrayList<>();

        String sql = "SELECT " +
                "  L.Titulo, " +
                "  A.data_aluguel, " +
                "  A.data_prevista_dev, " +
                "  A.data_efetiva_dev, " +
                "  A.ID_aluguel, " +
                "  CASE " +
                "    WHEN A.data_efetiva_dev IS NULL AND A.data_prevista_dev < GETDATE() THEN 'Atrasado' " +
                "    WHEN A.data_efetiva_dev IS NULL THEN 'Ativo' " +
                "    ELSE 'Devolvido' " +
                "  END AS Status_Aluguel " +
                "FROM Aluguel AS A " +
                "JOIN Livro AS L ON A.ID_livro = L.ID_livro " +
                "WHERE A.ID_Usuario = ? " +
                "ORDER BY A.data_aluguel DESC";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);

            try (ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {

                    Aluguel aluguel = new Aluguel();
                    aluguel.setIdAluguel(rs.getInt("ID_aluguel"));
                    aluguel.setDataAluguel(rs.getTimestamp("data_aluguel"));
                    aluguel.setDataPrevistaDevolucao(rs.getDate("data_prevista_dev"));
                    aluguel.setDataEfetivaDevolucao(rs.getDate("data_efetiva_dev"));
                    aluguel.setStatusAluguel(rs.getString("Status_Aluguel"));

                    //Como esta query é para uma 'view', ela não traz o objeto Livro
                    //completo. Cria um objeto 'Livro' "fake" apenas com
                    //os dados que a query nos deu (o Título).
                    Livro livroParcial = new Livro();
                    livroParcial.setTitulo(rs.getString("Titulo"));

                    aluguel.setLivro(livroParcial);

                    //(O objeto Usuario não é preenchido aqui, pois já sabemos quem é)
                    historico.add(aluguel);
                }
            }
        }
        return historico;
    }

    //MÉTODO DEVOLVER LVIRO

    /**
     * Registra a devolução de um livro, definindo a data_efetiva_dev para agora.
     * @param idAluguel O ID do registro de aluguel a ser devolvido.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public void devolver(int idAluguel) throws SQLException {

        //Define a data_efetiva_dev para a data/hora atual
        //e só atualiza se o livro ainda não foi devolvido.
        String sql = "UPDATE Aluguel SET data_efetiva_dev = GETDATE() " +
                "WHERE ID_aluguel = ? AND data_efetiva_dev IS NULL";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idAluguel);
            pstmt.executeUpdate();
        }
    }

    //MÉTODO DE REMOVER HISTÓRICO DO USUÁRIO

    /**
     * Remove TODO o histórico de aluguéis de um usuário específico.
     *
     * Este método é crucial e deve ser chamado ANTES de tentar
     * REMOVER UM USUÁRIO (Cliente) da tabela 'Usuario', para evitar
     * erros de Foreign Key.
     *
     * @param idUsuario O ID do usuário cujo histórico será apagado.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public void removerHistoricoPorUsuario(int idUsuario) throws SQLException {

        String sql = "DELETE FROM Aluguel WHERE ID_Usuario = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            pstmt.executeUpdate();
        }
    }

    /*
     * Nota: O método removerPorLivro() não é necessário aqui, pois
     * essa lógica já está (corretamente) dentro da transação do
     * método LivroDAO.remover().
     */

}