package entities;

import java.time.LocalDate;
import java.util.ArrayList;

public abstract class Account {
    private long accountNumber;
    private long CIFNumber;
    private String accountType;
    private String balanceType;
    private LocalDate accOpenDate;
    private double accountBalance;
    private LocalDate lastwithdrawDate;
    private long mobileNo;
    private Boolean accStatus;
    public ArrayList<Transactions> transactionlist = new ArrayList<>(1000);

    public Account(long mobileNo, long accountNumber, long CIFNumber, String accountType, String balanceType,
            double balance, Boolean status) {
        this.accountNumber = accountNumber;
        this.CIFNumber = CIFNumber;
        this.accountType = accountType;
        this.balanceType = balanceType;
        this.accountBalance = balance;
        this.accOpenDate = LocalDate.now();// LocalDate.of(2021,01,01);
        this.lastwithdrawDate = LocalDate.now();// LocalDate.of(2021,01,01);
        this.mobileNo = mobileNo;
        this.accStatus = status;
    }

    public void setAccStatus(Boolean status) {
        this.accStatus = status;
    }

    public Boolean getAccStatus() {
        return this.accStatus;
    }

    public long getMobileNo() {
        return this.mobileNo;
    }

    public void setLastWithdrawDate() {
        this.lastwithdrawDate = LocalDate.now();
    }

    public LocalDate getLastWithdrawDate() {
        return this.lastwithdrawDate;
    }

    public LocalDate getAccOpenDate() {
        return this.accOpenDate;
    }

    public String getBalanceType() {
        return this.balanceType;
    }

    public double getAccountBalance() {
        return Math.round(this.accountBalance);
    }

    public long getAccNo() {
        return this.accountNumber;
    }

    public long getCIFNumber() {
        return this.CIFNumber;
    }

    public String getAccountType() {
        return this.accountType;
    }

    public void setAccountBalance(double balance) {
        this.accountBalance = balance;
    }

    public String toString(String format) {
        return String.format(format, accountNumber, accountType,Math.round(accountBalance));

    }

}