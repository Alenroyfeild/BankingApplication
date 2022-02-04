package services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import entities.Account;
import entities.Bank;
import entities.Transactions;

public class CurrentAccountServices {
    static Bank ba = Bank.getInstance();

    // this function is used to display notification for low minimum balance
    public static long displayNotification(long accountNumber) {
        Account acc = utils.searchAccount(accountNumber);
        if (acc!=null && acc.getAccountType().equals("MinimumBalance")
                && acc.getAccountBalance() < 5000) {
            long days = acc.getLastWithdrawDate().until(LocalDate.now(), ChronoUnit.DAYS);
            if (days > 25 && days < 30) {
                return days;
            } else if (days == 30) {
                double bal = acc.getAccountBalance();
                bal -= 125;
                acc.setAccountBalance(bal);
                ba.transactions.get(accountNumber)
                        .add(new Transactions("MinimumBalanceFine", "Debit",
                                LocalDate.now(), 0, utils.generateTransactionID(), -25, acc.getAccountBalance()));
            }

        }
        return 0;
    }
}
