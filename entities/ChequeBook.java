package entities;

import java.time.LocalDate;


public class ChequeBook {
    private long accountNumber;
    private String chequeStatus;
    private long chequeNumber;
    private LocalDate chequeIssuedDate;
    private double chequeAmount;
    public ChequeBook(long accountNumber,long chequeNumber,String chequeStatus,LocalDate chequeIssueDate,double chequeAmount){
        this.accountNumber=accountNumber;
        this.chequeNumber=chequeNumber;
        this.chequeStatus=chequeStatus;
        this.chequeIssuedDate=chequeIssueDate;
        this.chequeAmount=chequeAmount;
    }
    public LocalDate getChequeIssuedDate(){
        return this.chequeIssuedDate;
    }
    public double getChequeAmount(){
        return this.chequeAmount;
    }
    public String getChequeStatus(){
        return this.chequeStatus;
    }
    public long accountNumber(){
        return this.accountNumber;
    }
    public void setChequeStatus(String status){
        this.chequeStatus=status;
    }
    public long getChequeNo() {
        return this.chequeNumber;
    }
}
