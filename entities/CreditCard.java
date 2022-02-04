package entities;

import java.time.LocalDate;

public class CreditCard {
    private long accountNumber;
    private long cardNo;
    private int cardPin;
    private double balanceLimit;
    private LocalDate cardDate;
    private String cardStatus;
    private double usedBalance = 0;
    private LocalDate firstUsedDate;
    private double interestRate = 3.5;
    private LocalDate cardExpiryDate;
    private int cvvCode;

    public CreditCard(long accountNumber, long cardNo, int cardPin, double balanceLimit, LocalDate cardExpiryDate,int cvvCode,
            String cardStatus, boolean b) {
        this.accountNumber = accountNumber;
        this.cardNo = cardNo;
        this.cardPin = cardPin;
        this.balanceLimit = balanceLimit;
        this.cardExpiryDate = cardExpiryDate;
        this.cardStatus = cardStatus;
        this.cardDate=LocalDate.now();
        this.cvvCode=cvvCode;
    }

    public Boolean validateCVV(int CVV){
        if(this.cvvCode==CVV)
        return true;
        else
        return false;
    }
    public int getCvvCode(){
        return this.cvvCode;
    }
    public LocalDate getCardExpiryDate(){
        return this.cardExpiryDate;
    }
    public void setBalanceLimit(double d) {
        this.balanceLimit += d;
    }

    public double getInterestRate() {
        return this.interestRate;
    }

    public void setFirstUsedDate(LocalDate date) {
        this.firstUsedDate = date;
    }

    public LocalDate getFirstUsedDate() {
        if(this.usedBalance==0)
        return LocalDate.now();
        else
        return this.firstUsedDate;
    }

    public Boolean validateBalance(double balance) {
        if (this.usedBalance + balance <= balanceLimit)
            return true;
        else
            return false;
    }

    public void setUsedBalance(double balance) {
        this.usedBalance += balance;
    }

    public void setUsedBalanceZero(){
        this.usedBalance=0;
    }
    public void setCardStatus(String status) {
        this.cardStatus = status;
    }

    public double getUsedBalance() {
        return this.usedBalance;
    }

    public String getCardStatus() {
        return this.cardStatus;
    }

    public LocalDate getCardDate() {
        return this.cardDate;
    }

    public double getBalanceLimit() {
        return this.balanceLimit;
    }

    public long getAccNo() {
        return this.accountNumber;
    }

    public long getCardNo() {
        return this.cardNo;
    }

    public void setPin(int pin){
        this.cardPin=pin;
    }
    public Boolean validatePin(int pin) {
        if (this.cardPin == pin)
            return true;
        else
            return false;
    }
}
