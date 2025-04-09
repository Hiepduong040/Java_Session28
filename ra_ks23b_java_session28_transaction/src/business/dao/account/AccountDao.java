package business.dao.account;

import business.dao.AppDao;
import business.model.Account;
import business.model.AccountStatus;

import java.util.List;

public interface AccountDao extends AppDao {
    int fundsTransfer(int accSenderId, String accSenderName, int accReceiverId, String accReceiverName, double amount);
    List<Account> getAllAccounts();
    int createAccount(Account account);
    int updateAccount(int id, String name, AccountStatus status);
    int deleteAccount(int id); // Thêm method xóa
    Account getAccountById(int id); // Thêm method lấy tài khoản theo ID
}