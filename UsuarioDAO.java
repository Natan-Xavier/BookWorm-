package biblioteca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Classe UsuarioDAO (CORRIGIDA)
 * A verificação de credencial agora busca por "Funcionario" (sem acento).
 */
public class UsuarioDAO {

    // O método registrarUsuario(Usuario usuario) estava 100% correto
    // e não precisa de alterações.
    public boolean registrarUsuario(Usuario usuario) {
        String sql = "INSERT INTO Usuario (nome, E_mail, Senha, Sexo, data_Nasc, " +
                "Data_cadastro, Status_conta, Credencial) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoDAO.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuario.getNome());
            pstmt.setString(2, usuario.getEmail());
            pstmt.setString(3, usuario.getSenha());
            pstmt.setString(4, usuario.getSexo());
            pstmt.setDate(5, java.sql.Date.valueOf(usuario.getDataNascimento()));
            pstmt.setTimestamp(6, java.sql.Timestamp.valueOf(usuario.getDataCadastro()));
            pstmt.setString(7, usuario.getStatusConta());
            pstmt.setString(8, usuario.getCredencial()); // Já usará "Funcionario" (sem acento)

            int linhasAfetadas = pstmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao registrar usuário: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // O método validarLogin(String email, String senha) estava 100% correto
    // e não precisa de alterações. (O erro estava no método helper que ele chama).
    public Usuario validarLogin(String email, String senha) {
        String sql = "SELECT * FROM Usuario WHERE E_mail = ?";

        try (Connection conn = ConexaoDAO.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String senhaBanco = rs.getString("Senha");
                    if (senha.equals(senhaBanco)) {
                        // Chama o método helper corrigido
                        return criarUsuarioDoResultSet(rs);
                    } else {
                        return null; // Senha incorreta
                    }
                } else {
                    return null; // E-mail não encontrado
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao validar login: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Método Helper (Fábrica) - CORRIGIDO
     */
    private Usuario criarUsuarioDoResultSet(ResultSet rs) throws SQLException {

        // 1. Coleta todos os 9 campos da tabela
        // (Já estava correto, usando os nomes exatos das colunas: E_mail, data_Nasc, etc.)
        int id = rs.getInt("ID_Usuario");
        String nome = rs.getString("nome");
        String email = rs.getString("E_mail");
        String senha = rs.getString("Senha");
        String sexo = rs.getString("Sexo");
        LocalDate dataNasc = rs.getDate("data_Nasc").toLocalDate();
        LocalDateTime dataCadastro = rs.getTimestamp("Data_cadastro").toLocalDateTime();
        String statusConta = rs.getString("Status_conta");
        String credencial = rs.getString("Credencial");

        // 2. Decide qual classe instanciar (Polimorfismo!)

        if (credencial.equalsIgnoreCase("ADM")) {
            return new Administrador(id, nome, email, senha, sexo, dataNasc,
                    dataCadastro, statusConta, credencial);

            // --- CORREÇÃO AQUI ---
        } else if (credencial.equalsIgnoreCase("Funcionario")) { // Removido o acento
            return new Funcionario(id, nome, email, senha, sexo, dataNasc,
                    dataCadastro, statusConta, credencial);

        } else { // O padrão é "Cliente"
            return new Cliente(id, nome, email, senha, sexo, dataNasc,
                    dataCadastro, statusConta, credencial);
        }
    }
}