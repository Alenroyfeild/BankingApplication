package services;

import entities.CIF;
import entities.CurrentAccount;
import entities.Account;
import entities.SavingsAccount;
import entities.Bank;

public class BankMain {
    Bank ba = Bank.getInstance();

    // this function is used to validate adhar number
    public long validateAdharNumber(long aadharNo) {
        CIF cif = utils.searchCIFAdharindex(aadharNo);
        long mobileNo = 0;
        if (cif != null) {
            mobileNo = cif.getMobileNo();
            return mobileNo;
        }
        return mobileNo;
    }

    // this function is used to create CIF
    public CIF createCIF(String username, long aadharNumber, long mobileNo, int age, int pincode) {
        CIF cif = null;
        long cifno = utils.generateCIFno();
        cif = new CIF(cifno, username, mobileNo, aadharNumber, pincode, age);
        ba.cifList.add(new CIF(cifno, username, mobileNo, aadharNumber, pincode, age));
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
                        acc = new SavingsAccount(mobileNo,accountNumber, cifno, accountType, balanceType, 0);
                        ba.accountsList.add(new SavingsAccount(mobileNo,accountNumber, cifno, accountType, balanceType, 0));
                    } else {
                        acc = new SavingsAccount(mobileNo,accountNumber, cifno, accountType, balanceType, 2000);
                        ba.accountsList.add(new SavingsAccount(mobileNo,accountNumber, cifno, accountType, balanceType, 2000));
                    }
                } else {
                    if (balanceType.equals("ZeroBalanceAccount")) {
                        acc = new CurrentAccount(mobileNo,accountNumber, cifno, accountType, balanceType, 0);
                        ba.accountsList.add(new CurrentAccount(mobileNo,accountNumber, cifno, accountType, balanceType, 0));
                    } else {
                        acc = new CurrentAccount(mobileNo,accountNumber, cifno, accountType, balanceType, 5000);
                        ba.accountsList.add(new CurrentAccount(mobileNo,accountNumber, cifno, accountType, balanceType, 5000));
                    }
                }
            }
        }
        return acc;
    }

    // this function is used to return particular details of user
    public String getUsername(long mobileNo) {
        CIF cif = (CIF) utils.search(ba.cifList, value -> ((CIF) value).getMobileNo() == mobileNo);
        if (cif != null) {
            return cif.getUsername();
        }
        return null;
    }

}
