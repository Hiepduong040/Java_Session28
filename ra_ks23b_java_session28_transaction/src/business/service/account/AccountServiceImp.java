package business.service.account;

import business.dao.account.AccountDao;
import business.dao.account.AccountDaoImp;
import business.model.Account;
import business.model.AccountStatus;

import java.util.List;

public class AccountServiceImp implements AccountService {
    private final AccountDao accountDao;

    public AccountServiceImp() {
        accountDao = new AccountDaoImp();
    }

    @Override
    public int fundsTransfer(int accSenderId, String accSenderName, int accReceiverId, String accReceiverName, double amount) {
        return accountDao.fundsTransfer(accSenderId, accSenderName, accReceiverId, accReceiverName, amount);
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountDao.getAllAccounts();
    }

    @Override
    public int createAccount(Account account) {
        return accountDao.createAccount(account);
    }


    @Override
    public int updateAccount(int id, String name, AccountStatus status) {
        return accountDao.updateAccount(id, name, status);
    }

    @Override
    public int deleteAccount(int id) {
        return accountDao.deleteAccount(id);
    }

    @Override
    public Account getAccountById(int id) {
        return accountDao.getAccountById(id);
    }
}