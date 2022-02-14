package UserInterface;

import java.io.Console;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

import entities.Account;
import entities.Bank;
import entities.CIF;
import entities.CreditCard;
import entities.CurrentAccount;
import entities.FixedDeposit;
import entities.Loan;
import entities.RecurringDeposit;
import entities.SavingsAccount;
import entities.Transactions;
import entities.UserLogin;
import services.ATMTransaction;
import services.BankAdminService;
import services.BankMain;
import services.CreditCardServices;
import services.CurrentAccountServices;
import services.FDServices;
import services.LoanServices;
import services.RDServices;
import services.SavingsAccountServices;
import services.utils;

public class BankMainUI {
    static Scanner sc = new Scanner(System.in);
    static BankMain bm = new BankMain();
    static Bank ba = Bank.getInstance();

    // this function function is used to choose type of login
    public static int loginPage() {

        int choice = 0;
        do {
             System.out.println("\033[H\033[2J");
            System.out.println("\n\n\n---------------------------------------------------------------------");
            System.out.println("        --- Welcome to Zoho Bank ---");
            System.out.println("\nChoose the Options : ");
            System.out.println(" 1.Admin Login");
            System.out.println(" 2.User Login ");
            System.out.println(" 3.Quit");
            try {
                System.out.print("Enter choice : ");
                choice = Integer.parseInt(sc.next());
                return choice;
            } catch (NumberFormatException e) {
                System.out.println("You have entered wrong choice.\nPlease again Enter : ");
                choice = 0;
            }
        } while (choice < 1 || choice > 3);
        return 0;
    }

    // this function is used to display Admin home page
    public static int adminHomePage() {
        int choice = 0;
        do {
            System.out.println(
                    "\n\n\n--------------------------------------------------------------------------------------------\n");
            System.out.println("        --- Welcome to Zoho Bank ---\n   ~ Admin Page ~\n");
            System.out.println("Choose the Options : ");
            System.out.println(" 1.CIF services");
            System.out.println(" 2.Account Services");
            System.out.println(" 3.Transactions");
            System.out.println(" 4.Loan Services");
            System.out.println(" 5.FD services");
            System.out.println(" 6.RD services");
            System.out.println(" 7.Exit");
            try {
                System.out.print("Enter choice : ");
                choice = Integer.parseInt(sc.next());
                return choice;
            } catch (NumberFormatException e) {
                System.out.println("You have entered wrong choice.\nPlease again Enter : ");
                choice = 0;
            }
        } while (choice < 1 || choice > 7);
        return 0;
    }

    // this function is used to display user entrance page
    public static int adminHomesubPage() {
        int choice = 0;
        do {
            System.out.println(
                    "\n--------------------------------------------------------------------------------------------");
            System.out.println("Choose the Options : ");
            System.out.println("1.Custom Account");
            System.out.println("2.All Accounts");
            try {
                System.out.print("Enter choice : ");
                choice = Integer.parseInt(sc.next());
            } catch (NumberFormatException e) {
                System.out.println("You have entered wrong choice.\nPlease again Enter : ");
                choice = 0;
            }
        } while (choice < 1 || choice > 2);
        return choice;
    }

    // this function is used to call particular method based on choice
    public static int adminPage() {
        int choice1 = adminHomePage();
        int choice = 0;
        if (choice1 != 7) {
            int choice2 = adminHomesubPage();
            if (choice2 == 2)
                choice = choice1 * 2;
            else
                choice = choice1 * 2 - 1;
        }
        return choice;
    }

