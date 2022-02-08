package UserInterface;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import entities.Account;
import entities.Bank;
import entities.CIF;
import entities.CreditCard;
import entities.Transactions;
import services.CreditCardServices;
import services.utils;

public class CreditCardServicesUI {
    static Scanner sc = new Scanner(System.in);
    static Bank ba = Bank.getInstance();
    static CreditCardServices ccs = new CreditCardServices();

    // this function is used to display loan page
    public static int creditCardPage() {
        int choice = 0;
        do {
            System.out.println(
                    "\n\n------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("\n   -- Credit Card Servics page  --");
            System.out.println("\nChoose the Options : ");
            System.out.println(" 1.Apply Credit Card");
            System.out.println(" 2.Pay Credit Card Bill");
            System.out.println(" 3.Virtual Credit Card");
            System.out.println(" 4.Credit Card Statements");
            System.out.println(" 5.Change Card Pin");
            System.out.println(" 6.Block\\ UnBlock Card");
            System.out.println(" 7.Quit");
            try {
                System.out.print("Enter choice : ");
                choice = Integer.parseInt(sc.next());
                return choice;
            } catch (NumberFormatException e) {
                System.out.println("\nYou have entered wrong choice.\nPlease again Enter : ");
                choice = 0;
            }
        } while (choice < 1 || choice > 7);
        return 0;
    }

    public static void CreditCardUI(long mobileNo) {
        int choice = 0;
        System.out.println("\033[H\033[2J");
        CCNotification(mobileNo);
        Account acc = null;
        CreditCard cc = null;
        int x = 0;
        do {
            choice = creditCardPage();
            if (choice == 1)
                acc = UtilsUI.displayAccountNumber(mobileNo);
            if (choice != 1 && choice != 7 && x == 0) {
                cc = UtilsUI.displayCredtCardNo(mobileNo);
                if (cc.getCardStatus().equals("Block")) {
                    System.out.println("\n\nCredit Card is Blocked First UnBlock the Card ");
                    choice = 6;
                }
                x = 1;
            }
            if (choice == 1) {
                applyCreditCard(acc, mobileNo);
            } else if (choice == 2) {
                payCreditCardBill(cc);
            } else if (choice == 3) {
                displayCC(cc, mobileNo);
            } else if (choice == 4) {
                displayCardStatements(cc, mobileNo);
            } else if (choice == 5) {
                updatePin(cc);
            } else if (choice == 6) {
                updateCardStatus(cc,mobileNo);
                //break;
            } else {
                System.out.println("\nBack to Home Page");
            }
        } while (choice < 1 || choice > 7 || choice != 7);
    }

    private static void updateCardStatus(CreditCard cc,long mobileNo) {
        if (cc == null) {
            return;
        }
        if (cc.getCardStatus().equals("active")) {
            System.out.print("Enter 1 to Block Card : ");
            int x = sc.nextInt();
            if (x == 1) {
                cc.setCardStatus("Block");
                System.out.println("\n\nSuccessfully Credit Card is Blocked");
            } else {
                return;
            }
        } else {
            System.out.print("Enter 1 to UnBlock Card : ");
            int x = sc.nextInt();
            if (x == 1) {
                cc.setCardStatus("active");
                System.out.println("\n\nSuccessfully Credit Card is UnBlocked");
            } else {
                return;
            }
        }
    }

    public static void CCNotification(long mobileNo) {
        ArrayList<CreditCard> ccList = utils.getAllCreditCards(mobileNo);
        if (ccList == null)
            return;
        System.out.println(
                "\n   --   Credit Card Details   --\n------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.format("%1$-15s%2$-20s%3$-20s%4$-20s%5$-20s%6$-20s\n",
                "Credit Card No",
                "Account No", "Balance Limit", "Used Balance", "Available Balance", "CC Bill due Date");
        System.out.println(
                "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        for (CreditCard cc : ccList) {
            System.out.format("%1$-15s%2$-20s%3$-20s%4$-20s%5$-20s%6$-20s\n", cc.getCardNo(), cc.getAccNo(),
                    cc.getBalanceLimit(), Math.round(cc.getUsedBalance()),
                    Math.round(cc.getBalanceLimit() - cc.getUsedBalance()), cc.getFirstUsedDate().plusDays(43));
        }
        System.out.println(
                "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    private static void updatePin(CreditCard cc) {
        if (cc == null)
            return;
        int cvvCode = UtilsUI.getCardPin("CVV code ", 3);
        int newCardPin = UtilsUI.getCardPin("New Card Pin", 4);
        Boolean status = ccs.updateCVV(cc, cvvCode, newCardPin);
        if (status) {
            System.out.println("\n\nSuccessfully updated Card Pin");
        } else {
            System.out.println("\n\n Invalid CVV");
            System.out.println(" Card Pin updation failure");
        }
    }

    private static void applyCreditCard(Account acc, long mobileNo) {
        if (!acc.getAccountType().equals("CurrentAccount")) {
            System.out.println("Reference Account Must be a Current Account");
        }
        Boolean bool = utils.validateCard(acc);
        if (bool) {
            int cardPin = UtilsUI.getCardPin("New Card Pin", 4);
            CreditCard cc = ccs.applyCC(acc, cardPin);
            if (cc != null) {
                displayCC(cc, mobileNo);
            }
        } else {
            System.out.println("For this account Number Already credit card available");
        }
    }

    private static void displayCardStatements(CreditCard cc, long mobileNo) {
        if (cc == null)
            return;
        ArrayList<Transactions> transList = ccs.getCCTransactions(cc, mobileNo);
        if (transList == null)
            return;
        LocalDate date = ATMTransactionsUI.getMiniStatementsDate();
        String cardNo = cc.getCardNo() + "";
        String accNo = cc.getAccNo() + "";
        System.out.println("\n Acc No : 2XXXXX" + accNo.substring(6) + "\t\t Card No : 7XXXXX" + cardNo.substring(6));
        System.out.println(
                "\n   --    Credit Card Statements   --\n------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.format("%1$-20s%2$-20s%3$-30s%4$-20s%5$-20s%6$-20s\n", "Transaction Mode", "Transaction ID",
                "TransactionType", "Date",
                "TransactionAmount", "AvailabeBalance");
        System.out.println(
                "------------------------------------------------------------------------------------------------------------------------------------------------");
        for (Transactions tlist : transList) {
            if (tlist.getTransactionDate().compareTo(date) >= 0) {
                System.out.format("%1$-20s%2$-20s%3$-30s%4$-20s%5$-20s%6$-20s\n", tlist.getTransactionMode(),
                        tlist.getTransactionID(),
                        tlist.getTransactionType(), tlist.getTransactionDate(), Math.round(tlist.getAmount()),
                        tlist.getBalance());
            }
        }
        System.out.println(
                "------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    private static void displayCC(CreditCard cc, long mobileNo) {
        if (cc == null)
            return;
        CIF cif = utils.getCIFAccount(mobileNo);
        System.out.println("\n\n      ~ Credit Card Details ~");
        System.out.println(" User Name        : " + cif.getUsername());
        System.out.println(" Card No          : " + cc.getCardNo());
        System.out.println(" CC Expiry Date   : " + cc.getCardExpiryDate());
        System.out.println(" CC CVV code      : " + cc.getCvvCode());
        System.out.println(" CC Balance Limit : " + cc.getBalanceLimit());
        System.out.println(" CC status        : " + cc.getCardStatus());
    }

    public static void ccWithdraw(Account acc) {
        CreditCard cc = utils.getCreditCard(acc.getAccNo());
        if (cc == null) {
            System.out.println("No credit cards available with this Account No " + acc.getAccNo());
            return;
        }
        System.out.println("\n");
        int pin = UtilsUI.getCardPin("Card Pin ", 4);
        if (cc.validatePin(pin)) {
            if (cc.getCardStatus() != "Block") {
                double amount = UtilsUI.getAmount();
                if (amount <= (cc.getBalanceLimit() - cc.getUsedBalance())) {
                    ccs.CCwithdraw(acc, cc, amount);
                    System.out.println("\n\nTransaction Successfull \nAvailable Credit Card Balance : "
                            + (cc.getBalanceLimit() - cc.getUsedBalance()));
                } else {
                    System.out.println("Insufficiet Balance \nAvailable Credit Card Balance : "
                            + (cc.getBalanceLimit() - cc.getUsedBalance()));
                }
            }
        } else {
            System.out.println("Invalid Pin");
        }
    }

    public static void payCreditCardBill(CreditCard cc) {
        if (cc == null) {
            return;
        }
        double amount = ccs.getCreditCardBill(cc);
        if (amount <= 0) {
            System.out.println("No Credit card Bills available");
        } else {
            System.out.println("Credit card Bill : " + amount);
            System.out.print("Enter 1 to continue : ");
            int x = sc.nextInt();
            if (x == 1) {
                double status = ccs.payCreditCardBill(cc, amount);
                if (status > 0) {
                    System.out.println("\nInsufficient Balance\nAvailable Balance :" + status);
                } else if (status == -1) {
                    System.out.println("\nCredit Card Bill Paid Successfull");
                }
            } else {
                return;
            }
        }
    }
}
