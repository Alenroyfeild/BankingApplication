package entities;

import java.time.LocalDate;

public class CIF {
    private long cifNumber;
    private String username;
    private LocalDate dateofBirth;
    private long aadharNumber;
    private int age;
    private long mobileNo;
    private String[] address=new String[5];
    public CIF(long cifNumber, String username,long mobileNo, long aadharNumber, String[] address, int age) {
        this.cifNumber = cifNumber;
        this.aadharNumber = aadharNumber;
        this.username = username;
        this.mobileNo=mobileNo;
        this.address=address;
        this.age = age;
    }

    public void setAddress(String[] address){
        this.address=address;
    }
    public String[] getAddress(){
        return this.address;
    }
    public long getMobileNo(){
        return this.mobileNo;
    }
    public void setMobileNumber(long mobileNumber){
        this.mobileNo=mobileNumber;
    }
    public long getCIFno() {
        return this.cifNumber;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username){
        this.username=username;
    }
    public LocalDate getDateofBirth() {
        return this.dateofBirth;
    }

    public long getAadharNumber() {
        return this.aadharNumber;
    }

    public int getAge() {
        return this.age;
    }
}
