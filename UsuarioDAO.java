package biblioteca.dao;

import biblioteca.model.Usuario;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

/**
 * DAO para a entidade Usuario.
 * Esta classe é responsável por todas as interações com a tabela 'Usuario'
 */
public class UsuarioDAO {

    /**
     * Cadastra um novo usuário no banco de dados.
     * Baseado no seu script SQL de 'Cadastro'.
     * @param usuario O objeto Usuario (do model) preenchido com todos os dados.
     * @throws SQLException Se ocorrer um erro de SQL (ex: e-mail duplicado).
     */
    public void cadastrar(Usuario usuario) throws SQLException {

        String sql = "INSERT INTO Usuario (nome, E_mail, Senha, Sexo, data_Nasc, Credencial, Status_conta) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        //Usei o try-with-resources para garantir que a conexão e o statement
        //    sejam fechados automaticamente.
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            //Preenche os placeholders (?) com os dados do objeto Usuario
            pstmt.setString(1, usuario.getNome());
            pstmt.setString(2, usuario.getEmail());
            pstmt.setString(3, usuario.getSenha());
            pstmt.setString(4, usuario.getSexo());
            pstmt.setDate(5, usuario.getDataNascimento());
            pstmt.setString(6, usuario.getCredencial() != null ? usuario.getCredencial() : "Cliente");
            pstmt.setString(7, usuario.getStatusConta() != null ? usuario.getStatusConta() : "Ativo");
            //Executa o comando INSERT
            pstmt.executeUpdate();
        }
    }

    /**
     * Tenta autenticar um usuário com base no e-mail e na senha.
     * @param email O e-mail do usuário.
     * @param senhaHasheada A senha já processada (hash) para comparação.
     * @return Um objeto Usuario preenchido se o login for bem-sucedido, ou null se não for.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public Usuario login(String email, String senhaHasheada) throws SQLException {

        String sql = "SELECT ID_Usuario, nome, E_mail, Credencial, Status_conta " +
                "FROM Usuario " +
                "WHERE E_mail = ? AND Senha = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            //Preenche os placeholders (?)
            pstmt.setString(1, email);
            pstmt.setString(2, senhaHasheada);

            //Executa a query
            try (ResultSet rs = pstmt.executeQuery()) {

                //Verifica se o banco encontrou um usuário
                if (rs.next()) {
                    //Se encontrou, "monta" o objeto Usuario com os dados do banco
                    Usuario usuarioLogado = new Usuario();
                    usuarioLogado.setIdUsuario(rs.getInt("ID_Usuario"));
                    usuarioLogado.setNome(rs.getString("nome"));
                    usuarioLogado.setEmail(rs.getString("E_mail"));
                    usuarioLogado.setCredencial(rs.getString("Credencial"));
                    usuarioLogado.setStatusConta(rs.getString("Status_conta"));

                    return usuarioLogado;
                }
            }
        }

        //Se o 'if (rs.next())' for falso, não encontrou ninguém.
        return null;
    }


    //MÉTODO DE ATUALIZAR/EDITAR PEFIL

    /**
     * Atualiza os dados cadastrais de um usuário no banco.
     * Baseado no seu script SQL 'Atualização (Update)'.
     * @param usuario O objeto Usuario com os NOVOS dados (o ID é usado para encontrar).
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public void atualizarPerfil(Usuario usuario) throws SQLException {

        String sql = "UPDATE Usuario SET nome = ?, E_mail = ?, Senha = ?, Sexo = ?, data_Nasc = ? " +
                "WHERE ID_Usuario = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuario.getNome());
            pstmt.setString(2, usuario.getEmail());
            pstmt.setString(3, usuario.getSenha());
            pstmt.setString(4, usuario.getSexo());
            pstmt.setDate(5, usuario.getDataNascimento());
            pstmt.setInt(6, usuario.getIdUsuario());
            //Executa o UPDATE
            pstmt.executeUpdate();
        }
    }

    //MÉTODO DE BLOQUEAR/DESBLOQUEAR CONTA

    /**
     * Atualiza o Status_conta de um usuário (ex: 'Ativo' ou 'Bloqueado').
     * Baseado no seu script SQL 'Bloquear/Desbloquear Conta'.
     * @param idUsuario O ID do usuário a ser modificado.
     * @param novoStatus O novo status (ex: "Ativo" ou "Bloqueado").
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public void atualizarStatus(int idUsuario, String novoStatus) throws SQLException {

        String sql = "UPDATE Usuario SET Status_conta = ? WHERE ID_Usuario = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, novoStatus);
            pstmt.setInt(2, idUsuario);

            pstmt.executeUpdate();
        }
    }

    //MÉTODO REMOVER USUARIO

    /**
     * Remove um usuário do banco de dados (ADM ou Cliente).
     *
     * ATENÇÃO: Este método SÓ DELETA DA TABELA "USUARIO".
     *
     * Se o usuário for um cliente com pendências de aluguéis, este método
     * VAI FALHAR (erro de Foreign Key).
     *
     * A remoção de cliente DEVE ser feita por uma camada de 'Service'
     * que remove o histórico de 'Aluguel' ANTES de chamar este método,
     *
     * @param idUsuario O ID do usuário a ser removido.
     * @throws SQLException Se houver um erro (incluindo violação de FK).
     */
    public void remover(int idUsuario) throws SQLException {

        String sql = "DELETE FROM Usuario WHERE ID_Usuario = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            pstmt.executeUpdate();
        }
    }

    //MÉTODO DE BUSCAR POR ID

    /**
     * Busca um usuário completo pelo seu ID.
     * @param idUsuario O ID a ser buscado.
     * @return O objeto Usuario, ou null se não encontrado.
     * @throws SQLException
     */
    public Usuario buscarPorId(int idUsuario) throws SQLException {

        String sql = "SELECT * FROM Usuario WHERE ID_Usuario = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        }
        return null;
    }
    //MÉTODO DE LISTAGEM DE TODOS OS USUÁRIOS

    /**
     * Lista TODOS os usuários do sistema.
     * @return Uma lista de todos os Usuários.
     * @throws SQLException
     */
    public List<Usuario> listarTodos() throws SQLException {

        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM Usuario ORDER BY nome";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
        }
        return usuarios;
    }

    //MÉTODO DE LISTAGEM DE USUÁRIOS ESPECIFICOS POR CREDENCIAL
    /**
     * Lista todos os usuários de uma CREDENCIAL específica.
     * @param credencial A credencial (ex: "Cliente").
     * @return Uma lista de Usuários filtrada.
     * @throws SQLException
     */
    public List<Usuario> listarPorCredencial(String credencial) throws SQLException {

        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM Usuario WHERE Credencial = ? ORDER BY nome";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, credencial);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    usuarios.add(mapearUsuario(rs));
                }
            }
        }
        return usuarios;
    }


    //MÉTODO AUXILIAR ADICIONADO

    /**
     * Método 'helper' privado para "montar" um objeto Usuario
     * a partir de um ResultSet, evitando repetição de código.
     * @param rs O ResultSet (na linha correta).
     * @return O objeto Usuario preenchido.
     * @throws SQLException
     */
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(rs.getInt("ID_Usuario"));
        usuario.setNome(rs.getString("nome"));
        usuario.setEmail(rs.getString("E_mail"));
        usuario.setSenha(rs.getString("Senha")); // Importante para o 'atualizarPerfil'
        usuario.setSexo(rs.getString("Sexo"));
        usuario.setDataNascimento(rs.getDate("data_Nasc"));
        usuario.setDataCadastro(rs.getTimestamp("Data_cadastro"));
        usuario.setStatusConta(rs.getString("Status_conta"));
        usuario.setCredencial(rs.getString("Credencial"));
        return usuario;
    }

}