package entities;

import java.time.LocalDate;

public class FixedDeposit {
    private long accountNumer;
    private long FDAccNumber;
    private LocalDate depositDate;
    private double interestRate,FDAmount,FDInterestAmount;
    private int FDMonths;
    private Boolean status=false;
    private long mobileNo;
    public FixedDeposit(long mobileNo,long accountNumber,long FDAccNumber,LocalDate depositdate,double interestRate,double FDAmount,int FDMonths,Boolean status){
        this.mobileNo=mobileNo;
        this.accountNumer=accountNumber;
        this.FDAccNumber=FDAccNumber;
        this.depositDate=depositdate;
        this.interestRate=interestRate;
        this.FDAmount=FDAmount;
        this.FDMonths=FDMonths;
        this.status=status;
    }
    public long getMobileNo(){
        return this.mobileNo;
    }
    public void setFDInterestAmount(double amount){
        this.FDInterestAmount=amount;
    }
    public double getFDInterestAmount(){
        return this.FDInterestAmount;
    }
    public long getAccountNumber(){
        return this.accountNumer;
    }
    public long getFDAccNo(){
        return this.FDAccNumber;
    }
    public LocalDate getFDDepositDate(){
        return this.depositDate;
    }
    public double getFDinterestRate(){
        return this.interestRate;
    }
    public double getFDAmount(){
        return this.FDAmount;
    }
    public int getFDMonths(){
        return this.FDMonths;
    }
    public Boolean getStatus(){
        return this.status;
    }
    public void setStatus(Boolean status){
        this.status=status;
    }
}
