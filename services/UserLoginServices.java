package services;

import entities.Bank;
import entities.CIF;
import entities.UserLogin;

public class UserLoginServices {
    Bank ba = Bank.getInstance();

    //this function is used to validate mobile no
    public UserLogin validateMoblieNo(long mobileNo) {
        return (UserLogin) utils.search(ba.userLogins, value -> ((UserLogin) value).getMobileNumber() == mobileNo);
    }

    //this function function is used to validate password
    public Boolean validatePassword(UserLogin acc,String password){
        if(acc.validatePassword(password))
        return true;
        else
        return false;
    }
    // this function is used to update mobile number
    public Boolean updateMobileNo(long mobileno,long newMobileNo) {
        UserLogin ul=(UserLogin) utils.search(ba.userLogins, value->((UserLogin) value).getMobileNumber()==mobileno);
        if(ul!=null){
            ul.setMobileNumber(newMobileNo);
            CIF cif=(CIF) utils.search(ba.cifList,value->((CIF) value).getMobileNo()==mobileno);
            cif.setMobileNumber(newMobileNo);
            return true;
        }
        return false;
    }
     // this function is to change password
     public Boolean setPassword(long mobileno, String password) {
        UserLogin acc=(UserLogin)utils.search(ba.userLogins, value->((UserLogin) value).getMobileNumber()==mobileno);
        if(acc!=null){
            acc.changePassword(password);
            return true;
        }
        return false;
    }
}
