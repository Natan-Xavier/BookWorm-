package biblioteca.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe responsável por criar e fornecer conexões com o banco de dados.
 * (Padrão de projeto: Factory)
 */
public class ConnectionFactory {

    //1. A URL foi modificada:
    private static final String URL = "jdbc:sqlserver://DESKTOP-GO01ONT\\MSSQLSERVER01;databaseName=teste_Banco1;integratedSecurity=true;trustServerCertificate=true";
    private static final String USER = "";
    private static final String PASSWORD = "";
    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    //Fim dos Dados

    /**
     * Tenta estabelecer uma nova conexão com o banco de dados.
     * * @return um objeto Connection pronto para ser usado.
     * @throws SQLException se a conexão falhar.
     */
    public static Connection getConnection() throws SQLException {
        try {
            //1. Carrega o driver JDBC do SQL Server
            Class.forName(DRIVER);

            //2. Tenta conectar e retorna a conexão
            return DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (ClassNotFoundException e) {
            //Este erro acontece se o JAR do driver (mssql-jdbc.jar) não estiver no projeto
            throw new SQLException("Falha Crítica: Driver JDBC (mssql-jdbc.jar) não encontrado no ClassPath.", e);
        }
    }

}