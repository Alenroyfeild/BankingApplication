package services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import UserInterface.UtilsUI;
import entities.Account;
import entities.Bank;
import entities.Loan;
import entities.Transactions;

public class LoanServices {
    Bank ba = Bank.getInstance();

    // this function is used to create loan account
    public Loan createLoanAccount(long mobileNo, long accountNumber, String loanAccType, double loanAmount,
            int noofMonths) {
        long loanAccNumber = utils.generateLoanAccNo();
        double interestRate = getInterestRate(loanAccType);
        Account acc = utils.searchAccount(accountNumber);
        double balance = acc.getAccountBalance();
        balance += loanAmount;
        acc.setAccountBalance(balance);
        if (ba.transactions.containsKey(accountNumber))
            ba.transactions.get(accountNumber).add(new Transactions(loanAccNumber, "online", "LoanAmount-Credit",
                    LocalDate.now(), loanAmount, utils.generateTransactionID(), 0,
                    acc.getAccountBalance()));
        else {
            ArrayList<Transactions> trans = new ArrayList<>();
            trans.add(new Transactions(loanAccNumber, "online", "LoanAmount-Credit", LocalDate.now(), loanAmount,
                    utils.generateTransactionID(), 0, acc.getAccountBalance()));
            ba.transactions.put(accountNumber, trans);
        }
        Loan acc1 = new Loan(mobileNo, accountNumber, loanAccNumber, loanAccType, LocalDate.now(), loanAmount,
                interestRate,
                noofMonths,
                noofMonths, true);
        if (ba.loanList.containsKey(mobileNo)) {
            ba.loanList.get(mobileNo).add(acc1);
        } else {
            ArrayList<Loan> arr = new ArrayList<>();
            arr.add(acc1);
            ba.loanList.put(mobileNo, arr);
        }

        return acc1;
    }

    // this function is used to return interest rate based on loan account type
    public double getInterestRate(String loanAccType) {
        if (loanAccType.equals("PersonalLoan")) {
            return 10.5;
        } else {
            return 8.5;
        }
    }

    public ArrayList<Loan> displayLoanDetails(long mobileNo) {
        if (ba.loanList.containsKey(mobileNo)) {
            return ba.loanList.get(mobileNo);
        }
        return null;
    }

    // this function is used to return loan amount
    public double getLoanAmount(Account acc) {
        double amt;
        int ref = 0;
        do {
            amt = UtilsUI.getAmount();
            if (acc.getAccountType().equals("SavingsAccount")) {
                if (acc.getBalanceType().equals("ZeroBalanceAccount")) {
                    if (amt <= 10000) {
                        ref = 1;
                        return amt;
                    } else {
                        return -1;
                    }
                } else {
                    if (amt <= 200000) {
                        ref = 1;
                        return amt;
                    } else {
                        return -2;
                    }
                }
            } else {
                if (acc.getBalanceType().equals("ZeroBalanceAccount")) {
                    if (amt <= 100000) {
                        ref = 1;
                        return amt;
                    } else {
                        return -3;
                    }
                } else {
                    if (amt <= 2000000) {
                        ref = 1;
                        return amt;
                    } else {
                        return -4;
                    }
                }
            }
        } while (ref == 0);
    }

    // this function is used to display loan details
    public Loan displayLoanDetails(long mobileNo, long accNo) {
        if (ba.loanList.containsKey(mobileNo)) {
            for (Loan loan : ba.loanList.get(mobileNo)) {
                if (loan.getLoanAccNo() == accNo) {
                    return loan;
                }
            }
        }
        return null;
    }

    // this function is used to calculate the days
    public int countDays(LocalDate date) {
        date=date.minusMonths(1);
        long mons = date.until(LocalDate.now(), ChronoUnit.MONTHS);
        return (int) mons;
    }

    // this function is used for auto Pay EMI
    public int autoPayEMI(Account acc, Loan loan) {
        if (loan.getAccountStatus()) {
            if (loan.getMonthsRemain() == 0) {
                loan.setAccountStatus(false);
                return 0;
            }
            if (loan.getMonthsRemain() >= 1) {
                double EMI = calculateEMI(loan.getnoofMonths(), loan.getInterestRate(),
                        loan.getLoanAmount());
                int mons = countDays(loan.getDueDate());
                while (mons >= 1 && loan.getMonthsRemain() >= 1) {
                    loan.setMonthsRemain();
                    double balance = acc.getAccountBalance();
                    balance -= EMI;
                    acc.setAccountBalance(balance);
                    if (ba.transactions.containsKey(acc.getAccNo()))
                        ba.transactions.get(acc.getAccNo())
                                .add(new Transactions(loan.getLoanAccNo(), "online", "LoanEMI-Dedit",
                                        LocalDate.now(), -EMI, utils.generateTransactionID(), 0,
                                        acc.getAccountBalance()));
                    else {
                        ArrayList<Transactions> trans = new ArrayList<>();
                        trans.add(new Transactions(loan.getLoanAccNo(), "online", "LoanEMI-Dedit",
                                LocalDate.now(), -EMI, utils.generateTransactionID(), 0,
                                acc.getAccountBalance()));
                        ba.transactions.put(acc.getAccNo(), trans);
                    }
                    mons--;

                }if(loan.getMonthsRemain()==0){
                    loan.setAccountStatus(false);
                }
            }
        }
        return 0;
    }

