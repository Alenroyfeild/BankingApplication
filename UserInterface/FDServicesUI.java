package UserInterface;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import entities.Account;
import entities.CIF;
import entities.FixedDeposit;
import services.FDServices;
import services.utils;

public class FDServicesUI {
    static Scanner sc = new Scanner(System.in);
    static FDServices fds = new FDServices();
    static DecimalFormat df = new DecimalFormat("0.00");
    // this function is used to display interest details based on amount
    public static int FDServicesPage() {
        int choice = 0;
        do {
            System.out.println(
                    "\n\n-------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("\n        --- Fixed Deposit Services page ---");
            System.out.println("\nChoose the Options : ");
            System.out.println(" 1.Create FD");
            System.out.println(" 2.WithDraw FD Account");
            System.out.println(" 3.FD's Summary");
            System.out.println(" 4.FD Interest Details");
            System.out.println(" 5.Back to Deposit Selection");
            try {
                System.out.print("Enter choice : ");
                choice = Integer.parseInt(sc.next());
                System.out.println(
                        "\n------------------------------------------------------------------------------------------------------------------------------------------------");
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
                if (fd.getStatus()) {
                    LocalDate date = utils.getFDMatureDate(fd);
                    fds.getTotalInterestAmt(fd);
                    if (date != null)
                        System.out.println(
                                "FD Acc no :(" + fd.getFDAccNo() + ")    FD Mature Date : " + date + "    FD Amount : "
                                        + fd.getFDAmount() + "   FD Interest : " + (fd.getFDInterestAmount()) + "\n");
                }
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
        FixedDeposit fdAcc = null;
        do {
            choice = FDServicesPage();
            if (choice == 1) {
                createFDAccount(mobileNo);
            } else if (choice == 2) {
                fdAcc = UtilsUI.displayFDAccNumbers(mobileNo);
                withdrawFD(fdAcc, mobileNo);
            } else if (choice == 3) {
                displayFDSummary(mobileNo);
            } else if (choice == 4) {
                System.out.println("\033[H\033[2J");
                displayFDInterestDetails();
            } else {
                System.out.println("\nBack to Deposit Page");
            }
        } while (choice < 1 || choice > 5 || choice != 5);
    }

    public static void displayFDSummary(long mobileNo) {
        ArrayList<FixedDeposit> FDList = null;
        FDList = utils.getUserAllFDAcc(mobileNo);
        if (FDList != null) {
            System.out.println(
                    "\n   --    FD Accounts    --\n----------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.format("%1$-30s%2$-20s%3$-20s%4$-20s%5$-20s%6$-20s%7$-20s%8$-20s\n", "FDAccountNumber",
                    "FD Amount",
                    "FD Interest Rate",
                    "FD Depositdate", "FD Mature Date", "FD Mature Amount", "FD Interest", "FD Status");
            System.out.println(
                    "-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            for (FixedDeposit fd : FDList) {
                fds.getTotalInterestAmt(fd);
                String status;
                if (fd.getStatus()) {
                    status = "Active";
                } else {
                    status = "Closed";
                }
                System.out.format("%1$-30s%2$-20s%3$-20s%4$-20s%5$-20s%6$-20s%7$-20s%8$-20s\n", fd.getFDAccNo(),
                        df.format(fd.getFDAmount()), fd.getFDinterestRate(), fd.getFDDepositDate(),
                        utils.getFDMatureDate(fd), df.format(fd.getFDAmount() + fd.getFDInterestAmount()),
                        df.format(fd.getFDInterestAmount()), status);
            }
            System.out.println(
                    "----------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        }
    }

    // this function is used to withdraw FD amount
    private static void withdrawFD(FixedDeposit fdAcc, long mobileNo) {
        if (fdAcc == null)
            return;
        System.out.println("\nSelect the Account for FD amount Deposit :");
        Account acc = UtilsUI.displayAccountNumber(mobileNo);
        double amount = fds.doFDWithdraw(acc, fdAcc);
        if (amount >= 0) {
            System.out.println("\nTransaction Successfull....");
            System.out.println("FD Amount : " + df.format(fdAcc.getFDAmount()) + " is added to selected account");
            System.out.println("Interest Amount : " + df.format(amount) + "is also added");
            System.out.println("Successfully FD Account is closed");
        } else if (amount == -1) {
            System.out.println("\nToday only you have applied FD");
            System.out.println("Transaction Successfull....");
            System.out.println("FD Amount : " + df.format(fdAcc.getFDAmount()) + "  is added to selected account");
            System.out.println("Successfully FD Account is closed");
        } else if (amount == -2) {
            System.out.println("\nThis FD account is already closed");
        }
    }

    // this function is used to create FD account
    private static void createFDAccount(long mobileNo) {
        System.out.println("\nProvide details for FD Account :");
        System.out.println("\n---------------------------------------------------------------------\n");
        int mons = getMonths();
        CIF cif = UtilsUI.getNomineeAadharNo();
        if (cif == null) {
            System.out.println("Create Account for Nominee ");
            return;
        }
        System.out.println("\n Nominee Name : " + cif.getCustomerFullname());
        System.out.print("Enter 1 to continue or any number to exit : ");
        int x = sc.nextInt();
        if (x != 1)
            return;
        System.out.println("\nSelect the account to withdraw for FD deposit : ");
        Account acc = UtilsUI.displayAccountNumber(mobileNo);
        double amount = getAmount(acc);
        FixedDeposit acc1 = fds.createFD(cif.getAadharNumber(), mobileNo, acc.getAccNo(), amount, mons);
        displayFDAccountDetails(acc1);
    }

    // this function is used to display FD details
    public static void displayFDAccountDetails(FixedDeposit fd) {
        if (fd == null)
            return;
        System.out.println("\n  --  Fixed Deposit Passbook  -- ");
        System.out.println(" FD Acc Holder Name   : " + utils.getUsername(fd.getMobileNo()));
        System.out.println(" FD Account No        : " + fd.getFDAccNo());
        System.out.println(" FD Nominee Aadhar No : " + fd.getNomineeAadhar());
        System.out.println(" Nominee Aadhar Name  : " + utils.getUsername(fd.getNomineeAadhar()));
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
            if (amount >= 100) {
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
        System.out.println("    Months     |    interest rate (p.a)");
        System.out.println("-----------------------------------------------");
        System.out.println("   1  to  5    |       6.5%      ");
        System.out.println("   6  to  11   |       7.50%   ");
        System.out.println("   12 to  23   |       8.25%   ");
        System.out.println("   above 24    |       8.50%      ");
        System.out.println("\n For age above 60 will get InterestRate + 0.75% \n");
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
