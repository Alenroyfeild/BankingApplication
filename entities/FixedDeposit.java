package entities;

import java.time.LocalDate;

public class FixedDeposit {
    private long FDAccNumber;
    private LocalDate depositDate;
    private double interestRate, FDAmount, FDInterestAmount;
    private int FDMonths;
    private Boolean status = false;
    private long mobileNo;
    private long nomineeAadhar;

    public FixedDeposit(long nomineeAadhar, long mobileNo, long FDAccNumber, LocalDate depositdate,
            double interestRate, double FDAmount, int FDMonths, Boolean status) {
        this.nomineeAadhar = nomineeAadhar;
        this.mobileNo = mobileNo;
        this.FDAccNumber = FDAccNumber;
        this.depositDate = depositdate;
        this.interestRate = interestRate;
        this.FDAmount = FDAmount;
        this.FDMonths = FDMonths;
        this.status = status;
    }

    public long getNomineeAadhar() {
        return this.nomineeAadhar;
    }

    public long getMobileNo() {
        return this.mobileNo;
    }

    public void setFDInterestAmount(double amount) {
        this.FDInterestAmount = amount;
    }

    public double getFDInterestAmount() {
        return this.FDInterestAmount;
    }

    public long getFDAccNo() {
        return this.FDAccNumber;
    }

    public LocalDate getFDDepositDate() {
        return this.depositDate;
    }

    public double getFDinterestRate() {
        return this.interestRate;
    }

    public double getFDAmount() {
        return this.FDAmount;
    }

    public int getFDMonths() {
        return this.FDMonths;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
