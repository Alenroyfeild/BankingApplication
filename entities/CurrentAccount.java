package entities;

public class CurrentAccount extends Account {
    private double transactionfee=1.75;
    
    public CurrentAccount(long mobileNo,long accountNumber,long CIFNumber,String accountType,String balanceType,double accountBalance,Boolean status){
        super(mobileNo,accountNumber,CIFNumber,accountType,balanceType,accountBalance,status);
    }
    public double getTransactionFee(){
        return this.transactionfee;
    }
    
}
