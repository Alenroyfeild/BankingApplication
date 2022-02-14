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
            System.out.println(" 1.Create RD");
            System.out.println(" 2.Contribute RD monthly Amount");
            System.out.println(" 3.WithDraw RD Account");
            System.out.println(" 4.RD Passbook");
            System.out.println(" 5.RD Statements");
            System.out.println(" 6.RD Summary");
            System.out.println(" 7.RD Interest Details");
            System.out.println(" 8.Back to Deposit Selection");
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
        } while (choice < 1 || choice > 8);
        return 0;
    }

    public static void RDNotification(long mobileNo) {
        ArrayList<RecurringDeposit> RDList = utils.getUserRDAcc(mobileNo);
        if (RDList != null) {
            System.out.println(
                    "-------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("  --  Notifications  -- \n");
            for (RecurringDeposit rd : RDList) {
                LocalDate date = utils.getRDDueDate(rd);
                if (rd.getRDRemainingMonths() >= 1) {
                    System.out.println("RD Acc no :(" + rd.getRDID() + ")    RD Amount :(" + rd.getRDAmount()
                            + ")     Remaining Months :(" + rd.getRDRemainingMonths() + ")  next Due date : " + date
                            + "\n");
                }
                if (rd.getRDRemainingMonths() == 0 && rd.getRDstatus()) {
                    System.out.println("RD Acc no :(" + rd.getRDID() + ") is matured .You can withdraw it.");
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
        RecurringDeposit rdAcc = null;
        do {
            choice = RDServicesPage();
            if (choice == 1) {
                createRDAccount(mobileNo);
            } else if (choice == 2) {
                rdAcc = UtilsUI.displayRDAccNumbers(mobileNo);
                payRDAmount(rdAcc, mobileNo);
            } else if (choice == 3) {
                rdAcc = UtilsUI.displayRDAccNumbers(mobileNo);
                withdrawRD(rdAcc, mobileNo);
            } else if (choice == 4) {
                System.out.println("\033[H\033[2J");
                rdAcc = UtilsUI.displayRDAccNumbers(mobileNo);
                displayRDdetails(rdAcc);
            } else if (choice == 5) {
                System.out.println("\033[H\033[2J");
                rdAcc = UtilsUI.displayRDAccNumbers(mobileNo);
                displayRDStatements(rdAcc);
            } else if (choice == 6) {
                displayRDSummary(mobileNo);
            } else if (choice == 7) {
                displayRDInterestDetails();
            } else {
                System.out.println("\nBack to HomePage");
            }
        } while (choice < 1 || choice > 8 || choice != 8);
    }

    private static void displayRDInterestDetails() {
        System.out.println("\nRecurring Deposit interest is based on duration ");
        System.out.println("    Months         interest rate ");
        System.out.println("   1  to  12           6.10%   ");
        System.out.println("   13  to  24          6.20%   ");
        System.out.println("   25 to   36          6.30%   ");
        System.out.println("   37 to   48          6.40%   ");
        System.out.println("   above 48            6.50%   ");
        System.out.println("\n For age above 60 will get InterestRate + 0.5% ");
    }

    public static void displayRDSummary(long mobileNo) {
        ArrayList<RecurringDeposit> RDList = null;
        RDList = utils.getUserAllRDAcc(mobileNo);
        if (RDList != null) {
            System.out.println(
                    "\n  --   RD Accounts  --\n------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.format("%1$-20s%2$-10s%3$-20s%4$-20s%5$-20s%6$-20s%7$-20s%8$-15s%9$-15s%10$-15s\n",
                    "RDAccountNumber",
                    "RDAmount", "RDInterestRate", "RDDurationMonths", "RDMonthsRemain",
                    "RDAppliedDate", "RD Mature Date", "RDMatureAmount", "RDInterest", "RDDueDate");
            System.out.println(
                    "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            for (RecurringDeposit rd : RDList) {
                LocalDate date = rd.getRDOpenDate().plusMonths(rd.getRDTenure());
                double matureAmount = (Math.round(rds.getMatureAmount(rd)*100))/100;
                double interest = matureAmount - (rd.getRDAmount() * rd.getRDTenure());
                String dueDate;
                if (rd.getRDRemainingMonths() == 0)
                    dueDate = "-";
                else
                    dueDate = utils.getRDDueDate(rd) + "";
                System.out.format("%1$-20s%2$-10s%3$-20s%4$-20s%5$-20s%6$-20s%7$-20s%8$-15s%9$-15s%10$-15s\n",
                        rd.getRDID(),
                        rd.getRDAmount(),
                        rd.getRDInterestRate(), rd.getRDTenure(), rd.getRDRemainingMonths(),
                        rd.getRDOpenDate(), date, matureAmount, interest, dueDate);
            }
            System.out.println(
                    "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        }
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
                "\n  --   RD Statement  --\n------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.format("%1$-20s%2$-10s%3$-20s%4$-20s%5$-20s%6$-20s%7$-20s%8$-15s%9$-15s%10$-15s\n",
                "RDAccountNumber",
                "RDAmount", "RDInterestRate", "RDDurationMonths", "RDMonthsRemain",
                "RDAppliedDate", "RD Mature Date", "RDMatureAmount", "RDInterest", "RDDueDate");
        System.out.println(
                "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        LocalDate date = rd.getRDOpenDate().plusMonths(rd.getRDTenure());
        double matureAmount = (Math.round(rds.getMatureAmount(rd)*100))*0.01;
        double interest = matureAmount - (rd.getRDAmount() * rd.getRDTenure());
        String dueDate;
        if (rd.getRDRemainingMonths() == 0)
            dueDate = "-";
        else
            dueDate = utils.getRDDueDate(rd) + "";
        System.out.format("%1$-20s%2$-10s%3$-20s%4$-20s%5$-20s%6$-20s%7$-20s%8$-15s%9$-15s%10$-15s\n",
                rd.getRDID(),
                rd.getRDAmount(),
                rd.getRDInterestRate(), rd.getRDTenure(), rd.getRDRemainingMonths(),
                rd.getRDOpenDate(), date, matureAmount, interest, dueDate);
        System.out.println(
                "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println(
                "\n   --   RD Transactions   --\n--------------------------------------------------------------------------------------------------------");
        System.out.format("%1$-15s%2$-20s%3$-25s%4$-20s%5$-20s\n", "Transaction ID",
                "Transaction Mode",
                "TransactionDescription", "Date", "Balance");
        System.out.println(
                "--------------------------------------------------------------------------------------------------------");
        for (Transactions tlist : arrList) {
            System.out.format("%1$-15s%2$-20s%3$-25s%4$-20s%5$-20s\n", tlist.getTransactionID(),
                    tlist.getTransactionMode(),
                    tlist.getTransactionType(), tlist.getTransactionDate(),
                    Math.round(tlist.getAmount()));
        }
        System.out.println(
                "--------------------------------------------------------------------------------------------------------");
    }

    // this function is used to pay monthly RD amount
    private static void payRDAmount(RecurringDeposit rdAcc, long mobileNo) {
        if (rdAcc == null) {
            return;
        }
        if (rdAcc.getRDstatus()
                && rdAcc.getRDOpenDate().plusMonths(rdAcc.getRDTenure()).compareTo(LocalDate.now()) <= 0) {
            System.out.println("\nRD Acc no :(" + rdAcc.getRDID() + ") is matured .You can withdraw it.");
            return;
        } else if (rdAcc.getRDRemainingMonths() == 0 && rdAcc.getRDstatus()) {
            System.out.println("\nYou have successfully paid all RD duration months");
            System.out.println("Wait up to Mature Date : " + rdAcc.getRDOpenDate().plusMonths(rdAcc.getRDTenure()));
            return;
        }
        System.out.println();
        if (rdAcc.getRDstatus()) {
            if (rdAcc.getRDRemainingMonths() >= 1) {
                System.out.println("\nRD Amount : " + rdAcc.getRDAmount());
                if (rds.checkDate(rdAcc) || rdAcc.getRDstatus()) {
                    System.out.print("\nEnter 1 to continue or any integer to exist : ");
                    int x = sc.nextInt();
                    if (x != 1)
                        return;
                    System.out.println("\nSelect the Account to withdraw for RD Amount : ");
                    Account acc = UtilsUI.displayAccountNumber(mobileNo);
                    rds.payRDAmount(acc, rdAcc);
                    System.out.println("\nRD Amount paid successfully ");
                    if (rdAcc.getRDRemainingMonths() == 0) {
                        System.out.println(
                                "\nYou have successfuly paid all RD's Amount.\nYou will get full mature amount on "
                                        + rdAcc.getRDOpenDate().plusMonths(rdAcc.getRDTenure()));
                    } else {
                        System.out.println("Remaining months : " + rdAcc.getRDRemainingMonths());
                        System.out.println("Next RD Due Date : " + utils.getRDDueDate(rdAcc));
                    }
                } else {
                    System.out.println("\nThis month RD is already paid");
                    System.out.println("if you want to pay for next month.");
                    System.out.print("Enter 1 to continue or any integer to exist : ");
                    int x = sc.nextInt();
                    if (x != 1)
                        return;
                    System.out.println("\nSelect the Account to withdraw for RD Amount : ");
                    Account acc = UtilsUI.displayAccountNumber(mobileNo);
                    if (rdAcc.getRDAmount() <= acc.getAccountBalance()) {
                        rds.payRDAmount(acc, rdAcc);
                        System.out.println("\nRD Amount paid successfully for next month");
                        if (rdAcc.getRDRemainingMonths() == 0) {
                            System.out.println("\nYou will get full mature amount on "
                                    + rdAcc.getRDOpenDate().plusMonths(rdAcc.getRDTenure()));
                        }
                    } else {
                        System.out
                                .println("\n Insufficient Balance \n Available Balance  : " + acc.getAccountBalance());
                    }
                }
            }
        } else {
            System.out.println("\n\n No RD Accounts is available");
        }
    }

    // this function is used to withdraw RD amount
    private static void withdrawRD(RecurringDeposit rdAcc, long mobileNo) {
        if (rdAcc == null)
            return;
        if (rdAcc.getRDstatus())
            if (rdAcc.getRDOpenDate().plusMonths(rdAcc.getRDTenure()).compareTo(LocalDate.now()) <= 0) {
                System.out.println("Select the Source Account to deposit RD Amount :");
                Account acc = UtilsUI.displayAccountNumber(mobileNo);
                double amount = Math.round(rds.addRDAmount(acc, rdAcc)*100)*0.01;
                double interest = amount - (rdAcc.getRDAmount() * (rdAcc.getRDTenure()));
                System.out.println("\nRD Total Amount : " + rdAcc.getRDAmount() * rdAcc.getRDTenure()
                        + "  RD Interest : " + interest);
                System.out.println("\nMature Amount     : " + amount + " is added to your Account");
                System.out.println("Successfully RD is closed");
                System.out.println("Available Balance : " + acc.getAccountBalance());
                return;
            }
        LocalDate eligibleDate = rds.validateRDdate(rdAcc);
        if (rdAcc.getRDstatus()) {
            if (eligibleDate == null) {
                System.out.println("Select the Source Account to deposit RD Amount :");
                Account acc = UtilsUI.displayAccountNumber(mobileNo);
                double amount = (Math.round(rds.addRDAmount(acc, rdAcc)*100))*0.01;
                double interest = amount - (rdAcc.getRDAmount() * (rdAcc.getRDTenure() - rdAcc.getRDRemainingMonths()));
                System.out.println("\nRD Total Amount : " + rdAcc.getRDAmount() * rdAcc.getRDTenure()
                        + "  RD Interest : " + interest);
                System.out.println("\nMature Amount : " + Math.round(amount) + " is added to your Account");
                System.out.println("Successfully RD is closed");
                System.out.println("Available Balance : " +acc.getAccountBalance());
            } else {
                System.out.println("\nYou are not eligible to withdraw wait for up to this date : " + eligibleDate);
            }
        } else {
            System.out.println("No RD Acounts is available");
        }
    }

    // this function is used to create RD account
    private static void createRDAccount(long mobileNo) {
        System.out.println("\nProvide details for RD Account :");
        System.out.println("\n---------------------------------------------------------------------\n");
        CIF cif = UtilsUI.getNomineeAadharNo();
        if (cif == null) {
            System.out.println("Create Account for Nominee ");
            return;
        }
        System.out.println(" Nominee Name : " + cif.getCustomerFullname());
        System.out.print("\nEnter 1 to continue or any number to exit : ");
        int x = sc.nextInt();
        int mons = getMonths();
        System.out.println("\nSelect Account to withdraw for RD Amount & for Auto payments : ");
        Account acc = UtilsUI.displayAccountNumber(mobileNo);
        if (x != 1)
            return;
        double amount = getAmount(acc);
        if (amount == 0)
            return;
        RecurringDeposit rdAcc = rds.createRD(mobileNo, acc.getAccNo(), amount, mons, cif.getAadharNumber());
        displayRDdetails(rdAcc);
    }

    // this function is used display RD details
    public static void displayRDdetails(RecurringDeposit rd) {
        if (rd == null)
            return;
        System.out.println("\n    ~  Recurring Deposit Passbook  ~");
        System.out.println(" RD Acc Holder Name   : " + utils.getUsername(rd.getMobileNo()));
        System.out.println(" RD Account No        : " + rd.getRDID());
        System.out.println(" RD Nominee Aadhar No : " + rd.getNomineeAadhar());
        System.out.println(" Nominee Aadhar Name  : " + utils.getUsername(rd.getNomineeAadhar()));
        System.out.println(" RD Mounthly AMount   : " + rd.getRDAmount());
        System.out.println(" RD Interest Rate(p.a): " + rd.getRDInterestRate());
        System.out.println(" RD Duration          : " + rd.getRDTenure());
        System.out.println(" RD Remaining Months  : " + rd.getRDRemainingMonths());
        System.out.println(" RD Acc Opendate      : " + rd.getRDOpenDate());
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
