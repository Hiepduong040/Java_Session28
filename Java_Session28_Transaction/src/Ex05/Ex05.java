package Ex05;

import java.sql.*;
import java.time.LocalDate;

public class Ex05 {
    public static void main(String[] args) {
        String URL = "jdbc:mysql://localhost:3306/portal_db";
        String USER = "root";
        String PASS = "12345678";
        Connection conn = null;
        PreparedStatement insertOrderStmt = null;
        PreparedStatement insertDetailStmt = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Ket noi thanh cong");
            conn.setAutoCommit(false);
            String insertOrderSQL = "insert into orders (customer_name, order_date) values (?, ?)";
            insertOrderStmt = conn.prepareStatement(insertOrderSQL, Statement.RETURN_GENERATED_KEYS);
            insertOrderStmt.setString(1, "Nguyen Van B");
            insertOrderStmt.setDate(2, Date.valueOf(LocalDate.now()));
            insertOrderStmt.executeUpdate();
            ResultSet rs = insertOrderStmt.getGeneratedKeys();
            int orderId = 0;
            if (rs.next()) orderId = rs.getInt(1);
            System.out.println("Da tao don hang voi ID: " + orderId);
            String insertDetailSQL = "insert into order_details (order_id, product_name, quantity) values (?, ?, ?)";
            insertDetailStmt = conn.prepareStatement(insertDetailSQL);
            insertDetailStmt.setInt(1, orderId);
            insertDetailStmt.setString(2, "San pham A");
            insertDetailStmt.setInt(3, 2);
            if (insertDetailStmt.getParameterMetaData().getParameterCount() > 0 && 2 <= 0) {
                throw new SQLException("So luong khong hop le");
            }
            insertDetailStmt.executeUpdate();
            System.out.println("Da them chi tiet 1: San pham A, so luong 2");
            insertDetailStmt.setInt(1, orderId);
            insertDetailStmt.setString(2, "San pham B");
            insertDetailStmt.setInt(3, -1);
            if (insertDetailStmt.getParameterMetaData().getParameterCount() > 0 && -1 <= 0) {
                throw new SQLException("So luong khong hop le");
            }
            insertDetailStmt.executeUpdate();
            System.out.println("Da them chi tiet 2: San pham B, so luong -1");
            conn.commit();
            System.out.println("Don hang va chi tiet da duoc luu");
        } catch (SQLException e) {
            System.out.println("Loi: " + e.getMessage());
            try {
                if (conn != null) {
                    System.out.println("Loi xay ra. Rollback");
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.out.println("Loi khi rollback: " + ex.getMessage());
            }
        } finally {
            try {
                if (insertOrderStmt != null) insertOrderStmt.close();
                if (insertDetailStmt != null) insertDetailStmt.close();
                if (conn != null) conn.close();
                System.out.println("Da dong ket noi");
            } catch (SQLException e) {
                System.out.println("Loi khi dong ket noi: " + e.getMessage());
            }
        }
    }
}