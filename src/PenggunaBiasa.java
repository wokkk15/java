import java.sql.*;
import java.util.Scanner;

public class PenggunaBiasa {
    public static void PilihPelatih(String username, Statement statement) {
        try {
            String checkUserRole = "SELECT role FROM users WHERE username = '" + username + "'";
            ResultSet resultSet = statement.executeQuery(checkUserRole);
            if (resultSet.next()) {
                String role = resultSet.getString("role");
                if (role.equals("Pengguna Biasa")) {
                    System.out.println("Anda adalah pengguna biasa.");
    
                    // Menampilkan daftar pelatih yang tersedia
                    viewPelatih.lihatDaftar(statement);
    
                    // Memeriksa apakah pengguna telah mereservasi pelatih sebelumnya
                    String checkReservation = "SELECT * FROM reservasiPelatih WHERE username = '" + username + "'";
                    ResultSet reservationResult = statement.executeQuery(checkReservation);
                    if (reservationResult.next()) {
                        System.out.println("Anda telah mereservasi pelatih sebelumnya.");

                    } else {
                        try (Scanner scanner = new Scanner(System.in)) {
                            System.out.println("Silakan pilih pelatih dengan memasukkan username-nya:");
                            String chosenCoach = scanner.nextLine();
    
                            // Memperbarui tabel pengguna biasa dengan informasi pelatih yang dipilih
                            String insertReservation = "INSERT INTO reservasiPelatih (username, pelatihUsername, direservasiPada) " +
                                    "VALUES (('" + username + "'), " + "('" + chosenCoach + "'), NOW())";
    
                            boolean isValidEntry = false;
                            while (!isValidEntry) {
                                try {
                                    int rowsAffected = statement.executeUpdate(insertReservation);
                                    if (rowsAffected > 0) {
                                        System.out.println("Anda telah memilih pelatih: " + chosenCoach);
                                        isValidEntry = true; // Berhenti jika berhasil
                                    } else {
                                        System.out.println("Gagal memilih pelatih.");
                                    }
                                } catch (SQLException e) {
                                    System.out.println("Tidak Valid! Silakan masukkan ulang username pelatih yang TERSEDIA!:");
                                    chosenCoach = scanner.nextLine();
                                    insertReservation = "INSERT INTO reservasiPelatih (username, pelatihUsername, direservasiPada) " +
                                            "VALUES (('" + username + "'), " + "('" + chosenCoach + "'), NOW())";
                                }
                            }
                        }
                    }
                } else {
                    System.out.println("Anda bukan pengguna biasa. Operasi tidak diizinkan.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void cekPengguna(String username, Statement statement) {
        try {
            String checkUserRole = "SELECT role FROM users WHERE username = '" + username + "'";
            ResultSet resultSet = statement.executeQuery(checkUserRole);
            if (resultSet.next()) {
                String role = resultSet.getString("role");
                if (role.equals("Pengguna Biasa")) {
                    System.out.println("Anda adalah pengguna biasa.");
                    buatJadwalLatihan(username, statement);
                } else {
                    System.out.println("Anda bukan pengguna biasa. Operasi tidak diizinkan.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void buatJadwalLatihan(String username, Statement statement) {
        try {
            try (// Logika untuk membuat jadwal latihan
            Scanner scanner = new Scanner(System.in)) {
                System.out.println("Masukkan jadwal latihan Anda:");
                System.out.println("Hari:");
                String hari = scanner.nextLine();
                System.out.println("Masukkan waktu latihan (Jam:Menit:Detik): ");
                String waktu = scanner.nextLine();

                // Simpan jadwal latihan ke database
                String insertSchedule = "INSERT INTO jadwal_latihan (username, hari, waktu) VALUES ('" + username + "', '" + hari + "', '" + waktu + "')";
                int rowsAffected = statement.executeUpdate(insertSchedule);

                if (rowsAffected > 0) {
                    System.out.println("Jadwal latihan berhasil dibuat.");
                } else {
                    System.out.println("Gagal membuat jadwal latihan.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void tampilkanJadwal(String username, Statement statement){
        try{
            String checkUserRole = "SELECT * FROM jadwal_latihan WHERE username = '" + username + "'";
            ResultSet resultSet = statement.executeQuery(checkUserRole);
            System.out.println("Jadwal latihan untuk pengguna " + username + ":");

        while (resultSet.next()) {
            String hari = resultSet.getString("hari");
            String waktu = resultSet.getString("waktu");

            System.out.println("Hari: " + hari + ", Waktu: " + waktu);
        }

    } catch (SQLException e) {
        e.printStackTrace();
        }
    }

    public static void viewLaporan(String username, Statement statement) {
        try {
            String checkUserRole = "SELECT role FROM users WHERE username = ?";
            PreparedStatement ps1 = statement.getConnection().prepareStatement(checkUserRole);
            ps1.setString(1, username);
            ResultSet resultSet = ps1.executeQuery();
            if (resultSet.next()) {
                String role = resultSet.getString("role");
                if (role.equals("Pengguna Biasa")) {
                    System.out.println("Anda adalah seorang Pengguna Biasa.");
    
                    String checkValidasi = "SELECT * FROM laporan_gizi WHERE nama_partisipan = ?";
                    PreparedStatement ps2 = statement.getConnection().prepareStatement(checkValidasi);
                    ps2.setString(1, username);
                    ResultSet checkResult = ps2.executeQuery();
    
                    boolean found = false;
    
                    // Loop untuk menampilkan semua laporan gizi dari partisipan yang sama
                    while (checkResult.next()) {
                        found = true;
                        System.out.println("=================================================================================================================");
                        System.out.println("Laporan gizi untuk partisipan: " + username);
                        System.out.println("=================================================================================================================");
                        System.out.format("%-18s%-18s%-18s%-18s%-18s%-18s%-18s\n", "Protein (gram)", "Karbo (gram)", "Lemak (gram)", "Kalori (kcal)", "Vitamin", "Mineral", "Suplemen");
                        System.out.format("%-18s%-18s%-18s%-18s%-18s%-18s%-18s\n", "------------------", "------------------", "------------------", "------------------", "------------------", "------------------", "------------------");
                        System.out.format("%-18s%-18s%-18s%-18s%-18s%-18s%-18s\n", checkResult.getString("totalProtein"), checkResult.getString("totalKarbohidrat"), checkResult.getString("totalLemak"), checkResult.getString("kalori"), checkResult.getString("vitamin"), checkResult.getString("mineral"), checkResult.getString("suplemen"));
                        System.out.println("Pola makan yang disarankan: " + checkResult.getString("polaMakan"));
                        System.out.println("Goals partisipan: " + checkResult.getString("tujuanPartisipan"));
                        System.out.println("=================================================================================================================");
                    }
                    
                    if (!found) {
                        System.out.println("Tidak ada laporan gizi yang tersedia untuk partisipan: " + username);
                    }
    
                    checkResult.close();
                } else {
                    System.out.println("Anda bukan Pengguna Biasa. Operasi tidak diizinkan.");
                }
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
