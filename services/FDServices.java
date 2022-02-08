package services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import entities.Account;
import entities.Bank;
import entities.CIF;
import entities.FixedDeposit;
import entities.Transactions;

public class FDServices {

    Bank ba = Bank.getInstance();

    // this function is used to create FD Account
    public FixedDeposit createFD(long nomineeAadhar,long mobileNo, long accountNumber, double FDAmount, int FDMonths) {
        long FDAccNo = utils.generateFDAccNo();
        Account acc = utils.searchAccount(accountNumber);
        double balance = acc.getAccountBalance();
        balance -= FDAmount;
        acc.setAccountBalance(balance);
        CIF cif = utils.searchCIF(mobileNo);
        int age = cif.getAge();
        double interestRate = getInterestRate(FDMonths);
        if (age >= 60)
            interestRate += 0.75;
        if (ba.transactions.containsKey(accountNumber))
            ba.transactions.get(accountNumber).add(new Transactions(FDAccNo, "online", "FDAmount-Deposit",
                    LocalDate.now(), -FDAmount, utils.generateTransactionID(), 0,
                    acc.getAccountBalance()));
        else {
            ArrayList<Transactions> trans = new ArrayList<>();
            trans.add(new Transactions(FDAccNo, "online", "FDAmount-Deposit",
                    LocalDate.now(), -FDAmount, utils.generateTransactionID(), 0,
                    acc.getAccountBalance()));
            ba.transactions.put(accountNumber, trans);
        }
        FixedDeposit acc1 = new FixedDeposit(nomineeAadhar,mobileNo, FDAccNo, LocalDate.now(), interestRate, FDAmount, FDMonths,
                true);
        if (ba.FDList.containsKey(mobileNo)) {
            ba.FDList.get(mobileNo).add(acc1);
        } else {
            ArrayList<FixedDeposit> arr = new ArrayList<>();
            arr.add(acc1);
            ba.FDList.put(mobileNo, arr);
        }

        return acc1;
    }

    // this function is used to return interest rate based on amount
    public double getInterestRate(double mons) {
        if (mons < 6 && mons >= 1) {
            float ir = 6.5f;
            return ir;
        } else if (mons >= 6 && mons < 12) {
            float ir = 7.5f;
            return ir;
        } else if (mons >= 12 && mons < 24) {
            float ir = 8.25f;
            return ir;
        } else if (mons >= 24) {
            float ir = 8.5f;
            return ir;
        }
        return 0;
    }

    // this function is used to count days from last withdraw day to present day
    public int[] getFDDays(FixedDeposit fd) {
        int a[] = new int[2];
        int mons = fd.getFDMonths();
        int totalDays = Math.round((mons * 365) / 12);
        int days = 0;
        if (!fd.getFDDepositDate().equals(LocalDate.now())) {
            days = (int) fd.getFDDepositDate().until(LocalDate.now(), ChronoUnit.DAYS);
        }
        a[0] = totalDays;
        a[1] = days;
        return a;
    }

    // this function is used to calculate the interest
    public double interestCalc(double rate, long days, double lamount) {
        double amt = (rate * days * lamount) / 36500;
        return amt;
    }

    // this function is used to return uptodate interest amount
    public void getInterestAmt(FixedDeposit fd) {
        int days = getFDDays(fd)[1];
        double fdAmt = fd.getFDAmount();
        double rate = getInterestRate(fd.getFDDepositDate().until(LocalDate.now(), ChronoUnit.MONTHS));
        double intAmt = interestCalc(rate, days, fdAmt);
        fd.setFDInterestAmount(intAmt);
    }

    // this function is used to return total mature amount
    public void getTotalInterestAmt(FixedDeposit fd) {
        int days = getFDDays(fd)[0];
        double fdAmt = fd.getFDAmount();
        double rate = fd.getFDinterestRate();
        double intAmt = interestCalc(rate, days, fdAmt);
        fd.setFDInterestAmount(intAmt);
    }

    // this function is used to auto credit FD amount
    public void autoCreditFDAmount(Account acc, FixedDeposit fd) {
        if (fd.getStatus()) {
            int days = getFDDays(fd)[1];
            int totaldays = getFDDays(fd)[0];
            if (days >= totaldays) {
                double balance = acc.getAccountBalance();
                double amount;
                getTotalInterestAmt(fd);
                amount = fd.getFDInterestAmount();
                balance += fd.getFDAmount() + amount;
                acc.setAccountBalance(balance);
                ba.transactions.get(acc.getAccNo())
                        .add(new Transactions("online", "FDAmount-Credit",
                                LocalDate.now(), fd.getFDAmount(), utils.generateTransactionID(), 0,
                                acc.getAccountBalance()));
                ba.transactions.get(acc.getAccNo())
                        .add(new Transactions("online", "FDInterest-Credit",
                                LocalDate.now(), fd.getFDInterestAmount(), utils.generateTransactionID(), 0,
                                acc.getAccountBalance()));
                fd.setStatus(false);
            }
        }
    }

    // this function is used to withdraw FD amount
    public double doFDWithdraw(Account acc, FixedDeposit fd) {
        if (fd.getStatus()) {
            double balance = acc.getAccountBalance();
            double amount;
            if (!fd.getFDDepositDate().equals(LocalDate.now())) {
                int days = getFDDays(fd)[1];
                int totaldays = getFDDays(fd)[0];
                if (days >= totaldays) {
                    getTotalInterestAmt(fd);
                } else {
                    getInterestAmt(fd);
                }
                amount = fd.getFDInterestAmount();
                balance += fd.getFDAmount() + amount;
            } else {
                balance += fd.getFDAmount();
                fd.setFDInterestAmount(0);
            }
            acc.setAccountBalance(balance);
            ba.transactions.get(acc.getAccNo())
                    .add(new Transactions("online", "FDAmount-Credit",
                            LocalDate.now(), fd.getFDAmount(), utils.generateTransactionID(), 0,
                            acc.getAccountBalance()));
            ba.transactions.get(acc.getAccNo())
                    .add(new Transactions("online", "FDInterest-Credit",
                            LocalDate.now(), fd.getFDInterestAmount(), utils.generateTransactionID(), 0,
                            acc.getAccountBalance()));
            fd.setStatus(false);
            if (fd.getFDInterestAmount() > 0) {
                return fd.getFDInterestAmount();
            } else {
                return -1;
            }

        } else {
            return -2;
        }
    }

}
