package entities;

public class CurrentAccount extends Account {
    private double transactionfee=1.75;
    
    public CurrentAccount(long mobileNo,long accountNumber,long CIFNumber,String accountType,String balanceType,double accountBalance){
        super(mobileNo,accountNumber,CIFNumber,accountType,balanceType,accountBalance);
    }
    public double getTransactionFee(){
        return this.transactionfee;
    }
    
}