    // this function is used to display user entrance page
    public static int userEntrancePage() {
        int choice = 0;
        do {
            System.out.println("\033[H\033[2J");
            System.out.println(
                    "\n\n\n--------------------------------------------------------------------------------------------");
            System.out.println("        --- User Entrance Page ---");
            System.out.println("Choose the Options : ");
            System.out.println(" 1.User Signup");
            System.out.println(" 2.User Login ");
            System.out.println(" 3.forgotten Password");
            System.out.println(" 4.forgotten Mobile Number");
            System.out.println(" 5.Quit");
            try {
                System.out.print("Enter choice : ");
                choice = Integer.parseInt(sc.next());
            } catch (NumberFormatException e) {
                System.out.println("You have entered wrong choice.\nPlease again Enter : ");
                choice = 0;
            }
        } while (choice < 1 || choice > 5);
        return choice;
    }

    public static int userAccEntrancePage(long mobileNo) {
        int choice = 0;
        do {
            // System.out.println("\033[H\033[2J");
            System.out.println(
                    "\n\n\n--------------------------------------------------------------------------------------------");
            System.out.println("        --- User Bank Menu Page ---");
            System.out.println("\n  Hi :) " + bm.getUsername(mobileNo));
            System.out.println("\nChoose the Options : ");
            System.out.println(" 1.Accounts(Savings\\Current)");
            System.out.println(" 2.Loans");
            System.out.println(" 3.Deposits");
            System.out.println(" 4.Credit Cards");
            System.out.println(" 5.Fund Services");
            System.out.println(" 6.Cheque Services");
            System.out.println(" 7.User Profile");
            System.out.println(" 8.Log out");
            try {
                System.out.print("Enter choice : ");
                choice = Integer.parseInt(sc.next());
            } catch (NumberFormatException e) {
                System.out.println("You have entered wrong choice.\nPlease again Enter : ");
                choice = 0;
            }
        } while (choice < 1 || choice > 8);
        return choice;
    }

    static BankAdminService bas = new BankAdminService();

    // this function is used to display passbook details
    public static void displayPassbookUI(long mobileNo, Account acc) {
        CIF cifList = bas.getUserCIFDetails(mobileNo);
        System.out.println("\033[H\033[2J");
        System.out.println("\n\n  -- User PassBook  --");
        System.out.println("User Name          : " + cifList.getCustomerFullname());
        System.out.println("Account number     : " + acc.getAccNo());
        System.out.println("Account Type       : " + acc.getAccountType());
        System.out.println("Balance Type       : " + acc.getBalanceType());
        System.out.println("Age                : " + cifList.getAge());
        System.out.println("Mobile number      : " + cifList.getMobileNo());
        System.out.println("Account opened Date: " + acc.getAccOpenDate());
        String[] address = cifList.getAddress();
        System.out.println("Address            : " + address[0] + "," + address[1] + "," + address[2] + "," + address[3]
                + "-" + address[4] + ".");
    }

    // this function is used to display notifications
    static ATMTransaction atm = new ATMTransaction();
    static LoanServices ls = new LoanServices();
    static RDServices rds = new RDServices();
    static FDServices fds = new FDServices();
    static CreditCardServices ccs = new CreditCardServices();

    public static void notifications() {
        for (UserLogin user : ba.userLogins) {
            ArrayList<Account> accList = utils.getAccNumbers(user.getMobileNumber());
            for (Account acc : accList) {
                if (ba.loanList.containsKey(user.getMobileNumber())) {
                    for (Loan loan : ba.loanList.get(user.getMobileNumber())) {
                        if (loan.getAccNo() == acc.getAccNo())
                            ls.autoPayEMI(acc, loan);
                    }
                }
                if (ba.RDList.containsKey(user.getMobileNumber())) {
                    for (RecurringDeposit rdAcc : ba.RDList.get(user.getMobileNumber())) {
                        if (rdAcc.getAccNo() == acc.getAccNo())
                            rds.autoRD(acc, rdAcc);
                    }
                }
                if (ba.CCList.containsKey(acc.getAccNo())) {
                    CreditCard cc = ba.CCList.get(acc.getAccNo());
                    ccs.autoPayCCBill(cc);
                }
            }
        }
    }

    static int x = 1;

