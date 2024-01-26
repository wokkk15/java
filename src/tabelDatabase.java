import java.sql.*;
public class tabelDatabase {
    static Statement statement;
    public static void buatTabel (Connection connection) {
        try (Statement statement = connection.createStatement()) {
            String createTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "username VARCHAR(20) UNIQUE NOT NULL," +
                    "password VARCHAR(20) NOT NULL," +
                    "email VARCHAR(255) UNIQUE NOT NULL," +
                    "nohp VARCHAR(13) UNIQUE NOT NULL," +
                    "nama VARCHAR(255) NOT NULL," +
                    "role VARCHAR(20) NOT NULL," +
                    "status_verifikasi VARCHAR(20) DEFAULT 'Unverified'" +
                    ")";
            statement.execute(createTable);

            String createTablePelatih = "CREATE TABLE IF NOT EXISTS Pelatih (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "namaPelatih VARCHAR(50) NOT NULL," +
                    "jenisOlahraga VARCHAR(50) NOT NULL," +
                    "pengalaman VARCHAR(255) NOT NULL," +
                    "sertifikasi VARCHAR(100) NOT NULL," +
                    "lamaPengalaman INT NOT NULL," +
                    "usernamePelatih VARCHAR(20) UNIQUE," +
                    "FOREIGN KEY (usernamePelatih) REFERENCES users(username)" +
                    ")";
            statement.execute(createTablePelatih);
            

            String createTable3 = "CREATE TABLE IF NOT EXISTS reservasiPelatih (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "username VARCHAR(20) UNIQUE," +
                    "pelatihUsername VARCHAR(20) UNIQUE," +
                    "direservasiPada DATETIME," +
                    "FOREIGN KEY (username) REFERENCES users(username)," +
                    "FOREIGN KEY (pelatihUsername) REFERENCES pelatih(usernamePelatih)" +
                    ")";
            statement.execute(createTable3);

            String createTableExerciseSchedule = "CREATE TABLE IF NOT EXISTS jadwal_latihan (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "username VARCHAR(50) NOT NULL," +
                    "hari VARCHAR(20) NOT NULL," +
                    "waktu TIME NOT NULL," +
                    "FOREIGN KEY (username) REFERENCES users(username)," +
                    "UNIQUE KEY unique_schedule (username, hari, waktu)" +
                    ")";

            statement.execute(createTableExerciseSchedule);

            String createTable4 = "CREATE TABLE IF NOT EXISTS laporan_gizi (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "nama_partisipan VARCHAR(20) NOT NULL," +
                    "totalProtein INT NOT NULL," +
                    "totalKarbohidrat INT NOT NULL," +
                    "totalLemak INT NOT NULL," +
                    "vitamin VARCHAR(100) NOT NULL," +
                    "mineral VARCHAR(100) NOT NULL," +
                    "kalori INT NOT NULL," +
                    "polaMakan VARCHAR(255) NOT NULL," +
                    "suplemen VARCHAR(255) NOT NULL," +
                    "tujuanPartisipan TEXT NOT NULL," +
                    "FOREIGN KEY (nama_partisipan) REFERENCES users(username)" +
                    ")";
            statement.execute(createTable4);

            String createTableGizi = "CREATE TABLE IF NOT EXISTS AhliGizi (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "usernameAhliGizi VARCHAR(20) UNIQUE," +
                    "pengalamanKerja INT NOT NULL," +
                    "sertifikasi VARCHAR(100) NOT NULL," +
                    "bidangKeahlian VARCHAR(255) NOT NULL," +
                    "namaAhliGizi VARCHAR(50) NOT NULL," +
                    "FOREIGN KEY (usernameAhliGizi) REFERENCES users(username)" +
                    ")";
           statement.execute(createTableGizi);

           String createTableCatatanLatihan = "CREATE TABLE IF NOT EXISTS CatatanLatihan (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "usernamePelatih VARCHAR(20) NOT NULL," +
                "namaLatihan VARCHAR(50) NOT NULL," +
                "repetisi INT NOT NULL," +
                "setLatihan INT NOT NULL," +
                "tips TEXT," +
                "FOREIGN KEY (usernamePelatih) REFERENCES Pelatih(usernamePelatih)" +
                ")";
           statement.execute(createTableCatatanLatihan);
           
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
