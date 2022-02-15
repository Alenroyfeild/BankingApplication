package services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import entities.Account;
import entities.Bank;
import entities.CIF;
import entities.RecurringDeposit;
import entities.Transactions;

public class RDServices {

    Bank ba = Bank.getInstance();

    // this function is used to create RD account
    public RecurringDeposit createRD(long mobileNo, long accountNumber, double amount, int mons, long nomineeAadharNo) {
        long RDAccNo = utils.generateRDAccNo();
        CIF cif = utils.searchCIF(mobileNo);
        int age = cif.getAge();
        double interestRate = getInterestRate(mons);
        if (age >= 60)
            interestRate += 0.5;
        Account acc = utils.searchAccount(accountNumber);
        double balance = acc.getAccountBalance();
        balance -= amount;
        acc.setAccountBalance(balance);
        if (ba.transactions.containsKey(accountNumber))
            ba.transactions.get(accountNumber).add(new Transactions(RDAccNo, "online", "RDAmount-Deposit",
                    LocalDate.now(), -amount, utils.generateTransactionID(), 0,
                    acc.getAccountBalance()));
        else {
            ArrayList<Transactions> trans = new ArrayList<>();
            trans.add(new Transactions(RDAccNo, "online", "RDAmount-Deposit",
                    LocalDate.now(), -amount, utils.generateTransactionID(), 0,
                    acc.getAccountBalance()));
            ba.transactions.put(accountNumber, trans);
        }
        RecurringDeposit rdAcc = new RecurringDeposit(mobileNo, accountNumber, RDAccNo, amount, interestRate,
                nomineeAadharNo,
                LocalDate.now(), mons, mons - 1, true);
        if (ba.RDList.containsKey(mobileNo))
            ba.RDList.get(mobileNo).add(rdAcc);
        else {
            ArrayList<RecurringDeposit> arr = new ArrayList<>();
            arr.add(rdAcc);
            ba.RDList.put(mobileNo, arr);

        }
        return rdAcc;
    }

    //this function is used to return interest rate based on months duration
    private double getInterestRate(int mons) {
        if (mons > 1 && mons <= 12) {
            return 6.1;
        } else if (mons > 12 && mons <= 24) {
            return 6.2;
        } else if (mons <= 36 && mons > 24) {
            return 6.3;
        } else if (mons <= 48 && mons > 36) {
            return 6.4;
        } else if (mons > 48) {
            return 6.5;
        }
        return 0;
    }

    // this function is used to calculate interest amount
    public static double calcInterest(double rdAmount, double ir, double n, int tenure) {
        double a[] = new double[tenure];
        for (int i = 0; i < tenure; i++) {
            int j = i + 1;
            double x = 1 + (ir * 0.01) / n, y = n * j / 12;
            double m = rdAmount * Math.pow(x, y);
            a[i] = m;
        }
        double amount = 0;
        for (int i = 0; i < tenure; i++) {
            amount = amount + a[i];
        }
        return amount;
    }

    // this function is used to check date
    public boolean checkDate(RecurringDeposit rdAcc) {
        LocalDate payDate = rdAcc.getRDOpenDate().plusMonths(rdAcc.getRDTenure() - rdAcc.getRDRemainingMonths())
                .plusDays(15);
        if (payDate.compareTo(LocalDate.now()) <=0) {
            return true;
        }
        return false;
    }

    // this function is used to auto Pay RD amount
    public void autoRD(Account acc, RecurringDeposit rdAcc) {
        if (checkDate(rdAcc)) {
            do {
                payRDAmount(acc, rdAcc);
            } while (checkDate(rdAcc) && rdAcc.getRDRemainingMonths() >= 1);

        }
    }

    // this function is used to add RD amount
    public double addRDAmount(Account acc, RecurringDeposit rdAcc) {
        double amount = calcInterest(rdAcc.getRDAmount(), rdAcc.getRDInterestRate(), 4, rdAcc.getRDTenure());
        double balance = acc.getAccountBalance();
        balance += amount;
        acc.setAccountBalance(balance);
        ba.transactions.get(acc.getAccNo()).add(new Transactions(rdAcc.getRDID(), "online", "RDAmount-Credit",
                LocalDate.now(), rdAcc.getTotalRDAmount(), utils.generateTransactionID(), 0,
                acc.getAccountBalance() - (amount - rdAcc.getTotalRDAmount())));
        ba.transactions.get(acc.getAccNo()).add(new Transactions(rdAcc.getRDID(), "online", "RDInterest-Credit",
                LocalDate.now(), amount - rdAcc.getTotalRDAmount(), utils.generateTransactionID(), 0,
                acc.getAccountBalance()));
        rdAcc.setRDStatus(false);
        return amount;
    }

