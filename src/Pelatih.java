import java.sql.*;
import java.util.Scanner;

public class Pelatih extends Pengguna {
    private String jenisOlahraga;
    private int lamaPengalaman;
    private String sertifikasiPelatih;
    private String pengalaman;
    public Pelatih(String nama, String username, String password, String email, String nohp, String jenisOlahraga, int lamaPengalaman, String sertifikasiPelatih, String pengalaman) {
        super(nama, username, password, email, nohp);
        this.jenisOlahraga = jenisOlahraga;
        this.lamaPengalaman = lamaPengalaman;
        this.sertifikasiPelatih = sertifikasiPelatih;
        this.pengalaman = pengalaman;
        //TODO Auto-generated constructor stub
    }

    public String getUsername() {
        return this.username;
    }
    public void registerPelatih(Connection connection, Statement statement) throws SQLException {    
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Masukkan bidang keahlian: ");
            this.jenisOlahraga = scanner.nextLine();
            System.out.print("Masukkan sertifikasi: ");
            this.sertifikasiPelatih = scanner.nextLine();
            System.out.print("Masukkan pengalaman: ");
            this.pengalaman = scanner.nextLine();
            System.out.print("Masukkan lama pengalaman (dalam bulan): ");
            this.lamaPengalaman = scanner.nextInt();
            scanner.nextLine(); // Membersihkan newline
        }
        super.register(connection, statement, "Pelatih");
    
