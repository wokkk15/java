import java.sql.*;

public class checkUsername {
    public static void checkValidation(Connection connection, Statement statement, String username) {
        try {
            String checkUser = "SELECT * FROM users WHERE username = '" + username + "'";
            ResultSet resultSet = statement.executeQuery(checkUser);
            if (resultSet.next()) {
                System.out.println("Maaf, Username telah dipakai. Coba lagi.");
                Menu.registerUser(connection, statement);
            } else {
                System.out.println("Username tersedia.");
                // Lakukan tindakan yang sesuai jika username belum ada
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}