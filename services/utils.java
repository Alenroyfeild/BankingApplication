package services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.Predicate;
import entities.*;

public class utils {
    static int cifGenarateCode = 3;
    static int checkGenarateCode = 1;
    static int loanGenarateCode = 2;
    static int accGenarateCode = 4;
    static int ccGenarateCode = 2;
    static int FDGenarateCode = 2;
    static int RDGenarateCode = 2;
    static int transactionID = 1;
    static Bank ba = Bank.getInstance();

    // this function is used to generate CIF number
    public static long generateCIFno() {
        long servicecode = 16032021;
        servicecode = servicecode * 100;
        servicecode = servicecode + cifGenarateCode;
        ccGenarateCode++;
        return servicecode;
    }

    // this function is used to generate Credit card number
    public static long generateCCno() {
        long servicecode = 86032021;
        servicecode = servicecode * 100;
        servicecode = servicecode + ccGenarateCode;
        cifGenarateCode++;
        return servicecode;
    }

    // Function to genarate account number
    public static long generateAccNo() {
        long servicecode = 26032021;
        servicecode = servicecode * 100;
        servicecode = servicecode + accGenarateCode;
        accGenarateCode++;
        return servicecode;
    }

    // Function to genarate loan account number
    public static long generateLoanAccNo() {
        long servicecode = 56032021l;
        servicecode = servicecode * 100;
        servicecode = servicecode + loanGenarateCode;
        loanGenarateCode++;
        return servicecode;
    }

    // Function to genarate FD account number
    public static long generateFDAccNo() {
        long servicecode = 66032021l;
        servicecode = servicecode * 100;
        servicecode = servicecode + FDGenarateCode;
        FDGenarateCode++;
        return servicecode;
    }

    // Function to genarate RD account number
    public static long generateRDAccNo() {
        long servicecode = 76032021l;
        servicecode = servicecode * 100;
        servicecode = servicecode + RDGenarateCode;
        RDGenarateCode++;
        return servicecode;
    }

    // Function to genarate transaction ID
    public static long generateTransactionID() {
        long servicecode = 46032021l;
        servicecode = servicecode * 100;
        servicecode = servicecode + transactionID;
        transactionID++;
        return servicecode;
    }

    // this function is used to genarate cheque number
    public static long genarateChequeNo() {
        long servicecode = 36032021l;
        servicecode = servicecode * 100;
        servicecode = servicecode + checkGenarateCode;
        checkGenarateCode++;
        return servicecode;
    }

    // this function is used to validate adhar number and CIF no
    public static Boolean forgotPassword(long cifNo, long aadharNo) {
        CIF cif = searchCIF(aadharNo);
        if (cif != null && cif.getCIFno() == cifNo) {
            return true;
        }
        return false;
    }

    // this function is used to return object
    public static Object search(ArrayList<? extends Object> list, Predicate<Object> condition) {
        int j = list.size() - 1, i = 0;
        while (i <= j) {
            if (condition.test(list.get(i)))
                return list.get(i);
            if (condition.test(list.get(j)))
                return list.get(j);
            i++;
            j--;
        }
        return null;
    }

    // this function is used to return password
    public static String getPassword(long mobileNo, int index) {
        return ba.userLogins.get(index).getPassword(mobileNo);
    }

    // this function is used to return CIF deatils
    public static CIF searchCIF(long mobileNo) {
        return (CIF) search(ba.cifList, value -> ((CIF) value).getMobileNo() == mobileNo);
    }

    // this function is used to return Account deatils
    public static Account searchAccount(long accountNumber) {
        return (Account) search(ba.accountsList, value -> ((Account) value).getAccNo() == accountNumber);
    }

    // this function is used to return all account numbers
    public static ArrayList<Account> getAccNumbers(long mobileNo) {
        ArrayList<Account> arrList = new ArrayList<>();
        int x = 0;
        for (Account acc : ba.accountsList) {
            if (acc.getMobileNo() == mobileNo && acc.getAccStatus()) {
                arrList.add(acc);
                x = 1;
            }
        }
        if (x == 1)
            return arrList;
        else
            return null;
    }

    // this function is used to return CIF no
    public static long getCIF(long mobileNo) {
        CIF cif = (CIF) search(ba.cifList, value -> ((CIF) value).getMobileNo() == mobileNo);
        return cif.getCIFno();
    }

