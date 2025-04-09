package business.service.account;

import business.model.Account;
import business.model.AccountStatus;
import business.service.AppService;

import java.util.List;

public interface AccountService extends AppService {
    int fundsTransfer(int accSenderId, String accSenderName, int accReceiverId, String accReceiverName, double amount);
    List<Account> getAllAccounts();
    int createAccount(Account account);
    int updateAccount(int id, String name, AccountStatus status);

    int deleteAccount(int id); // Thêm method xóa
    Account getAccountById(int id); // Thêm method tra cứu
}