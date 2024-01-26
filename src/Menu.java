import java.sql.*;
import java.util.Scanner;
public class Menu {
    static Statement statement;
    static Connection connection;
    private static Scanner scanner = new Scanner(System.in);

    public static void tampilkanMenu() {
        try {
            connection = databaseCON.getConnection();
            statement = databaseCON.getConnection().createStatement();
            //lokasi awal tabel
            System.out.println("Welcome to Rifan Fitnes.");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Silahkan pilih opsi: ");
            int option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    registerUser(connection, statement);
                    break;
                case 2:
                    loginUser(connection, statement);
                    break;
                case 3:
                    System.out.println("Terima kasih telah menggunakan aplikasi Rifan Fitnes. Goodbye n Have a Healthy Life ;)");
                    break;
                default:
                    System.out.println("Opsi tidak valid. Coba lagi.");
                    break;
            }

            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void registerUser(Connection connection, Statement statement) throws SQLException {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("=== Form Pendaftaran ===");
            String username = "";
            String password = "";
            String confirmPassword = "";
            String email = "";
            String nohp = "";
            String nama = "";
            String role = "";

            boolean validInput = false;
            while (!validInput) {
                System.out.print("Masukkan Username: ");
                username = scanner.nextLine();
                checkUsername.checkValidation(connection, statement, username);
                if (!username.matches("^[\\w]+$")) {
                    System.out.println("Username tidak valid. Hanya boleh menggunakan huruf, angka, dan underscore. Coba lagi.");
                } else {
                    validInput = true;
                }
            }

            validInput = false;
            while (!validInput) {
                System.out.print("Masukkan Password: ");
                password = scanner.nextLine();
                if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=_!?&])(?=\\S+$).{8,20}$")) {
                    System.out.println("Password tidak valid. Harus minimal 8 karakter dan maksimal 20 karakter yang terdiri dari kombinasi huruf kapital, huruf kecil, karakter, dan angka. Coba lagi.");
                } else {
                    validInput = true;
                }
            }

            validInput = false;
            while (!validInput) {
                System.out.print("Konfirmasi Password: ");
                confirmPassword = scanner.nextLine();
                if (!confirmPassword.equals(password)) {
                    System.out.println("Konfirmasi password tidak cocok. Coba lagi.");
                } else {
                    validInput = true;
                }
            }

            validInput = false;
            while (!validInput) {
                System.out.print("Masukkan Email: ");
                email = scanner.nextLine();
                if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                    System.out.println("Email tidak valid. Harus mengikuti format umum email. Coba lagi.");
                } else {
                    try {
                        String checkEmailQuery = "SELECT * FROM users WHERE email = ?";
                        PreparedStatement checkEmailStatement = connection.prepareStatement(checkEmailQuery);
                        checkEmailStatement.setString(1, email);
                        ResultSet emailResultSet = checkEmailStatement.executeQuery();
            
                        if (emailResultSet.next()) {
                            System.out.println("Email sudah terdaftar. Gunakan email lain.");
                        } else {
                            validInput = true;
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

            validInput = false;
            while (!validInput) {
                System.out.print("Masukkan No HP: ");
                nohp = scanner.nextLine();
                if (!nohp.matches("^\\d{10,13}$")) {
                    System.out.println("No HP tidak valid. Hanya boleh menggunakan angka dan panjang 10-13 digit. Coba lagi.");
                } else {
                    validInput = true;
                }
            }

            validInput = false;
            while (!validInput) {
                System.out.print("Masukkan Nama Lengkap: ");
                nama = scanner.nextLine();
                if (!nama.matches("^[\\p{L} ]+$")) {
                    System.out.println("Nama tidak valid. Hanya boleh menggunakan huruf dan spasi. Coba lagi.");
                } else {
                    validInput = true;
                }
            }

            validInput = false;
            while (!validInput) {
                System.out.println("Pilih jenis pengguna:");
                System.out.println("1. Pengguna Biasa");
                System.out.println("2. Pelatih");
                System.out.println("3. Ahli Gizi");
                System.out.print("Masukkan pilihan anda: ");
                int roleOption = scanner.nextInt();
                scanner.nextLine();

            // Misalnya, untuk bagian Pengguna Biasa
                switch (roleOption) {
                    case 1:
                        role = "Pengguna Biasa";
                        String insertUser = "INSERT INTO users (username, password, email, nohp, nama, role) VALUES (?, ?, ?, ?, ?, ?)";
                        try (PreparedStatement ps1 = connection.prepareStatement(insertUser)) {
                            ps1.setString(1, username);
                            ps1.setString(2, password);
                            ps1.setString(3, email);
                            ps1.setString(4, nohp);
                            ps1.setString(5, nama);
                            ps1.setString(6, role);
                            ps1.executeUpdate();
                        }
                        System.out.println("Pendaftaran Pengguna Biasa berhasil.");
                        validInput = true;
                        break;
                    case 2:
                        role = "Pelatih";
                        Pelatih pelatih = new Pelatih(nama, username, confirmPassword, email, nohp, null, 0, null, null);
                        pelatih.registerPelatih(connection, statement);
                        validInput = true;
                        break;
                    case 3:
                        role = "Ahli Gizi";
                        AhliGizi ahliGizi = new AhliGizi(nama, username, password, email, nohp, null, 0, null);
                        ahliGizi.register(connection, statement);
                        validInput = true;
                        break;
                    default:
                        System.out.println("Pilihan tidak valid. Coba lagi.");
                        break;
                }
            }
        }
    }
    public static void loginUser(Connection connection, Statement statement) throws SQLException {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Masukkan Username: ");
            String username = scanner.nextLine();
            System.out.print("Masukkan Password: ");
            String password = scanner.nextLine();
            Authenticator.authenticateUser(connection, statement, username, password);
            Pengguna pengguna = new Pengguna(null, username, password, null, null); // Perbarui dengan parameter yang sesuai jika diperlukan
            boolean loggedIn = pengguna.login(connection, username, password);
   
            if (loggedIn) {
                String role = pengguna.getRole();
                if (role.equals("Pelatih")) {
                    System.out.println("Anda adalah seorang pelatih.");
                    System.out.println("Pilih fitur yang ingin anda gunakan:");
                    System.out.println("1. Cek partisipan");
                    System.out.println("2. Tampilkan laporan gizi partisipan");
                    System.out.println("3. Buat jadwal latihan partisipan");
                    System.out.println("4. Tampilkan jadwal latihan partisipan");
                    System.out.println("5. Buat catatan untuk partisipan");
                    System.out.println("6. Tampilkan catatan untuk partisipan");
                    System.out.print("Masukkan pilihan anda: ");
                    int pelatihOption = scanner.nextInt();
                    scanner.nextLine();

                    switch (pelatihOption) {
                        case 1:
                            Pelatih.tampilkanDaftarPartisipan(username, connection);
                            break;
                        case 2:
                            Pelatih.tampilkanLaporanGiziPartisipan(username, statement, connection);
                            break;
                        case 3:
                            Pelatih.buatJadwalLatihan(username, statement, connection);
                            break;
                        case 4:
                            Pelatih.tampilkanJadwal(username, statement, connection);
                            break;
                        case 5:
                            Pelatih.buatCatatanPartisipan(connection, username);
                            break;
                        case 6:
                            System.out.print("Masukkan nama partisipan anda: ");
                            String Pusername = scanner.nextLine();
                            Pelatih.tampilkanCatatan(connection, Pusername);
                        default:
                            System.out.println("Opsi tidak valid. Coba lagi.");
                            break;
                    }
                }
                else if(role.equals("Pengguna Biasa")) {
                    System.out.println("Anda adalah seorang pengguna biasa");
                    System.out.println("Pilih fitur yang ingin anda gunakan:");
                    System.out.println("1. Pilih pelatih");
                    System.out.println("2. Buat jadwal latihan");
                    System.out.println("3. Lihat jadwal latihan");
                    System.out.println("4. Lihat laporan gizi anda");
                    System.out.println("5. Lihat catatan pelatih anda");
                    System.out.print("Masukkan pilihan anda: ");
                    int opsi = scanner.nextInt();
                    switch (opsi) {
                        case 1:
                            PenggunaBiasa.PilihPelatih(username, statement);
                            break;
                        case 2:
                            PenggunaBiasa.cekPengguna(username, statement);
                            break;
                        case 3:
                            PenggunaBiasa.tampilkanJadwal(username, statement);
                            break;
                        case 4:
                            PenggunaBiasa.viewLaporan(username, statement);
                            break;
                        case 5:
                            Pelatih.tampilkanCatatan(connection, username);
                        default:
                            System.out.println("Opsi tidak valid. Coba lagi.");
                            break;
                    }
                }
                else if(role.equals("Admin")) {
                    System.out.println("Anda adalah seorang admin");
                    System.out.println("Pilih fitur yang ingin anda gunakan:");
                    System.out.println("1. Tampilkan Semua Pengguna");  
                    System.out.println("2. Cari Pengguna");
                    System.out.println("3. Verifikasi akun pengguna"); 
                    System.out.println("4. Tampilkan berkas untuk verifikasi"); 
                    System.out.print("Masukkan pilihan anda: ");

                    int adminOption = scanner.nextInt();
                    scanner.nextLine();

                    switch (adminOption) {
                        case 1:
                            Admin.displayAllUserInformation(statement);
                            break;
                        case 2:
                            Admin.searchUser(statement);
                            break;
                        case 3:
                            Admin admin = new Admin(null, username, password, null, null);
                            admin.verifikasi(statement);
                            break;
                        case 4:
                            Admin.tampilkanBerkas(statement);
                        default:
                            System.out.println("Opsi tidak valid. Coba lagi.");
                            break;
                    }
                }
                else if(role.equals("Ahli Gizi")) {
                    System.out.println("Anda adalah seorang ahli gizi");
                    System.out.println("Pilih fitur yang ingin anda gunakan:");
                    System.out.println("1. Tampilkan daftar pengguna");
                    System.out.println("2. Buat laporan gizi pengguna");
                    System.out.println("3. Lihat laporan gizi pengguna");
                    System.out.print("Masukkan pilihan anda: ");
                    int ahliGizi = scanner.nextInt();
                    scanner.nextLine();

                    switch (ahliGizi) {
                        case 1:
                            AhliGizi.tampilkanDaftarPartisipan(username, statement);
                            break;
                        case 2:
                            AhliGizi.buatLaporan(username, statement); 
                            break;
                        case 3:
                            AhliGizi.viewLaporan(username, statement);
                            break;
                        default:
                            System.out.println("Opsi tidak valid. Coba lagi.");
                            break;
                        }
            } else {
                System.out.println("Login gagal. Coba lagi.");
                loginUser(connection, statement); // Ulangi proses login
                }
            }
        }
    }
}
