package services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;

import entities.Account;
import entities.Bank;
import entities.CreditCard;
import entities.Transactions;

public class CreditCardServices {
    static Bank ba = Bank.getInstance();

    public CreditCard applyCC(Account acc, int cardPin) {
        if (acc.getAccountType().equals("CurrentAccount")) {
            long cardNo = utils.generateCCno();
            Random rand = new Random();
            int cvvCode = rand.nextInt(1000);
            CreditCard cc = new CreditCard(acc.getAccNo(), cardNo, cardPin, 10000, LocalDate.now().plusYears(5),
                    cvvCode, "active", false);
            ArrayList<Transactions> trans = new ArrayList<>();
            trans.add(new Transactions(cardNo, "online", "CreditCardbalance-Credit", LocalDate.now(), 0,
                    utils.generateTransactionID(), 0, 10000));
            ba.CCList.put(acc.getAccNo(), cc);
            if (ba.transactions.containsKey(acc.getAccNo())) {
                ba.transactions.get(acc.getAccNo())
                        .add(new Transactions(cardNo, "online", "CreditCardbalance-Credit", LocalDate.now(), 0,
                                utils.generateTransactionID(), 0, 10000));
            } else {
                ba.transactions.put(acc.getAccNo(), trans);
            }
            return cc;
        }
        return null;
    }

    public void CCwithdraw(Account acc, CreditCard cc, double amount) {
        if (cc.getUsedBalance() == 0) {
            cc.setFirstUsedDate(LocalDate.now());
        }
        cc.setUsedBalance(amount);
        ba.transactions.get(acc.getAccNo())
                .add(new Transactions(cc.getCardNo(), "online", "CreditCard-Debit",
                        LocalDate.now(), amount, utils.generateTransactionID(), 0,
                        (cc.getBalanceLimit() - cc.getUsedBalance())));
    }

    public double getCreditCardBill(CreditCard cc) {
        ArrayList<Transactions> transList = getTransactions(cc, cc.getFirstUsedDate(), LocalDate.now());
        if (transList == null)
            return -1;
        double amount = calcBillAmount(transList);
        if (amount > 0)
            return amount;
        else
            return 0;
    }

    private double calcBillAmount(ArrayList<Transactions> transList) {
        double amount = 0;
        for (Transactions trans : transList) {
            amount += calcBill(trans.getAmount(), trans.getTransactionDate(), trans.getBillPaid());
            trans.setBillPaid(true);
        }
        return amount;
    }

    private double calcBill(double amount, LocalDate transactionDate, boolean billPaid) {
        if (!billPaid) {
            long days;
            if (LocalDate.now().equals(transactionDate))
                days = 1;
            else
                days = transactionDate.until(LocalDate.now(), ChronoUnit.DAYS);
            if (days < 0) {
                return 0;
            }
            double bill = (amount * days * 3.5) / 2800;
            return bill;
        } else {
            return 0;
        }
    }

    public void autoPayCCBill(CreditCard cc) {
        if (cc.getFirstUsedDate().until(LocalDate.now(), ChronoUnit.DAYS) >= 28) {
            ArrayList<Transactions> transList = getTransactions(cc, cc.getFirstUsedDate(), LocalDate.now());
            if (transList == null)
                return;
            double amount = calcBillAmount(transList);
            cc.setLastCardBillAmt(amount);
            cc.setCardBillStatus(true);
            payCreditCardBill(cc, amount);
        }
    }

    private ArrayList<Transactions> getTransactions(CreditCard cc, LocalDate firstUsedDate, LocalDate now) {
        if (ba.CCList.containsKey(cc.getAccNo())) {
            ArrayList<Transactions> transList = new ArrayList<>();
            for (Transactions trans : ba.transactions.get(cc.getAccNo())) {
                if (trans.getAccountNumber() == cc.getCardNo()
                        && trans.getTransactionType().equals("CreditCard-Debit")) {
                    if (trans.getTransactionDate().compareTo(firstUsedDate) >= 0
                            && trans.getTransactionDate().compareTo(now) <= 0)
                        transList.add(trans);
                }
            }
            return transList;
        }
        return null;
    }

    public double payCreditCardBill(CreditCard cc, double amount) {
        Account acc = utils.getRefAccount(cc.getAccNo());
        if (acc.getAccountBalance() >= amount) {
            double balance = acc.getAccountBalance();
            balance -= amount;
            acc.setAccountBalance(balance);
            ba.transactions.get(acc.getAccNo())
                    .add(new Transactions(acc.getAccNo(), "online", "CreditCardBill-Debit",
                            LocalDate.now(), -amount, utils.generateTransactionID(), 0, balance));
            ba.transactions.get(acc.getAccNo())
                    .add(new Transactions(cc.getCardNo(), "online", "CreditCardBill-Paid",
                            LocalDate.now(), amount, utils.generateTransactionID(), 0,
                            0));
            cc.setUsedBalanceZero();
            cc.setCardBillStatus(true);
            cc.setLastCardBillAmt(amount);
            cc.setFirstUsedDate(LocalDate.now());
            if (cc.getBalanceLimit() <= 100000)
                cc.setBalanceLimit(1000);
            ba.transactions.get(acc.getAccNo())
                    .add(new Transactions(cc.getCardNo(), "online", "CreditCardbalance-Credit", LocalDate.now(), 0,
                            utils.generateTransactionID(), 0, cc.getBalanceLimit()));
            return -1;
        }
        return acc.getAccountBalance();
    }

    public ArrayList<Transactions> getCCTransactions(CreditCard cc, long mobileNo) {
        if (ba.CCList.containsKey(cc.getAccNo())) {
            ArrayList<Transactions> transList = new ArrayList<>();
            for (Transactions trans : ba.transactions.get(cc.getAccNo())) {
                if (trans.getAccountNumber() == cc.getCardNo()) {
                    transList.add(trans);
                }
            }
            return transList;
        }
        return null;
    }

    public Boolean updateCVV(CreditCard cc, int cvvCode, int newCardPin) {
        if (cc.validateCVV(cvvCode)) {
            cc.setPin(newCardPin);
            return true;
        } else {
            return false;
        }
    }

}
