package com.biblioteca.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe responsável por criar e fornecer conexões com o banco de dados.
 * (Padrão de projeto: Factory)
 */
public class ConnectionFactory {

    // --- Seus Dados de Conexão ---
    // (Mantive exatamente os seus dados)
    private static final String URL = "jdbc:sqlserver://NOTE_GOMES:1433;databaseName=teste_Banco1;encrypt=false";
    private static final String USER = "sa";
    private static final String PASSWORD = "32452";
    private static final String DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    // --- Fim dos Dados ---

    /**
     * Tenta estabelecer uma nova conexão com o banco de dados.
     * * @return um objeto Connection pronto para ser usado.
     * @throws SQLException se a conexão falhar.
     */
    public static Connection getConnection() throws SQLException {
        try {
            // 1. Carrega o driver JDBC do SQL Server
            // (Em drivers modernos, isso é opcional, mas é uma boa prática garantir)
            Class.forName(DRIVER);

            // 2. Tenta conectar e retorna a conexão
            return DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (ClassNotFoundException e) {
            // Este erro acontece se o JAR do driver (mssql-jdbc.jar) não estiver no projeto
            throw new SQLException("Falha Crítica: Driver JDBC (mssql-jdbc.jar) não encontrado no ClassPath.", e);
        }
    }

    //
    // --- O MÉTODO desconectar() FOI REMOVIDO PROPOSITALMENTE ---
    //
    // Vamos usar o 'try-with-resources' em todos os nossos DAOs,
    // que fecha a conexão automaticamente, sendo mais seguro.
    //
}