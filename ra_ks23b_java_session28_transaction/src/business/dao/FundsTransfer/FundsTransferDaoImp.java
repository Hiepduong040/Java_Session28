package business.dao.FundsTransfer;


import business.config.ConnectionDB;
import business.model.FundsTransfer;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FundsTransferDaoImp implements FundsTransferDao {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public List<FundsTransfer> getTransactionHistory() {
        List<FundsTransfer> transactions = new ArrayList<>();
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{call get_transaction_history()}");
            ResultSet rs = callSt.executeQuery();
            while (rs.next()) {
                FundsTransfer ft = new FundsTransfer(
                        rs.getInt("fd_id"),
                        rs.getInt("acc_sender_id"),
                        rs.getInt("acc_receiver_id"),
                        rs.getDouble("fd_amount"),
                        rs.getDate("fd_created"),
                        rs.getBoolean("fd_status")
                );
                transactions.add(ft);
            }
        } catch (SQLException e) {
            System.err.println("Có lỗi khi lấy lịch sử giao dịch: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return transactions;
    }

    @Override
    public double getTransferAmountByDateRange(Date startDate, Date endDate) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{call get_transfer_amount_by_date(?,?,?)}");
            callSt.setString(1, DATE_FORMAT.format(startDate));
            callSt.setString(2, DATE_FORMAT.format(endDate));
            callSt.registerOutParameter(3, Types.DOUBLE);
            callSt.execute();
            return callSt.getDouble(3);
        } catch (SQLException e) {
            System.err.println("Có lỗi khi thống kê số tiền chuyển: " + e.getMessage());
            return 0;
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
    }

    @Override
    public double getReceivedAmountByAccount(int accountId) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{call get_received_amount_by_account(?,?)}");
            callSt.setInt(1, accountId);
            callSt.registerOutParameter(2, Types.DOUBLE);
            callSt.execute();
            return callSt.getDouble(2);
        } catch (SQLException e) {
            System.err.println("Có lỗi khi thống kê số tiền nhận: " + e.getMessage());
            return 0;
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
    }

    @Override
    public int getSuccessfulTransactionsByDateRange(Date startDate, Date endDate) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{call get_successful_transactions_by_date(?,?,?)}");
            callSt.setString(1, DATE_FORMAT.format(startDate));
            callSt.setString(2, DATE_FORMAT.format(endDate));
            callSt.registerOutParameter(3, Types.INTEGER);
            callSt.execute();
            return callSt.getInt(3);
        } catch (SQLException e) {
            System.err.println("Có lỗi khi thống kê giao dịch thành công: " + e.getMessage());
            return 0;
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
    }
}