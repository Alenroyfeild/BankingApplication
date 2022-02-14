package UserInterface;

import java.io.Console;
import java.util.ArrayList;
import java.util.Scanner;

import entities.Account;
import entities.Bank;
import entities.CIF;
import entities.CreditCard;
import entities.FixedDeposit;
import entities.Loan;
import entities.RecurringDeposit;
import entities.UserLogin;
import services.BankMain;
import services.LoanServices;
import services.UserLoginServices;
import services.utils;

public class UtilsUI {
    static Scanner sc = new Scanner(System.in);
    static int ref = 0;
    static Console c = System.console();

    // this function is used to validate user details
    public static Boolean validateLogin(long mobileNo, String password) {
        UserLoginServices uls = new UserLoginServices();
        UserLogin acc = uls.validateMoblieNo(mobileNo);
        if (acc != null) {
            Boolean bool = uls.validatePassword(acc, password);
            if (bool) {
                if (acc.getCIFno() == 0) {
                    acc.setCIFno(AccountCreationUI.createCIF(mobileNo));
                    AccountCreationUI.createAccountUI(acc.getCIFno(), mobileNo);
                    System.out.println("Account created successfully");
                }
                return true;
            } else {
                System.out.println("\nInvalid Password");
                return false;
            }
        }
        System.out.println("\nInvalid Mobile Number");
        return false;
    }

    // this function is used to validate Payee account number
    public static Account getPayeeAccNumber() {
        long accountNumber = getAccNumber();
        return utils.searchAccount(accountNumber);
    }

    // this function is used to get number
    public static long getNo(String name, int numberLength) {
        long number;
        do {
            do {
                try {
                    System.out.print("Enter the " + name + " number : ");
                    number = Long.parseLong(sc.next());
                } catch (NumberFormatException e) {
                    System.out.println("Entered " + name + " number is not a valid integer number..");
                    number = 0;
                }
            } while (number == 0);
            if (Math.floor(Math.log10(number) + 1) == numberLength)
                ref = 1;
            else
                System.out.println("Enter the valid " + numberLength + " digit " + name + " number:");
        } while (ref == 0);
        ref = 0;
        return number;
    }

    // this function is used to get account number from user
    public static long getAccNumber() {
        long accountNumber = getNo("Account", 10);
        return accountNumber;
    }

    // this function is used to get adhar number from user
    public static long getAadharno() {
        long aadharno = getNo("Aadhar ", 12);
        return aadharno;
    }

    // this function is used to get mobileno from user
    public static long getMobileNo() {
        long mobileno = getNo("Mobile", 10);
        return mobileno;
    }

    // this function is used to get cheque number from user
    public static long getChequeNo() {
        long chequeNo = getNo("Cheque", 10);
        return chequeNo;
    }

    // this function is used to get pincode
    public static int getPincode() {
        int Pincode = (int) getNo("Pincode", 6);
        return Pincode;
    }

