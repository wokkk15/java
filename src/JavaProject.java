import java.sql.*;

public class JavaProject {
    //isi
    public static void main(String[] args) throws SQLException {
        Connection connection = null;
        try {
            // Mendapatkan koneksi dari DatabaseCON
            connection = databaseCON.getConnection();
            // Panggil metode buatTabel dengan koneksi yang disediakan
            tabelDatabase.buatTabel(connection);
            // Tampilkan menu setelah membuat tabel
            Menu.tampilkanMenu();
        } finally {
            // Tutup koneksi setelah selesai digunakan
            try {
                if (connection != null) {
                    connection.close();

                    System.out.println("Koneksi ditutup.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
