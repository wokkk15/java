import java.sql.*;
public class viewPelatih {
    public static void lihatDaftar(Statement statement) {
        String query = "SELECT p.usernamePelatih, p.jenisOlahraga, r.username, p.lamaPengalaman/12 AS pengalamanTahun, p.lamaPengalaman%12 AS pengalamanBulan " +
                "FROM Pelatih p " +
                "LEFT JOIN reservasiPelatih r ON p.usernamePelatih = r.pelatihUsername";

        try (ResultSet resultSet = statement.executeQuery(query)) {
            System.out.println("=== Daftar Pelatih ===");
            System.out.printf("| %-5s | %-20s | %-20s | %-20s | %-20s |\n", "No", "Pelatih", "Jenis Olahraga", "Reservasi Oleh", "Pengalaman");
            System.out.println("|-------|----------------------|----------------------|----------------------|----------------------|");

            int count = 1;
            while (resultSet.next()) {
                String coach = resultSet.getString("usernamePelatih");
                String jenisOlahraga = resultSet.getString("jenisOlahraga");
                String reservasi = resultSet.getString("username");
                int pengalamanTahun = resultSet.getInt("pengalamanTahun");
                int pengalamanBulan = resultSet.getInt("pengalamanBulan");

                System.out.printf("| %-5d | %-20s | %-20s | %-20s | %-4d tahun %-2d bulan |\n", count, coach, jenisOlahraga, reservasi != null ? reservasi : "Tidak tereservasi", pengalamanTahun, pengalamanBulan);
                count++;
            }
            System.out.println("|-------|----------------------|----------------------|----------------------|----------------------|");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

