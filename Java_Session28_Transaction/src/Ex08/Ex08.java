package Ex08;

import java.sql.*;
import java.time.LocalDate;

public class Ex08 {
    static String URL = "jdbc:mysql://localhost:3306/portal_db";
    static String USER = "root";
    static String PASS = "12345678";

    public static void main(String[] args) throws InterruptedException {
        Thread user1 = new Thread(() -> bookRoom(1, 101, "CONFIRMED"));
        Thread user2 = new Thread(() -> bookRoom(2, 101, "CONFIRMED"));
        Thread user3 = new Thread(() -> bookRoom(999, 102, "CONFIRMED"));
        user1.start();
        user2.start();
        user3.start();
        user1.join();
        user2.join();
        user3.join();
    }

    static void bookRoom(int customerId, int roomId, String status) {
        Connection conn = null;
        PreparedStatement pstmtCheckRoom = null;
        PreparedStatement pstmtUpdateRoom = null;
        PreparedStatement pstmtInsertBooking = null;
        PreparedStatement pstmtLogFail = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASS);
            conn.setAutoCommit(false);
            String sqlCheckRoom = "select availability from rooms where room_id = ? for update";
            pstmtCheckRoom = conn.prepareStatement(sqlCheckRoom);
            pstmtCheckRoom.setInt(1, roomId);
            ResultSet rs = pstmtCheckRoom.executeQuery();
            if (rs.next() && rs.getBoolean("availability")) {
                String sqlUpdateRoom = "update rooms set availability = false where room_id = ?";
                pstmtUpdateRoom = conn.prepareStatement(sqlUpdateRoom);
                pstmtUpdateRoom.setInt(1, roomId);
                pstmtUpdateRoom.executeUpdate();
                String sqlInsertBooking = "insert into bookings(customer_id, room_id, booking_date, status) values (?, ?, ?, ?)";
                pstmtInsertBooking = conn.prepareStatement(sqlInsertBooking);
                pstmtInsertBooking.setInt(1, customerId);
                pstmtInsertBooking.setInt(2, roomId);
                pstmtInsertBooking.setDate(3, Date.valueOf(LocalDate.now()));
                pstmtInsertBooking.setString(4, status);
                pstmtInsertBooking.executeUpdate();
                conn.commit();
                System.out.println("Dat phong thanh cong cho khach hang ID: " + customerId);
            } else {
                String sqlLogFail = "insert into failed_bookings(customer_id, room_id, reason) values (?, ?, ?)";
                pstmtLogFail = conn.prepareStatement(sqlLogFail);
                pstmtLogFail.setInt(1, customerId);
                pstmtLogFail.setInt(2, roomId);
                pstmtLogFail.setString(3, "Phong da duoc dat hoac khong ton tai");
                pstmtLogFail.executeUpdate();
                conn.commit();
                System.out.println("Phong khong kha dung. Ghi log that bai cho khach hang ID: " + customerId);
            }
        } catch (SQLException e) {
            System.out.println("Loi: " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback();
                    System.out.println("Co loi xay ra. Rollback cho khach hang ID: " + customerId);
                }
            } catch (SQLException ex) {
                System.out.println("Loi khi rollback: " + ex.getMessage());
            }
        } finally {
            try {
                if (pstmtCheckRoom != null) pstmtCheckRoom.close();
                if (pstmtUpdateRoom != null) pstmtUpdateRoom.close();
                if (pstmtInsertBooking != null) pstmtInsertBooking.close();
                if (pstmtLogFail != null) pstmtLogFail.close();
                if (conn != null) conn.close();
                System.out.println("Da dong ket noi cho khach hang ID: " + customerId);
            } catch (SQLException e) {
                System.out.println("Loi khi dong ket noi: " + e.getMessage());
            }
        }
    }
}