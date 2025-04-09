package Ex02;

import java.sql.*;

public class Ex02 {
    public static void main(String[] args) {
        String URL = "jdbc:mysql://localhost:3306/portal_db";
        String USER = "root";
        String PASS = "12345678";
        Connection conn = null;
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;
        Statement checkStmt = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Ket noi thanh cong");
            conn.setAutoCommit(false);
            System.out.println("Da tat che do auto-commit");
            stmt1 = conn.prepareStatement("insert into test_table(id, name) values (?, ?)");
            stmt1.setInt(1, 1);
            stmt1.setString(2, "Alice");
            stmt1.executeUpdate();
            System.out.println("Chen ban ghi 1 thanh cong");
            stmt2 = conn.prepareStatement("insert into test_table(id, name) values (?, ?)");
            stmt2.setInt(1, 1);
            stmt2.setString(2, "Bob");
            stmt2.executeUpdate();
            System.out.println("Chen ban ghi 2 thanh cong");
            conn.commit();
            System.out.println("Da commit thay doi");
        } catch (SQLException e) {
            System.out.println("Loi: " + e.getMessage());
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("Da rollback cac thay doi");
                } catch (SQLException rollbackEx) {
                    System.out.println("Loi khi rollback: " + rollbackEx.getMessage());
                }
            }
        } finally {
            try {
                checkStmt = conn.createStatement();
                rs = checkStmt.executeQuery("select * from test_table");
                System.out.println("Du lieu hien tai trong bang:");
                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("id") + ", Name: " + rs.getString("name"));
                }
            } catch (SQLException e) {
                System.out.println("Loi khi truy van du lieu: " + e.getMessage());
            }
            try {
                if (stmt1 != null) stmt1.close();
                if (stmt2 != null) stmt2.close();
                if (checkStmt != null) checkStmt.close();
                if (rs != null) rs.close();
                if (conn != null) conn.close();
                System.out.println("Da dong ket noi");
            } catch (SQLException ex) {
                System.out.println("Loi khi dong ket noi: " + ex.getMessage());
            }
        }
    }
}