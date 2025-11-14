package com.biblioteca.dao;

import com.biblioteca.model.Genero;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para a entidade Genero.
 */
public class GeneroDAO {

    /**
     * Cadastra um novo gênero no banco de dados.
     * Após o cadastro, o ID gerado pelo banco é inserido
     * de volta no objeto 'genero' que foi passado como parâmetro.
     * @param genero O objeto Genero (apenas com nome) a ser inserido.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public void cadastrar(Genero genero) throws SQLException {

        String sql = "INSERT INTO Genero (nome_genero) VALUES (?)";

        //Usa o RETURN_GENERATED_KEYS para pegar o ID que o SQL Server criou
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, genero.getNome());
            pstmt.executeUpdate();

            //Pega o ID gerado e o coloca de volta no objeto
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    genero.setIdGenero(rs.getInt(1));
                }
            }
        }
    }

    //MÉTODO EDITAR/ATUALIZAR GENERO
    /**
     * Atualiza os dados de um gênero existente no banco.
     * @param genero O objeto Genero com o ID e os novos dados.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public void editar(Genero genero) throws SQLException {

        String sql = "UPDATE Genero SET nome_genero = ? WHERE ID_Genero = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, genero.getNome());
            pstmt.setInt(2, genero.getIdGenero());
            pstmt.executeUpdate();
        }
    }

    //MÉTODO REMOVER GENERO

    /**
     * Remove um gênero do banco de dados.
     *
     * IMPORTANTE: O banco de dados (SQL Server) irá falhar
     * (e lançar uma SQLException) se você tentar remover um gênero
     * que está associado a algum livro (erro de Foreign Key).
     *
     * A camada de 'Service' será responsável por tratar
     * esse erro e avisar o usuário.
     *
     * @param idGenero O ID do gênero a ser removido.
     * @throws SQLException Se a remoção falhar (ex: Foreign Key).
     */
    public void remover(int idGenero) throws SQLException {

        String sql = "DELETE FROM Genero WHERE ID_Genero = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idGenero);
            pstmt.executeUpdate();
        }
    }

    //MÉTODO LISTAR GENERO

    /**
     * Lista TODOS os gêneros cadastrados, em ordem alfabética.
     * Essencial para popular dropdowns no WebSite.
     * @return Uma Lista de objetos Genero.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public List<Genero> listarTodos() throws SQLException {

        List<Genero> generos = new ArrayList<>();
        String sql = "SELECT ID_Genero, nome_genero FROM Genero ORDER BY nome_genero";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Genero genero = new Genero();
                genero.setIdGenero(rs.getInt("ID_Genero"));
                genero.setNome(rs.getString("nome_genero"));
                generos.add(genero);
            }
        }
        return generos;
    }

    //MÉTODO BUSCAR GENERO POR ID

    /**
     * Busca um gênero específico pelo seu ID.
     *
     * @param idGenero O ID do gênero.
     * @return O objeto Genero preenchido, or null se não for encontrado.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public Genero buscarPorId(int idGenero) throws SQLException {

        String sql = "SELECT ID_Genero, nome_genero FROM Genero WHERE ID_Genero = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idGenero);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Genero genero = new Genero();
                    genero.setIdGenero(rs.getInt("ID_Genero"));
                    genero.setNome(rs.getString("nome_genero"));
                    return genero;
                }
            }
        }
        //Retorna null se o 'if (rs.next())' for falso
        return null;
    }
}