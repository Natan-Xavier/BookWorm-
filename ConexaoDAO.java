package biblioteca; // Ou o pacote onde você guarda seus DAOs

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe de conexão atualizada com os dados específicos do seu banco.
 */
public class ConexaoDAO {

    // --- Seus Dados de Conexão ---
    private static final String URL = "jdbc:sqlserver://NOTE_GOMES:1433;databaseName=teste_Banco1;encrypt=false";
    private static final String USER = "sa";
    private static final String PASSWORD = "32452";
    // --- Fim dos Dados ---

    /**
     * Tenta estabelecer uma conexão com o banco de dados.
     * @return um objeto Connection.
     * @throws SQLException se a conexão falhar.
     */
    public static Connection conectar() throws SQLException {
        try {
            // 1. Carrega o driver JDBC do SQL Server
            //
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // 2. Conecta usando a URL, usuário e senha fornecidos
            // É a forma mais segura e recomendada de conectar.
            return DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (ClassNotFoundException e) {
            // Este erro acontece se o JAR do driver (mssql-jdbc.jar) não estiver no projeto
            throw new SQLException("Driver JDBC do SQL Server (mssql-jdbc.jar) não encontrado. " +
                    "Verifique o Build Path do seu projeto.", e);
        }
    }

    /**
     * Método utilitário para fechar a conexão de forma segura.
     */
    public static void desconectar(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão: " + e.getMessage());
            }
        }
    }
}