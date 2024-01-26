import java.sql.*;

public class Pengguna {
    protected String nama;
    protected String username;
    protected String password;
    protected String email;
    protected String nohp;
    private String role;

    public Pengguna(String nama, String username, String password, String email, String nohp) {
        this.nama = nama;
        this.username = username;
        this.password = password;
        this.email = email;
        this.nohp = nohp;
    }

    public String getRole() {
        return this.role;
    }

    public String getUsername() {
        return this.username;
    }

    public String getNama() {
        return this.nama;
    }
    // Konstruktor dan metode lain yang bersifat umum untuk semua jenis pengguna

    public void register(Connection connection, Statement statement, String role) throws SQLException {
        String insertUser = "INSERT INTO users (username, password, email, nohp, nama, role) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps1 = connection.prepareStatement(insertUser)) {
            ps1.setString(1, this.username);
            ps1.setString(2, this.password);
            ps1.setString(3, this.email);
            ps1.setString(4, this.nohp);
            ps1.setString(5, this.nama);
            ps1.setString(6, role);
            ps1.executeUpdate();
            this.role = role;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean login(Connection connection, String username, String password) throws SQLException {
        String checkUser = "SELECT * FROM users WHERE username = ? AND password = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(checkUser);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            this.username = resultSet.getString("username");
            this.password = resultSet.getString("password");
            this.role = resultSet.getString("role");
            return true; // Otentikasi berhasil
        }

        return false; // Otentikasi gagal
    }

}