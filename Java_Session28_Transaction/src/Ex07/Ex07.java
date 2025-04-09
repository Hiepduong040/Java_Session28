package Ex07;

import java.sql.*;

public class Ex07 {
    static String URL = "jdbc:mysql://localhost:3306/portal_db";
    static String USER = "root";
    static String PASS = "12345678";

    public static void main(String[] args) throws InterruptedException {
        testIsolationLevel(Connection.TRANSACTION_READ_UNCOMMITTED, "READ_UNCOMMITTED");
        testIsolationLevel(Connection.TRANSACTION_READ_COMMITTED, "READ_COMMITTED");
        testIsolationLevel(Connection.TRANSACTION_REPEATABLE_READ, "REPEATABLE_READ");
        testIsolationLevel(Connection.TRANSACTION_SERIALIZABLE, "SERIALIZABLE");
    }

    static void testIsolationLevel(int isolationLevel, String levelName) throws InterruptedException {
        System.out.println("Bat dau thu nghiem voi Isolation Level: " + levelName);
        Thread writer = new Thread(() -> {
            try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
                conn.setAutoCommit(false);
                String sql = "insert into orders(customer_name, status) values (?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, "Nguyen Van B");
                pstmt.setString(2, "PENDING");
                pstmt.executeUpdate();
                System.out.println("[Writer] Chen xong, chua commit - " + levelName);
                Thread.sleep(5000);
                conn.commit();
                System.out.println("[Writer] Commit xong - " + levelName);
            } catch (Exception e) {
                System.out.println("Loi Writer: " + e.getMessage());
            }
        });

        Thread reader = new Thread(() -> {
            try {
                Thread.sleep(1000);
                Connection conn = DriverManager.getConnection(URL, USER, PASS);
                conn.setTransactionIsolation(isolationLevel);
                conn.setAutoCommit(false);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("select * from orders");
                System.out.println("[Reader] Ket qua doc lan 1 - " + levelName + ":");
                boolean hasData = false;
                while (rs.next()) {
                    hasData = true;
                    System.out.println(rs.getInt("order_id") + " - " + rs.getString("customer_name") + " - " + rs.getString("status"));
                }
                if (!hasData) {
                    System.out.println("Khong co du lieu");
                }
                Thread.sleep(6000);
                ResultSet rs2 = stmt.executeQuery("select * from orders");
                System.out.println("[Reader] Ket qua doc lan 2 - " + levelName + ":");
                hasData = false;
                while (rs2.next()) {
                    hasData = true;
                    System.out.println(rs2.getInt("order_id") + " - " + rs2.getString("customer_name") + " - " + rs2.getString("status"));
                }
                if (!hasData) {
                    System.out.println("Khong co du lieu");
                }
                conn.commit();
                conn.close();
            } catch (Exception e) {
                System.out.println("Loi Reader: " + e.getMessage());
            }
        });

        writer.start();
        reader.start();
        writer.join();
        reader.join();
        System.out.println("Ket thuc thu nghiem voi Isolation Level: " + levelName);
        System.out.println("-----------------------------------");
    }
}