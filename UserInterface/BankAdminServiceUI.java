package UserInterface;

import java.io.Console;
import java.util.ArrayList;
import java.util.Scanner;

import entities.Account;
import entities.CIF;
import entities.FixedDeposit;
import entities.Loan;
import entities.RecurringDeposit;
import entities.Transactions;
import services.BankAdminService;
import services.LoanServices;
import services.utils;

public class BankAdminServiceUI {
        static BankAdminService bas = new BankAdminService();

        static Scanner sc = new Scanner(System.in);
        static Console c = System.console();

        // this function is used for admin login
        public static Boolean login() {
                char[] pass = c.readPassword("Enter your password      : ");
                String password = new String(pass);
                Boolean bool = bas.validateLogin(password);
                return bool;
        }

        // this function is used to diaplay CIF details
        public static void displayCIFDetails() {
                ArrayList<CIF> cifList = new ArrayList<>();
                cifList = bas.displayCIFDetails();

                System.out.println(
                                "\n   --    CIF List   --\n--------------------------------------------------------------------------------------------------------------------");
                System.out.format("%1$-30s%2$-20s%3$-20s%4$-20s%5$-20s%6$-20s\n", "CIFNo", "UserName", "AadharNumber",
                                "MobileNumber",
                                "Pincode", "Age");
                System.out.println(
                                "-------------------------------------------------------------------------------------------------------------------");
                for (CIF cif : cifList) {
                        System.out.format("%1$-30s%2$-20s%3$-20s%4$-20s%5$-20s%6$-20s\n",
                                        cif.getCIFno(), cif.getUsername(), cif.getAadharNumber(), cif.getMobileNo(),
                                        cif.getPincode(),
                                        cif.getAge());
                }
                System.out.println(
                                "--------------------------------------------------------------------------------------------------------------------");

        }

        // this function is used to display account details
        public static void displayAccountDetails() {
                ArrayList<Account> accList = new ArrayList<>();
                accList = bas.displayAccDetails();
                System.out.println(
                                "\n   --    Accounts List   --\n-------------------------------------------------------------------------------------------------------------------------------------------");
                System.out.format("%1$-30s%2$-20s%3$-20s%4$-30s%5$-20s%6$-20s\n", "AccountNumber", "CIF no",
                                "AccountType",
                                "BalanceType", "AccountOpenDate", "AccountBalance");
                System.out.println(
                                "-------------------------------------------------------------------------------------------------------------------------------------------");
                for (Account acc : accList) {
                        System.out.println(acc.toString("%1$-30s%2$-20s%3$-20s%4$-30s%5$-20s%6$-20s\n"));
                }
                System.out.println(
                                "-------------------------------------------------------------------------------------------------------------------------------------------");
        }

        static LoanServices ls = new LoanServices();

        // this function is used to display loan details
        public static void displayLoanDetails() {
                ArrayList<Loan> loanList = new ArrayList<>();
                loanList = bas.displayLoanDetails();
                System.out.println(
                                "\n  --  All Loan Details  --\n-------------------------------------------------------------------------------------------------------------------------------------------");
                System.out.format("%1$-30s%2$-20s%3$-20s%4$-30s%5$-20s%6$-20s\n", "LoanAccountNumber", "LoanAmount",
                                "LoanInterestRate", "LoanDurationMonths", "LoanMonthsRemain", "LoanAppliedDate");
                System.out.println(
                                "-------------------------------------------------------------------------------------------------------------------------------------------");
                for (Loan loan : loanList) {
                        System.out.format("%1$-30s%2$-20s%3$-20s%4$-30s%5$-20s%6$-20s\n", loan.getLoanAccNo(),
                                        loan.getLoanAmount(),
                                        loan.getInterestRate(), loan.getnoofMonths(), loan.getMonthsRemain(),
                                        loan.getLoanDate());
                }
                System.out.println(
                                "-------------------------------------------------------------------------------------------------------------------------------------------");
        }

