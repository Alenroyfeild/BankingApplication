package entities;

import java.text.DecimalFormat;
import java.time.LocalDate;

public class Transactions {

    private long senderAccountNumber;
    private String transactionType;
    private String transactionMode;
    private LocalDate transactionDate;
    private double amount;
    private double fee;
    private Boolean status;
    private long receiverAccountNumber;
    private String receiverIFSC;
    private long transactionID;
    private long accountNumber;
    private double balance;
    private boolean billPaid;

    public Transactions(String transactionMode, String transactionType, LocalDate transactionDate, double amount,
            long transactionID, double fee, double balance) {
        this.transactionMode = transactionMode;
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
        this.amount = amount;
        this.transactionID = transactionID;
        this.fee = fee;
        this.balance = balance;
    }

    public Transactions(long accountNumber, String transactionMode, String transactionType, LocalDate transactionDate,
            double amount,
            long transactionID, double fee, double balance) {
        this(transactionMode, transactionType, transactionDate, amount, transactionID, fee, balance);
        this.accountNumber = accountNumber;
    }

    public Transactions(long accountNumber, String transactionMode, String transactionType, LocalDate transactionDate,
            double amount,
            long transactionID, double fee, double balance, boolean billPaid) {
        this(accountNumber, transactionMode, transactionType, transactionDate, amount, transactionID, fee, balance);
        this.billPaid = billPaid;
    }

    public boolean getBillPaid() {
        return this.billPaid;
    }

    public void setBillPaid(boolean status) {
        this.billPaid = status;
    }

    public String getBalance() {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(this.balance);
    }

    public long getAccountNumber() {
        return this.accountNumber;
    }

    public String getTransactionMode() {
        return this.transactionMode;
    }

    public long getsenderAccountNumber() {
        return this.senderAccountNumber;
    }

    public String getTransactionType() {
        return this.transactionType;
    }

    public LocalDate getTransactionDate() {
        return this.transactionDate;
    }

    public double getAmount() {
        return this.amount;
    }

    public double getFee() {
        return this.fee;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public long getReceiverAccountNumber() {
        return this.receiverAccountNumber;
    }

    public String getReceiverIFSC() {
        return this.receiverIFSC;
    }

    public long getTransactionID() {
        return this.transactionID;
    }
}