    // this function is used to return loan account numbers
    public static ArrayList<Loan> getLoanAccNumbers(long mobileNo) {
        if (ba.loanList.containsKey(mobileNo)) {
            ArrayList<Loan> arrList = new ArrayList<>();
            int x = 0;
            for (Loan acc : ba.loanList.get(mobileNo)) {
                if (acc.getAccountStatus()) {
                    arrList.add(acc);
                    x = 1;
                }
            }
            if (x == 1)
                return arrList;
        }
        return null;
    }

    // this function is used to return FD account numbers
    public static ArrayList<FixedDeposit> getFDAccNumbers(long mobileNo) {
        if (ba.FDList.containsKey(mobileNo)) {
            ArrayList<FixedDeposit> arrList = new ArrayList<>();
            for (FixedDeposit acc : ba.FDList.get(mobileNo)) {
                if (acc.getStatus())
                    arrList.add(acc);
            }
            if (arrList.size() > 0)
                return arrList;
        }
        return null;
    }

    // this function is used to check account balance is sufficient to perform
    // transaction
    public static Boolean validBalance(Account acc, double balance) {
        if (acc != null && acc.getAccountBalance() >= balance) {
            return true;
        }
        return false;
    }

    // this function is used to set password
    public static Boolean setPassword(long mobileno, String password) {
        UserLogin ul = (UserLogin) search(ba.userLogins, value -> ((UserLogin) value).getMobileNumber() == mobileno);
        if (ul != null) {
            ul.changePassword(password);
            return true;
        } else
            return false;
    }

    // this function is used to return CIF details
    public static CIF searchCIFAdharindex(long aadharNo) {
        return (CIF) search(ba.cifList, value -> ((CIF) value).getAadharNumber() == aadharNo);
    }

    public static ArrayList<Loan> getUserLoanAcc(long mobileNo) {
        ArrayList<Loan> loanList = new ArrayList<>();
        int x = 0;
        if (ba.loanList.containsKey(mobileNo)) {
            for (Loan loan : ba.loanList.get(mobileNo)) {
                loanList.add(loan);
                x = 1;
            }
        }
        if (x == 1)
            return loanList;
        else
            return null;
    }

    // this function is used to return user name
    public static String getUsername(long cifNumber) {
        CIF cif = (CIF) search(ba.cifList, value -> ((CIF) value).getCIFno() == cifNumber);
        return cif.getCustomerFullname();
    }

    // this function is used to validate aadhar number
    public static CIF validateAadhar(long aadharNo) {
        CIF cif = (CIF) search(ba.cifList, value -> ((CIF) value).getAadharNumber() == aadharNo);
        return cif;
    }

    // this function is used to return all RD account numbers
    public static ArrayList<RecurringDeposit> getRDAccNumbers(long mobileNo) {
        if (ba.RDList.containsKey(mobileNo)) {
            ArrayList<RecurringDeposit> arrList = new ArrayList<>();
            int x = 0;
            for (RecurringDeposit acc : ba.RDList.get(mobileNo)) {
                if (acc.getRDstatus()) {
                    arrList.add(acc);
                    x = 1;
                }
            }
            if (x == 1)
                return arrList;
        }
        return null;
    }

    public static Account getRefAccount(long accNo) {
        Account acc = (Account) search(ba.accountsList, value -> ((Account) value).getAccNo() == accNo);
        return acc;
    }

    public static ArrayList<FixedDeposit> getUserAllFDAcc(long mobileNo) {
        ArrayList<FixedDeposit> FDList = new ArrayList<>();
        int x = 0;
        if (ba.FDList.containsKey(mobileNo)) {
            for (FixedDeposit fd : ba.FDList.get(mobileNo)) {
                FDList.add(fd);
                x = 1;
            }
        }
        if (x == 1)
            return FDList;
        else
            return null;
    }

    // this function is used to return user name
    public static String getname(long aadharNo) {
        CIF cif = (CIF) search(ba.cifList, value -> ((CIF) value).getAadharNumber() == aadharNo);
        return cif.getCustomerFullname();
    }

    public static Account getAccount(long mobileNo) {
        Account acc = (Account) search(ba.accountsList,
                value -> ((Account) value).getMobileNo() == mobileNo && ((Account) value).getAccStatus());
        return acc;
    }