        // this function is used to display transaction details
        public static void displayTransactions() {
                ArrayList<Transactions> allTransactions = new ArrayList<>();
                allTransactions = bas.getTransactions();
                System.out.println(
                                "\n\n   --    All Bank Transactions   --\n--------------------------------------------------------------------------------------------------------");
                System.out.format("%1$-30s%2$-20s%3$-30s%4$-20s%5$-20s\n", "AccountNumber", "Transaction Mode",
                                "TransactionType", "Date", "Balance");
                System.out.println(
                                "--------------------------------------------------------------------------------------------------------");
                for (Transactions tlist : allTransactions) {
                        System.out.format("%1$-30s%2$-20s%3$-30s%4$-20s%5$-20s\n", tlist.getAccountNumber(),
                                        tlist.getTransactionMode(),
                                        tlist.getTransactionType(), tlist.getTransactionDate(),
                                        Math.round(tlist.getAmount()));
                }
                System.out.println(
                                "--------------------------------------------------------------------------------------------------------");

        }

        // this function is used to display selected CIF details
        public static void displaySelectedCIF() {
                long mobileNo = UtilsUI.getMobileNo();
                CIF cif = bas.getUserCIFDetails(mobileNo);
                if (cif != null) {
                        System.out.println(
                                        "\n   --    CIF Details   --\n--------------------------------------------------------------------------------------------------------------------");
                        System.out.format("%1$-30s%2$-20s%3$-20s%4$-20s%5$-20s%6$-20s\n", "CIFNo", "UserName",
                                        "AadharNumber", "MobileNumber",
                                        "Pincode", "Age");
                        System.out.println(
                                        "-------------------------------------------------------------------------------------------------------------------");
                        System.out.format("%1$-30s%2$-20s%3$-20s%4$-20s%5$-20s%6$-20s\n",
                                        cif.getCIFno(), cif.getUsername(), cif.getAadharNumber(), cif.getMobileNo(),
                                        cif.getPincode(),
                                        cif.getAge());
                        System.out.println(
                                        "--------------------------------------------------------------------------------------------------------------------");

                } else {
                        System.out.println("CIF is not exist with aadhar number...");
                }

        }

        // this function is used to display selected account details
        public static void displaySelectedAccount() {
                long accNo = UtilsUI.getAccNumber();
                Account acc = bas.getUserAccountDetails(accNo);
                if (acc != null) {
                        System.out.println(
                                        "\n   --    Accounts List   --\n-------------------------------------------------------------------------------------------------------------------------------------------");
                        System.out.format("%1$-30s%2$-20s%3$-20s%4$-30s%5$-20s%6$-20s\n", "AccountNumber", "CIF no",
                                        "AccountType",
                                        "BalanceType", "AccountOpenDate", "AccountBalance");
                        System.out.println(
                                        "-------------------------------------------------------------------------------------------------------------------------------------------");
                        System.out.println(acc.toString("%1$-30s%2$-20s%3$-20s%4$-30s%5$-20s%6$-20s\n"));
                        System.out.println(
                                        "-------------------------------------------------------------------------------------------------------------------------------------------");

                } else {
                        System.out.println("\nAccount is not Existed ...");
                }
        }

        // this function is used display selected account transactions
        public static void displaySelectedAccountTransactions() {
                long accNo = UtilsUI.getAccNumber();
                Account acc = utils.searchAccount(accNo);
                ATMTransactionsUI.doMiniStatementsUI(acc);
        }

