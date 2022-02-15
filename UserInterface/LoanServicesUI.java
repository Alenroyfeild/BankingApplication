package UserInterface;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import entities.Account;
import entities.Bank;
import entities.Loan;
import entities.Transactions;
import services.ATMTransaction;
import services.LoanServices;
import services.utils;

public class LoanServicesUI {
    static Scanner sc = new Scanner(System.in);
    static Bank ba = Bank.getInstance();
    static LoanServices ls = new LoanServices();
    static ATMTransaction atm = new ATMTransaction();
    static DecimalFormat df = new DecimalFormat("0.00");
    // this function is used to display loan page
    public static int loanPage() {

        int choice = 0;
        do {
            System.out.println(
                    "\n\n\n--------------------------------------------------------------------------------------------");
            System.out.println("\n        ---  Loan Services Page  ---");
            System.out.println("\nChoose the Options : ");
            System.out.println(" 1.Apply loan ");
            System.out.println(" 2.Contribute loan EMI Amount");
            System.out.println(" 3.Loans Summary");
            System.out.println(" 4.Loan Passbook");
            System.out.println(" 5.Loan Statements");
            System.out.println(" 6.Loan Interest Details");
            System.out.println(" 7.Back to Home");
            try {
                System.out.print("Enter choice : ");
                choice = Integer.parseInt(sc.next());
                System.out.println(
                        "\n--------------------------------------------------------------------------------------------");
                return choice;
            } catch (NumberFormatException e) {
                System.out.println("You have entered wrong choice.\nPlease again Enter : ");
                choice = 0;
            }
        } while (choice < 1 || choice > 7);
        return 0;
    }

    // this function is used to call particular method based on choice
    public static void LoanUI(long mobileNo) {
        int choice;
        System.out.println("\033[H\033[2J");
        loanNotifications(mobileNo);
        Loan loan = null;
        do {
            choice = loanPage();
            if (choice == 1) {
                createLoanAccount(mobileNo);
            } else if (choice == 2) {
                loan = UtilsUI.displayLoanAccountNumber(mobileNo);
                payLoanAmount(loan, mobileNo);
            } else if (choice == 4) {
                System.out.println("\033[H\033[2J");
                loan = UtilsUI.displayLoanAccountNumber(mobileNo);
                displayLoanDetails(loan);
            } else if (choice == 3) {
                System.out.println("\033[H\033[2J");
                displayLoanSummary(mobileNo);
            } else if (choice == 5) {
                System.out.println("\033[H\033[2J");
                loan = UtilsUI.displayLoanAccountNumber(mobileNo);
                displayLoanStatments(loan);
            } else if (choice == 6) {
                displayLoanInterestDetails();
            } else {
                System.out.println("\nBack to home page");
            }
        } while (choice < 1 || choice > 7 || choice != 7);
    }

    //this function is used to display loan interest details
    private static void displayLoanInterestDetails() {
        System.out.println("\nLoan Interest Details : \n");
        System.out.println("---------------------------------------");
        System.err.println("  Loan Type        |    InterestRate(p.a)");
        System.out.println("---------------------------------------");
        System.out.println("  Home Loan        |        8.5");
        System.out.println("  Personal Loan    |       10.5");
    }