    // this function is used to pay RD amount
    public void payRDAmount(Account acc, RecurringDeposit rdAcc) {
        double balance = acc.getAccountBalance();
        balance -= rdAcc.getRDAmount();
        acc.setAccountBalance(balance);
        if (ba.transactions.containsKey(acc.getAccNo()))
            ba.transactions.get(acc.getAccNo()).add(new Transactions(rdAcc.getRDID(), "online", "RDAmount-Deposit",
                    LocalDate.now(), -rdAcc.getRDAmount(), utils.generateTransactionID(), 0, acc.getAccountBalance()));
        else {
            ArrayList<Transactions> trans = new ArrayList<>();
            trans.add(new Transactions(rdAcc.getRDID(), "online", "RDAmount-Deposit",
                    LocalDate.now(), -rdAcc.getRDAmount(), utils.generateTransactionID(), 0, acc.getAccountBalance()));
            ba.transactions.put(acc.getAccNo(), trans);
        }

        rdAcc.setRDremainingMonths();
        rdAcc.setTotalAmount();
    }

    // this function is used to return RD statement
    public ArrayList<Transactions> getRDStatementList(RecurringDeposit rd) {
        if (ba.transactions.containsKey(rd.getAccNo())) {
            ArrayList<Transactions> list = new ArrayList<>();
            for (Transactions trans : ba.transactions.get(rd.getAccNo())) {
                if (trans.getAccountNumber() == rd.getRDID()) {
                    list.add(trans);
                }
            }
            return list;
        }
        return null;
    }

    // this function is used to return RD details
    public RecurringDeposit displayRDDetails(long mobileNo, long rdAccNo) {
        if (ba.RDList.containsKey(mobileNo)) {
            for (RecurringDeposit rd : ba.RDList.get(mobileNo)) {
                if (rd.getRDID() == rdAccNo)
                    return rd;
            }
        }
        return null;
    }

    // this function is used to validate RD date
    public LocalDate validateRDdate(RecurringDeposit rdAcc) {
        LocalDate eligibleDate = rdAcc.getRDOpenDate().plusMonths(Math.round(rdAcc.getRDTenure() * (0.6)));
        if (eligibleDate.compareTo(LocalDate.now()) <= 0) {
            return null;
        }
        return eligibleDate;
    }

    // this function is used to withdraw RD amount
    public double dowithdrawRD(Account acc, RecurringDeposit rdAcc) {
        double amount = calcInterest(rdAcc.getRDAmount(),
                getInterestRate(rdAcc.getRDTenure() - rdAcc.getRDRemainingMonths()), 4,
                (int) rdAcc.getRDOpenDate().until(LocalDate.now(), ChronoUnit.MONTHS) + 1);
        double balance = acc.getAccountBalance();
        balance += amount;
        acc.setAccountBalance(balance);
        ba.transactions.get(acc.getAccNo()).add(new Transactions(rdAcc.getRDID(), "online", "RDAmount-Credit",
                LocalDate.now(), rdAcc.getTotalRDAmount(), utils.generateTransactionID(), 0,
                acc.getAccountBalance() - (amount - rdAcc.getTotalRDAmount())));
        ba.transactions.get(acc.getAccNo()).add(new Transactions(rdAcc.getRDID(), "online", "RDInterest-Credit",
                LocalDate.now(), amount - rdAcc.getTotalRDAmount(), utils.generateTransactionID(), 0,
                acc.getAccountBalance()));
        rdAcc.setRDStatus(false);
        return amount;
    }

    public double getMatureAmount(RecurringDeposit rd) {
        return calcInterest(rd.getRDAmount(), rd.getRDInterestRate(), 4, rd.getRDTenure());
    }

}