        // Contoh: menyimpan data Pelatih ke dalam database
        try {
            String query = "INSERT INTO Pelatih (namaPelatih, jenisOlahraga, lamaPengalaman, sertifikasi, pengalaman, usernamePelatih) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, getNama());
            preparedStatement.setString(2, jenisOlahraga);
            preparedStatement.setInt(3, lamaPengalaman);
            preparedStatement.setString(4, sertifikasiPelatih);
            preparedStatement.setString(5, pengalaman);
            preparedStatement.setString(6, getUsername());
    
            preparedStatement.executeUpdate();
    
            System.out.println("Registrasi Pelatih berhasil.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getNama() {
        return this.nama;
    }

    public static void tampilkanDaftarPartisipan(String username, Connection connection) {
        try {
            String checkUserRole = "SELECT role FROM users WHERE username = ?";
            PreparedStatement roleStatement = connection.prepareStatement(checkUserRole);
            roleStatement.setString(1, username);
            ResultSet roleResultSet = roleStatement.executeQuery();
    
            if (roleResultSet.next()) {
                String role = roleResultSet.getString("role");
                if (role.equals("Pelatih")) {
                    System.out.println("Anda adalah seorang pelatih.");
    
                    // Ambil data partisipan yang terdaftar di kelas pelatih dengan nama pelatih tertentu
                    String getParticipants = "SELECT username FROM reservasiPelatih WHERE pelatihUsername = ?";
    
                    PreparedStatement participantsStatement = connection.prepareStatement(getParticipants);
                    participantsStatement.setString(1, username);
                    ResultSet participantsResultSet = participantsStatement.executeQuery();
    
                    System.out.println("Daftar partisipan Anda:");
                    System.out.printf("| %-5s | %-20s |\n", "No", "Partisipan");
                    System.out.println("|-------|----------------------|");
    
                    int count = 1;
                    while (participantsResultSet.next()) {
                        String participant = participantsResultSet.getString("username");
                        System.out.printf("| %-5d | %-20s |\n", count, participant);
                        count++;
                    }
                    System.out.println("|-------|----------------------|");
                } else {
                    System.out.println("Anda bukan pelatih. Operasi tidak diizinkan.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void tampilkanLaporanGiziPartisipan(String username, Statement statement, Connection connection) {
        try {
            String checkUserRole = "SELECT role FROM users WHERE username = ?";
            PreparedStatement ps1 = statement.getConnection().prepareStatement(checkUserRole);
            ps1.setString(1, username);
            ResultSet resultSet = ps1.executeQuery();
            if (resultSet.next()) {
                String role = resultSet.getString("role");
                if (role.equals("Pelatih")) {
                    try (Scanner scanner = new Scanner(System.in)) {
                        tampilkanDaftarPartisipan(username, connection);
                        System.out.println("Masukkan nama partisipan yang ingin Anda lihat laporannya:");
                        String partisipanUsername = scanner.nextLine();
    
                        String checkPelatih = "SELECT * FROM reservasiPelatih WHERE username = ? AND pelatihUsername = ?";
                        PreparedStatement ps2 = statement.getConnection().prepareStatement(checkPelatih);
                        ps2.setString(1, partisipanUsername);
                        ps2.setString(2, username);
                        ResultSet resultSet2 = ps2.executeQuery();
    
                        if (resultSet2.next()) {
                            System.out.println("Anda adalah pelatih dari partisipan ini.");
                            String checkValidasi = "SELECT * FROM laporan_gizi WHERE nama_partisipan = ?";
                            PreparedStatement ps3 = statement.getConnection().prepareStatement(checkValidasi);
                            ps3.setString(1, partisipanUsername);
                            ResultSet checkResult = ps3.executeQuery();
    
                            boolean found = false;
    
                            // Loop untuk menampilkan semua laporan gizi dari partisipan yang sama
                            while (checkResult.next()) {
                                found = true;
                                System.out.println("==============================================================================");
                                System.out.println("Laporan gizi untuk partisipan: " + partisipanUsername);
                                System.out.println("==============================================================================");
                                System.out.format("%-18s%-18s%-18s%-18s%-18s%-18s%-18s\n", "Protein (gram)", "Karbo (gram)", "Lemak (gram)", "Kalori (kcal)", "Vitamin", "Mineral", "Suplemen");
                                System.out.format("%-18s%-18s%-18s%-18s%-18s%-18s%-18s\n", "------------------", "------------------", "------------------", "------------------", "------------------", "------------------", "------------------");
                                System.out.format("%-18s%-18s%-18s%-18s%-18s%-18s%-18s\n", checkResult.getString("totalProtein"), checkResult.getString("totalKarbohidrat"), checkResult.getString("totalLemak"), checkResult.getString("kalori"), checkResult.getString("vitamin"), checkResult.getString("mineral"), checkResult.getString("suplemen"));
                                System.out.println("Pola makan yang disarankan: " + checkResult.getString("polaMakan"));
                                System.out.println("Goals partisipan: " + checkResult.getString("tujuanPartisipan"));
                                System.out.println("==============================================================================");
                            }
    
                            if (!found) {
                                System.out.println("Tidak ada laporan gizi yang tersedia untuk partisipan: " + partisipanUsername);
                            }
    
                            checkResult.close();
                        } else {
                            System.out.println("Partisipan tidak memilih Anda sebagai pelatih.");
                        }
                        resultSet2.close();
                        ps2.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Anda bukan Pelatih. Operasi tidak diizinkan.");
                }
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void buatJadwalLatihan(String pelatihUsername, Statement statement, Connection connection) {
        try {
            String checkUserRole = "SELECT role FROM users WHERE username = '" + pelatihUsername + "'";
            ResultSet resultSet = statement.executeQuery(checkUserRole);
            if (resultSet.next()) {
                String role = resultSet.getString("role");
                if (role.equals("Pelatih")) {
                    try (Scanner scanner = new Scanner(System.in)) {
                        tampilkanDaftarPartisipan(pelatihUsername, connection);
                        System.out.println("Masukkan username partisipan:");
                        String partisipanUsername = scanner.nextLine();

                        // Memeriksa apakah partisipan memilih pelatih ini
                        String checkPartisipanPelatih = "SELECT * FROM reservasiPelatih WHERE username = ('" + partisipanUsername + "') AND pelatihUsername = ('" + pelatihUsername + "')";
                        ResultSet checkResult = statement.executeQuery(checkPartisipanPelatih);
                        if (checkResult.next()) {
                            System.out.println("Anda adalah pelatih dari partisipan ini.");

                            System.out.println("Masukkan hari latihan:");
                            String hari = scanner.nextLine();

                            System.out.println("Masukkan waktu latihan (Jam:Menit:Detik): ");
                            String waktu = scanner.nextLine();

                            String insertJadwal = "INSERT INTO jadwal_latihan (username, hari, waktu) VALUES ('" + partisipanUsername + "', '" + hari + "', '" + waktu + "')";
                            statement.executeUpdate(insertJadwal);
                            System.out.println("Jadwal latihan telah dimasukkan untuk partisipan: " + partisipanUsername);
                        } else {
                            System.out.println("Partisipan tidak memilih Anda sebagai pelatih.");
                        }
                    }
                } else {
                    System.out.println("Anda bukan pelatih. Operasi tidak diizinkan.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Terjadi kesalahan saat membuat jadwal latihan:");
            System.out.println("Pesan kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void tampilkanJadwal(String username, Statement statement, Connection connection) {
        try {
            String checkUserRole = "SELECT role FROM users WHERE username = '" + username + "'";
            ResultSet resultSet = statement.executeQuery(checkUserRole);
            if (resultSet.next()) {
                String role = resultSet.getString("role");
                if (role.equals("Pelatih")) {
                    try (Scanner scanner = new Scanner(System.in)) {
                        tampilkanDaftarPartisipan(username, connection);
                        System.out.println("Masukkan username partisipan untuk melihat jadwal mereka:");
                        String partisipanUsername = scanner.nextLine();
    
                        // Memeriksa apakah partisipan memilih pelatih ini
                        String checkPartisipanPelatih = "SELECT * FROM reservasiPelatih WHERE username = ('" + partisipanUsername + "') AND pelatihUsername = ('" + username + "')";
                        ResultSet checkResult = statement.executeQuery(checkPartisipanPelatih);
                        if (checkResult.next()) {
                            try {
                                String checkUser = "SELECT * FROM jadwal_latihan WHERE username = '" + partisipanUsername + "'";
                                ResultSet resultSetPartisipan = statement.executeQuery(checkUser);
                                System.out.println("Jadwal latihan untuk pengguna " + partisipanUsername + ":");
                                while (resultSetPartisipan.next()) {
                                    String hari = resultSetPartisipan.getString("hari");
                                    String waktu = resultSetPartisipan.getString("waktu");
    
                                    System.out.println("Hari: " + hari + ", Waktu: " + waktu);
                                }
                                resultSetPartisipan.close(); // Tutup ResultSet setelah digunakan
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        } else {
                            System.out.println("Partisipan tidak memilih Anda sebagai pelatih.");
                        }
                        checkResult.close(); // Tutup ResultSet setelah digunakan
                    }
                } else {
                    System.out.println("Anda bukan pelatih. Operasi tidak diizinkan.");
                }
                resultSet.close(); // Tutup ResultSet setelah digunakan
            }
        } catch (SQLException e) {
            System.out.println("Terjadi kesalahan saat melihat jadwal latihan:");
            System.out.println("Pesan kesalahan: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void buatCatatanPartisipan(Connection connection, String usernamePelatih) {
        try {
            try (Scanner scanner = new Scanner(System.in)) {
                System.out.println("Masukkan detail latihan untuk pengguna:");
                System.out.print("Nama Latihan: ");
                String namaLatihan = scanner.nextLine();

                System.out.print("Repetisi: ");
                int repetisi = scanner.nextInt();
                scanner.nextLine(); 

                System.out.print("Set Latihan: ");
                int setLatihan = scanner.nextInt();
                scanner.nextLine();

                System.out.print("Tips (jika ada): ");
                String tips = scanner.nextLine();

                // Menyimpan detail latihan ke dalam database
                String insertLatihanQuery = "INSERT INTO CatatanLatihan (usernamePelatih, namaLatihan, repetisi, setLatihan, tips) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement insertLatihanStatement = connection.prepareStatement(insertLatihanQuery);
                insertLatihanStatement.setString(1, usernamePelatih);
                insertLatihanStatement.setString(2, namaLatihan);
                insertLatihanStatement.setInt(3, repetisi);
                insertLatihanStatement.setInt(4, setLatihan);
                insertLatihanStatement.setString(5, tips);

                insertLatihanStatement.executeUpdate();
            }

            System.out.println("Detail latihan berhasil disimpan.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void tampilkanCatatan(Connection connection, String usernamePartisipan) {
        try {
            String query = "SELECT cl.namaLatihan, cl.repetisi, cl.setLatihan, cl.tips " +
                           "FROM CatatanLatihan cl " +
                           "JOIN reservasiPelatih rp ON cl.usernamePelatih = rp.pelatihUsername " +
                           "WHERE rp.username = ?";
            
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, usernamePartisipan);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                System.out.println("=== Catatan Latihan ===");
                do {
                    System.out.println("Nama Latihan: " + resultSet.getString("namaLatihan"));
                    System.out.println("Repetisi: " + resultSet.getInt("repetisi"));
                    System.out.println("Set Latihan: " + resultSet.getInt("setLatihan"));
                    System.out.println("Tips: " + resultSet.getString("tips"));
                    System.out.println("------------");
                } while (resultSet.next());
            } else {
                System.out.println("Tidak ada catatan latihan yang dapat ditampilkan.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}    
