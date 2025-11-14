package com.biblioteca.dao;

import com.biblioteca.model.Editora;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para a entidade Editora.
 */
public class EditoraDAO {

    /**
     * Cadastra uma nova editora no banco de dados.
     * Após o cadastro, o ID gerado pelo banco é inserido
     * de volta no objeto 'editora' que foi passado como parâmetro.
     * @param editora O objeto Editora (apenas com nome) a ser inserido.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public void cadastrar(Editora editora) throws SQLException {

        String sql = "INSERT INTO Editora (nome_editora) VALUES (?)";

        //Usa o RETURN_GENERATED_KEYS para pegar o ID que o SQL Server criou
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, editora.getNome());
            pstmt.executeUpdate();

            //Pega o ID gerado e o coloca de volta no objeto
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    editora.setIdEditora(rs.getInt(1));
                }
            }
        }
    }

    //MÉTODO EDITAR/ATUALIZAR EDITORA

    /**
     * Atualiza os dados de uma editora existente no banco.
     * @param editora O objeto Editora com o ID e os novos dados.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public void editar(Editora editora) throws SQLException {

        String sql = "UPDATE Editora SET nome_editora = ? WHERE ID_Editora = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, editora.getNome());
            pstmt.setInt(2, editora.getIdEditora());
            pstmt.executeUpdate();
        }
    }

    //MÉTODO REMOVER EDITORA

    /**
     * Remove uma editora do banco de dados.
     *
     * IMPORTANTE: O banco de dados (SQL Server) irá falhar
     * (e lançar uma SQLException) se você tentar remover uma editora
     * que está associada a algum livro (erro de Foreign Key).
     *
     * A camada de 'Service' será responsável por tratar esse
     * erro e avisar o usuário.
     *
     * @param idEditora O ID da editora a ser removida.
     * @throws SQLException Se a remoção falhar (ex: Foreign Key).
     */
    public void remover(int idEditora) throws SQLException {

        String sql = "DELETE FROM Editora WHERE ID_Editora = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idEditora);
            pstmt.executeUpdate();
        }
    }

    //MÉTODO LISTAR EDITORA

    /**
     * Lista TODAS as editoras cadastrados, em ordem alfabética.
     * Essencial para popular dropdowns no WebSite.
     *
     * @return Uma Lista de objetos Editora.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public List<Editora> listarTodos() throws SQLException {

        List<Editora> editoras = new ArrayList<>();
        String sql = "SELECT ID_Editora, nome_editora FROM Editora ORDER BY nome_editora";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Editora editora = new Editora();
                editora.setIdEditora(rs.getInt("ID_Editora"));
                editora.setNome(rs.getString("nome_editora"));
                editoras.add(editora);
            }
        }
        return editoras;
    }

    //MÉTODO BUSCAR EDITORA POR ID

    /**
     * Busca uma editora específica pelo seu ID.
     * @param idEditora O ID da editora.
     * @return O objeto Editora preenchido, or null se não for encontrado.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public Editora buscarPorId(int idEditora) throws SQLException {

        String sql = "SELECT ID_Editora, nome_editora FROM Editora WHERE ID_Editora = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idEditora);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Editora editora = new Editora();
                    editora.setIdEditora(rs.getInt("ID_Editora"));
                    editora.setNome(rs.getString("nome_editora"));
                    return editora;
                }
            }
        }
        //Retorna null se o 'if (rs.next())' for falso
        return null;
    }
}