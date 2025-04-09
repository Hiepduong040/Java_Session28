package business.service.fundsTransfer;

import business.model.FundsTransfer;

import java.util.Date;
import java.util.List;

public interface FundsTransferService {
    List<FundsTransfer> getTransactionHistory();
    double getTransferAmountByDateRange(Date startDate, Date endDate);
    double getReceivedAmountByAccount(int accountId);
    int getSuccessfulTransactionsByDateRange(Date startDate, Date endDate);
}
