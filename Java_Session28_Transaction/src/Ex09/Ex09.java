package Ex09;

import java.sql.*;
import java.util.Scanner;

public class Ex09 {
    static String URL = "jdbc:mysql://localhost:3306/portal_db";
    static String USER = "root";
    static String PASS = "12345678";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhap ma user: ");
        int userId = Integer.parseInt(sc.nextLine());
        System.out.print("Nhap ma dau gia: ");
        int auctionId = Integer.parseInt(sc.nextLine());
        System.out.print("Nhap gia dat: ");
        double bidAmount = Double.parseDouble(sc.nextLine());
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            try (
                    PreparedStatement getUser = conn.prepareStatement("select balance from users where user_id = ?");
                    PreparedStatement getAuction = conn.prepareStatement("select highest_bid from auctions where auction_id = ?");
                    PreparedStatement updateAuction = conn.prepareStatement("update auctions set highest_bid = ? where auction_id = ?");
                    PreparedStatement insertBid = conn.prepareStatement("insert into bids(auction_id, user_id, bid_amount, bid_timestamp) values (?, ?, ?, ?)");
                    PreparedStatement insertFail = conn.prepareStatement("insert into failed_bids(user_id, auction_id, reason) values (?, ?, ?)")
            ) {
                getUser.setInt(1, userId);
                ResultSet rsUser = getUser.executeQuery();
                if (!rsUser.next()) {
                    insertFail.setInt(1, userId);
                    insertFail.setInt(2, auctionId);
                    insertFail.setString(3, "Nguoi dung khong ton tai");
                    insertFail.executeUpdate();
                    conn.rollback();
                    System.out.println("Nguoi dung khong ton tai. Ghi log that bai");
                    return;
                }
                double balance = rsUser.getDouble("balance");
                getAuction.setInt(1, auctionId);
                ResultSet rsAuction = getAuction.executeQuery();
                if (!rsAuction.next()) {
                    insertFail.setInt(1, userId);
                    insertFail.setInt(2, auctionId);
                    insertFail.setString(3, "Phien dau gia khong ton tai");
                    insertFail.executeUpdate();
                    conn.rollback();
                    System.out.println("Phien dau gia khong ton tai. Ghi log that bai");
                    return;
                }
                double highestBid = rsAuction.getDouble("highest_bid");
                if (bidAmount <= highestBid) {
                    insertFail.setInt(1, userId);
                    insertFail.setInt(2, auctionId);
                    insertFail.setString(3, "Gia dat phai cao hon gia hien tai");
                    insertFail.executeUpdate();
                    conn.rollback();
                    System.out.println("Gia dat khong hop le. Ghi log that bai");
                    return;
                }
                if (balance < bidAmount) {
                    insertFail.setInt(1, userId);
                    insertFail.setInt(2, auctionId);
                    insertFail.setString(3, "So du khong du");
                    insertFail.executeUpdate();
                    conn.rollback();
                    System.out.println("So du khong du. Ghi log that bai");
                    return;
                }
                updateAuction.setDouble(1, bidAmount);
                updateAuction.setInt(2, auctionId);
                updateAuction.executeUpdate();
                insertBid.setInt(1, auctionId);
                insertBid.setInt(2, userId);
                insertBid.setDouble(3, bidAmount);
                insertBid.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                insertBid.executeUpdate();
                conn.commit();
                System.out.println("Dat gia thanh cong");
            } catch (SQLException e) {
                System.out.println("Loi: " + e.getMessage());
                conn.rollback();
                System.out.println("Co loi xay ra. Rollback");
            }
        } catch (SQLException e) {
            System.out.println("Loi ket noi: " + e.getMessage());
        }
    }
}