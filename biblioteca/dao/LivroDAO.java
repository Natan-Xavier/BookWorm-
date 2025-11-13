package com.biblioteca.dao;

import com.biblioteca.model.Autor;
import com.biblioteca.model.Editora;
import com.biblioteca.model.Genero;
import com.biblioteca.model.Livro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para a entidade Livro.
 */
public class LivroDAO {

    /**
     * Cadastra um novo livro no banco de dados, incluindo seus
     * autores e gêneros em uma transação.
     *
     * @param livro O objeto Livro (com autores, generos e editora) a ser salvo.
     * @throws SQLException Se ocorrer um erro no banco.
     */
    public void cadastrar(Livro livro) throws SQLException {

        // 1: Inserir o Livro principal.
        String sqlLivro = "INSERT INTO Livro (Titulo, ISBN, Sinopse, ano_publicacao, capa, data_Cadastro, paginas, livro, ID_Editora) " +
                          "VALUES (?, ?, ?, ?, ?, GETDATE(), ?, ?, ?)";

        // 2: Inserir na tabela-link de autores
        String sqlAutor = "INSERT INTO Livro_Autor (ID_livro, ID_Autor) VALUES (?, ?)";

        // 3: Inserir na tabela-link de gêneros
        String sqlGenero = "INSERT INTO Livro_Genero (ID_livro, ID_Genero) VALUES (?, ?)";

        // O try-with-resources garante que a conexão será fechada
        try (Connection conn = ConnectionFactory.getConnection()) {

            // INÍCIO DA TRANSAÇÃO
            // O auto-commit não está ligado. Então nada será salvo até mandar (conn.commit()).
            conn.setAutoCommit(false);

            int idNovoLivro = -1;

            // Parte 1: Salvar o livro principal
            try (PreparedStatement pstmtLivro = conn.prepareStatement(sqlLivro, Statement.RETURN_GENERATED_KEYS)) {

                pstmtLivro.setString(1, livro.getTitulo());
                pstmtLivro.setString(2, livro.getIsbn());
                pstmtLivro.setString(3, livro.getSinopse());
                pstmtLivro.setInt(4, livro.getAnoPublicacao());
                pstmtLivro.setString(5, livro.getCapaPath()); // VARCHAR (caminho)
                pstmtLivro.setInt(6, livro.getPaginas());
                pstmtLivro.setString(7, livro.getLivroPath()); // VARCHAR (caminho)
                pstmtLivro.setInt(8, livro.getEditora().getIdEditora());

                pstmtLivro.executeUpdate();

                // Pegar o ID gerado (SCOPE_IDENTITY())
                try (ResultSet rs = pstmtLivro.getGeneratedKeys()) {
                    if (rs.next()) {
                        idNovoLivro = rs.getInt(1);
                    }
                }
            }
            // Não conseguiu o ID solta essa execeção
            if (idNovoLivro == -1) {
                throw new SQLException("Falha ao cadastrar livro, não foi possível obter o ID.");
            }

            // Parte 2: Salvar os Autores (em Lote)
            try (PreparedStatement pstmtAutor = conn.prepareStatement(sqlAutor)) {
                for (Autor autor : livro.getAutores()) {
                    pstmtAutor.setInt(1, idNovoLivro);
                    pstmtAutor.setInt(2, autor.getIdAutor());
                    pstmtAutor.addBatch(); // Adiciona a operação ao estilo Batch ("lote")
                }
                pstmtAutor.executeBatch(); // Executa todas as operações de uma vez
            }

            // Parte 3: Salvar os Gêneros (em Lote)
            try (PreparedStatement pstmtGenero = conn.prepareStatement(sqlGenero)) {
                for (Genero genero : livro.getGeneros()) {
                    pstmtGenero.setInt(1, idNovoLivro);
                    pstmtGenero.setInt(2, genero.getIdGenero());
                    pstmtGenero.addBatch();
                }
                pstmtGenero.executeBatch();
            }

            // FIM DA TRANSAÇÃO
            // Se deu certo salvará no banco
            conn.commit();

        } catch (SQLException e) {
            // O catch é ativado, caso tenha dado falha em algum dos processos anteriores.

            // conn.rollback() // O try-with-resources já faz o rollback em caso de erro
            //                 // se o commit() não for chamado.

            // Apenas relançamos a exceção para a camada de serviço saber que falhou.
            throw new SQLException("Erro ao cadastrar livro (transação falhou): " + e.getMessage(), e);
        }
        // A conexão é fechada automaticamente aqui pelo try-with-resources.
    }

    // MÉTODO DE BUSCA
    /**
     * Busca livros no banco de dados com base em um termo.
     * A busca é feita no título, nome dos autores e nome dos gêneros.
     * @param termo O texto a ser buscado (ex: "Harry", "Rowling", "Fantasia")
     * @return Uma Lista de Livros (sem autores e generos preenchidos)
     * @throws SQLException
     */
    public List<Livro> buscarPorTermo(String termo) throws SQLException {
        String sql = "SELECT " +
                "    L.ID_livro, L.Titulo, L.ISBN, L.Sinopse, L.ano_publicacao, " +
                "    L.data_Cadastro, L.paginas, L.capa, L.livro, " +
                "    E.ID_Editora, E.nome_editora " +
                "FROM " +
                "    Livro AS L " +
                "JOIN " +
                "    Editora AS E ON L.ID_Editora = E.ID_Editora " +
                "WHERE " +
                "    L.Titulo LIKE ? " +
                "    OR EXISTS (SELECT 1 FROM Livro_Autor LA JOIN Autor A ON LA.ID_Autor = A.ID_Autor WHERE LA.ID_livro = L.ID_livro AND A.nome_autor LIKE ?) " +
                "    OR EXISTS (SELECT 1 FROM Livro_Genero LG JOIN Genero G ON LG.ID_Genero = G.ID_Genero WHERE LG.ID_livro = L.ID_livro AND G.nome_genero LIKE ?)";

        List<Livro> livros = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String termoBusca = "%" + termo + "%";
            pstmt.setString(1, termoBusca);
            pstmt.setString(2, termoBusca);
            pstmt.setString(3, termoBusca);

            try (ResultSet rs = pstmt.executeQuery()) {
                // Para cada linha que o banco retorna...
                while (rs.next()) {
                    // ...irá mapear o resultado para um objeto Livro
                    // e o adicionar à lista
                    livros.add(mapearLivroComEditora(rs));
                }
            }
        } catch (SQLException e) {
            // Em caso de erro, relança a exceção
            throw new SQLException("Erro ao buscar livros por termo: " + e.getMessage(), e);
        }

