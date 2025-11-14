package com.biblioteca.dao;

import com.biblioteca.model.Autor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para a entidade Autor.
 */
public class AutorDAO {

    /**
     * Cadastra um novo autor no banco de dados.
     * Após o cadastro, o ID gerado pelo banco é inserido
     * de volta no objeto 'autor' que foi passado como parâmetro.
     * @param autor O objeto Autor (apenas com nome) a ser inserido.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public void cadastrar(Autor autor) throws SQLException {

        String sql = "INSERT INTO Autor (nome_autor) VALUES (?)";

        //Usa o RETURN_GENERATED_KEYS para pegar o ID que o SQL Server criou
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, autor.getNome());
            pstmt.executeUpdate();
            //Pega o ID gerado e o coloca de volta no objeto
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    autor.setIdAutor(rs.getInt(1));
                }
            }
        }
    }

    //MÉTODO EDITAR/ATUALIZAR AUTOR
    /**
     * Atualiza os dados de um autor existente no banco.
     * @param autor O objeto Autor com o ID e os novos dados.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public void editar(Autor autor) throws SQLException {

        String sql = "UPDATE Autor SET nome_autor = ? WHERE ID_Autor = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, autor.getNome());
            pstmt.setInt(2, autor.getIdAutor());
            pstmt.executeUpdate();
        }
    }

    //MÉTODO REMOVER AUTOR
    /**
     * Remove um autor do banco de dados.
     *
     * IMPORTANTE: O banco de dados (SQL Server) irá falhar
     * (e lançar uma SQLException) se tentar remover um autor
     * que está associado a algum livro (erro de Foreign Key).
     *
     * Isso é o comportamento esperado. A camada de 'Service'
     * será responsável por tratar esse erro
     * e avisar o usuário (ex: "Não pode remover autor com livros cadastrados").
     *
     * @param idAutor O ID do autor a ser removido.
     * @throws SQLException Se a remoção falhar (ex: Foreign Key).
     */
    public void remover(int idAutor) throws SQLException {

        String sql = "DELETE FROM Autor WHERE ID_Autor = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idAutor);
            pstmt.executeUpdate();
        }
    }

    //MÉTODO LISTAR AUTOR
    /**
     * Lista TODOS os autores cadastrados, em ordem alfabética.
     * Essencial para popular dropdowns no WebSite.
     * @return Uma Lista de objetos Autor.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public List<Autor> listarTodos() throws SQLException {

        List<Autor> autores = new ArrayList<>();
        String sql = "SELECT ID_Autor, nome_autor FROM Autor ORDER BY nome_autor";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Autor autor = new Autor();
                autor.setIdAutor(rs.getInt("ID_Autor"));
                autor.setNome(rs.getString("nome_autor"));
                autores.add(autor);
            }
        }
        return autores;
    }

    //MÉTODO BUSCA AUTOR POR ID

    /**
     * Busca um autor específico pelo seu ID.
     * @param idAutor O ID do autor.
     * @return O objeto Autor preenchido, or null se não for encontrado.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public Autor buscarPorId(int idAutor) throws SQLException {

        String sql = "SELECT ID_Autor, nome_autor FROM Autor WHERE ID_Autor = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idAutor);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Autor autor = new Autor();
                    autor.setIdAutor(rs.getInt("ID_Autor"));
                    autor.setNome(rs.getString("nome_autor"));
                    return autor;
                }
            }
        }
        //Retorna null se o 'if (rs.next())' for falso
        return null;
    }
}