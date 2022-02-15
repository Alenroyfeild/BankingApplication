package services;

import java.util.ArrayList;
import entities.Account;
import entities.Bank;
import entities.CIF;
import entities.FixedDeposit;
import entities.Loan;
import entities.RecurringDeposit;
import entities.Transactions;

public class BankAdminService {
    Bank ba = Bank.getInstance();
    ArrayList<CIF> ciflist = new ArrayList<>();
    ArrayList<Account> acclist = new ArrayList<>();

    // this function is used to return CIF details of all accounts
    public ArrayList<CIF> displayCIFDetails() {
        for (CIF cif : ba.cifList) {
            ciflist.add(cif);
        }
        return ciflist;
    }

    // this function is used to return Account details of all accounts
    public ArrayList<Account> displayAccDetails() {
        for (Account acc : ba.accountsList) {
            acclist.add(acc);
        }
        return acclist;
    }

    // this function is used to return all Loan Details
    public ArrayList<Loan> displayLoanDetails() {
        ArrayList<Loan> allLoans = new ArrayList<>();
        for (ArrayList<Loan> loanList : ba.loanList.values()) {
            allLoans.addAll(loanList);
        }
        return allLoans;
    }

    // this function is used to return Transaction details of all accounts
    public ArrayList<Transactions> getTransactions() {
        ArrayList<Transactions> allTrans = new ArrayList<>();
        for (ArrayList<Transactions> arrList : ba.transactions.values()) {
            allTrans.addAll(arrList);
        }
        return allTrans;
    }

    // this function is used to return Account details of custom account
    public Account getUserAccountDetails(long accNo) {
        Account acc = utils.searchAccount(accNo);
        if (acc != null)
            return acc;
        return null;
    }

    // this function is used to return CIF details of custom account
    public CIF getUserCIFDetails(long mobileNo) {
        CIF cif = utils.searchCIF(mobileNo);
        if (cif != null)
            return cif;
        return null;
    }

    // this funtion is used to validate admin login
    public Boolean validateLogin(String password) {
        if (ba.validatePassword(password)) {
            return true;
        }
        return false;
    }

    // this function is used to get all FD details
    public ArrayList<FixedDeposit> getFDDetails() {
        ArrayList<FixedDeposit> allFD = new ArrayList<>();
        for (ArrayList<FixedDeposit> FDList : ba.FDList.values()) {
            allFD.addAll(FDList);
        }
        return allFD;
    }

    // this function is used to get all RD details
    public ArrayList<RecurringDeposit> getRDDetails() {
        ArrayList<RecurringDeposit> allRD = new ArrayList<>();
        for (ArrayList<RecurringDeposit> RDList : ba.RDList.values()) {
            allRD.addAll(RDList);
        }
        return allRD;
    }

    // this function is used to return user loan account details
    public Loan getLoanAccDetails(long accNo) {
        for (Account acc : ba.accountsList) {
            if (ba.loanList.containsKey(acc.getAccNo()))
                for (Loan loan : ba.loanList.get(acc.getAccNo())) {
                    if (loan.getLoanAccNo() == accNo) {
                        return loan;
                    }
                }
        }
        return null;
    }

    // this function is used to return FD Account details
    public FixedDeposit getFDAccDetails(long accNo) {
        for (Account acc : ba.accountsList) {
            if (ba.FDList.containsKey(acc.getAccNo()))
                for (FixedDeposit fd : ba.FDList.get(acc.getAccNo())) {
                    if (fd.getFDAccNo() == accNo) {
                        return fd;
                    }
                }
        }
        return null;
    }

    // this function is used to return RD Account details
    public RecurringDeposit getRDAccDetails(long accNo) {
        for (Account acc : ba.accountsList) {
            if (ba.RDList.containsKey(acc.getAccNo()))
                for (RecurringDeposit rd : ba.RDList.get(acc.getAccNo())) {
                    if (rd.getRDID() == accNo) {
                        return rd;
                    }
                }
        }
        return null;
    }
}
