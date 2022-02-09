package services;

import entities.Account;
import entities.Bank;
import entities.CIF;

public class userProfile {
    BankAdminService bas = new BankAdminService();
    Bank ba = Bank.getInstance();

    // this function is used to display Passbook details
    public Object[] displayPassbook(long mobileNo, long accNo) {
        CIF cifList;
        cifList = bas.getUserCIFDetails(mobileNo);
        Account accList;
        accList = bas.getUserAccountDetails(accNo);
        Object a[] = new Object[2];
        a[0] = cifList;
        a[1] = accList;
        return a;
    }

    // this funtion is used to update pincode
    public Boolean updateAddress(long mobileNo, String[] address) {
        CIF cif = utils.searchCIF(mobileNo);
        if (cif != null) {
            cif.setAddress(address);;
            return true;
        }
        return false;
    }

    //this function is used to update username
    public Boolean updateUsername(long mobileNo, String name) {
        CIF cif = utils.searchCIF(mobileNo);
        if (cif != null) {
            cif.setUsername(name);
            return true;
        }
        return false;
    }

}