    // this function is used to Pay EMI
    public int payEMI(Account acc, Loan loan, double amount) {
        LocalDate date = loan.getLoanDate().plusMonths(loan.getnoofMonths() - loan.getMonthsRemain());
        long days = date.until(LocalDate.now(), ChronoUnit.DAYS);
        if (loan.getLoanDate().equals(LocalDate.now()))
            return -2;
        if (days > 0) {
            loan.setMonthsRemain();
            double balance = acc.getAccountBalance();
            balance -= amount;
            acc.setAccountBalance(balance);
            loan.setDueDate(LocalDate.now().plusDays(days+5));
            ba.transactions.get(acc.getAccNo())
                    .add(new Transactions(loan.getLoanAccNo(), "online", "LoanEMI-Dedit",
                            LocalDate.now(), -amount, utils.generateTransactionID(), 0,
                            acc.getAccountBalance()));
            return loan.getMonthsRemain();
        } else {
            return -1;
        }
    }

    // this function is used to pay total loan amount
    public void payTotalLoanAmount(Account acc, Loan loan, double amount) {
        double balance = acc.getAccountBalance();
        balance -= amount;
        acc.setAccountBalance(balance);
        ba.transactions.get(acc.getAccNo()).add(new Transactions(loan.getLoanAccNo(), "online", "LoanEMI-Dedit",
                LocalDate.now(), -amount, utils.generateTransactionID(), 0,
                acc.getAccountBalance()));
        loan.setMonthsZero();
        loan.setAccountStatus(false);
    }

    // this function is used to return EMI amount
    public double getEMIAmount(Loan loan) {
        if (loan.getAccountStatus()) {
            if (loan.getMonthsRemain() >= 1) {
                double EMI = calculateEMI(loan.getnoofMonths(), loan.getInterestRate(),
                        loan.getLoanAmount());
                return Math.round(EMI);
            } else {
                loan.setAccountStatus(false);
                return -1;
            }
        } else {
            return -2;
        }
    }

    // this function is used to calculate EMI amount
    public double calculateEMI(int noofMonths, double interestRate, double loanAmount) {
        double amt, intamt = (noofMonths * loanAmount * interestRate) / (1200);
        amt = (loanAmount + intamt) / noofMonths;
        return amt;
    }

    // this function is used to get loan Amount and remaining interest amount
    public double[] getLoanBalance(int remainMons, int noofLoanMonths, double interestRate, double lamount) {
        double arr[] = new double[2];
        double tintamt = (interestRate * lamount * noofLoanMonths) / (1200);
        double rintamt = (tintamt / noofLoanMonths) * remainMons;
        double loanBal = ((lamount * remainMons) / (noofLoanMonths));
        arr[0] = Math.round(loanBal);
        arr[1] = Math.round(rintamt);
        return arr;
    }

    // this function is used to return total loan amount
    public double[] getTotalLoanAmount(long mobileNo, Loan loan) {
        double arr[] = new double[2];
        if (ba.loanList.containsKey(mobileNo)) {
            if (loan.getAccountStatus()) {
                if (loan.getMonthsRemain() >= 1) {
                    arr = getLoanBalance(loan.getMonthsRemain(), loan.getnoofMonths(), loan.getInterestRate(),
                            loan.getLoanAmount());
                    return arr;
                } else {
                    arr[0] = -1;
                    return arr;
                }
            } else {
                arr[0] = -2;
                return arr;
            }

        }
        arr[0] = 0;
        return arr;
    }

    // this function is used to return loan statement
    public ArrayList<Transactions> getLoanStatementList(long accNo, long loanAccNumber) {
        if (ba.transactions.containsKey(accNo)) {
            ArrayList<Transactions> list = new ArrayList<>();
            for (Transactions trans : ba.transactions.get(accNo)) {
                if (trans.getAccountNumber() == loanAccNumber) {
                    list.add(trans);
                }
            }
            return list;
        }
        return null;
    }
}