        return livros;
    }


    /**
     * MÉTODO AUXILIAR (HELPER)
     * Mapeia uma linha do ResultSet para um objeto Livro.
     * Este método NÃO mapeia autores ou gêneros tendo em vista velocidade de resposta.
     * @param rs O ResultSet
     * @return Um objeto Livro preenchido.
     * @throws SQLException
     */
    private Livro mapearLivroComEditora(ResultSet rs) throws SQLException {
        Livro livro = new Livro();
        livro.setIdLivro(rs.getInt("ID_livro"));
        livro.setTitulo(rs.getString("Titulo"));
        livro.setIsbn(rs.getString("ISBN"));
        livro.setSinopse(rs.getString("Sinopse"));
        livro.setAnoPublicacao(rs.getInt("ano_publicacao"));
        livro.setDataCadastro(rs.getDate("data_Cadastro"));
        livro.setPaginas(rs.getInt("paginas"));
        livro.setCapaPath(rs.getString("capa"));
        livro.setLivroPath(rs.getString("livro"));
        Editora editora = new Editora();
        editora.setIdEditora(rs.getInt("ID_Editora"));
        editora.setNome(rs.getString("nome_editora"));
        livro.setEditora(editora);

        return livro;
    }

    // MÉTODO PARA CARREGAR TUDO DO LIVRO NA PÁGINA
    /**
     * Busca um único livro pelo seu ID, carregando
     * todas as suas informações, incluindo a editora,
     * a lista de autores e a lista de gêneros.
     * @param idLivro O ID do livro a ser buscado.
     * @return Um objeto Livro totalmente preenchido, ou null se não for encontrado.
     * @throws SQLException
     */
    public Livro buscarPorId(int idLivro) throws SQLException {
        Livro livro = null;

        // 1: Busca o livro principal e a editora
        String sqlLivro = "SELECT " +
                "    L.ID_livro, L.Titulo, L.ISBN, L.Sinopse, L.ano_publicacao, " +
                "    L.data_Cadastro, L.paginas, L.capa, L.livro, " +
                "    E.ID_Editora, E.nome_editora " +
                "FROM Livro AS L " +
                "JOIN Editora AS E ON L.ID_Editora = E.ID_Editora " +
                "WHERE L.ID_livro = ?";

        // 2: Busca os autores
        String sqlAutores = "SELECT A.ID_Autor, A.nome_autor " +
                "FROM Autor A " +
                "JOIN Livro_Autor LA ON A.ID_Autor = LA.ID_Autor " +
                "WHERE LA.ID_livro = ?";

        // 3: Busca os gêneros
        String sqlGeneros = "SELECT G.ID_Genero, G.nome_genero " +
                "FROM Genero G " +
                "JOIN Livro_Genero LG ON G.ID_Genero = LG.ID_Genero " +
                "WHERE LG.ID_livro = ?";

        try (Connection conn = ConnectionFactory.getConnection()) {

            // Parte 1: Buscar o Livro principal
            try (PreparedStatement pstmt = conn.prepareStatement(sqlLivro)) {
                pstmt.setInt(1, idLivro);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        livro = mapearLivroComEditora(rs);
                    }
                }
            }

            // Se não encontrou o livro (livro == null), não há o que fazer.
            if (livro == null) {
                return null; // Retorna nulo (livro não encontrado)
            }

            // Parte 2: Buscar e adicionar os Autores
            try (PreparedStatement pstmtAutores = conn.prepareStatement(sqlAutores)) {
                pstmtAutores.setInt(1, idLivro);
                try (ResultSet rsAutores = pstmtAutores.executeQuery()) {
                    while (rsAutores.next()) {
                        Autor autor = new Autor();
                        autor.setIdAutor(rsAutores.getInt("ID_Autor"));
                        autor.setNome(rsAutores.getString("nome_autor"));
                        // Adicionamos o autor à lista dentro do objeto livro
                        livro.adicionarAutor(autor);
                    }
                }
            }

            // Parte 3: Buscar e adicionar os Gêneros
            try (PreparedStatement pstmtGeneros = conn.prepareStatement(sqlGeneros)) {
                pstmtGeneros.setInt(1, idLivro);
                try (ResultSet rsGeneros = pstmtGeneros.executeQuery()) {
                    while (rsGeneros.next()) {
                        Genero genero = new Genero();
                        genero.setIdGenero(rsGeneros.getInt("ID_Genero"));
                        genero.setNome(rsGeneros.getString("nome_genero"));
                        // Adicionamos o gênero à lista dentro do objeto livro
                        livro.adicionarGenero(genero);
                    }
                }
            }

        } catch (SQLException e) {
            throw new SQLException("Erro ao buscar livro por ID: " + e.getMessage(), e);
        }

        // Retorna o objeto Livro completo
        return livro;
    }


    // ... (Aqui virão os outros métodos: editar, remover) ...

}
}