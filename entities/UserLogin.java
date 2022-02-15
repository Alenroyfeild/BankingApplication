package entities;

public class UserLogin {
    private long mobileNumber;
    private String password;
    private long cifNumber = 0;

    public UserLogin(long mobileNumber, String password) {
        this.mobileNumber = mobileNumber;
        this.password = password;
    }

    public void setCIFno(long cifNumber) {
        this.cifNumber = cifNumber;
    }

    public long getCIFno() {
        return this.cifNumber;
    }

    public long getMobileNumber() {
        return this.mobileNumber;
    }

    public void setMobileNumber(long mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    private void setPassword(String password) {
        this.password = password;
    }

    public Boolean validatePassword(String password) {
        return this.password.equals(password);
    }

    public String getPassword(long mobileNo) {
        if (this.mobileNumber == mobileNo)
            return this.password;
        else
            return null;
    }

    public void changePassword(String password) {
        setPassword(password);
    }
}