    //this function is used to display loan interest details
    public static void loanNotifications(long mobileNo) {
        ArrayList<Loan> loanList = utils.getLoanAccNumbers(mobileNo);
        if (loanList != null) {
            System.out.println(
                    "-------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("  --  Notifications  -- \n");
            for (Loan loan : loanList) {
                LocalDate date = utils.getLoanDueDate(loan);
                if (loan.getMonthsRemain() >= 1)
                    System.out.println(
                            "loan Acc no :(" + loan.getLoanAccNo() + ")      LoanType :(" + loan.getLoanType()
                                    + ")     Remaining EMI :(" + loan.getMonthsRemain() + ")    next Due date : "
                                    + date + "\n");
            }
            System.out.println(
                    "-------------------------------------------------------------------------------------------------------------------------------------------");
        }
    }

    // this function is used to display loan statement
    private static void displayLoanStatments(Loan loan) {
        if (loan == null)
            return;
        ArrayList<Transactions> arrList = ls.getLoanStatementList(loan.getAccNo(), loan.getLoanAccNo());
        String accNo = (loan.getAccNo() + "");
        System.out.println("\n  Account No : 2*****" + accNo.substring(6));
        System.out.println(
                "\n  --   Loan Details  --\n------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.format("%1$-20s%2$-20s%3$-20s%4$-20s%5$-20s%6$-20s%7$-20s%8$-15s\n", "LoanAccountNumber",
                "LoanAccountType",
                "LoanAmount", "LoanInterestRate", "LoanDurationMonths", "LoanMonthsRemain", "LoandueDate",
                "LoanEMIAmount");
        System.out.println(
                "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        double EMI = ls.calculateEMI(loan.getnoofMonths(), loan.getInterestRate(), loan.getLoanAmount());
        System.out.format("%1$-20s%2$-20s%3$-20s%4$-20s%5$-20s%6$-20s%7$-20s%8$-15s\n", loan.getLoanAccNo(),
                loan.getLoanType(), df.format(loan.getLoanAmount()), loan.getInterestRate(), loan.getnoofMonths(),
                loan.getMonthsRemain(), utils.getLoanDueDate(loan), df.format(EMI));
        System.out.println(
                "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        System.out.println(
                "\n   --   Loan Statement   --\n--------------------------------------------------------------------------------------------------------");
        System.out.format("%1$-15s%2$-20s%3$-25s%4$-20s%5$-20s\n", "Transaction ID",
                "Transaction Mode",
                "TransactionDescription", "Date", "Balance");
        System.out.println(
                "--------------------------------------------------------------------------------------------------------");
        if (arrList != null)
            for (Transactions tlist : arrList) {
                System.out.format("%1$-15s%2$-20s%3$-25s%4$-20s%5$-20s\n", tlist.getTransactionID(),
                        tlist.getTransactionMode(),
                        tlist.getTransactionType(), tlist.getTransactionDate(),
                        df.format(tlist.getAmount()));
            }
        System.out.println(
                "--------------------------------------------------------------------------------------------------------");
    }

    // this function is used to display loan details
    public static void displayLoanDetails(Loan loan) {
        if (loan == null)
            return;
        displayLoanPassbookDetailsUI(loan);
    }

    // this function is used to display loan summary details
    public static void displayLoanSummary(long mobileNo) {
        ArrayList<Loan> arr = utils.getLoanAccNumbers(mobileNo);
        if (arr == null)
            return;
        System.out.println(
                "\n  --   Loan Accounts  --\n------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.format("%1$-20s%2$-20s%3$-20s%4$-20s%5$-20s%6$-20s%7$-20s%8$-15s\n", "LoanAccountNumber",
                "LoanAccountType",
                "LoanAmount", "LoanInterestRate", "LoanDurationMonths", "LoanMonthsRemain", "LoandueDate",
                "LoanEMIAmount");
        System.out.println(
                "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        for (Loan loan : arr) {
            String dueDate = utils.getLoanDueDate(loan) + "";
            double EMI = ls.calculateEMI(loan.getnoofMonths(), loan.getInterestRate(), loan.getLoanAmount());
            String amount = df.format(EMI);
            if (loan.getMonthsRemain() == 0) {
                amount = "-";
                dueDate = "-";
            }
            System.out.format("%1$-20s%2$-20s%3$-20s%4$-20s%5$-20s%6$-20s%7$-20s%8$-15s\n", loan.getLoanAccNo(),
                    loan.getLoanType(), df.format(loan.getLoanAmount()), loan.getInterestRate(), loan.getnoofMonths(),
                    loan.getMonthsRemain(), dueDate, amount);
        }
        System.out.println(
                "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    // this function is used to choose payment type
    private static int payLoanAmountPage() {
        int choice = 0;
        do {
            System.out.println("\nchoose type of paying : ");
            System.out.println("1.EMI");
            System.out.println("2.Total pay");
            System.out.println("3.Back to Loan Services");
            System.out.print("enter choice : ");
            try {
                choice = Integer.parseInt(sc.next());
            } catch (NumberFormatException e) {
                System.out.println("entered choice is not a valid integer number\nplease enter choice : ");
                choice = 0;
            }
        } while (choice == 0);
        return choice;
    }

    // this function is used to pay loan amount
    private static void payLoanAmount(Loan loan, long mobileNo) {
        if (loan == null)
            return;
        int choice;
        do {
            choice = payLoanAmountPage();
            if (loan != null && loan.getAccountStatus()) {
                if (choice == 1) {
                    double amount = ls.getEMIAmount(loan);
                    if (amount == -1 || amount == -2) {
                        System.out.println("\nYou have already cleared loan Amount");
                        return;
                    }
                    if (amount == 0) {
                        System.out.println("\nInvalid Loan AccountNumber");
                        return;
                    }
                    System.out.println("\nEMI amount to pay : " + amount);
                    System.out.print("Enter 1 to continue or any number to exit : ");
                    int x = sc.nextInt();
                    if (x == 1) {
                        System.out.println("\nSelect the Account to withdraw for Loan EMI");
                        Account acc = UtilsUI.displayAccountNumber(mobileNo);
                        double balance = acc.getAccountBalance();
                        if (amount <= balance) {
                            int monsRemain = ls.payEMI(acc, loan, amount);
                            if (monsRemain >= 0) {
                                System.out.println("\nTransaction Successful");
                                System.out.println("Remaining EMIs : " + monsRemain);
                                if (monsRemain != 0)
                                    System.out.println("Next due date  : " + utils.getLoanDueDate(loan));
                                if (monsRemain == 0) {
                                    System.out
                                            .println("\nSuccessfully You pay all Loan EMI's and loan Account is closed");
                                    loan.setAccountStatus(false);
                                }
                            } else if (monsRemain == -1) {
                                System.out.println("\nYou have already paid this month EMI");
                            } else if (monsRemain == -2) {
                                System.out.println("\nToday only you have created Loan Account");
                            }
                        } else {
                            System.out
                                    .println("\nInsufficient Balance\nAvailable Balance : " + acc.getAccountBalance());
                        }
                    } else {
                        System.out.println("\nBack to Loan Services Page");
                        choice = 3;
                    }

                } else if (choice == 2) {
                    double arr[] = new double[2];
                    arr = ls.getTotalLoanAmount(mobileNo, loan);
                    double amount = arr[0];
                    double interestAmount = arr[1];
                    if (amount == -1 || amount == -2) {
                        System.out.println("\nYou have already cleared loan Amount");
                        return;
                    }
                    if (amount == 0) {
                        System.out.println("\nInvalid Loan AccountNumber");
                        return;
                    }
                    System.out.println("\nTotal Remaining amount to pay : " + amount);
                    System.out.print("Enter 1 to continue or any Integer to exit : ");
                    int x = sc.nextInt();
                    if (x == 1) {
                        System.out.println("\nSelect the Account to withdraw for loan");
                        Account acc = UtilsUI.displayAccountNumber(mobileNo);
                        double balance = acc.getAccountBalance();
                        if (amount <= balance) {
                            ls.payTotalLoanAmount(acc, loan, amount);
                            System.out.println("\nTransaction Succesfull");
                            System.out.println("You save upto : " + interestAmount);
                            System.out.println("Successfully Loan Account is closed");
                        } else {
                            System.out.println("Insufficient Balance\nAvailable Balance : "+acc.getAccountBalance());
                            System.out.println("\nBack to Loan Services page");
                        }
                    } else {
                        System.out.println("\nBack to Loan Services Page");
                        choice = 3;
                    }

                } else {
                    System.out.println("\nInsufficient Balance");
                }
            }
        } while (choice < 1 || choice > 3);
    }

    // this function is used to create loan account
    private static void createLoanAccount(long mobileNo) {
        String loanAccType = loanType();
        if (loanAccType == null)
            return;
        System.out.println("\nSelect Account to Depoist Loan Amount & for Auto payments : ");
        Account acc = UtilsUI.displayAccountNumber(mobileNo);
        double loanAmount = getLoanAmount(acc);
        int noofMonths = getLoanMonths(acc);
        Loan acc1 = ls.createLoanAccount(mobileNo, acc.getAccNo(), loanAccType, loanAmount, noofMonths);
        if (acc1 != null) {
            System.out.println("\nLoan Account created Successfully");
            displayLoanPassbookDetailsUI(acc1);
        } else {
            System.out.println("\nLoan Account creation failure");
        }
    }

    // this function is used to display loan passbook details
    public static void displayLoanPassbookDetailsUI(Loan acc) {
        if (acc != null) {
            System.out.println("\n ---  Loan Account Passbook  ---");
            System.out.println("Loan Acc Holder Name       : "+utils.getUsername(acc.getMobileNo()));
            System.out.println("Loan Account Number        : " + acc.getLoanAccNo());
            System.out.println("Loan Account Type          : " + acc.getLoanType());
            System.out.println("Loan Amount                : " + acc.getLoanAmount());
            System.out.println("Loan InterestRate          : " + acc.getInterestRate());
            System.out.println("Loan Duration Months       : " + acc.getnoofMonths());
            System.out.println("Loan Months Remain         : " + acc.getMonthsRemain());
            System.out.println("Loan Applied Date          : " + acc.getLoanDate());
            if (acc.getAccountStatus()) {
                System.out.println("Loan Status                : active");
            } else {
                System.out.println("Loan Status                : Inactive");
            }
        } else {
            System.out.println("\nLoan Account Number is invalid");
        }
    }

    // this function is to used to get duration of months for loan Account
    private static int getLoanMonths(Account acc) {
        int ref = 0;
        do {
            System.out.println("\n");
            if (acc.getAccountType().equals("CurrentAccount")) {
                if (acc.getBalanceType().equals("MinimumBalanceAccount")) {
                    System.out.println("enter the no of months to pay loan");
                    System.out.println("list : 12 24 36 48 60 72 84 96 108 120 months");
                    int m = sc.nextInt();
                    if (m % 12 == 0) {
                        ref = 1;
                        return m;
                    } else {
                        System.out.println("no of months must be any of the given list..");
                    }
                } else {
                    System.out.println("enter the no of months to pay loan");
                    System.out.println("list : 12 24 36months");
                    int m = sc.nextInt();
                    if (m % 12 == 0 & m <= 36) {
                        ref = 1;
                        return m;
                    } else {
                        System.out.println("no of months must be any of the given list..");
                    }
                }
            } else {
                if (acc.getBalanceType().equals("MinimumBalanceAccount")) {
                    System.out.println("enter the no of months to pay loan");
                    System.out.println("12 24 36 48 months");
                    int m = sc.nextInt();
                    if (m % 12 == 0 && m <= 48) {
                        ref = 1;
                        return m;
                    } else {
                        System.out.println("no of months must be any of the given list..");
                    }
                } else {
                    System.out.println("enter the no of months to pay loan");
                    System.out.println("12 24 months");
                    int m = sc.nextInt();
                    if (m % 12 == 0 && m <= 24) {
                        ref = 1;
                        return m;
                    } else {
                        System.out.println("no of months must be any of the given list..");
                    }
                }
            }
        } while (ref == 0);
        return 0;
    }

    // this function is used to choose loan account type
    public static String loanType() {
        int choice = 0;
        String loanAccType = "";
        do {
            System.out.println("\n\n---------------------------------------------------------------------");
            System.out.println("Choose the Loan Type : ");
            System.out.println(" 1.Personal loan");
            System.out.println(" 2.Home loan");
            System.out.println(" 3.Back to Loan Services");
            try {
                System.out.print("Enter choice : ");
                choice = Integer.parseInt(sc.next());
            } catch (NumberFormatException e) {
                System.out.println("You have entered wrong choice.\nPlease again Enter : ");
                choice = 0;
            }
            if (choice == 1) {
                loanAccType = "PersonalLoan";
            } else if (choice == 2) {
                loanAccType = "HomeLoan";
            } else if (choice == 3) {
                loanAccType = null;
            }
        } while (choice < 1 || choice > 3);
        return loanAccType;
    }

    // this function is used to get loan amount
    public static double getLoanAmount(Account acc) {
        System.out.println("Provide details for Loan");
        double amount;
        do {
            amount = ls.getLoanAmount(acc);
            if (amount == -1) {
                System.out.println("loan amount is below or equal to 10k .....");
            } else if (amount == -2) {
                System.out.println("loan amount is below or equal to 2 lakhs....");
            } else if (amount == -3) {
                System.out.println("loan amount is below or equal to 1 lakh......");
            } else if (amount == 4) {
                System.out.println("loan amount is below 20lakhs.....");
            }
        } while (amount <= 0);
        return amount;
    }
}
