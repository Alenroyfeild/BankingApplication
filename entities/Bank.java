package entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Bank {
    private String branchName;
    private String IFSC;
    private String branchAddress;
    private String password="l";
    private static Bank ba=null;
    public ArrayList<UserLogin> userLogins=new ArrayList<>();
    public ArrayList<CIF> cifList=new ArrayList<>();
    public ArrayList<Account> accountsList=new ArrayList<>();
    public Map<Long,ArrayList<Transactions>> transactions=new HashMap<>();
    public Map<Long,ArrayList<ChequeBook>> chequesList=new HashMap<>();
    public Map<Long,ArrayList<Loan>> loanList=new HashMap<>();
    public Map<Long,ArrayList<FixedDeposit>> FDList=new HashMap<>();
    public Map<Long,ArrayList<RecurringDeposit>> RDList=new HashMap<>();
    public Map<Long,CreditCard> CCList=new HashMap<>();
    private  Bank(){
        this.branchName="NorthPlaza";
        this.IFSC="ZOHO001";
        this.branchAddress="Chennai";
    }
    public static Bank getInstance(){
        if(ba==null)
        ba=new Bank();
        return ba;
    }
    public Boolean validatePassword(String password){
        if(this.password.equals(password)){
            return true;
        }
        return false;
    }
    public String getBranchName(){
        return this.branchName;
    }
    public String getBranchAddress(){
        return this.branchAddress;
    }
    public String getIFSC(){
        return this.IFSC;
    }
   

}