    // this function is used to get amount from user
    public static double getAmount() {
        double amount;
        do {
            try {
                System.out.print("\nEnter Amount : ");
                amount = Double.parseDouble(sc.next());
                if (amount <= 0) {
                    System.out.println("Please enter greater than zero...");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entered Amount is not a valid integer number..\nplease enter Amount: ");
                amount = 0;
            }
        } while (amount <= 0);
        return amount;
    }

    static Bank ba = Bank.getInstance();
    static UserLoginServices uls = new UserLoginServices();

    // this function is used for forgotten password
    public static void forgotPassword() {
        long mobileNo = getMobileNo();
        UserLogin acc = uls.validateMoblieNo(mobileNo);
        if (acc != null) {
            String pass = acc.getPassword(mobileNo);
            if (pass != null)
                System.out.println("Password : " + pass);
        } else {
            System.out.println("Invalid mobile number");
        }
    }

    // this function is used to display loan account numbers
    public static Loan displayLoanAccountNumber(long mobileNo) {
        ArrayList<Loan> arrList = utils.getLoanAccNumbers(mobileNo);
        if (arrList == null) {
            System.out.println("No Loan accounts available");
            return null;
        }
        for (int i = 0; i < arrList.size(); i++) {
            if (!arrList.get(i).getAccountStatus()) {
                arrList.remove(i);
            }
        }
        if (arrList.size() == 1) {
            return arrList.get(0);
        }
        int i, x;
        do {
            i = 0;
            System.out.println("\n Choose Loan Account Number ");
            for (Loan arr : arrList) {
                if (arr.getAccountStatus()) {
                    i++;
                    System.out.println(i + " " + arr.getLoanAccNo() + "    " + arr.getLoanType());
                }
            }
            System.out.print("Enter choice : ");
            x = sc.nextInt();
            System.out.println(
                    "-------------------------------------------------------------------------------------------------------------------------------------------");
        } while (x < 1 || x > i);
        return arrList.get(--x);

    }

    // this function is used to display account numbers
    public static Account displayAccountNumber(long mobileNo) {
        ArrayList<Account> arrList = utils.getAccNumbers(mobileNo);
        if (arrList == null)
            return null;
        if (arrList.size() == 1) {
            System.out.println("1." + arrList.get(0).getAccNo() + "  " + arrList.get(0).getAccountType() + "\n");
            return arrList.get(0);
        }
        int i, x;
        do {
            i = 0;
            System.out.println("\n Choose Account Number \n");
            for (Account obj : arrList) {
                i++;
                System.out
                        .println(i + " " + obj.getAccNo() + "  " + obj.getAccountType() + "  "
                                + obj.getAccountBalance());
            }
            System.out.print("Enter choice : ");
            x = sc.nextInt();
            System.out.println(
                    "-------------------------------------------------------------------------------------------------------------------------------------------");
        } while (x < 1 || x > i);
        return arrList.get(--x);

    }

    // this function is used forgotten mobile no
    public static void forgotMobileNo() {
        long aadharNo = getAadharno();
        BankMain bm = new BankMain();
        long mobileNo = bm.validateAdharNumber(aadharNo);
        if (mobileNo != 0) {
            System.out.println("\nMobile Number : " + mobileNo);
        } else {
            System.out.println("\nInvalid Aadhar Number");
        }
    }

    // this function is used to display FD account numbers
    public static CreditCard displayCredtCardNo(long mobileNo) {
        ArrayList<CreditCard> arrList = utils.getCreditCardS(mobileNo);
        if (arrList == null) {
            System.out.println("\nNo Credit cards available");
            return null;
        }
        if (arrList.size() == 1)
            return arrList.get(0);
        int i, x;
        do {
            i = 0;
            System.out.println("\n Choose Credit Card ");
            for (CreditCard arr : arrList) {
                i++;
                System.out.println(i + " " + arr.getCardNo());
            }
            System.out.print("Enter choice : ");
            x = sc.nextInt();
            System.out.println(
                    "-------------------------------------------------------------------------------------------------------------------------------------------");
        } while (x < 1 || x > i);
        return arrList.get(--x);
    }

    // this function is used to display FD account numbers
    public static FixedDeposit displayFDAccNumbers(long mobileNo) {
        ArrayList<FixedDeposit> arrList = utils.getFDAccNumbers(mobileNo);
        if (arrList == null) {
            System.out.println("\nNo FD accounts available");
            return null;
        }
        if (arrList.size() == 1)
            return arrList.get(0);
        int i, x;
        do {
            i = 0;
            System.out.println("\n Choose FD Account Number ");
            for (FixedDeposit arr : arrList) {
                i++;
                System.out
                        .println(i + " " + arr.getFDAccNo() + "  " + arr.getFDAmount() + "  " + arr.getFDDepositDate());
            }
            System.out.print("Enter choice : ");
            x = sc.nextInt();
            System.out.println(
                    "-------------------------------------------------------------------------------------------------------------------------------------------");
        } while (x < 1 || x > i);
        return arrList.get(--x);
    }

    // this function is used to display RD account numbers
    public static RecurringDeposit displayRDAccNumbers(long mobileNo) {
        ArrayList<RecurringDeposit> arrList = utils.getRDAccNumbers(mobileNo);
        if (arrList == null) {
            System.out.println("\nNo RD accounts available");
            return null;
        }
        if (arrList.size() == 1)
            return arrList.get(0);
        int i, x;
        do {
            i = 0;
            System.out.println("\n Choose RD Account Number ");
            for (RecurringDeposit arr : arrList) {
                i++;
                System.out.println(i + " " + arr.getRDID() + "    RD Amount : " + arr.getRDAmount());
            }
            System.out.print("Enter choice : ");
            x = sc.nextInt();
            System.out.println(
                    "-------------------------------------------------------------------------------------------------------------------------------------------");
        } while (x < 1 || x > i);
        return arrList.get(--x);
    }

    public static String[] getAddress() {
        String[] address = new String[5];
        System.out.print("Enter Do.No/Street name  : ");
        sc.nextLine();
        address[0] = sc.nextLine();
        System.out.print("Enter Village Name       : ");
        address[1] = sc.nextLine();
        System.out.print("Enter District Name      :");
        address[2] = sc.nextLine();
        System.out.print("Enter State Name         : ");
        address[3] = sc.nextLine();
        int pincode = getPincode();
        address[4] = pincode + "";
        return address;
    }

    // this function is used to get aadhar number
    public static CIF getNomineeAadharNo() {
        CIF acc1 = null;
        long aadharNo = 0;
        do {
            System.out.println("\nEnter Nominee Details : ");
            aadharNo = getAadharno();
            acc1 = utils.searchCIF(aadharNo);
            if (acc1 == null) {
                System.out.println("No account exist with this Aadhar No : " + aadharNo);
                break;
            }
        } while (aadharNo == 0);
        return acc1;
    }

    static LoanServices ls = new LoanServices();

    public static void displayAccountsSummary(long mobileNo) {
        ArrayList<Account> accList = utils.getAccNumbers(mobileNo);
        System.out.println("\033[H\033[2J");
        System.out.println(
                "\n   --    Accounts Summary   --\n-------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.format("%1$-30s%2$-25s%3$-20s\n", "AccountNumber",
                "AccountType", "AccountBalance");
        System.out.println(
                "-------------------------------------------------------------------------------------------------------------------------------------------");

        for (Account acc : accList) {
            if (acc != null) {
                System.out.print(acc.toString("%1$-30s%2$-25s%3$-20s\n"));
            }
        }
        System.out.println(
                "-------------------------------------------------------------------------------------------------------------------------------------------");
        LoanServicesUI.displayLoanSummary(mobileNo);
        FDServicesUI.displayFDSummary(mobileNo);
        RDServicesUI.displayRDSummary(mobileNo);
        CreditCardServicesUI.CCNotification(mobileNo);
        System.out.print("Press any key : ");
        sc.next();
    }

    public static int getCardPin(String name, int length) {
        int number;
        do {
            do {
                try {
                    System.out.print("\nEnter Your " + name + " : ");
                    number = Integer.parseInt(sc.next());
                } catch (NumberFormatException e) {
                    System.out.println("Entered " + name + " number is not a valid integer number..");
                    number = 0;
                }
            } while (number == 0);
            if (Math.floor(Math.log10(number) + 1) == length)
                ref = 1;
            else
                System.out.println("Enter the valid " + length + " digit " + name + " number:");
        } while (ref == 0);
        ref = 0;
        return number;
    }
}
