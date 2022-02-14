package UserInterface;

import java.io.Console;
import java.util.Arrays;
import java.util.Scanner;
import entities.Bank;
import entities.CIF;
import services.BankAdminService;
import services.userProfile;
import services.utils;

public class UserProfileUI {
    static Scanner sc = new Scanner(System.in);
    static userProfile up = new userProfile();
    static Bank ba = Bank.getInstance();

    // this function is used to display User profile menu
    public static int userProfileMenuPage(long mobileNo) {
        int choice = 0;
        do {
            System.out.println(
                    "\n\n\n--------------------------------------------------------------------------------------------");
            System.out.println("\n  --  Welcome to User Profile Page  --");
            System.out.println(" 1.View Profile");
            System.out.println(" 2.Update PreferredName");
            System.out.println(" 3.Update Password");
            System.out.println(" 4.Update Address");
            System.out.println(" 5.Back to Home");
            try {
                System.out.print("Enter choice : ");
                choice = Integer.parseInt(sc.next());
            } catch (NumberFormatException e) {
                System.out.println("You have entered wrong choice.\nPlease again Enter : ");
                choice = 0;
            }
            if (choice == 1) {
                System.out.println("\033[H\033[2J");
                displayProfileUI(mobileNo);
            } else if (choice == 2) {
                doUpdateName(mobileNo);
            } else if (choice == 3) {
                doUpdatePassword(mobileNo);
            } else if (choice == 4) {
                doUpdateAddress(mobileNo);
            } else {
                System.out.println("Back to Home page");
            }
        } while (choice < 1 || choice > 5 || choice != 5);
        return choice;
    }

    static BankAdminService bas = new BankAdminService();

    // this function is used to display passbook details
    private static void displayProfileUI(long mobileNo) {
        CIF cifList = bas.getUserCIFDetails(mobileNo);
        System.out.println("\n\n-----------------------------------------------------------");
        System.out.println("\n  -- User Profile Info --\n");
        System.out.println("Customer Full Name   : " + cifList.getCustomerFullname());
        System.out.println("Preferred name       : " + cifList.getUsername());
        System.out.println("Age                  : " + cifList.getAge());
        System.out.println("Aadhar Number        : " + cifList.getAadharNumber());
        System.out.println("Mobile number        : " + cifList.getMobileNo());
        String[] address = cifList.getAddress();
        System.out
                .println("Address              : " + address[0] + "," + address[1] + "," + address[2] + "," + address[3]
                        + "-" + address[4] + ".");
    }

    // this function is used to display Profile update menu and its methods
    private static void doUpdateAddress(long mobileno) {
        System.out.println("\nEnter New  Address Details");
        String[] address = UtilsUI.getAddress();
        Boolean bool = up.updateAddress(mobileno, address);
        if (bool)
            System.out.println("\nSuccessfully Pincode updated...\n");
        else
            System.out.println("\nPincode is not updated\n");
    }

    private static void doUpdatePassword(long mobileno) {
        boolean noMatch;
        do {
            Console c = System.console();
            char[] pass = c.readPassword("\nEnter the New password    : ");
            char[] pass1 = c.readPassword("Confirm the New password  : ");
            noMatch = !Arrays.equals(pass, pass1);
            if (noMatch) {
                System.out.format("Passwords don't match. Try again.%n");
            } else {
                String password = new String(pass);
                Boolean bool = utils.setPassword(mobileno, password);
                if (bool)
                    System.out.println("\nYour Password Updated Successfully.....\n");
                else
                    System.out.println("\nPassword is not Updated...\n");
            }
        } while (noMatch);
    }

    private static void doUpdateName(long mobileno) {
        System.out.print("\nEnter New Username : ");
        String name = sc.next();
        Boolean bool = up.updateUsername(mobileno, name);
        if (bool)
            System.out.println("\nSuccessfully Customername updated...\n");
        else
            System.out.println("\nCustomername not updated\n");

    }
}
