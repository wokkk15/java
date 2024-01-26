import java.sql.*;
import java.util.Scanner;

public class AhliGizi extends Pengguna {
    private String bidangKeahlian;
    private int pengalamanKerja;
    private String sertifikasi;

    public AhliGizi(String nama, String username, String password, String email, String nohp, String bidangKeahlian, int pengalamanKerja, String sertifikasi) {
        super(nama, username, password, email, nohp);
        this.bidangKeahlian = bidangKeahlian;
        this.pengalamanKerja = pengalamanKerja;
        this.sertifikasi = sertifikasi;
    // Inisialisasi atribut AhliGizi
    }

    public String getUsername() {
        return this.username;
    }

    public void register(Connection connection, Statement statement) throws SQLException {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Masukkan bidang keahlian: ");
            this.bidangKeahlian = scanner.nextLine();

            System.out.print("Masukkan lama pengalaman (dalam bulan): ");
            this.pengalamanKerja = scanner.nextInt();
            scanner.nextLine(); // Membersihkan newline

            System.out.print("Masukkan sertifikasi: ");
            this.sertifikasi = scanner.nextLine(); 
        }

        super.register(connection, statement, "Ahli Gizi");

        try {
            String query = "INSERT INTO AhliGizi (usernameAhliGizi, pengalamanKerja, sertifikasi, bidangKeahlian, namaAhliGizi) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, getUsername());
            preparedStatement.setInt(2, pengalamanKerja);
            preparedStatement.setString(3, sertifikasi);
            preparedStatement.setString(4, bidangKeahlian);
            preparedStatement.setString(5, super.getNama());


            preparedStatement.executeUpdate();

            System.out.println("Registrasi Ahli Gizi berhasil.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean login(Connection connection, String inputUsername, String inputPassword) throws SQLException {
        boolean loggedIn = super.login(connection, inputUsername, inputPassword);
        if (loggedIn) {
            String role = super.getRole();
            if (role.equals("Ahli Gizi")) {
                return true; // Otentikasi berhasil
            }
        }
        return false; // Otentikasi gagal
    }

    public static void tampilkanDaftarPartisipan(String usernameAhliGizi, Statement statement) {
        try {
            String checkUserRole = "SELECT role FROM users WHERE username = '" + usernameAhliGizi + "'";
            ResultSet resultSet = statement.executeQuery(checkUserRole);
            if (resultSet.next()) {
                String role = resultSet.getString("role");
                if (role.equals("Ahli Gizi")) {
                    System.out.println("Anda adalah seorang Ahli Gizi.");
                    viewPenggunaBiasa.lihatDaftar(statement);
                } else {
                    System.out.println("Anda bukan Ahli Gizi. Operasi tidak diizinkan.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void viewLaporan(String usernameAhliGizi, Statement statement) {
        try {
            String checkUserRole = "SELECT role FROM users WHERE username = ?";
            PreparedStatement ps1 = statement.getConnection().prepareStatement(checkUserRole);
            ps1.setString(1, usernameAhliGizi);
            ResultSet resultSet = ps1.executeQuery();
            if (resultSet.next()) {
                String role = resultSet.getString("role");
                if (role.equals("Ahli Gizi")) {
                    tampilkanDaftarPartisipan(usernameAhliGizi, statement);
    
                    try (Scanner scanner = new Scanner(System.in)) {
                        System.out.println("Masukkan nama partisipan yang ingin Anda lihat laporannya:");
                        String partisipanUsername = scanner.nextLine();
    
                        String checkValidasi = "SELECT * FROM laporan_gizi WHERE nama_partisipan = ?";
                        PreparedStatement ps2 = statement.getConnection().prepareStatement(checkValidasi);
                        ps2.setString(1, partisipanUsername);
                        ResultSet checkResult = ps2.executeQuery();
    
                        boolean found = false;
    
                        // Loop untuk menampilkan semua laporan gizi dari partisipan yang sama
                        while (checkResult.next()) {
                            found = true;
                            System.out.println("==========================================================================================================================================");
                            System.out.println("Laporan gizi untuk partisipan: " + partisipanUsername);
                            System.out.println("==========================================================================================================================================");
                            System.out.format("%-20s%-20s%-20s%-20s%-20s%-20s%-20s\n", "Protein (gram)", "Karbo (gram)", "Lemak (gram)", "Kalori (kcal)", "Vitamin", "Mineral", "Suplemen");
                            System.out.format("%-20s%-20s%-20s%-20s%-20s%-20s%-20s\n", "--------------------", "--------------------", "--------------------", "--------------------", "--------------------", "--------------------", "--------------------");
                            System.out.format("%-20s%-20s%-20s%-20s%-20s%-20s%-20s\n", checkResult.getString("totalProtein"), checkResult.getString("totalKarbohidrat"), checkResult.getString("totalLemak"), checkResult.getString("kalori"), checkResult.getString("vitamin"), checkResult.getString("mineral"), checkResult.getString("suplemen"));
                            System.out.println("Pola makan yang disarankan: " + checkResult.getString("polaMakan"));
                            System.out.println("Goals partisipan: " + checkResult.getString("tujuanPartisipan"));
                            System.out.println("==========================================================================================================================================");
                        }
                        
    
                        if (!found) {
                            System.out.println("Tidak ada laporan gizi untuk partisipan: " + partisipanUsername);
                        }
    
                        checkResult.close();
                        ps2.close();
                    }
                } else {
                    System.out.println("Anda bukan Ahli Gizi. Operasi tidak diizinkan.");
                }
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void buatLaporan(String usernameahliGizi, Statement statement) {
        try {
            String checkUserRole = "SELECT role FROM users WHERE username = ?";
            PreparedStatement ps1 = statement.getConnection().prepareStatement(checkUserRole);
            ps1.setString(1, usernameahliGizi);
            ResultSet resultSet = ps1.executeQuery();
            if (resultSet.next()) {
                String role = resultSet.getString("role");
                if (role.equals("Ahli Gizi")) {
                    tampilkanDaftarPartisipan(usernameahliGizi, statement);
                    try (Scanner scanner = new Scanner(System.in)) {
                        System.out.println("Masukkan username partisipan:");
                        String partisipanUsername = scanner.nextLine();

                        String checkValidasi = "SELECT * FROM users WHERE username = ?";
                        PreparedStatement ps2 = statement.getConnection().prepareStatement(checkValidasi);
                        ps2.setString(1, partisipanUsername);
                        ResultSet checkResult = ps2.executeQuery();
                        if (checkResult.next()) {
                            String rolePartisipan = checkResult.getString("role");
                            if (rolePartisipan.equals("Pengguna Biasa")){
                                System.out.println("Partisipan terdaftar.");

                                System.out.print("Masukkan protein yang dibutuhkan(gram):");
                                String protein = scanner.nextLine();

                                System.out.print("Masukkan karbohidrat yang dibutuhkan(gram):");
                                String karbohidrat = scanner.nextLine();

                                System.out.print("Masukkan lemak yang dibutuhkan(gram):");
                                String lemak = scanner.nextLine();

                                System.out.print("Masukkan kalori yang dibutuhkan(kcal):");
                                String kalori = scanner.nextLine();

                                System.out.print("Masukkan nama vitamin yang dibutuhkan:");
                                String vitamin = scanner.nextLine();
                                
                                System.out.print("Masukkan nama mineral yang dibutuhkan:");
                                String mineral = scanner.nextLine();

                                System.out.print("Masukkan catatan pola makan untuk partisipan:");
                                String polaMakan = scanner.nextLine();

                                System.out.print("Masukkan nama suplemen yang dibutuhkan:");
                                String suplemen = scanner.nextLine();

                                System.out.print("Masukkan tujuan partisipan:");
                                String tujuan = scanner.nextLine();

                                String insertLaporanGizi = "INSERT INTO laporan_gizi (nama_partisipan, totalProtein, totalKarbohidrat, totalLemak, kalori, vitamin, mineral, suplemen, polaMakan, tujuanPartisipan) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                                PreparedStatement ps3 = statement.getConnection().prepareStatement(insertLaporanGizi);
                                ps3.setString(1, partisipanUsername);
                                ps3.setString(2, protein);
                                ps3.setString(3, karbohidrat);
                                ps3.setString(4, lemak);
                                ps3.setString(5, kalori);
                                ps3.setString(6, vitamin);
                                ps3.setString(7, mineral);
                                ps3.setString(8, suplemen);
                                ps3.setString(9, polaMakan);
                                ps3.setString(10, tujuan);
                                ps3.executeUpdate();
                                System.out.println("Laporan gizi telah dibuat untuk partisipan: " + partisipanUsername);
                            } else {
                                System.out.println("Partisipan bukan merupakan pengguna biasa!");
                            }
                            
                        } else {
                            System.out.println("Partisipan tidak terdaftar.");
                        }
                        checkResult.close();
                    }
                } else {
                    System.out.println("Anda bukan Ahli Gizi. Operasi tidak diizinkan.");
                }
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
