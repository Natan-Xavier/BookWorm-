package biblioteca;

import java.io.InputStream;
import java.sql.*;

public class LivroDAO {

    // Configuração do banco
    private static final String URL = "jdbc:sqlserver://NOTE_GOMES:1433;databaseName=teste_Banco1;encrypt=false";
    private static final String USER = "sa";
    private static final String PASSWORD = "32452";

    // Método para inserir um livro completo
    public void adicionarLivroCompleto(
            String titulo,
            String isbn,
            String sinopse,
            int ano,
            InputStream capa, long tamanhoCapa,
            int paginas,
            InputStream livro, long tamanhoLivro,
            int idEditora,
            int idAutor,
            int idGenero
    ) {
        Connection conn = null;
        PreparedStatement stmtLivro = null;
        PreparedStatement stmtAutor = null;
        PreparedStatement stmtGenero = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            conn.setAutoCommit(false); // Início da transação

            // 1️⃣ Inserir livro principal
            String sqlLivro = """
                INSERT INTO Livro (Titulo, ISBN, Sinopse, ano_publicacao, capa, paginas, livro, ID_Editora)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?);
                """;

            stmtLivro = conn.prepareStatement(sqlLivro, Statement.RETURN_GENERATED_KEYS);
            stmtLivro.setString(1, titulo);
            stmtLivro.setString(2, isbn);
            stmtLivro.setString(3, sinopse);
            stmtLivro.setInt(4, ano);
            stmtLivro.setBinaryStream(5, capa, (int) tamanhoCapa);
            stmtLivro.setInt(6, paginas);
            stmtLivro.setBinaryStream(7, livro, (int) tamanhoLivro);
            stmtLivro.setInt(8, idEditora);

            int linhasAfetadas = stmtLivro.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new SQLException("Falha ao inserir o livro: nenhuma linha afetada.");
            }

            // Captura o ID gerado
            rs = stmtLivro.getGeneratedKeys();
            int idNovoLivro = 0;
            if (rs.next()) {
                idNovoLivro = rs.getInt(1);
                System.out.println("✅ Novo livro inserido com ID: " + idNovoLivro);
            } else {
                throw new SQLException("Falha ao obter o ID do novo livro.");
            }

            // 2️⃣ Associar autor
            String sqlAutor = "INSERT INTO Livro_Autor (ID_livro, ID_Autor) VALUES (?, ?)";
            stmtAutor = conn.prepareStatement(sqlAutor);
            stmtAutor.setInt(1, idNovoLivro);
            stmtAutor.setInt(2, idAutor);
            stmtAutor.executeUpdate();
            System.out.println("✅ Autor associado com sucesso.");

            // 3️⃣ Associar gênero
            String sqlGenero = "INSERT INTO Livro_Genero (ID_livro, ID_Genero) VALUES (?, ?)";
            stmtGenero = conn.prepareStatement(sqlGenero);
            stmtGenero.setInt(1, idNovoLivro);
            stmtGenero.setInt(2, idGenero);
            stmtGenero.executeUpdate();
            System.out.println("✅ Gênero associado com sucesso.");

            // 4️⃣ Commit final
            conn.commit();
            System.out.println("🎉 Livro cadastrado com sucesso no banco!");

        } catch (SQLException ex) {
            System.err.println("⚠️ Erro ao inserir livro completo:");
            ex.printStackTrace(); // mostra o erro detalhado
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("⏪ Transação revertida (rollback).");
                } catch (SQLException e2) {
                    e2.printStackTrace();
                }
            }
        } finally {
            // Fecha os recursos
            try { if (rs != null) rs.close(); } catch (SQLException ignored) {}
            try { if (stmtLivro != null) stmtLivro.close(); } catch (SQLException ignored) {}
            try { if (stmtAutor != null) stmtAutor.close(); } catch (SQLException ignored) {}
            try { if (stmtGenero != null) stmtGenero.close(); } catch (SQLException ignored) {}
            try { if (conn != null) conn.close(); } catch (SQLException ignored) {}
        }
    }
}
