package UserInterface;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import entities.Account;
import entities.Transactions;
import services.ATMTransaction;
import services.utils;

public class ATMTransactionsUI {
    static ATMTransaction atm = new ATMTransaction();
    static Scanner sc = new Scanner(System.in);
    static DecimalFormat df = new DecimalFormat("0.00");

    // this function is used to choose the Fund services types
    public static int fundServicesPage() {
        int choice;
        do {
            System.out.println(
                    "\n\n------------------------------------------------------------------------------------------------------------------------------------------------");

            System.out.println("\n   --  Fund Services Page --");
            System.out.println(" 1.Withdraw");
            System.out.println(" 2.Credit Card Withdraw");
            System.out.println(" 3.Deposit");
            System.out.println(" 4.Balance Enquiry");
            System.out.println(" 5.Amount Transfer");
            System.out.println(" 6.Mini Statements");
            System.out.println(" 7.Back to Home");
            try {
                System.out.print("Enter choice : ");
                choice = Integer.parseInt(sc.next());
            } catch (NumberFormatException e) {
                System.out.println("You have entered wrong choice.\nPlease again Enter : ");
                choice = 0;
            }
        } while (choice < 1 || choice > 7);
        System.out.println(
                "\n------------------------------------------------------------------------------------------------------------------------------------------------");
        return choice;
    }

    // this function is used to call particualr function based on the selection of
    // fund service
    public static void fundServices(long mobileNo) {
        int choice;
        Account acc = null;
        System.out.println("\033[H\033[2J");
        do {
            choice = fundServicesPage();
            if (choice != 2 && choice != 5 && choice != 7) {
                acc = UtilsUI.displayAccountNumber(mobileNo);
                if (acc == null) {
                    System.out.println("No Accounts available");
                }
            }
            if (choice == 1) {
                doWithdrawUI(acc);
            } else if (choice == 2) {
                CreditCardServicesUI.ccWithdraw(mobileNo);
            } else if (choice == 3) {
                doDepositUI(acc);
            } else if (choice == 4) {
                doBalanceEnquiry(acc);
            } else if (choice == 5) {
                doAmountTransferUI(mobileNo);
            } else if (choice == 6) {
                doMiniStatementsUI(acc.getAccNo(), 1);
            } else {
                System.out.println("\nBack to Home Page");
            }
        } while (choice < 1 || choice > 7 || choice != 7);
    }

    // this function is used to deposit amount
    public static void doDepositUI(Account acc) {
        double bal = -1;
        System.out.println("\nAvailable Balance  : " + acc.getAccountBalance());
        double amount = UtilsUI.getAmount();
        bal = atm.doDeposit(acc, amount, "Credit");
        if (bal != -1) {
            System.out.println("\nTransaction successfull...");
            System.out.println("Available Balance  : " + acc.getAccountBalance());
        } else {
            System.out.println("\nTransaction Failure...");
        }
    }

    // this function is used to choose the duration to display transactions
    public static int miniStatementsMonth() {
        int choice = 0;
        do {
            System.out.println("\nChoose the duration : ");
            System.out.println(" 1.Today transactions");
            System.out.println(" 2.last 1 Month");
            System.out.println(" 3.last 3 Months");
            System.out.println(" 4.last 6 Months");
            System.out.println(" 5.All transactions");
            System.out.println(" 6.Back to Fund Services Page");
            try {
                System.out.print("Enter Choice : ");
                choice = Integer.parseInt(sc.next());
            } catch (NumberFormatException e) {
                System.out.println("You have entered wrong choice.\nPlease again Enter : ");
                choice = 0;
            }
        } while (choice < 1 || choice > 6);
        return choice;
    }

    // this function is used to call method based on choice.
    public static LocalDate getMiniStatementsDate() {
        int choice = miniStatementsMonth();
        LocalDate nMonthsBackDate = LocalDate.now();
        LocalDate presentDate = nMonthsBackDate;
        if (choice == 1) {
            nMonthsBackDate = presentDate;
        } else if (choice == 2) {
            nMonthsBackDate = presentDate.minusMonths(1);
        } else if (choice == 3) {
            nMonthsBackDate = presentDate.minusMonths(3);
        } else if (choice == 4) {
            nMonthsBackDate = presentDate.minusMonths(6);
        } else if (choice == 5) {
            nMonthsBackDate = LocalDate.of(2021, 01, 01);
        } else {
            System.out.println("\nBack to Fund Services Page");
            nMonthsBackDate = null;
        }
        return nMonthsBackDate;
    }

