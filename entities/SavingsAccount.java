package entities;

public class SavingsAccount extends Account{
    private double interestRate;
    public SavingsAccount(long mobileNo,long accountNumber,long CIFNumber,String accountType,String balanceType,double accountBalance,Boolean status){
        super(mobileNo,accountNumber,CIFNumber,accountType,balanceType,accountBalance,status);
        this.interestRate=3.75;
    }
    
    public double getInterestRate(){
        return this.interestRate;
    }
    public void setInterestRate(double interestRate){
        this.interestRate=interestRate;
    }
    
}
