package presentation;

import business.model.Account;
import business.model.AccountStatus;
import business.service.account.AccountService;
import business.service.account.AccountServiceImp;

import java.util.List;
import java.util.Scanner;

public class AccountUI {
    private static final AccountService accountService = new AccountServiceImp();

    public static void displayAccount(AccountService accountService) {
        List<Account> listAccounts = accountService.getAllAccounts();
        if (listAccounts.isEmpty()) {
            System.out.println("Không có tài khoản nào.");
        } else {
            for (Account account : listAccounts) {
                System.out.println(account.toString());
            }
        }
    }

    public static void displayAccountMenu(Scanner scanner) {
        do {
            System.out.println("***************ACCOUNT MENU**************");
            System.out.println("1. Danh sách tài khoản");
            System.out.println("2. Tạo tài khoản");
            System.out.println("3. Cập nhật tài khoản");
            System.out.println("4. Xóa tài khoản");
            System.out.println("5. Chuyển khoản");
            System.out.println("6. Tra cứu số dư tài khoản");
            System.out.println("7. Thoát");
            System.out.print("Lựa chọn của bạn: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        displayAccount(accountService);
                        break;
                    case 2:
                        createAccount(scanner);
                        break;
                    case 3:
                        updateAccount(scanner);
                        break;
                    case 4:
                        deleteAccount(scanner);
                        break;
                    case 5:
                        fundsTransfer(scanner, accountService);
                        break;
                    case 6:
                        checkBalance(scanner);
                        break;
                    case 7:
                        System.out.println("Tạm biệt!");
                        System.exit(0);
                    default:
                        System.err.println("Vui lòng chọn từ 1-7");
                }
            } catch (NumberFormatException e) {
                System.err.println("Vui lòng nhập số từ 1-7");
            }
        } while (true);
    }

    public static void createAccount(Scanner scanner) {
        System.out.println("Nhập tên tài khoản:");
        String accName = scanner.nextLine();
        System.out.println("Nhập số dư tài khoản:");
        double balance = Double.parseDouble(scanner.nextLine());
        Account account = new Account(0, accName, balance, AccountStatus.ACTIVE);
        int result = accountService.createAccount(account);
        if (result == 1) {
            System.out.println("Tạo tài khoản thành công!!!");
        } else {
            System.err.println("Tạo tài khoản thất bại!!!");
        }
    }

    public static void updateAccount(Scanner scanner) {
        System.out.println("Nhập ID tài khoản cần cập nhật:");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.println("Nhập tên mới:");
        String newName = scanner.nextLine();
        System.out.println("Chọn trạng thái (1: ACTIVE, 2: INACTIVE, 3: BLOCKED):");
        int statusChoice = Integer.parseInt(scanner.nextLine());
        AccountStatus status = switch (statusChoice) {
            case 1 -> AccountStatus.ACTIVE;
            case 2 -> AccountStatus.INACTIVE;
            case 3 -> AccountStatus.BLOCKED;
            default -> AccountStatus.ACTIVE;
        };

        int result = accountService.updateAccount(id, newName, status);
        if (result == 1) {
            System.out.println("Cập nhật tài khoản thành công!!!");
        } else {
            System.err.println("Cập nhật tài khoản thất bại! Tài khoản không tồn tại.");
        }
    }

    public static void deleteAccount(Scanner scanner) {
        System.out.println("Nhập ID tài khoản cần xóa:");
        int id = Integer.parseInt(scanner.nextLine());
        int result = accountService.deleteAccount(id);
        if (result == 1) {
            System.out.println("Xóa tài khoản thành công (đã chuyển sang trạng thái INACTIVE)!");
        } else {
            System.err.println("Xóa tài khoản thất bại! Tài khoản không tồn tại.");
        }
    }

    public static void checkBalance(Scanner scanner) {
        System.out.println("Nhập ID tài khoản cần tra cứu:");
        int id = Integer.parseInt(scanner.nextLine());
        Account account = accountService.getAccountById(id);
        if (account != null) {
            System.out.println("Số dư tài khoản: " + account.getBalance());
        } else {
            System.err.println("Không tìm thấy tài khoản!");
        }
    }

    public static void fundsTransfer(Scanner scanner, AccountService accountService) {
        System.out.println("Nhập số tài khoản người gửi:");
        int accSenderId = Integer.parseInt(scanner.nextLine());
        System.out.println("Nhập tên tài khoản người gửi:");
        String accSenderName = scanner.nextLine();
        System.out.println("Nhập số tài khoản người nhận:");
        int accReceiverId = Integer.parseInt(scanner.nextLine());
        System.out.println("Nhập tên tài khoản người nhận:");
        String accReceiverName = scanner.nextLine();
        System.out.println("Nhập số tiền chuyển:");
        double amount = Double.parseDouble(scanner.nextLine());
        int result = accountService.fundsTransfer(accSenderId, accSenderName, accReceiverId, accReceiverName, amount);
        switch (result) {
            case 1:
                System.err.println("Thông tin tài khoản người gửi không chính xác");
                break;
            case 2:
                System.err.println("Thông tin tài khoản người nhận không chính xác");
                break;
            case 3:
                System.err.println("Số dư tài khoản không đủ để chuyển khoản");
                break;
            case 4:
                System.out.println("Chuyển khoản thành công!!!");
                break;
            default:
                System.err.println("Có lỗi xảy ra khi chuyển khoản");
        }
    }
}