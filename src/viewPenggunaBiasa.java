import java.sql.*;

public class viewPenggunaBiasa {
    public static void lihatDaftar(Statement statement) {
        String selectUsers = "SELECT * FROM users WHERE role = 'pengguna biasa'";
        try (ResultSet participantsResultSet = statement.executeQuery(selectUsers)) {
            System.out.println("=== Daftar Partisipan ===");
            System.out.printf("| %-5s | %-20s |\n", "No", "Partisipan");
            System.out.println("|-------|----------------------|");
            
            int count = 1;
            while (participantsResultSet.next()) {
                String participant = participantsResultSet.getString("username");
                System.out.printf("| %-5d | %-20s |\n", count, participant);
                count++;
            }
            System.out.println("|-------|----------------------|");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
