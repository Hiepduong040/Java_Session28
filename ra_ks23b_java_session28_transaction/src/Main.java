import presentation.AccountUI;
import presentation.FundsTransferUI;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("***************MAIN MENU**************");
            System.out.println("1. Quản lý tài khoản");
            System.out.println("2. Quản lý giao dịch");
            System.out.println("3. Thoát");
            System.out.print("Lựa chọn của bạn: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        AccountUI.displayAccountMenu(scanner);
                        break;
                    case 2:
                        FundsTransferUI.displayFundsTransferMenu(scanner);
                        break;
                    case 3:
                        System.out.println("Tạm biệt!");
                        System.exit(0);
                    default:
                        System.err.println("Vui lòng chọn từ 1-3");
                }
            } catch (NumberFormatException e) {
                System.err.println("Vui lòng nhập số từ 1-3");
            }
        } while (true);
    }
}