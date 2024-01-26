import java.sql.*;
public class Authenticator {
    public static void authenticateUser(Connection connection, Statement statement, String username, String password) {
        try {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement ps = statement.getConnection().prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                String statusVerifikasi = resultSet.getString("status_verifikasi");
                if (statusVerifikasi.equals("Verified")) {
                    System.out.println("Login berhasil!");
                    // Login berhasil jika akun sudah diverifikasi
                } else {
                    System.out.println("Akun belum diverifikasi. Silakan hubungi admin.");
                    Menu.loginUser(connection, statement); // Login gagal jika akun belum diverifikasi
                }
            } else {
                System.out.println("Login gagal. Username atau password salah.");
                // Login gagal jika username atau password salah
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Login gagal jika ada kesalahan saat query
        }
    }
}
