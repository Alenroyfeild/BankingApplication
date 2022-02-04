package UserInterface;

import java.util.ArrayList;
import java.util.Scanner;

import entities.Account;
import entities.ChequeBook;
import services.ATMTransaction;
import services.ChequeServices;
import services.userProfile;
import services.utils;

public class CheckServicesUI {
    static userProfile up = new userProfile();
    static Scanner sc = new Scanner(System.in);
    static ChequeServices cs = new ChequeServices();

    // this function is used to display cheques menu
    static void doChequeBook(long mobileNo) {
        Account acc = UtilsUI.displayAccountNumber(mobileNo);
        int choice = 0;
        do {
            System.out.println("\n\n\n--------------------------------------------------------------------------------------------");
            System.out.println("\n  --  Welcome to Check Services Page  --");
            System.out.println(" 1.Genarate Cheque");
            System.out.println(" 2.Cheques Summary");
            System.out.println(" 3.Cancel cheque");
            System.out.println(" 4.Check Deposit");
            System.out.println(" 5.exit");
            try {
                System.out.print("Enter choice  : ");
                choice = Integer.parseInt(sc.next());
            } catch (NumberFormatException e) {
                System.out.println("You have entered wrong choice.\nPlease again Enter : ");
                choice = 0;
            }
            if (choice == 1) {
                doChequeTransferUI(acc);
            } else if (choice == 2) {
                System.out.println("\033[H\033[2J");
                doChequeBookInfo(acc);
            } else if (choice == 3) {
                docancelCheque(acc);
            } else if (choice == 4) {
                doChequeDeposit(acc);
            } else {
                System.out.println("\nBack to Home page");
            }
        } while (choice < 1 || choice > 5 || choice != 5);

    }

    static ATMTransaction atm = new ATMTransaction();

    //this unction is used to deposit amount through cheque
    private static void doChequeDeposit(Account acc) {
        double bal = -1;
        long a[] = new long[2];
        a = doChequeValidDeposit(acc.getAccNo());
        double checkAmount = a[0];
        long payerAccountNumber = a[1];
        if (checkAmount == 0) {
            System.out.println("\nCheque Transaction Failure...");
            return;
        }
        if (checkAmount != -1) {
            atm.dowithdraw(utils.searchAccount(payerAccountNumber), checkAmount, "Cheque-Withdraw", -1);
            bal = atm.doDeposit(acc, checkAmount, "Cheque-Deposit");
        }
        if (bal != -1) {
            System.out.println("\nCheque Transaction successfull...");
            System.out.println("Available Balance : "+bal);
        } else {
            System.out.println("\nCheque Transaction Failure...");
        }
    }

    // this function is used to validate the cheque Deposit is possible or not
    public static long[] doChequeValidDeposit(long accountNumber) {
        long chequeNo = UtilsUI.getChequeNo();
        ChequeBook cb = cs.getChequeAcount(chequeNo);
        Account payerAcc = cs.getChequeAcountNo(chequeNo);
        long a[] = new long[2];
        double chequeAmount = cs.validateCheque(payerAcc, cb);
        if (cb != null) {
            if (payerAcc.getAccNo() != accountNumber) {
                if (chequeAmount == -2) {
                    System.out.println("\nCheque is bounced");
                    return a;
                } else if (chequeAmount == -3) {
                    System.out.println("\nCheque is out of validity period");
                    return a;
                } else if (chequeAmount == -1) {
                    System.out.println("\nCheque is not active");
                    return a;
                }
            } else {
                System.out.println("\npayee account number and payer account number must be different.");
                return a;
            }
        } else {
            System.out.println("\nCheque is not exist");
            return a;
        }
        a[0] = (long) chequeAmount;
        a[1] = payerAcc.getAccNo();
        return a;
    }

    // this function is used to transafer amount via cheque
    public static void doChequeTransferUI(Account acc) {
        double amount = UtilsUI.getAmount();
        long a[] = new long[2];
        a = cs.genarateCheque(acc, amount);
        System.out.println("Cheqe No : " + a[0]);
    }

    // this function is used to cancel cheque
    private static void docancelCheque(Account acc) {
        doChequeBookInfo(acc);
        long chequeNo = UtilsUI.getChequeNo();
        int status = cs.cancelCheque(acc.getAccNo(), chequeNo);
        if (status == 0) {
            System.out.println("\nSuccessfully cheque cancelled");
        } else if (status == -1) {
            System.out.println("\nCheque number is not exist");
        } else {
            System.out.println("\nTransaction Failure");
        }
    }

    // this function is used to display cheque book details
    static void doChequeBookInfo(Account acc) {
        ArrayList<ChequeBook> chequeList = new ArrayList<>();
        chequeList = cs.displayCheque(acc.getAccNo());
        String accNo = (acc.getAccNo() + "");
        System.out.println("\nAccount No : ******" + accNo.substring(6));
        System.out.println(
                "\n   --    Cheque Book information   --\n------------------------------------------------------------------------------------------------------------------------------------");
        System.out.format("%1$-30s%2$-20s%3$-20s%4$-20s\n", "ChequeNumber", "ChequeStatus",
                "ChequeIssueDate", "ChequeAmount");
        System.out.println(
                "------------------------------------------------------------------------------------------------------------------------------------");
        for (ChequeBook chequelist : chequeList) {
            System.out.format("%1$-30s%2$-20s%3$-20s%4$-20s\n", chequelist.getChequeNo(), chequelist.getChequeStatus(),
                    chequelist.getChequeIssuedDate(), chequelist.getChequeAmount());
        }
        System.out.println(
                "------------------------------------------------------------------------------------------------------------------------------------");

    }
}
