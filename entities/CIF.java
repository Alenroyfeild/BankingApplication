package entities;

import java.time.LocalDate;

public class CIF {
    private long cifNumber;
    private String customerFullname;
    private LocalDate dateofBirth;
    private long aadharNumber;
    private int age;
    private long mobileNo;
    private String displayName;
    private String[] address = new String[5];

    public CIF(long cifNumber, String username, String displayName, long mobileNo, long aadharNumber, String[] address,
            int age) {
        this.cifNumber = cifNumber;
        this.aadharNumber = aadharNumber;
        this.customerFullname = username;
        this.displayName = displayName;
        this.mobileNo = mobileNo;
        this.address = address;
        this.age = age;
    }

    public void setAddress(String[] address) {
        this.address = address;
    }

    public String[] getAddress() {
        return this.address;
    }

    public long getMobileNo() {
        return this.mobileNo;
    }

    public void setMobileNumber(long mobileNumber) {
        this.mobileNo = mobileNumber;
    }

    public long getCIFno() {
        return this.cifNumber;
    }

    public String getCustomerFullname() {
        return this.customerFullname;
    }

    public String getUsername() {
        return this.displayName;
    }

    public void setUsername(String username) {
        this.displayName = username;
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
