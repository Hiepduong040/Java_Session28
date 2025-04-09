package business.service.fundsTransfer;

import business.dao.FundsTransfer.FundsTransferDao;
import business.dao.FundsTransfer.FundsTransferDaoImp;
import business.model.FundsTransfer;

import java.util.Date;
import java.util.List;

public class FundsTransferServiceImp implements FundsTransferService {
    private final FundsTransferDao fundsTransferDao;

    public FundsTransferServiceImp() {
        fundsTransferDao = new FundsTransferDaoImp();
    }

    @Override
    public List<FundsTransfer> getTransactionHistory() {
        return fundsTransferDao.getTransactionHistory();
    }

    @Override
    public double getTransferAmountByDateRange(Date startDate, Date endDate) {
        return fundsTransferDao.getTransferAmountByDateRange(startDate, endDate);
    }

    @Override
    public double getReceivedAmountByAccount(int accountId) {
        return fundsTransferDao.getReceivedAmountByAccount(accountId);
    }

    @Override
    public int getSuccessfulTransactionsByDateRange(Date startDate, Date endDate) {
        return fundsTransferDao.getSuccessfulTransactionsByDateRange(startDate, endDate);
    }
}