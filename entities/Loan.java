package entities;

import java.time.LocalDate;

public class Loan {
    private long loanAccountNumber;
    private LocalDate paidDate,loanDate;
    private double interestRate,loanAmount;
    private Boolean status=false;
    private int monthsRemain=0;
    private final int DAYS=30;
    private int noofLoanMonths;
    private long accountNumber;
    private long mobileNo;

    private String loanType;
    public Loan(long mobileNo,long accountNumber,long loanAccountNumber,String loanType, LocalDate loanDate, double loanAmt, double interestRate, int noofLoanMonths,
    int monthsRemain,
    Boolean act){
        this.mobileNo=mobileNo;
        this.accountNumber=accountNumber;
        this.loanAccountNumber = loanAccountNumber;
        this.loanType=loanType;
        this.loanDate = loanDate;
        this.paidDate = loanDate;
        this.loanAmount = loanAmt;
        this.interestRate = interestRate;
        this.noofLoanMonths = noofLoanMonths;
        this.monthsRemain = monthsRemain;
        this.status = act;
        
    }
    public long getMobileNo(){
        return this.mobileNo;
    }
    public long getAccNo(){
        return this.accountNumber;
    }
    public String getLoanType(){
        return this.loanType;
    }
    public int getnoofMonths(){
        return this.noofLoanMonths;
    }
    public int getDays(){
        return this.DAYS;
    }
    public void setMonthsRemain(){
        this.monthsRemain-=1;
    }
    public int getMonthsRemain(){
        return this.monthsRemain;
    }
    public double getLoanAmount(){
        return this.loanAmount;
    }
    public void setLoanAmount(double loanAmount){
        this.loanAmount=loanAmount;
    }
    public void setAccountStatus(Boolean status){
        this.status=status;
    }
    public void setMonthsZero(){
        this.monthsRemain=0;
    }
    public Boolean getAccountStatus(){
        return this.status;
    }
    public void setInterestRate(double interestRate){
        this.interestRate=interestRate;
    }
    public double getInterestRate(){
        return this.interestRate;
    }
    public long getLoanAccNo(){
        return this.loanAccountNumber;
    }
    public LocalDate getPaidDate(){
        return this.paidDate;
    }
    public void setPaidDate(LocalDate paidDate){
        this.paidDate=paidDate;
    }
    public LocalDate getLoanDate(){
        return this.loanDate;
    }

}