        // this function is used display selected loan account details
        public static void displaySelectedLoanAccountsDetails() {
                long accNo = UtilsUI.getAccNumber();
                //LoanServicesUI.displayLoanDetailsUI(accNo);
                Loan loan = bas.getLoanAccDetails(accNo);
                if (loan != null) {
                        System.out.println(
                                        "\n  --   Loan Details  --\n------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                        System.out.format("%1$-30s%2$-20s%3$-20s%4$-30s%5$-20s%6$-20s%7$-20s\n", "LoanAccountNumber",
                                        "LoanAccountType",
                                        "LoanAmount", "LoanInterestRate", "LoanDurationMonths", "LoanMonthsRemain",
                                        "LoanAppliedDate");
                        System.out.println(
                                        "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                        System.out.format("%1$-30s%2$-20s%3$-20s%4$-30s%5$-20s%6$-20s%7$-20s\n", loan.getLoanAccNo(),
                                        loan.getLoanType(), loan.getLoanAmount(), loan.getInterestRate(),
                                        loan.getnoofMonths(),
                                        loan.getMonthsRemain(), loan.getLoanDate());
                        System.out.println(
                                        "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

                } else {
                        System.out.println("\nInvalid Loan Account Number");
                }
        }

        // this function is used display FD account details
        public static void displayFDDetails() {
                ArrayList<FixedDeposit> acc = bas.getFDDetails();
                        System.out.println(
                                        "\n   --    Accounts List   --\n-------------------------------------------------------------------------------------------------------------------------------------------");
                        System.out.format("%1$-30s%2$-20s%3$-20s%4$-30s%5$-20s\n", "FDAccountNumber", "FD Amount",
                                        "FD Interest Rate",
                                        "FD Depositdate", "FD duration(mons)");
                        System.out.println(
                                        "-------------------------------------------------------------------------------------------------------------------------------------------");
                        for (FixedDeposit fd : acc) {
                                System.out.format("%1$-30s%2$-20s%3$-20s%4$-30s%5$-20s\n", fd.getFDAccNo(),
                                                fd.getFDAmount(), fd.getFDinterestRate(), fd.getFDDepositDate(),
                                                fd.getFDMonths());
                        }
                        System.out.println(
                                        "-------------------------------------------------------------------------------------------------------------------------------------------");

        }

        // this function is used display selected FD account details
        public static void displaySelectedFDAccountDetails() {
                long accNo = UtilsUI.getAccNumber();
                FixedDeposit fd = bas.getFDAccDetails(accNo);
                if (fd != null) {
                        System.out.println(
                                        "\n   --    Accounts List   --\n-------------------------------------------------------------------------------------------------------------------------------------------");
                        System.out.format("%1$-30s%2$-20s%3$-20s%4$-30s%5$-20s\n", "FDAccountNumber", "FD Amount",
                                        "FD Interest Rate",
                                        "FD Depositdate", "FD duration(mons)");
                        System.out.println(
                                        "-------------------------------------------------------------------------------------------------------------------------------------------");
                        System.out.format("%1$-30s%2$-20s%3$-20s%4$-30s%5$-20s\n", fd.getFDAccNo(),
                                        fd.getFDAmount(), fd.getFDinterestRate(), fd.getFDDepositDate(),
                                        fd.getFDMonths());
                        System.out.println(
                                        "-------------------------------------------------------------------------------------------------------------------------------------------");

                } else {
                        System.out.println("FD Account is not Existed with this number : "+accNo);
                }
        }

        // this function is used display selected RD account details
        public static void displaySelectedRDAccountDetails() {
                long accNo = UtilsUI.getAccNumber();
                RecurringDeposit rd = bas.getRDAccDetails(accNo);
                if (rd != null) {
                        System.out.println(
                                        "\n  --   RD Details  --\n------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                        System.out.format("%1$-30s%2$-20s%3$-20s%4$-30s%5$-20s%6$-20s\n", "RDAccountNumber",
                                        "RDAmount", "RDInterestRate", "RDDurationMonths", "RDMonthsRemain",
                                        "RDAppliedDate");
                        System.out.println(
                                        "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                        System.out.format("%1$-30s%2$-20s%3$-20s%4$-30s%5$-20s%6$-20s\n", rd.getRDID(),
                                        rd.getRDAmount(),
                                        rd.getRDInterestRate(), rd.getRDTenure(), rd.getRDRemainingMonths(),
                                        rd.getRDOpenDate());
                        System.out.println(
                                        "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

                }else{
                        System.out.println("FD Account is not Existed with this number : "+accNo);
                }
        }

        // this function is used display RD account details
        public static void displayRDDetails() {
                ArrayList<RecurringDeposit> accList = bas.getRDDetails();
                if (accList != null) {
                        System.out.println(
                                        "\n  --   RD Details  --\n------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                        System.out.format("%1$-30s%2$-20s%3$-20s%4$-30s%5$-20s%6$-20s\n", "RDAccountNumber",
                                        "RDAmount", "RDInterestRate", "RDDurationMonths", "RDMonthsRemain",
                                        "RDAppliedDate");
                        System.out.println(
                                        "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                        for (RecurringDeposit rd : accList) {
                                System.out.format("%1$-30s%2$-20s%3$-20s%4$-30s%5$-20s%6$-20s\n", rd.getRDID(),
                                                rd.getRDAmount(),
                                                rd.getRDInterestRate(), rd.getRDTenure(), rd.getRDRemainingMonths(),
                                                rd.getRDOpenDate());
                        }
                        System.out.println(
                                        "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

                }
        }

}
