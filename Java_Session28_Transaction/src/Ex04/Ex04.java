package Ex04;

import java.sql.*;

public class Ex04 {
    public static void main(String[] args) {
        String URL = "jdbc:mysql://localhost:3306/portal_db";
        String USER = "root";
        String PASS = "12345678";
        Connection connA = null;
        Connection connB = null;
        PreparedStatement withdrawStmt = null;
        PreparedStatement depositStmt = null;
        int fromAccountId = 1;
        int toAccountId = 2;
        double amount = 300.0;
        try {
            connA = DriverManager.getConnection(URL, USER, PASS);
            connB = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Ket noi thanh cong");
            connA.setAutoCommit(false);
            connB.setAutoCommit(false);
            String withdrawSQL = "update bank_accounts set balance = balance - ? where account_id = ? and balance >= ? and bank_name = ?";
            withdrawStmt = connA.prepareStatement(withdrawSQL);
            withdrawStmt.setDouble(1, amount);
            withdrawStmt.setInt(2, fromAccountId);
            withdrawStmt.setDouble(3, amount);
            withdrawStmt.setString(4, "BankA");
            int rows1 = withdrawStmt.executeUpdate();
            if (rows1 == 0) {
                throw new SQLException("Khong du tien hoac tai khoan gui khong ton tai");
            }
            System.out.println("Da tru " + amount + " tu tai khoan ID " + fromAccountId + " tai BankA");
            String depositSQL = "update bank_accounts set balance = balance + ? where account_id = ? and bank_name = ?";
            depositStmt = connB.prepareStatement(depositSQL);
            depositStmt.setDouble(1, amount);
            depositStmt.setInt(2, toAccountId);
            depositStmt.setString(3, "BankB");
            int rows2 = depositStmt.executeUpdate();
            if (rows2 == 0) {
                throw new SQLException("Tai khoan nhan khong ton tai");
            }
            System.out.println("Da cong " + amount + " vao tai khoan ID " + toAccountId + " tai BankB");
            connA.commit();
            connB.commit();
            System.out.println("Chuyen khoan thanh cong. Giao dich da duoc commit");
        } catch (SQLException e) {
            System.out.println("Loi: " + e.getMessage());
            try {
                if (connA != null) {
                    System.out.println("Rollback ket noi BankA");
                    connA.rollback();
                }
                if (connB != null) {
                    System.out.println("Rollback ket noi BankB");
                    connB.rollback();
                }
            } catch (SQLException ex) {
                System.out.println("Loi khi rollback: " + ex.getMessage());
            }
        } finally {
            try {
                if (withdrawStmt != null) withdrawStmt.close();
                if (depositStmt != null) depositStmt.close();
                if (connA != null) connA.close();
                if (connB != null) connB.close();
                System.out.println("Da dong ket noi");
            } catch (SQLException e) {
                System.out.println("Loi khi dong ket noi: " + e.getMessage());
            }
        }
    }
}