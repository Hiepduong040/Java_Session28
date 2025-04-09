package Ex03;

import java.sql.*;

public class Ex03 {
    public static void main(String[] args) {
        String URL = "jdbc:mysql://localhost:3306/portal_db";
        String USER = "root";
        String PASS = "12345678";
        Connection conn = null;
        PreparedStatement withdrawStmt = null;
        PreparedStatement depositStmt = null;
        int fromAccountId = 1;
        int toAccountId = 2;
        double amount = 500.0;
        try {
            conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Ket noi thanh cong");
            System.out.println("Auto-commit ban dau: " + conn.getAutoCommit());
            conn.setAutoCommit(false);
            System.out.println("Auto-commit sau khi tat: " + conn.getAutoCommit());
            String withdrawSQL = "update accounts set balance = balance - ? where id = ? and balance >= ?";
            withdrawStmt = conn.prepareStatement(withdrawSQL);
            withdrawStmt.setDouble(1, amount);
            withdrawStmt.setInt(2, fromAccountId);
            withdrawStmt.setDouble(3, amount);
            int rows1 = withdrawStmt.executeUpdate();
            if (rows1 == 0) {
                throw new SQLException("Khong du so du trong tai khoan ID = " + fromAccountId);
            }
            System.out.println("Da tru " + amount + " tu tai khoan ID = " + fromAccountId);
            String depositSQL = "update accounts set balance = balance + ? where id = ?";
            depositStmt = conn.prepareStatement(depositSQL);
            depositStmt.setDouble(1, amount);
            depositStmt.setInt(2, toAccountId);
            int rows2 = depositStmt.executeUpdate();
            if (rows2 == 0) {
                throw new SQLException("Tai khoan nhan ID = " + toAccountId + " khong ton tai");
            }
            System.out.println("Da cong " + amount + " vao tai khoan ID = " + toAccountId);
            conn.commit();
            System.out.println("Chuyen tien thanh cong. Giao dich da duoc commit");
        } catch (SQLException e) {
            System.out.println("Loi: " + e.getMessage());
            try {
                if (conn != null) {
                    System.out.println("Co loi xay ra. Rollback");
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.out.println("Loi khi rollback: " + ex.getMessage());
            }
        } finally {
            try {
                if (withdrawStmt != null) withdrawStmt.close();
                if (depositStmt != null) depositStmt.close();
                if (conn != null) conn.close();
                System.out.println("Da dong ket noi");
            } catch (SQLException e) {
                System.out.println("Loi khi dong ket noi: " + e.getMessage());
            }
        }
    }
}