package services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;

import entities.Account;
import entities.Bank;
import entities.Transactions;

public class ATMTransaction {
    Bank ba = Bank.getInstance();

    // this function is used to withdraw amount from custom account
    public double dowithdraw(Account acc, double amount, String transactionType, double fee) {
        if (acc != null) {
            double balance = acc.getAccountBalance();
            if (balance == 0) {
                return -2;
            }
            String accType = acc.getAccountType();
            if (!accType.equals("SavingsAccount")) {
                if (fee == 0)
                    fee = 1.75;
            }
            if (fee == -1)
                fee = 0;
            if (fee != 0)
                fee = -fee;
            if (balance >= (amount + fee)) {
                balance -= (amount + fee);
                acc.setAccountBalance(balance);
                if (ba.transactions.containsKey(acc.getAccNo()))
                    ba.transactions.get(acc.getAccNo())
                            .add(new Transactions(acc.getAccNo(), "online", transactionType,
                                    LocalDate.now(), -amount, utils.generateTransactionID(), fee, balance));
                else {
                    ArrayList<Transactions> trans = new ArrayList<>();
                    trans.add(new Transactions(acc.getAccNo(), "online", transactionType, LocalDate.now(), -amount,
                            utils.generateTransactionID(), fee, balance));
                    ba.transactions.put(acc.getAccNo(), trans);
                }
                return balance;
            } else {
                return -2;
            }
        }
        return -1;

    }

    // this funtion is used to deposit amount to custom account
    public double doDeposit(Account acc, double amount, String transactionType) {
        if (acc != null) {
            double balance = acc.getAccountBalance();
            balance += amount;
            acc.setAccountBalance(balance);
            if (ba.transactions.containsKey(acc.getAccNo()))
                ba.transactions.get(acc.getAccNo()).add(new Transactions(acc.getAccNo(), "online", transactionType,
                        LocalDate.now(), amount, utils.generateTransactionID(), 0, acc.getAccountBalance()));
            else {
                ArrayList<Transactions> trans = new ArrayList<>();
                trans.add(new Transactions(acc.getAccNo(), "online", transactionType, LocalDate.now(), amount,
                        utils.generateTransactionID(), 0, acc.getAccountBalance()));
                ba.transactions.put(acc.getAccNo(), trans);
            }
            return acc.getAccountBalance();
        }
        return -1;
    }

    // this function is used to return transactions list of an Account
    public ArrayList<Transactions> dominiStatement(long accNo, LocalDate date) {
        if (accNo != 0
                && ba.transactions.containsKey(accNo)) {
            ArrayList<Transactions> list = new ArrayList<>();
            for (Transactions tlist : ba.transactions.get(accNo)) {
                if (tlist.getTransactionDate().compareTo(date) >= 0) {
                    list.add(tlist);
                }
            }
            return list;
        }
        return new ArrayList<Transactions>();
    }

    // this function is used to return NEFT fee
    public static double getNEFTfee(double amount) {
        double fee = 0;
        if (amount < 10000) {
            fee = 2.5;
        } else if (amount >= 10000 && amount < 10000) {
            fee = 5;
        } else if (amount >= 100000 && amount < 200000) {
            fee = 15;
        } else {
            fee = 25;
        }
        return fee;
    }

    // this function is used to return RTGS fee
    public static double getRTGSFee(double amount) {
        double fee;
        if (amount > 200000 && amount < 500000) {
            fee = 25;
        } else {
            fee = 50;
        }
        return fee;
    }

    // this function is used to perfom NEFT transaction
    public double[] doNEFT(Account payerAcc, Account payeeAcc, double amount) {
        double fee = getNEFTfee(amount);
        double withdrawStatus = dowithdraw(payerAcc, amount,
                "NEFT-Receiver-" + utils.getUsername(payeeAcc.getCIFNumber()), fee);
        double a[] = new double[2];
        a[0] = withdrawStatus;
        if (withdrawStatus >= 0) {
            double depositStatus = doDeposit(payeeAcc, amount,
                    "NEFT-Sender-" + utils.getUsername(payerAcc.getCIFNumber()));
            a[1] = depositStatus;
        }
        return a;
    }

    // this function is used to perfom RTGS transaction
    public double[] doRTGS(Account payerAcc, Account payeeAcc, double amount) {
        double fee = getRTGSFee(amount);
        double withdrawStatus = dowithdraw(payerAcc, amount,
                "RTGS-Receiver-" + utils.getUsername(payeeAcc.getCIFNumber()), fee);
        double a[] = new double[2];
        a[0] = withdrawStatus;
        if (withdrawStatus >= 0) {
            double depositStatus = doDeposit(payeeAcc, amount,
                    "RTGS-Sender-" + utils.getUsername(payerAcc.getCIFNumber()));
            a[1] = depositStatus;
        }
        return a;
    }

    //this function is used to return last ten transactions
    public ArrayList<Transactions> getTenTransactions(long accNo) {
        if (accNo != 0
                && ba.transactions.containsKey(accNo)) {
            ArrayList<Transactions> list = new ArrayList<>();
            ArrayList<Transactions> list1 = ba.transactions.get(accNo);
            ListIterator<Transactions> list2 = list1.listIterator(list1.size());
            int x = 1;
            while ((x <= 10) && list2.hasPrevious()) {
                {
                    list.add(list2.previous());
                    x++;
                }
            }
            Collections.reverse(list);
            return list;
        }
        return new ArrayList<Transactions>();
    }

}
