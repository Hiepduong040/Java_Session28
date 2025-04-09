package business.dao.account;

import business.config.ConnectionDB;
import business.model.Account;
import business.model.AccountStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDaoImp implements AccountDao {
    @Override
    public int fundsTransfer(int accSenderId, String accSenderName, int accReceiverId, String accReceiverName, double amount) {
        // Code hiện tại của bạn (giữ nguyên)
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            conn.setAutoCommit(false);
            callSt = conn.prepareCall("{call funds_transfer_amount(?,?,?,?,?,?)}");
            callSt.setInt(1, accSenderId);
            callSt.setString(2, accSenderName);
            callSt.setInt(3, accReceiverId);
            callSt.setString(4, accReceiverName);
            callSt.setDouble(5, amount);
            callSt.registerOutParameter(6, Types.INTEGER);
            callSt.execute();
            conn.commit();
            return callSt.getInt(6);
        } catch (SQLException e) {
            System.err.println("Có lỗi xảy ra trong quá trình chuyển khoản, dữ liệu đã được rollback");
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            return 0;
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
    }

    @Override
    public List<Account> getAllAccounts() {
        // Code hiện tại của bạn (giữ nguyên)
        List<Account> accounts = new ArrayList<>();
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{call get_all_accounts()}");
            ResultSet rs = callSt.executeQuery();
            while (rs.next()) {
                Account account = new Account(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("balance"),
                        AccountStatus.valueOf(rs.getString("status"))
                );
                accounts.add(account);
            }
        } catch (SQLException e) {
            System.err.println("Có lỗi xảy ra khi lấy danh sách tài khoản: " + e.getMessage());
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
        return accounts;
    }

    @Override
    public int createAccount(Account account) {
        // Code hiện tại của bạn (giữ nguyên)
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{call create_account(?,?,?,?)}");
            callSt.setString(1, account.getName());
            callSt.setDouble(2, account.getBalance());
            callSt.setString(3, account.getStatus().toString());
            callSt.registerOutParameter(4, Types.INTEGER);
            callSt.execute();
            return callSt.getInt(4);
        } catch (SQLException e) {
            System.err.println("Có lỗi khi tạo tài khoản: " + e.getMessage());
            return 0;
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
    }

    @Override
    public int updateAccount(int id, String name, AccountStatus status) {
        // Code hiện tại của bạn (giữ nguyên)
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{call update_account(?,?,?,?)}");
            callSt.setInt(1, id);
            callSt.setString(2, name);
            callSt.setString(3, status.toString());
            callSt.registerOutParameter(4, Types.INTEGER);
            callSt.execute();
            return callSt.getInt(4);
        } catch (SQLException e) {
            System.err.println("Có lỗi khi cập nhật tài khoản: " + e.getMessage());
            return 0;
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
    }

    @Override
    public int deleteAccount(int id) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{call delete_account(?,?)}");
            callSt.setInt(1, id);
            callSt.registerOutParameter(2, Types.INTEGER);
            callSt.execute();
            return callSt.getInt(2);
        } catch (SQLException e) {
            System.err.println("Có lỗi khi xóa tài khoản: " + e.getMessage());
            return 0;
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
    }

    @Override
    public Account getAccountById(int id) {
        Connection conn = null;
        CallableStatement callSt = null;
        try {
            conn = ConnectionDB.openConnection();
            callSt = conn.prepareCall("{call get_account_by_id(?)}");
            callSt.setInt(1, id);
            ResultSet rs = callSt.executeQuery();
            if (rs.next()) {
                return new Account(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("balance"),
                        AccountStatus.valueOf(rs.getString("status"))
                );
            }
            return null;
        } catch (SQLException e) {
            System.err.println("Có lỗi khi tra cứu tài khoản: " + e.getMessage());
            return null;
        } finally {
            ConnectionDB.closeConnection(conn, callSt);
        }
    }
}