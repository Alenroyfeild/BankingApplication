package UserInterface;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import entities.Account;
import entities.CIF;
import entities.RecurringDeposit;
import entities.Transactions;
import services.RDServices;
import services.utils;

public class RDServicesUI {
    static Scanner sc = new Scanner(System.in);
    static RDServices rds = new RDServices();

    // this function is used to display interest details based on amount
    public static int RDServicesPage() {
        int choice = 0;
        do {
            System.out.println(
                    "\n\n-------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("\n  -- RD services page  -- ");
            System.out.println("Choose the Options : ");
            System.out.println(" 1.Apply RD Account");
            System.out.println(" 2.Pay RD monthly Amount");
            System.out.println(" 3.WithDraw RD Account");
            System.out.println(" 4.RD Account Details");
            System.out.println(" 5.RD Statements");
            System.out.println(" 6.Exit");
            try {
                System.out.print("Enter choice : ");
                choice = Integer.parseInt(sc.next());
                return choice;
            } catch (NumberFormatException e) {
                System.out.println("You have entered wrong choice.\nPlease again Enter : ");
                choice = 0;
            }
        } while (choice < 1 || choice > 4);
        return 0;
    }

    public static void RDNotification(long mobileNo) {
        ArrayList<RecurringDeposit> RDList = utils.getUserRDAcc(mobileNo);
        if (RDList != null) {
            System.out.println(
                    "-------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("  --  Notifications  -- \n");
            for (RecurringDeposit rd : RDList) {
                Account acc = utils.getRefAccount(rd.getAccNo());
                LocalDate date = utils.getRDDueDate(rd);
                if (rd.getRDRemainingMonths() >= 1) {
                    System.out.println("RD Acc no :(" + rd.getRDID() + ")    RD Amount :(" + rd.getRDAmount()
                            + ")     Remaining Months :(" + rd.getRDRemainingMonths() + ")  next Due date : " + date
                            + "\n");
                }
                if (rd.getRDOpenDate().plusMonths(rd.getRDTenure()).compareTo(LocalDate.now()) >= 0)
                    if (rd.getRDRemainingMonths() == 0) {
                        double amount = rds.addRDAmount(acc, rd);
                        System.out.println("RD Acc no :(" + rd.getRDID() + ")    RD interest(" + Math.round(amount)
                                + ") ,Total RD amount : ("
                                + rd.getTotalRDAmount() + ") is added to your Account\n");
                        rd.setRDStatus(false);
                    }
            }
            System.out.println(
                    "-------------------------------------------------------------------------------------------------------------------------------------------");
        }
    }

    // this function is used to call particular method based on choice
    public static void RDServiceUI(long mobileNo) {
        int choice = 0;
        System.out.println("\033[H\033[2J");
        RDNotification(mobileNo);
        RecurringDeposit rdAcc=null;
        int x=0;
        do {
            choice = RDServicesPage();
            if(choice!=1&&x==0)
            rdAcc = UtilsUI.displayRDAccNumbers(mobileNo);
            x=1;
            Account acc = null;
            if (choice == 1) {
                acc = UtilsUI.displayAccountNumber(mobileNo);
                createRDAccount(mobileNo, acc);
                x=0;
            } else if (choice == 2) {
                payRDAmount(rdAcc);
            } else if (choice == 3) {
                withdrawRD(rdAcc);
            } else if (choice == 4) {
                System.out.println("\033[H\033[2J");
                displayRDdetails(rdAcc);
            } else if (choice == 5) {
                System.out.println("\033[H\033[2J");
                displayRDStatements(rdAcc);
            } else {
                System.out.println("\nBack to HomePage");
            }
        } while (choice < 1 || choice > 6 || choice != 6);
    }

    // this function is used display RD statements
    private static void displayRDStatements(RecurringDeposit rd) {
        if (rd == null) {
            return;
        }
        ArrayList<Transactions> arrList = rds.getRDStatementList(rd);
        if (arrList == null)
            return;
        String accNo = (rd.getAccNo() + "");
        System.out.println("\n  Account No : 2*****" + accNo.substring(6));
        System.out.println(
                "\n  --   RD Details  --\n------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.format("%1$-30s%2$-20s%3$-20s%4$-30s%5$-20s%6$-20s\n", "RDAccountNumber",
                "RDAmount", "RDInterestRate", "RDDurationMonths", "RDMonthsRemain", "RDAppliedDate");
        System.out.println(
                "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.format("%1$-30s%2$-20s%3$-20s%4$-30s%5$-20s%6$-20s\n", rd.getRDID(), rd.getRDAmount(),
                rd.getRDInterestRate(), rd.getRDTenure(), rd.getRDRemainingMonths(), rd.getRDOpenDate());
        System.out.println(
                "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        System.out.println(
                "\n   --   RD Statement   --\n--------------------------------------------------------------------------------------------------------");
        System.out.format("%1$-15s%2$-20s%3$-20s%4$-20s%5$-20s\n", "Transaction ID",
                "Transaction Mode",
                "TransactionType", "Date", "Balance");
        System.out.println(
                "--------------------------------------------------------------------------------------------------------");
        for (Transactions tlist : arrList) {
            System.out.format("%1$-15s%2$-20s%3$-20s%4$-20s%5$-20s\n", tlist.getTransactionID(),
                    tlist.getTransactionMode(),
                    tlist.getTransactionType(), tlist.getTransactionDate(),
                    Math.round(tlist.getAmount()));
        }
        System.out.println(
                "--------------------------------------------------------------------------------------------------------");
    }

    // this function is used to pay monthly RD amount
    private static void payRDAmount(RecurringDeposit rdAcc) {
        if (rdAcc == null) {
            return;
        }
        Account acc = utils.getRefAccount(rdAcc.getAccNo());
        if (rdAcc.getRDstatus()) {
            if (rdAcc.getRDAmount() <= acc.getAccountBalance()) {
                if (rdAcc.getRDRemainingMonths() >= 1) {
                    if (rds.checkDate(rdAcc)) {
                        rds.payRDAmount(acc, rdAcc);
                    } else {
                        System.out.println("\nThis month RD is already paid");
                        System.out.println("if you want to pay for next month.");
                        System.out.print("Enter 1 to continue : ");
                        int x = sc.nextInt();
                        if (x != 1)
                            return;
                        rds.payRDAmount(acc, rdAcc);
                        System.out.println("\nRD Amount paid successfully for next month");
                        if (rdAcc.getRDRemainingMonths() == 0) {
                            System.out.println("\nYou will get full mature amount on "
                                    + rdAcc.getRDOpenDate().plusMonths(rdAcc.getRDTenure()));
                        }
                    }
                }
                if (rdAcc.getRDOpenDate().plusMonths(rdAcc.getRDTenure()).compareTo(LocalDate.now()) <= 0) {
                    double amount = rds.addRDAmount(acc, rdAcc);
                    System.out.println("\nMature Amount     : " + Math.round(amount) + " is added to your Account");
                    System.out.println("Available Balance : " + Math.round(acc.getAccountBalance()));
                }
            } else {
                System.out.println("\n Insufficient Balance \n Available Balance  : " + acc.getAccountBalance());
            }
        } else {
            System.out.println("\n\nThis RD Account is closed");
        }
    }

    // this function is used to withdraw RD amount
    private static void withdrawRD(RecurringDeposit rdAcc) {
        if (rdAcc == null)
            return;
        Account acc = utils.getRefAccount(rdAcc.getAccNo());
        LocalDate eligibleDate = rds.validateRDdate(rdAcc);
        if (rdAcc.getRDstatus()) {
            if (eligibleDate == null) {
                double amount = rds.dowithdrawRD(acc, rdAcc);
                System.out.println("\nMature Amount : " + Math.round(amount) + " is added to your Account");
                System.out.println("Available Balance : " + Math.round(acc.getAccountBalance()));
            } else {
                System.out.println("\nYou are not eligible to withdraw wait for up to this date : " + eligibleDate);
            }
        } else {
            System.out.println("This RD Account is closed");
        }
    }

    // this function is used to create RD account
    private static void createRDAccount(long mobileNo, Account acc) {
        System.out.println("\nProvide details for RD Account :");
        System.out.println("\n---------------------------------------------------------------------\n");
        CIF cif = UtilsUI.getNomineeAadharNo();
        if (cif == null) {
            System.out.println("Create Account for Nominee ");
            return;
        }
        System.out.println(" Nominee Name : " + cif.getUsername());
        System.out.print("Enter 1 to continue or any number to exit : ");
        int x = sc.nextInt();
        if (x != 1)
            return;
        double amount = getAmount(acc);
        if (amount == 0)
            return;
        int mons = getMonths();
        RecurringDeposit rdAcc = rds.createRD(mobileNo, acc.getAccNo(), amount, mons, cif.getAadharNumber());
        displayRDdetails(rdAcc);
    }

    // this function is used display RD details
    public static void displayRDdetails(RecurringDeposit rd) {
        System.out.println("\n    ~  Recurring Deposit Passbook  ~");
        System.out.println(" RD Account No      : " + rd.getRDID());
        System.out.println(" RD Linked Acc No   : " + rd.getAccNo());
        System.out.println(" RD Mounthly AMount : " + rd.getRDAmount());
        System.out.println(" RD Interest Rate   : " + rd.getRDInterestRate());
        System.out.println(" RD Duration        : " + rd.getRDTenure());
        System.out.println(" RD Remaining Months: " + rd.getRDRemainingMonths());
        System.out.println(" RD Acc Opendate    : " + rd.getRDOpenDate());
    }

    

    // this function is used get amount
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
                System.out.println("\nRD amount must be greater than 99 /-");
            }
        } while (!bool);
        return amount;
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

    // this function is used to display RD details
    public static void displayRDDetails() {
        System.out.println("\n RD Interest Rate 5-6% per year");
        System.out.println("  ~ RD Months Duration ~");
        System.out.println(" Min : 6 Months ");
        System.out.println(" Max : 120 Months ");
        System.out.print("Enter Months : ");
    }

    // this function is used to get FD mons from the user
    public static int getMons() {
        int mons = 0;
        do {
            try {
                displayRDDetails();
                mons = Integer.parseInt(sc.next());
                if (mons < 6 || mons > 120) {
                    mons = 0;
                    System.out.println("\nMonths must be choosen from 6 to 120 months");
                }
            } catch (Exception e) {
                System.out.println("\nentered months is not a valid integer");
            }
        } while (mons == 0);
        return mons;
    }
}
