package entities;

import java.time.LocalDate;

public class CIF {
    private long cifNumber;
    private String username;
    private LocalDate dateofBirth;
    private long aadharNumber;
    private int pincode;
    private int age;
    private long mobileNo;

    public CIF(long cifNumber, String username,long mobileNo, long aadharNumber, int pincode, int age) {
        this.cifNumber = cifNumber;
        this.aadharNumber = aadharNumber;
        this.username = username;
        this.mobileNo=mobileNo;
        this.pincode = pincode;
        this.age = age;
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

    public int getPincode() {
        return this.pincode;
    }

    public void setPincode(int pincode) {
        this.pincode = pincode;
    }

    public int getAge() {
        return this.age;
    }
}
