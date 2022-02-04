package services;

import java.time.LocalDate;
import java.util.ArrayList;

import entities.Account;
import entities.Bank;
import entities.ChequeBook;

public class ChequeServices {
    // this function is used to genarate cheque
    public long[] genarateCheque(Account acc, double amount) {
        long chequeNo = utils.genarateChequeNo();
        long a[] = new long[2];
        if (ba.chequesList.containsKey(acc.getAccNo())) {
            ba.chequesList.get(acc.getAccNo())
                    .add(new ChequeBook(acc.getAccNo(), chequeNo, "active", LocalDate.now(), amount));
        } else {
            ArrayList<ChequeBook> cheque = new ArrayList<>();
            cheque.add(new ChequeBook(acc.getAccNo(), chequeNo, "active", LocalDate.now(), amount));
            ba.chequesList.put(acc.getAccNo(), cheque);
        }
        a[0] = chequeNo;
        a[1] = acc.getAccNo();
        return a;
    }

    Bank ba = Bank.getInstance();

    // this function is used to display all cheques details
    public ArrayList<ChequeBook> displayCheque(long accountNumber) {
        if (ba.chequesList.containsKey(accountNumber)) {
            return ba.chequesList.get(accountNumber);
        }
        return new ArrayList<ChequeBook>();
    }

    // this function is used to check the cheque duration period
    private Boolean validCheckPeriod(ChequeBook acc) {
        LocalDate checkValidDate = acc.getChequeIssuedDate().plusMonths(3);
        if (checkValidDate.compareTo(LocalDate.now()) >= 0) {
            return true;
        }
        return false;
    }

    //this function is used to return cheque details
    public ChequeBook getChequeAcount(long chequeNo) {
        for (Account acc : ba.accountsList) {
            if(ba.chequesList.containsKey(acc.getAccNo()))
            for (ChequeBook check : ba.chequesList.get(acc.getAccNo())) {
                if (check.getChequeNo() == chequeNo) {
                    return check;
                }
            }
        }
        return null;
    }

    // this function is used to validate cheque
    public double validateCheque(Account acc, ChequeBook cheques) {
        double balance = cheques.getChequeAmount();
        Boolean bool1 = validCheckPeriod(cheques);
        Boolean bool = utils.validBalance(acc, balance);
        if (bool1) {
            if (bool) {
                if (cheques.getChequeStatus() == "active") {
                    cheques.setChequeStatus("Approved");
                    return cheques.getChequeAmount();
                } else {
                    return -1;
                }
            } else {
                cheques.setChequeStatus("Bounce");
                return -2;
            }
        } else {
            cheques.setChequeStatus("OutofValidPeriod");
            return -3;
        }
    }

    // this function is used to cancel cheque
    public int cancelCheque(long accountNumber, long chequeNo) {
        if (ba.chequesList.containsKey(accountNumber)) {
            for (ChequeBook tac : ba.chequesList.get(accountNumber)) {
                if (tac.getChequeNo() == chequeNo) {
                    tac.setChequeStatus("inActive");
                    return 0;
                } else {
                    return -1;
                }
            }

        }
        return -2;
    }

    //this function is used to return cheque Account number
    public Account getChequeAcountNo(long chequeNo) {
        for (Account acc : ba.accountsList) {
            if(ba.chequesList.containsKey(acc.getAccNo()))
            for (ChequeBook check : ba.chequesList.get(acc.getAccNo())) {
                if (check.getChequeNo() == chequeNo) {
                    return acc;
                }
            }
        }
        return null;
    }
}
