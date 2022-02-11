package entities;

import java.time.LocalDate;

public class RecurringDeposit {
    private long rdID;
    private final double rdAmount;
    private double totalRDAmount;
    private long accountNumber;
    private double interestRate;
    private long nomineeAadhar;
    private LocalDate rdOpenDate;
    private int rdTenure;
    private int rdRemainingMonths;
    private Boolean rdStatus;
    private long mobileNo;

    public RecurringDeposit(long mobileNo,long accountNumber,long rdId, double rdAmount, double interestRate, long nomineeAadharNo, LocalDate rdOpenDate,
            int rdTenure,int rdRemainingMonths,Boolean rdStatus) {
                this.mobileNo=mobileNo;
                this.accountNumber=accountNumber;
        this.rdID = rdId;
        this.rdAmount = rdAmount;
        this.interestRate = interestRate;
        this.nomineeAadhar = nomineeAadharNo;
        this.rdOpenDate = rdOpenDate;
        this.rdTenure = rdTenure;
        this.rdRemainingMonths = rdRemainingMonths;
        this.rdStatus=rdStatus;
        this.totalRDAmount=0;
    }

    public long getMobileNo(){
        return this.mobileNo;
    }
    public double getTotalRDAmount(){
        return this.totalRDAmount;
    }
    public void setTotalAmount(){
        this.totalRDAmount+=rdAmount;
    }
    public long getAccNo(){
        return this.accountNumber;
    }
    public long getRDID(){
        return this.rdID;
    }
    public double getRDAmount(){
        return this.rdAmount;
    }
    public double getRDInterestRate(){
        return this.interestRate;
    }
    public long getNomineeAadhar(){
        return this.nomineeAadhar;
    }
    public LocalDate getRDOpenDate(){
        return this.rdOpenDate;
    }
    public int getRDTenure(){
        return this.rdTenure;
    }
    public int getRDRemainingMonths(){
        return this.rdRemainingMonths;
    }
    public void setRDremainingMonths(){
        this.rdRemainingMonths-=1;
    }
    public void setRDStatus(Boolean bool){
        this.rdStatus=bool;
    }
    public Boolean getRDstatus(){
        return this.rdStatus;
    }
}
