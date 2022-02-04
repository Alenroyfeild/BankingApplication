package UserInterface;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import entities.Account;
import entities.FixedDeposit;
import services.FDServices;
import services.utils;

public class FDServicesUI {
    static Scanner sc = new Scanner(System.in);
    static FDServices fds = new FDServices();

    // this function is used to display interest details based on amount
    public static int FDServicesPage() {
        int choice = 0;
        do {
            System.out.println(
                    "\n\n-------------------------------------------------------------------------------------------------------------------------------------------");
                    System.out.println("\n        --- Fixed Deposit Services page ---");
                    System.out.println("\nChoose the Options : ");
            System.out.println(" 1.Apply FD Account");
            System.out.println(" 2.WithDraw FD Account");
            System.out.println(" 3.FD Account Summary");
            System.out.println(" 4.FD Interest Details");
            System.out.println(" 5.Exit");
            try {
                System.out.print("Enter choice : ");
                choice = Integer.parseInt(sc.next());
                return choice;
            } catch (NumberFormatException e) {
                System.out.println("You have entered wrong choice.\nPlease again Enter : ");
                choice = 0;
            }
        } while (choice < 1 || choice > 5);
        return 0;
    }

    public static void FDNotification(long mobileNo) {
        ArrayList<FixedDeposit> FDList = utils.getUserFDAcc(mobileNo);
        if (FDList == null)
            return;
        if (FDList != null) {
            System.out.println(
                    "-------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("  --  Notifications  -- \n");
            for (FixedDeposit fd : FDList) {
                Account acc = utils.getRefAccount(fd.getAccountNumber());
                LocalDate date = utils.getFDMatureDate(fd);
                if (date != null && date.compareTo(LocalDate.now()) >= 0)
                    fds.autoCreditFDAmount(acc, fd);
                else if (date != null)
                    System.out.println("FD Acc no :(" + fd.getFDAccNo() + ")    FD Mature Date : " + date + "\n");
            }
            System.out.println(
                    "-------------------------------------------------------------------------------------------------------------------------------------------");

        }
    }

    // this function is used to call particular method based on choice
    public static void FDServiceUI(long mobileNo) {
        int choice = 0;
        System.out.println("\033[H\033[2J");
        FDNotification(mobileNo);
        FixedDeposit fdAcc=null;
        int x=0;
        do {
            choice = FDServicesPage();
            if(choice!=1&&x==0){
                fdAcc = UtilsUI.displayFDAccNumbers(mobileNo);
                x=1;
            }
            if (choice == 1) {
                Account acc = UtilsUI.displayAccountNumber(mobileNo);
                createFDAccount(mobileNo, acc);
                x=0;
            } else if (choice == 2) {
                withdrawFD(fdAcc);
            } else if (choice == 3) {
                displayFDAccountDetails(fdAcc);
            } else if (choice == 4) {
                System.out.println("\033[H\033[2J");
                displayFDInterestDetails();
            } else {
                System.out.println("\nBack to HomePage");
            }
        } while (choice < 1 || choice > 5 || choice != 5);
    }

    // this function is used to withdraw FD amount
    private static void withdrawFD(FixedDeposit fdAcc) {
        if (fdAcc == null)
            return;
        Account acc = utils.getRefAccount(fdAcc.getAccountNumber());
        double amount = fds.doFDWithdraw(acc, fdAcc);
        if (amount >= 0) {
            System.out.println("\nTransaction Successfull....");
            System.out.println("FD Amount is added to your linked account");
            System.out.println("Interest Amount : " + amount + "is also added");
        } else if (amount == -1) {
            System.out.println("\nToday only you have applied FD");
            System.out.println("Transaction Successfull....");
            System.out.println("FD Amount is added to your linked account");
        } else if (amount == -2) {
            System.out.println("\nThis FD account is already closed");
        }
    }

    // this function is used to create FD account
    private static void createFDAccount(long mobileNo, Account acc) {
        System.out.println("\nProvide details for RD Account :");
        System.out.println("\n---------------------------------------------------------------------\n");
        double amount = getAmount(acc);
        int mons = getMonths();
        FixedDeposit acc1 = fds.createFD(mobileNo, acc.getAccNo(), amount, mons);
        displayFDAccountDetails(acc1);
    }

    // this function is used to display FD details
    public static void displayFDAccountDetails(FixedDeposit fd) {
        if (fd == null)
            return;
        System.out.println("\n  --  Fixed Deposit Passbook  -- ");
        System.out.println(" FD Account No        : " + fd.getFDAccNo());
        System.out.println(" FD Linked Account No : " + fd.getAccountNumber());
        System.out.println(" FD Amount            : " + fd.getFDAmount());
        System.out.println(" FD Interest Rate     : " + fd.getFDinterestRate());
        System.out.println(" FD Duration in months: " + fd.getFDMonths());
        System.out.println(" FD Account Openeddate: " + fd.getFDDepositDate());
    }

    // this function is used to get amount for FD deposit
    public static double getAmount(Account acc) {
        double amount;
        boolean bool = false;
        do {
            amount = UtilsUI.getAmount();
            if (amount >= 1000) {
                bool = utils.validBalance(acc, amount);
                if (!bool) {
                    System.out.println("\nInsufficient Balance");
                    System.out.println("Available Balance : " + acc.getAccountBalance());
                    return 0;
                }
            } else {
                System.out.println("RD amount must be greater than 99 /-");
            }
        } while (!bool);
        return amount;
    }

    // this function is used to display FD interest details
    public static void displayFDInterestDetails() {
        System.out.println("\nFixed Deposit interest is based on duration ");
        System.out.println("    Months         interest rate ");
        System.out.println("   1  to  6           6.5%      ");
        System.out.println("   6  to  12          7.50%   ");
        System.out.println("   12 to  24          8.25%   ");
        System.out.println("   above 24           8.50%      ");
    }

    // this function is used to get FD mons from the user
    public static int getMons() {
        int mons = 0;
        do {
            try {
                displayFDInterestDetails();
                System.out.print("Enter no of Months : ");
                mons = Integer.parseInt(sc.next());
            } catch (Exception e) {
                System.out.println("\nentered months is not a valid integer");
            }
        } while (mons <= 0);
        return mons;
    }

    // this function is used to get no of months from user for FD duration
    public static int getMonths() {
        int ref = 0, mons;
        do {
            mons = getMons();
            if (mons >= 1) {
                ref = 1;
                return mons;
            } else {
                System.out.println("months must must be greater than 0..");
            }
        } while (ref == 0);
        return 0;
    }
}
