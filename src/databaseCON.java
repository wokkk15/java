import java.sql.*;

public class databaseCON {
    private static final String URL = "jdbc:mysql://localhost:3306/java_project";
    private static final String USER = "root";
    private static final String PASSWORD = "osa123";
    private static databaseCON instance;
    private Connection connection;

    private databaseCON() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Berhasil terhubung ke database.");
        } catch (SQLException e) {
            throw new RuntimeException("Gagal terhubung ke database.", e);
        }
    }

    public static synchronized Connection getConnection() {
        if (instance == null) {
            instance = new databaseCON();
        }
        return instance.connection;
    }
}