    public static ArrayList<RecurringDeposit> getUserAllRDAcc(long mobileNo) {
        ArrayList<RecurringDeposit> RDList = new ArrayList<>();
        int x = 0;
        if (ba.RDList.containsKey(mobileNo)) {
            for (RecurringDeposit rd : ba.RDList.get(mobileNo)) {
                RDList.add(rd);
                x = 1;
            }
        }
        if (x == 1)
            return RDList;
        else
            return null;
    }

    public static ArrayList<Loan> getUserAllLoanAcc(long mobileNo) {
        ArrayList<Loan> loanList = new ArrayList<>();
        int x = 0;
        if (ba.loanList.containsKey(mobileNo)) {
            for (Loan loan : ba.loanList.get(mobileNo)) {
                loanList.add(loan);
                x = 1;
            }
        }
        if (x == 1)
            return loanList;
        else
            return null;
    }

    // this function is used to return user name
    public static String getHoldername(long mobileNo) {
        CIF cif = (CIF) search(ba.cifList, value -> ((CIF) value).getMobileNo() == mobileNo);
        return cif.getCustomerFullname();
    }

    public static ArrayList<FixedDeposit> getUserFDAcc(long mobileNo) {
        ArrayList<FixedDeposit> FDList = new ArrayList<>();
        int x = 0;
        if (ba.FDList.containsKey(mobileNo)) {
            for (FixedDeposit fd : ba.FDList.get(mobileNo)) {
                FDList.add(fd);
                x = 1;
            }
        }
        if (x == 1)
            return FDList;
        else
            return null;
    }

    public static ArrayList<RecurringDeposit> getUserRDAcc(long mobileNo) {
        ArrayList<RecurringDeposit> RDList = new ArrayList<>();
        int x = 0;
        if (ba.RDList.containsKey(mobileNo)) {
            for (RecurringDeposit rd : ba.RDList.get(mobileNo)) {
                RDList.add(rd);
                x = 1;
            }
        }
        if (x == 1)
            return RDList;
        else
            return null;
    }

    public static LocalDate getLoanDueDate(Loan loan) {
        LocalDate date;
        date = loan.getLoanDate().plusMonths(loan.getnoofMonths() - loan.getMonthsRemain() + 1);
        date = date.plusDays(5);
        return date;
    }

    public static LocalDate getRDDueDate(RecurringDeposit rd) {
        LocalDate date1, date;
        date1 = rd.getRDOpenDate().plusMonths(rd.getRDTenure() - rd.getRDRemainingMonths());
        date = date1.plusDays(15);
        return date;
    }

    public static LocalDate getFDMatureDate(FixedDeposit fd) {
        LocalDate date;
        date = fd.getFDDepositDate().plusMonths(fd.getFDMonths());
        return date;
    }

    public static CIF getCIFAccount(long mobileNo) {
        CIF cif = (CIF) search(ba.cifList, value -> ((CIF) value).getMobileNo() == mobileNo);
        return cif;
    }

    public static CreditCard getCreditCard(long accNo) {
        if (ba.CCList.containsKey(accNo)) {
            return ba.CCList.get(accNo);
        }
        return null;
    }

    public static ArrayList<CreditCard> getCreditCardS(long mobileNo) {
        ArrayList<Account> accList = new ArrayList<>();
        accList = utils.getAccNumbers(mobileNo);
        ArrayList<CreditCard> ccList = new ArrayList<>();
        int x = 0;
        for (Account acc : accList) {
            if (ba.CCList.containsKey(acc.getAccNo())) {
                ccList.add(ba.CCList.get(acc.getAccNo()));
                x = 1;
            }
        }
        if (x == 1)
            return ccList;
        else
            return null;
    }

    public static Boolean validateCard(Account acc) {
        if (ba.CCList.containsKey(acc.getAccNo())) {
            return false;
        }
        return true;
    }

    public static ArrayList<CreditCard> getAllCreditCards(long mobileNo) {
        ArrayList<CreditCard> ccList = new ArrayList<>();
        ArrayList<Account> accList = getAccNumbers(mobileNo);
        int x = 0;
        for (Account acc : accList) {
            if (ba.CCList.containsKey(acc.getAccNo())) {
                ccList.add(ba.CCList.get(acc.getAccNo()));
                x = 1;
            }
        }
        if (x == 1) {
            return ccList;
        } else
            return null;
    }

}
