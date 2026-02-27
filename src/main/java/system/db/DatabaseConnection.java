package system.db;
import java.sql.*;

public class DatabaseConnection {
    private static final String HOST = System.getenv("DB_HOST");
    private static final String PORT = System.getenv("DB_PORT");
    private static final String NAME = System.getenv("DB_NAME");;
    private static final String URL = String.format("jdbc:postgresql://%s:%s/%s", HOST, PORT, NAME);
    private static final String PASSWORD = System.getenv("DB_PASSWORD");
    private static final String USER = System.getenv("DB_USER");

    private static Connection connection;

    private DatabaseConnection() {}

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Other error hapend: " + e.getMessage());
        }

        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error clossing database: " + e.getMessage());
        }
    }
}
