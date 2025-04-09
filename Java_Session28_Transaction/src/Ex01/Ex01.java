package Ex01;


import java.sql.*;

public class Ex01 {
    public static void main(String[] args) {
        String URL = "jdbc:mysql://localhost:3306";
        String USER = "root";
        String PASS = "12345678";
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Ket noi thanh cong!");
            System.out.println("Auto-commit ban dau: " + conn.getAutoCommit());
            conn.setAutoCommit(false);
            System.out.println("Da tat che do auto-commit");
            String insertSQL = "INSERT INTO users (id, name, email) VALUES (?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertSQL);
            insertStmt.setInt(1, 1);
            insertStmt.setString(2, "Duong Sy Hiep");
            insertStmt.setString(3, "hiepds@gmail.com");
            int rowsInserted = insertStmt.executeUpdate();
            System.out.println("Da chen " + rowsInserted + " dong vao bang users.");
            conn.commit();
            System.out.println("Da commit thay doi.");
            String selectSQL = "SELECT * FROM users WHERE id = ?";
            PreparedStatement selectStmt = conn.prepareStatement(selectSQL);
            selectStmt.setInt(1, 1);
            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                System.out.println("Du lieu xac minh:");
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Email: " + rs.getString("email"));
            } else {
                System.out.println("Khong tim thay du lieu vua chen.");
            }
        } catch (SQLException e) {
            System.err.println("Loi: " + e.getMessage());
            if (conn != null) {
                try {
                    System.out.println("Co loi xay ra. Rollback...");
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Loi khi rollback: " + ex.getMessage());
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("Da dong ket noi.");
                } catch (SQLException e) {
                    System.err.println("Loi khi dong ket noi: " + e.getMessage());
                }
            }
        }
    }
}
