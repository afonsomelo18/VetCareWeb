package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



/**
 * Classe utilitária responsável pela gestão da ligação à base de dados MySQL
 * através da API JDBC.
 * 
 * Esta classe encapsula o processo de carregamento do driver JDBC e a criação
 * de ligações à base de dados, seguindo o fluxo padrão de utilização do JDBC.
 * 
 */
public final class DBConnection 
{
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver"; // Nome do driver JDBC do MySQL
    private static final String HOST = "localhost"; // Servidor MySQL
    private static final String PORT = "3306"; // Porta do MySQL
    private static final String DATABASE = "VetCare"; // Nome da base de dados
    private static final String USER = "root"; // Utilizador da base de dados
    private static final String PASSWORD = "";
    private static final String PARAMETERS =
            "?useSSL=false&serverTimezone=UTC"; // Parâmetros adicionais da ligação JDBC
    private static final String SERVER_URL =
            "jdbc:mysql://" + HOST + ":" + PORT + PARAMETERS; // URL base do servidor MySQL (sem base de dados)
    private static final String DB_URL =
            "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + PARAMETERS; // URL completa com a base de dados
    private static boolean driverLoaded = false; // Indica se o driver já foi carregado
    
    
    
    /**
     * Construtor privado para impedir instanciação.
     */
    private DBConnection() {}
    
    
    /**
     * Obtém uma ligação à base de dados.
     * Caso a base de dados ainda não exista, esta será criada automaticamente.
     * 
     *
     * @return ligação JDBC ativa
     * @throws SQLException se ocorrer um erro de ligação
     */
    public static Connection getConnection() throws SQLException
    {
    	loadDriver();

        if (!databaseExists()) 
        {
            createDatabase();
        }

        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }
    
    
    
    /**
     * Carrega o driver JDBC do MySQL.
     *
     * @throws SQLException caso o driver não seja encontrado
     */
    private static void loadDriver() throws SQLException 
    {
        if (!driverLoaded) 
        {
            try 
            {
                Class.forName(DRIVER);
                driverLoaded = true;
            } 
            catch (ClassNotFoundException e) 
            {
                throw new SQLException("Driver JDBC do MySQL não encontrado.", e);
            }
        }
    }

    
    
    /**
     * Verifica se a base de dados já existe no servidor MySQL.
     *
     * @return {@code true} se a base de dados existir, {@code false} caso contrário
     */
    private static boolean databaseExists() 
    {

        try (Connection conn = DriverManager.getConnection(SERVER_URL, USER, PASSWORD);
             ResultSet catalogs = conn.getMetaData().getCatalogs()) 
        {
            while (catalogs.next())
            {
                String dbName = catalogs.getString(1);
                if (dbName.equalsIgnoreCase(DATABASE)) 
                {
                    return true;
                }
            }

        } 
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return false;
    }
    
    
    /**
     * Cria a base de dados no servidor MySQL.
     *
     * @throws SQLException se ocorrer um erro durante a criação
     */
    private static void createDatabase() throws SQLException 
    {

        try (Connection conn = DriverManager.getConnection(SERVER_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) 
        {

            String sql = "CREATE DATABASE " + DATABASE;
            stmt.executeUpdate(sql);
        }
    }
    
    
    public static void main(String[] args) {

        try (Connection conn = DBConnection.getConnection()) {

            if (conn != null && !conn.isClosed()) {
                System.out.println("Ligação à base de dados estabelecida com sucesso.");
            } else {
                System.out.println("Ligação falhou.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao ligar à base de dados:");
            System.out.println("Mensagem: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("Código de erro: " + e.getErrorCode());
        }
    }

}



