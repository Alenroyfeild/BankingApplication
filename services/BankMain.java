package services;

import entities.CIF;
import entities.CurrentAccount;

import java.time.LocalDate;

import entities.Account;
import entities.SavingsAccount;
import entities.Transactions;
import entities.Bank;

public class BankMain {
    Bank ba = Bank.getInstance();

    // this function is used to validate adhar number
    public long validateAdharNumber(long aadharNo) {
        CIF cif = utils.searchCIF(aadharNo);
        long mobileNo = 0;
        if (cif != null) {
            mobileNo = cif.getMobileNo();
            return mobileNo;
        }
        return mobileNo;
    }

    // this function is used to create CIF
    public CIF createCIF(String customerFullname, String displayName, long aadharNumber, long mobileNo, int age,
            String[] address) {
        CIF cif = null;
        long cifno = utils.generateCIFno();
        cif = new CIF(cifno, customerFullname, displayName, mobileNo, aadharNumber, address, age);
        ba.cifList.add(new CIF(cifno, customerFullname, displayName, mobileNo, aadharNumber, address, age));
        return cif;
    }

    // this function is used to create account
    public Account createAccount(long mobileNo, long cifno, String accountType, String balanceType) {
        Account acc = null;
        long accountNumber = utils.generateAccNo();
        for (CIF cif : ba.cifList) {
            if (cif.getCIFno() == cifno) {
                if (accountType.equals("SavingsAccount")) {
                    if (balanceType.equals("ZeroBalanceAccount")) {
                        acc = new SavingsAccount(mobileNo, accountNumber, cifno, accountType, balanceType, 0, true);
                        ba.accountsList.add(acc);
                    } else {
                        acc = new SavingsAccount(mobileNo, accountNumber, cifno, accountType, balanceType, 2000, true);
                        ba.accountsList.add(acc);
                    }
                } else {
                    if (balanceType.equals("ZeroBalanceAccount")) {
                        acc = new CurrentAccount(mobileNo, accountNumber, cifno, accountType, balanceType, 0, true);
                        ba.accountsList.add(acc);
                    } else {
                        acc = new CurrentAccount(mobileNo, accountNumber, cifno, accountType, balanceType, 5000, true);
                        ba.accountsList.add(acc);
                    }
                }
            }
        }
        return acc;
    }

    // this function is used to close the account
    public void closeAccount(Account acc, Account acc2) {
        acc2.setAccountBalance(acc.getAccountBalance() + acc2.getAccountBalance());
        ba.transactions.get(acc2.getAccNo()).add(new Transactions(acc2.getAccNo(), "online", "ClosedAccBalance-Credit",
                LocalDate.now(), acc.getAccountBalance(), utils.generateTransactionID(), 0, acc2.getAccountBalance()));
    }

}