    //this function is used to display transactions
    public static void displayTransactions(String accNo, ArrayList<Transactions> alist, int x) {
        System.out.println("\033[H\033[2J");
        System.out.println("\nAccount No : 2*****" + accNo.substring(6));
        System.out.println(
                "\n   --    Mini Statements   --\n------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.format("%1$-20s%2$-20s%3$-30s%4$-20s%5$-20s%6$-10s%7$-20s\n", "Transaction Mode", "Transaction ID",
                "TransactionDescription", "Date",
                "TransactionAmount", "Fee", "AvailabeBalance");
        System.out.println(
                "------------------------------------------------------------------------------------------------------------------------------------------------");
        for (Transactions tlist : alist) {
            if (x == 1) {
                if (tlist.getAccountNumber() / 1000000000 != 8)
                    System.out.format("%1$-20s%2$-20s%3$-30s%4$-20s%5$-20s%6$-10s%7$-20s\n", tlist.getTransactionMode(),
                            tlist.getTransactionID(),
                            tlist.getTransactionType(), tlist.getTransactionDate(), df.format(tlist.getAmount()),
                            df.format(tlist.getFee()), tlist.getBalance());
            } else if (x == 0) {
                if (tlist.getAccountNumber() / 1000000000 == 8)
                    System.out.format("%1$-20s%2$-20s%3$-30s%4$-20s%5$-20s%6$-10s%7$-20s\n", tlist.getTransactionMode(),
                            tlist.getTransactionID(),
                            tlist.getTransactionType(), tlist.getTransactionDate(), df.format(tlist.getAmount()),
                            df.format(tlist.getFee()), tlist.getBalance());
            }
        }
        System.out.println(
                "------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    // this function is used to display transactions
    public static void doMiniStatementsUI(long accNum, int y) {
        ArrayList<Transactions> alist = new ArrayList<>();
        String accNo = (accNum + "");
        alist = atm.getTenTransactions(accNum);
        displayTransactions(accNo, alist, y);
        alist = null;
        LocalDate date = getMiniStatementsDate();
        if (date == null)
            return;
        alist = atm.dominiStatement(accNum, date);
        displayTransactions(accNo, alist, y);
    }

    // this function is used to withdraw amount
    public static double getWithdrawAmount(Account acc) {
        double amount, amt = 0;
        String accType = acc.getAccountType();
        String balType = acc.getBalanceType();
        if (accType.equals("SavingsAccount")) {
            if (balType.equals("ZeroBalanceAccount")) {
                do {
                    amount = UtilsUI.getAmount();
                    if (amount < 5000) {
                        amt = amount;
                    } else {
                        System.out.println("Amount must be less than 5000");
                    }
                } while (amt == 0);
            } else {
                do {
                    amount = UtilsUI.getAmount();
                    if (amount < 10000) {
                        amt = amount;
                    } else {
                        System.out.println("Amount must be less than 10000");
                    }
                } while (amt == 0);
            }
        } else {
            if (balType.equals("ZeroBalanceAccount")) {
                do {
                    amount = UtilsUI.getAmount();
                    if (amount < 20000) {
                        amt = amount;
                    } else {
                        System.out.println("Amount must be less than 10000");
                    }
                } while (amt == 0);
            } else {
                do {
                    amount = UtilsUI.getAmount();
                    if (amount < 100000) {
                        amt = amount;
                    } else {
                        System.out.println("Amount must be less than 20000");
                    }
                } while (amt == 0);
            }
        }
        return amt;
    }

    // this function is used to withdraw amount
    public static void doWithdrawUI(Account acc) {
        System.out.println("Available Balance  : " + acc.getAccountBalance());
        double amount = getWithdrawAmount(acc);
        String accType = acc.getAccountType();
        double bal = atm.dowithdraw(acc, amount, "Dedit", 0);
        if (bal > -1) {
            if (!accType.equals("SavingsAccount")) {
                System.out.println("\nTransaction fee : 1.75 Rupees");
            }
            System.out.println("\nTransaction successfull...\nRemainig Balance : " + acc.getAccountBalance());
        } else if (bal == -2) {
            System.out.println("\n Insufficient Balance \n Available balance : " + acc.getAccountBalance()
                    + "\nTransaction Failure...");
        } else {
            System.out.println("\nTransaction Failure....");
        }
    }

    // this function is used to display account balance
    public static void doBalanceEnquiry(Account acc) {
        double bal = acc.getAccountBalance();
        if (bal != -1) {
            System.out.println("\nAccount Balance : " + bal);
        } else {
            System.out.println("\nTransaction Failure...");
        }
    }

    // this function is used to choose Type of amount transfer
    private static int AmountTransferPage() {
        int choice = 0;
        do {
            System.out.println("\nChoose the Type of Transaction : ");
            System.out.println(" 1.NEFT");
            System.out.println(" 2.RTGS");
            System.out.println(" 3.Back to Fund Services Page");
            try {
                System.out.print("Enter choice : ");
                choice = Integer.parseInt(sc.next());
            } catch (NumberFormatException e) {
                System.out.println("\nYou have entered wrong choice.\nPlease again Enter : ");
                choice = 0;
            }
        } while (choice < 1 || choice > 3);
        return choice;
    }

    // this function is used to call method based on choice
    public static void doAmountTransferUI(long mobileNo) {
        int choice = AmountTransferPage();
        Account payeeAcc = null;
        if (choice == 1 || choice == 2) {
            do {
                System.out.println("\nEnter Receiver Details : ");
                payeeAcc = UtilsUI.getPayeeAccNumber();
            } while (payeeAcc == null);
        }
        if (choice == 3) {
            System.out.println("\nBack to HomePage");
            return;
        }
        if (payeeAcc != null) {
            System.out.println("\n Receiver name  : " + utils.getUsername(payeeAcc.getCIFNumber()));
            System.out.println(" Receiver AccNo : " + payeeAcc.getAccNo());
            System.out.print("Enter 1 to continue or Any Integer to Exit: ");
            int x = sc.nextInt();
            if (x != 1)
                return;
            System.out.println("\nChoose Source Account : ");
            Account acc = UtilsUI.displayAccountNumber(mobileNo);
            if (acc != payeeAcc) {
                if (choice == 1) {
                    doNEFTUI(acc, payeeAcc);
                } else if (choice == 2) {
                    doRTGSUI(acc, payeeAcc);
                }
            } else {
                System.out.println("\nAccount numbers must be different...");
            }
        } else {
            System.out.println("\nReceiver Account Number is invalid");

        }
    }

    // this function is used to get amount from user
    private static double getTransferAmount(Account acc, String transferType) {
        String accType = acc.getAccountType();
        double amount = 0;
        if (transferType.equals("NEFT")) {
            if (accType.equals("SavingsAccount")) {
                do {
                    amount = UtilsUI.getAmount();
                    if (amount > 200000) {
                        System.out.println("Transfer Amount is must be less than 200000");
                    }
                } while (amount > 200000);
            } else {
                do {
                    amount = UtilsUI.getAmount();
                    if (amount > 1000000) {
                        System.out.println("Transfer Amount is must be less than 10Lakhs");
                    }
                } while (amount > 1000000);
            }
        } else {
            if (!accType.equals("SavingsAccount")) {
                do {
                    amount = UtilsUI.getAmount();
                    if (amount < 200000 || amount > 1000000) {
                        System.out.println("Transfer Amount is must be greater than 2 lakhs and less than 10 lakhs");
                    }
                } while (amount < 200000 || amount > 1000000);
            }
        }
        return amount;
    }

    // this function is used to transafer amount via RTGS
    private static void doRTGSUI(Account payerAcc, Account payeeAcc) {
        String accType = payerAcc.getAccountType();
        if (!accType.equals("SavingsAccount")) {
            double amount = getTransferAmount(payerAcc, "RTGS");
            double a[] = new double[2];
            a = atm.doRTGS(payerAcc, payeeAcc, amount);
            if (a[0] >= 0 && a[1] >= 0) {
                System.out.println("\nTransaction Successfull....");
                System.out.println(" Availale balance : " + payerAcc.getAccountBalance());
            } else if (a[0] == -2) {
                System.out.println("\n Insufficient Balance \n Availale balance : " + payerAcc.getAccountBalance());
                System.out.println("Transaction Failure....");
            } else {
                System.out.println("\nTransaction Failure....");
            }
        } else {
            System.out.println("\nSender Account must be Current Account...");
        }
    }

    // this function is used to transafer amount via NEFT
    private static void doNEFTUI(Account payerAcc, Account payeeAcc) {
        double amount = getTransferAmount(payerAcc, "NEFT");
        double a[] = new double[2];
        a = atm.doNEFT(payerAcc, payeeAcc, amount);
        if (a[0] >= 0 && a[1] >= 0) {
            System.out.println("\nTransaction Successfull...");
            System.out.println(" Availale balance : " + payerAcc.getAccountBalance());
        } else if (a[0] == -2) {
            System.out.println("\n Insufficient Balance \n Availale balance : " + payerAcc.getAccountBalance());
            System.out.println("Transaction Failure...");
        } else {
            System.out.println("\nTransaction Failure...");
        }
    }
}
