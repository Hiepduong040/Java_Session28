package presentation;

import business.model.FundsTransfer;
import business.service.fundsTransfer.FundsTransferService;
import business.service.fundsTransfer.FundsTransferServiceImp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class FundsTransferUI {
    private static final FundsTransferService fundsTransferService = new FundsTransferServiceImp();
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    public static void displayFundsTransferMenu(Scanner scanner) {
        do {
            System.out.println("*****************FT MENU***************");
            System.out.println("1. Lịch sử giao dịch");
            System.out.println("2. Thống kê số tiền chuyển trong khoảng từ ngày đến ngày");
            System.out.println("3. Thống kê số tiền nhận theo tài khoản");
            System.out.println("4. Thống kê số giao dịch thành công từ ngày đến ngày");
            System.out.println("5. Thoát");
            System.out.print("Lựa chọn của bạn: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        showTransactionHistory();
                        break;
                    case 2:
                        showTransferStatistics(scanner);
                        break;
                    case 3:
                        showReceivedAmountByAccount(scanner);
                        break;
                    case 4:
                        showSuccessfulTransactions(scanner);
                        break;
                    case 5:
                        System.out.println("Quay lại menu chính...");
                        return;
                    default:
                        System.err.println("Vui lòng chọn từ 1-5");
                }
            } catch (NumberFormatException e) {
                System.err.println("Vui lòng nhập số từ 1-5");
            }
        } while (true);
    }

    private static void showTransactionHistory() {
        List<FundsTransfer> transactions = fundsTransferService.getTransactionHistory();
        if (transactions.isEmpty()) {
            System.out.println("Không có giao dịch nào.");
        } else {
            System.out.println("Lịch sử giao dịch:");
            for (FundsTransfer ft : transactions) {
                System.out.printf("TK %d -> TK %d: %.2f - %s (Trạng thái: %s)%n",
                        ft.getSenderId(), ft.getReceiverId(), ft.getAmount(),
                        DATE_FORMAT.format(ft.getCreated()), ft.isStatus() ? "Thành công" : "Thất bại");
            }
        }
    }

    private static void showTransferStatistics(Scanner scanner) {
        System.out.println("Nhập ngày bắt đầu (dd/MM/yyyy):");
        String startDateStr = scanner.nextLine();
        System.out.println("Nhập ngày kết thúc (dd/MM/yyyy):");
        String endDateStr = scanner.nextLine();
        try {
            Date startDate = DATE_FORMAT.parse(startDateStr);
            Date endDate = DATE_FORMAT.parse(endDateStr);
            double totalAmount = fundsTransferService.getTransferAmountByDateRange(startDate, endDate);
            System.out.printf("Tổng tiền chuyển từ %s đến %s: %.2f VND%n", startDateStr, endDateStr, totalAmount);
        } catch (ParseException e) {
            System.err.println("Định dạng ngày không hợp lệ. Vui lòng nhập theo định dạng dd/MM/yyyy.");
        }
    }

    private static void showReceivedAmountByAccount(Scanner scanner) {
        System.out.println("Nhập ID tài khoản:");
        int accountId = Integer.parseInt(scanner.nextLine());
        double totalReceived = fundsTransferService.getReceivedAmountByAccount(accountId);
        System.out.printf("Tổng tiền nhận của tài khoản %d: %.2f VND%n", accountId, totalReceived);
    }

    private static void showSuccessfulTransactions(Scanner scanner) {
        System.out.println("Nhập ngày bắt đầu (dd/MM/yyyy):");
        String startDateStr = scanner.nextLine();
        System.out.println("Nhập ngày kết thúc (dd/MM/yyyy):");
        String endDateStr = scanner.nextLine();
        try {
            Date startDate = DATE_FORMAT.parse(startDateStr);
            Date endDate = DATE_FORMAT.parse(endDateStr);
            int count = fundsTransferService.getSuccessfulTransactionsByDateRange(startDate, endDate);
            System.out.printf("Số giao dịch thành công từ %s đến %s: %d giao dịch%n", startDateStr, endDateStr, count);
        } catch (ParseException e) {
            System.err.println("Định dạng ngày không hợp lệ. Vui lòng nhập theo định dạng dd/MM/yyyy.");
        }
    }
}