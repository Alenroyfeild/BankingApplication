package services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import entities.Account;
import entities.Bank;
import entities.Transactions;

public class SavingsAccountServices {
    static Bank ba = Bank.getInstance();

    // this function is used to displayNotification for low minimum balance
    public static long displayNotification(long accountNumber) {
        Bank b = Bank.getInstance();
        Account acc = utils.searchAccount(accountNumber);
        if (acc != null && acc.getAccountType().equals("MinimumBalance")
                && acc.getAccountBalance() < 2000) {
            long days = acc.getLastWithdrawDate().until(LocalDate.now(), ChronoUnit.DAYS);
            if (days > 25 && days < 30) {
                return days;
            } else if (days == 31) {
                double bal = acc.getAccountBalance();
                bal -= 25;
                acc.setAccountBalance(bal);
                b.transactions.get(accountNumber)
                        .add(new Transactions("MinimumBalanceFine", "Debit",
                                LocalDate.now(), 0, utils.generateTransactionID(), -25, acc.getAccountBalance()));
            }
        }
        return 0;
    }

    // this function is used to countDays
    public static long countdays(LocalDate lastwithdrawDate) {
        if (!lastwithdrawDate.equals(LocalDate.now())) {
            long days = lastwithdrawDate.until(LocalDate.now(), ChronoUnit.DAYS);
            return days;
        }
        return 0;
    }

    // this function is to addd savings interest to account
    public static double updateInterest(Account acc) {
        int age = getAge(acc.getCIFNumber());
        double bal = calcInterest(age, acc);
        double interest = bal;
        if (bal != 0) {
            if (acc != null) {
                bal += acc.getAccountBalance();
                acc.setAccountBalance(bal);
                acc.setLastWithdrawDate();
                if (ba.transactions.containsKey(acc.getAccNo())) {
                    ba.transactions.get(acc.getAccNo())
                            .add(new Transactions("online", "SavingsInterest-Credit",
                                    LocalDate.now(), interest, utils.generateTransactionID(), 0,
                                    acc.getAccountBalance()));
                } else {
                    ArrayList<Transactions> transList = new ArrayList<>();
                    transList.add(new Transactions("SavingsInterest", "Credit",
                            LocalDate.now(), interest, utils.generateTransactionID(), 0,
                            acc.getAccountBalance()));
                    ba.transactions.put(acc.getAccNo(), transList);
                }
                return interest;
            }

        }
        return 0;
    }

    // this function is used to return age
    public static int getAge(long cifno) {
        int j = ba.cifList.size() - 1, i = 0;
        while (i <= j) {
            if (ba.cifList.get(i).getCIFno() == cifno)
                return ba.cifList.get(i).getAge();
            if (ba.cifList.get(j).getCIFno() == cifno)
                return ba.cifList.get(j).getAge();
            i++;
            j--;
        }
        return -1;
    }

    // this fuction is used to calculate interest
    public static double calcInterest(int age, Account acc) {
        double balance = 0, interestRate = 3.75;
        if (age >= 60)
            interestRate += 0.50;
        balance = acc.getAccountBalance();
        if (balance != 0) {
            long days = countdays(acc.getLastWithdrawDate());
            if (days != 0) {
                double bal = (balance * interestRate * days) / (365 * 100);
                return bal;
            }

        }
        return 0;
    }
}
