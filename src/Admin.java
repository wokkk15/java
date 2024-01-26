import java.sql.*;
import java.util.Scanner;

public class Admin extends Pengguna {

    public Admin(String nama, String username, String password, String email, String nohp) {
        super(nama, username, password, email, nohp);
    }

    public static void lihatDaftar(Statement statement) {
        // Menampilkan daftar pengguna yang bukan admin beserta status verifikasinya
        String getUsers = "SELECT username, role, status_verifikasi FROM users WHERE role <> 'Admin'";

        try (ResultSet usersResultSet = statement.executeQuery(getUsers)) {
            System.out.println("=== Daftar Pengguna dan Status Verifikasinya ===");
            System.out.printf("| %-5s | %-20s | %-15s |\n", "No", "Username", "Status Verifikasi");
            System.out.println("|-------|----------------------|-------------------|");

            int count = 1;
            while (usersResultSet.next()) {
                String username = usersResultSet.getString("username");
                String statusVerifikasi = usersResultSet.getString("status_verifikasi");

                System.out.printf("| %-5d | %-20s | %-15s |\n", count, username, statusVerifikasi);
                count++;
            }
            System.out.println("|-------|----------------------|-------------------|");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void verifikasi(Statement statement) {
        try {
            try (Scanner scanner = new Scanner(System.in)) {
                lihatDaftar(statement);
                System.out.print("Masukkan username Pengguna yang ingin diverifikasi: ");
                String usernameCek = scanner.nextLine();

                String updateQuery = "UPDATE users SET status_verifikasi = 'Verified' WHERE username = ?";
                PreparedStatement ps = statement.getConnection().prepareStatement(updateQuery);
                ps.setString(1, usernameCek);
                int updatedRows = ps.executeUpdate();

                if (updatedRows > 0) {
                    System.out.println("Akun " + usernameCek + " telah diverifikasi.");
                } else {
                    System.out.println("Gagal memverifikasi akun " + usernameCek + ".");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void tampilkanBerkas(Statement statement) {
        try {
            String queryPelatih = "SELECT * FROM Pelatih INNER JOIN users ON Pelatih.usernamePelatih = users.username";
            ResultSet pelatihResultSet = statement.executeQuery(queryPelatih);

            System.out.println("=== Daftar Pengguna dengan Role Pelatih ===");
            while (pelatihResultSet.next()) {
                // Ambil data dari tabel Pelatih dan Users yang terkait
                String usernamePelatih = pelatihResultSet.getString("username");
                String namaPelatih = pelatihResultSet.getString("namaPelatih");
                String jenisOlahraga = pelatihResultSet.getString("jenisOlahraga");
                int lamaPengalaman = pelatihResultSet.getInt("lamaPengalaman");
                String statusVerifikasi = pelatihResultSet.getString("status_verifikasi");

                // Tampilkan informasi yang relevan untuk admin
                System.out.println("Username: " + usernamePelatih);
                System.out.println("Nama: " + namaPelatih);
                System.out.println("Jenis Olahraga: " + jenisOlahraga);
                System.out.println("Lama Pengalaman: " + lamaPengalaman + " bulan");
                System.out.println("Status Verifikasi: " + statusVerifikasi);
                System.out.println("-----------------------------------------");
            }

            // Lakukan hal yang serupa untuk AhliGizi
            String queryAhliGizi = "SELECT * FROM AhliGizi INNER JOIN users ON AhliGizi.usernameAhliGizi = users.username";
            ResultSet ahliGiziResultSet = statement.executeQuery(queryAhliGizi);

            System.out.println("=== Daftar Pengguna dengan Role Ahli Gizi ===");
            while (ahliGiziResultSet.next()) {
                // Ambil data dari tabel AhliGizi dan Users yang terkait
                String usernameAhliGizi = ahliGiziResultSet.getString("username");
                String namaAhliGizi = ahliGiziResultSet.getString("namaAhliGizi");
                int pengalamanKerja = ahliGiziResultSet.getInt("pengalamanKerja");
                String bidangKeahlian = ahliGiziResultSet.getString("bidangKeahlian");
                String statusVerifikasi = ahliGiziResultSet.getString("status_verifikasi");

                // Tampilkan informasi yang relevan untuk admin
                System.out.println("Username: " + usernameAhliGizi);
                System.out.println("Nama: " + namaAhliGizi);
                System.out.println("Bidang Keahlian: " + bidangKeahlian);
                System.out.println("Pengalaman Kerja: " + pengalamanKerja + " bulan");
                System.out.println("Status Verifikasi: " + statusVerifikasi);
                System.out.println("-----------------------------------------");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void displayAllUserInformation(Statement statement) {
        try {
            String query = "SELECT * FROM users";

            ResultSet resultSet = statement.executeQuery(query);

            System.out.println("=======================================");
            System.out.printf("| %-4s | %-15s | %-10s \n", "ID", "Username", "Role");
            System.out.println("=======================================");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String role = resultSet.getString("role");
                String email = resultSet.getString("email");
                String nohp = resultSet.getString("nohp");
                String nama = resultSet.getString("nama");
                //TODO

                System.out.printf("| %-4d | %-15s | %-10s \n", id, username, role);
                System.out.println("|-------------------------------------|");
                System.out.println("| Email       : " + email);
                System.out.println("| No HP       : " + nohp);
                System.out.println("| Nama Lengkap: " + nama);
                System.out.println("|-------------------------------------|");
            }

            System.out.println("=======================================");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void searchUser(Statement statement) {
        try {
            try (Scanner scanner = new Scanner(System.in)) {
                System.out.print("Masukkan nama pengguna yang ingin dicari: ");
                String searchKeyword = scanner.nextLine();

                String query = "SELECT * FROM users WHERE username LIKE '%" + searchKeyword + "%'";

                ResultSet resultSet = statement.executeQuery(query);

                System.out.println("=======================================");
                System.out.printf("| %-4s | %-15s | %-10s |\n", "ID", "Username", "Role");
                System.out.println("=======================================");

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String username = resultSet.getString("username");
                    String role = resultSet.getString("role");
                    String email = resultSet.getString("email");
                    String nohp = resultSet.getString("nohp");
                    String nama = resultSet.getString("nama");

                    System.out.printf("| %-4d | %-15s | %-10s |\n", id, username, role);
                    System.out.println("|-------------------------------------|");
                    System.out.println("| Email       : " + email);
                    System.out.println("| No HP       : " + nohp);
                    System.out.println("| Nama Lengkap: " + nama);
                    System.out.println("|-------------------------------------|");
                }
            }
            System.out.println("=======================================");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}