    public static void subNotifications(Account acc) {
        LocalDate lastWithdrawDate = acc.getLastWithdrawDate();
        double interest = SavingsAccountServices.updateInterest(acc);
        if (interest != 0) {
            System.out.println(
                    "-------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("  --  Notifications  -- \n");
            if (interest != 0)
                System.out.println("From " + lastWithdrawDate + " to " + LocalDate.now()
                        + " interest is added.\nInterest Amount : " + Math.round(interest)
                        + " is added to your Account\n");
            long days;
            if (acc.getAccountType().equals("SavingsAccount")) {
                days = SavingsAccountServices.displayNotification(acc.getAccNo());
            } else {
                days = CurrentAccountServices.displayNotification(acc.getAccNo());
            }
            if (days != 0) {
                System.out.println("Your Account balance has low minimum balance from past " + days + " days");
            }
            System.out.println(
                    "-------------------------------------------------------------------------------------------------------------------------------------------");
        }
        x = 0;
    }

    public static void closeAccount(Account acc, long mobileNo) {
        acc.setAccStatus(false);
        System.out.println("Choose the Account to transfer balance : ");
        Account acc2 = UtilsUI.displayAccountNumber(mobileNo);
        if (acc2 == null) {
            System.out.println("\nSuccessfully available balance is withdrawn");
        } else {
            bm.closeAccount( acc,acc2);
            ArrayList<Account> arrList = utils.getAccNumbers(mobileNo);
            if (arrList.size() == 1)
                System.out.println("\nOnly one Account is available.So,");
            System.out.println("Available Balance : " + acc2.getAccountBalance() + " is tranfered to selected Account");
        }
        System.out.println("\nSuccessfully Account is closed");
    }

    // this function is used to display Bank menu list
    public static int userBankMenuPage(long mobileNo) {
        String details = bm.getUsername(mobileNo);
        int choice = 0;
        do {
            Account acc = utils.getAccount(mobileNo);
            System.out.println(
                    "\n\n-------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println("                --- Welcome to Zoho Bank ---\n");
            System.out.println("     -- My Accounts Page  --");
            if (x == 1)
                subNotifications(acc);
            System.out.println("Hi " + details + " :)\n");
            if (acc != null) {
                String ac = acc.getAccNo() + "";
                System.out.println(" AccountNumber : 2XXXXX" + ac.substring(6) + "                Balance : "
                        + Math.round(acc.getAccountBalance()));
            } else {
                System.err.println("No Accounts Exist");
            }
            System.out.println("\nChoose the Options : ");
            System.out.println(" 1.Create Account");
            System.out.println(" 2.Passbook");
            System.out.println(" 3.Account statements");
            System.out.println(" 4.Close Account");
            System.out.println(" 5.Back to Home");
            try {
                System.out.print("Enter choice : ");
                choice = Integer.parseInt(sc.next());
            } catch (NumberFormatException e) {
                System.out.println("You have entered wrong choice.\nPlease again Enter : ");
                choice = 0;
            }
        } while (choice < 1 || choice > 5);
        return choice;
    }

    public static void main(String[] args) {
        Bank ba = Bank.getInstance();
        ba.userLogins.add(new UserLogin(9701660809l, "l"));
        ba.userLogins.get(0).setCIFno(1603202101l);
        String[] address = { "3/269-a", "Ananthapuram", "Kadapa", "AP", "626262" };
        ba.cifList.add(new CIF(1603202101l, "Balaji Royal", "Balaji", 9701660809l, 622988081663l, address, 21));
        ba.userLogins.add(new UserLogin(9701660808l, "l"));
        ba.userLogins.get(1).setCIFno(1603202102l);
        ba.cifList.add(new CIF(1603202102l, "Alex Royal", "Royal", 9701660808l, 622988081664l, address, 21));
        ba.accountsList
                .add(new SavingsAccount(9701660809l, 2603202101l, 1603202101l, "SavingsAccount",
                        "MinimumBalanceAccount", 300000, true));
        ba.accountsList
                .add(new CurrentAccount(9701660809l, 2603202102l, 1603202101l, "CurrentAccount",
                        "MinimumBalanceAccount", 300000, true));
        ba.accountsList
                .add(new SavingsAccount(9701660808l, 2603202103l, 1603202102l, "SavingsAccount",
                        "MinimumBalanceAccount", 300000, true));
        ArrayList<Loan> arr = new ArrayList<>();
        arr.add(new Loan(9701660809l, 2603202101l, 5603202101l, "PersonalLoan", LocalDate.of(2022, 01, 01), 100000, 8.5,
                6, 6,
                true));
        ba.loanList.put(9701660809l, arr);
        FixedDeposit acc1 = new FixedDeposit(622988081664l, 9701660809l, 6603202101l, LocalDate.of(2021, 01, 01), 7.5,
                100000, 12,
                true);
        ArrayList<FixedDeposit> arr1 = new ArrayList<>();
        arr1.add(acc1);
        ba.FDList.put(9701660809l, arr1);
        ArrayList<RecurringDeposit> ar = new ArrayList<>();
        ar.add(new RecurringDeposit(9701660809l, 2603202101l, 7603202101l, 1000, 6.1, 622988081664l,
                LocalDate.of(2021, 01, 01),
                14, 14, true));
        ba.RDList.put(9701660809l, ar);
        ba.CCList.put(2603202102l, new CreditCard(2603202102l, 8603202101l, 7112, 10000, LocalDate.of(2026, 01, 01),
                615, "active", false));
        ArrayList<Transactions> trans = new ArrayList<>();
        ba.CCList.get(2603202102l).setFirstUsedDate(LocalDate.of(2021, 02, 01));
        ba.CCList.get(2603202102l).setUsedBalance(1000);
        trans.add(new Transactions(8603202101l, "online", "CreditCard-Debit", LocalDate.of(2021, 02, 01), 1000,
                utils.generateTransactionID(), 0, 9000));
        ba.transactions.put(2603202102l, trans);
        BankMainUI.notifications();
        Console c = System.console();
        int button = 0;
        do {
            button = 2;//loginPage();
            if (button == 1) {
                int button4 = 0;
                if (BankAdminServiceUI.login()) {
                    do {
                        button4 = adminPage();
                        System.out.println("\033[H\033[2J");
                        if (button4 == 1) {
                            BankAdminServiceUI.displaySelectedCIF();
                        } else if (button4 == 2) {
                            BankAdminServiceUI.displayCIFDetails();
                        } else if (button4 == 3) {
                            BankAdminServiceUI.displaySelectedAccount();
                        } else if (button4 == 4) {
                            BankAdminServiceUI.displayAccountDetails();
                        } else if (button4 == 5) {
                            BankAdminServiceUI.displaySelectedAccountTransactions();
                        } else if (button4 == 6) {
                            BankAdminServiceUI.displayTransactions();
                        } else if (button4 == 7) {
                            BankAdminServiceUI.displaySelectedLoanAccountsDetails();
                        } else if (button4 == 8) {
                            BankAdminServiceUI.displayLoanDetails();
                        } else if (button4 == 9) {
                            BankAdminServiceUI.displaySelectedFDAccountDetails();
                        } else if (button4 == 10) {
                            BankAdminServiceUI.displayFDDetails();
                        } else if (button4 == 11) {
                            BankAdminServiceUI.displaySelectedRDAccountDetails();
                        } else if (button4 == 12) {
                            BankAdminServiceUI.displayRDDetails();
                        } else if (button == 0) {
                            System.out.println("Thank you...");
                        }
                    } while (button4 < 0 || button4 > 10 || button4 != 0);
                } else {
                    System.out.println("invalid password");
                }
            } else if (button == 2) {
                int button2;
                do {
                    button2 = userEntrancePage();
                    if (button2 == 1) {
                        AccountCreationUI.userSingin();
                    } else if (button2 == 2) {
                        long mobileNo = UtilsUI.getMobileNo();
                        char[] pass = c.readPassword("Enter your password : ");
                        String password = new String(pass);
                        if (UtilsUI.validateLogin(mobileNo, password)) {
                            UtilsUI.displayAccountsSummary(mobileNo);
                            int button5;
                            do {
                                button5 = userAccEntrancePage(mobileNo);
                                if (button5 == 1) {
                                    int button3 = 0;
                                    System.out.println("\033[H\033[2J");
                                    do {
                                        button3 = userBankMenuPage(mobileNo);
                                        if (button3 == 1) {
                                            long cifno = utils.getCIF(mobileNo);
                                            AccountCreationUI.createAccountUI(cifno, mobileNo);
                                        } else if (button3 == 2) {
                                            System.out.println("\033[H\033[2J");
                                            Account ac = UtilsUI.displayAccountNumber(mobileNo);
                                            displayPassbookUI(mobileNo, ac);
                                        } else if (button3 == 3) {
                                            Account ac = UtilsUI.displayAccountNumber(mobileNo);
                                            ATMTransactionsUI.doMiniStatementsUI(ac.getAccNo(), 1);
                                        } else if (button3 == 4) {
                                            Account ac = UtilsUI.displayAccountNumber(mobileNo);
                                            BankMainUI.closeAccount(ac, mobileNo);
                                        } else {
                                            System.out.println("Back to Home Page");
                                            x = 1;
                                        }
                                    } while (button3 < 1 || button3 > 5 || button3 != 5);
                                } else if (button5 == 2) {
                                    LoanServicesUI.LoanUI(mobileNo);
                                } else if (button5 == 3) {
                                    int button6 = 0;
                                    do {
                                        button6 = depositSelectionPage();
                                        if (button6 == 1) {
                                            FDServicesUI.FDServiceUI(mobileNo);
                                        } else if (button6 == 2) {
                                            RDServicesUI.RDServiceUI(mobileNo);
                                        } else {
                                            System.out.println("\nBack to Home page");
                                        }
                                    } while (button6 < 1 || button6 > 3 || button6 != 3);
                                } else if (button5 == 4) {
                                    CreditCardServicesUI.CreditCardUI(mobileNo);
                                } else if (button5 == 5) {
                                    ATMTransactionsUI.fundServices(mobileNo);
                                } else if (button5 == 6) {
                                    CheckServicesUI.doChequeBook(mobileNo);
                                } else if (button5 == 7) {
                                    UserProfileUI.userProfileMenuPage(mobileNo);
                                } else if (button5 == 8) {
                                    System.out.println("\nBack to Entrance Page");
                                }
                            } while (button5 < 1 || button5 > 8 || button5 != 8);
                        }
                    } else if (button2 == 3) {
                        UtilsUI.forgotPassword();
                    } else if (button2 == 4) {
                        UtilsUI.forgotMobileNo();
                    } else {
                        System.out.println("\nBack to selection Page");
                    }
                } while (button2 < 1 || button2 > 5 || button2 != 5);
            } else {
                System.out.println("\nThank You...!");
            }

        } while (button < 1 || button > 3 || button != 3);
    }

    private static int depositSelectionPage() {
        int choice = 0;
        do {
            System.out.println(
                    "\n\n\n--------------------------------------------------------------------------------------------");
            System.out.println("        --- Deposit Selection Page ---");
            System.out.println("Choose the Options : ");
            System.out.println(" 1.Fixed Deposits");
            System.out.println(" 2.Recurring Deposits ");
            System.out.println(" 3.Back to Home");
            try {
                System.out.print("Enter choice : ");
                choice = Integer.parseInt(sc.next());
            } catch (NumberFormatException e) {
                System.out.println("You have entered wrong choice.\nPlease again Enter : ");
                choice = 0;
            }
        } while (choice < 1 || choice > 3);
        return choice;
    }

}
