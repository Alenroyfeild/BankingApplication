package UserInterface;

import java.io.Console;
import java.util.Arrays;
import java.util.Scanner;

import entities.Account;
import entities.Bank;
import entities.CIF;
import entities.UserLogin;
import services.BankMain;
import services.UserLoginServices;

public class AccountCreationUI {
    static Scanner sc = new Scanner(System.in);
    static Bank ba = Bank.getInstance();
    static UserLoginServices uls = new UserLoginServices();

    // this unction is used to user sigin
    public static void userSingin() {
        long mobileNo = UtilsUI.getMobileNo();
        if (uls.validateMoblieNo(mobileNo) != null) {
            System.out.println("This mobile number has already account exists");
            System.out.print("Enter 1 to re-enter mobile number or any integer to exit : ");
            int x = sc.nextInt();
            if (x == 1)
                userSingin();
            else {
                System.out.println("Account Sign up is failed");
                return;
            }
        } else {
            Boolean noMatch = false;
            do {
                Console c = System.console();
                char[] pass = c.readPassword("Enter the New password    : ");
                char[] pass1 = c.readPassword("Confirm the New password  : ");
                noMatch = !Arrays.equals(pass, pass1);
                if (noMatch) {
                    System.out.format("Passwords don't match. Try again.%n");
                } else {
                    String password = new String(pass);
                    ba.userLogins.add(new UserLogin(mobileNo, password));
                    System.out.println("\nUser Account Successfully created");
                }
            } while (noMatch);
        }
    }

    static BankMain bm = new BankMain();

    // this fuction is used to create CIF
    public static long createCIF(long mobileNo) {
        System.out.println("\nProvide the details for creating CIF : ");
        System.out.println("--------------------------------------------------------");
        String name[] = getNames();
        long aadharNumber = UtilsUI.getAadharno();
        int age = getUserAge();
        String[] address = UtilsUI.getAddress();
        CIF ci = bm.createCIF(name[0], name[1], aadharNumber, mobileNo, age, address);
        return ci.getCIFno();
    }

    // this function is used to get Names
    private static String[] getNames() {
        String name[] = new String[2];
        System.out.print("Enter Full Name          : ");
        name[0] = sc.nextLine();
        System.out.print("Enter Preferred Name     : ");
        name[1] = sc.nextLine();
        return name;
    }

    // this function is used to get user Age above 9
    public static int getUserAge() {
        int age;
        do {
            do {
                try {
                    System.out.print("Enter Age                : ");
                    age = Integer.parseInt(sc.next());
                } catch (Exception e) {
                    System.out.println("\nentered  is not a valid integer number\n Enter age :");
                    age = 0;
                }
            } while (age == 0);
            if (age >= 10 && age <= 100)
                break;
            else
                System.out.println("Enter age above 9 and below 100 only");
        } while (true);
        return age;
    }

    // this function is used to choose the account type
    public static String accountType() {
        System.out.println("--------------------------------------------------------");
        System.out.println("Select Account Type");
        System.out.println("1. Current Account");
        System.out.println("2. Savings Account");
        System.out.println("--------------------------------------------------------");
        int choice = 0;
        String accType;
        do {
            System.out.print("Enter choice: ");
            try {
                choice = Integer.parseInt(sc.next());
            } catch (NumberFormatException e) {
                System.out.println("entered choice  is not a valid integer number");
            }

            if (choice == 1)
                accType = "CurrentAccount";
            else
                accType = "SavingsAccount";
        } while (choice < 1 || choice > 2);

        return accType;
    }

    // this function is used to choose the Balance Type
    public static String balanceType() {
        int choice = 0;
        String balanceType;
        do {
            System.out.println("--------------------------------------------------------");
            System.out.println("Choose the Balance Type : ");
            System.out.println("1. Zero Account Balance ");
            System.out.println("2. Minimum balance");
            System.out.println("--------------------------------------------------------");
            try {
                System.out.print("Enter choice: ");
                choice = Integer.parseInt(sc.next());
            } catch (NumberFormatException e) {
                System.out.println("entered choice is not a valid integer number");
            }
            if (choice == 2) {
                balanceType = "MinimumBalanceAccount";
            } else {
                balanceType = "ZeroBalanceAccount";
            }
        } while (choice < 1 || choice > 2);
        return balanceType;
    }

    // this function is used to create account
    public static Account createAccountUI(long cifno, long mobileNo) {
        String accountType = accountType();
        String balanceType = balanceType();
        Account acc = bm.createAccount(mobileNo, cifno, accountType, balanceType);
        for (CIF cif : ba.cifList) {
            if (cif.getCIFno() == cifno) {
                userPassbook(cif, acc);

            }
        }
        return acc;
    }

    // this function is used to Display User Account details
    public static void userPassbook(CIF cif, Account acc) {
        System.out.println("\033[H\033[2J");
        System.out.println("\n  ----    Passbook    ----");
        System.out.println(" CIF No          : " + cif.getCIFno());
        System.out.println(" Username        : " + cif.getCustomerFullname());
        System.out.println(" Age             : " + cif.getAge());
        System.out.println(" Account number  : " + acc.getAccNo());
        System.out.println(" Account Type    : " + acc.getAccountType());
        System.out.println(" Balance Type    : " + acc.getBalanceType());
        System.out.println(" Mobile Number   : " + cif.getMobileNo());
        System.out.println(" Account opened on " + acc.getAccOpenDate());
        String[] address = cif.getAddress();
        System.out.println(" Address         : " + address[0] + "," + address[1] + "," + address[2] + "," + address[3]
                + "-" + address[4] + ".");
    }
}
