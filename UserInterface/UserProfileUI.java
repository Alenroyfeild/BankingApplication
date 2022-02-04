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
            System.out.println("\n\n\n--------------------------------------------------------------------------------------------");
            System.out.println("\n  --  Welcome to User Profile Page  --");
            System.out.println(" 1.View Profile");
            System.out.println(" 2.Profile Update");
            System.out.println(" 3.Exit");
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
                doUserProfileUpdate(mobileNo);
            } else {
                System.out.println("Back to Home page");
            }
        } while (choice < 1 || choice > 3 || choice != 3);
        return choice;
    }

    static BankAdminService bas = new BankAdminService();

    // this function is used to display passbook details
    private static void displayProfileUI(long mobileNo) {
        CIF cifList = bas.getUserCIFDetails(mobileNo);
        System.out.println("\n\n-----------------------------------------------------------");
        System.out.println("\n  -- User Profile Info --");
        System.out.println("User Name          : " + cifList.getUsername());
        System.out.println("Age                : " + cifList.getAge());
        System.out.println("Aadhar Number      : " + cifList.getAadharNumber());
        System.out.println("Mobile number      : " + cifList.getMobileNo());
        System.out.println("Pincode            : " + cifList.getPincode());
    }

    // this function is used to display Profile update menu and its methods
    private static void doUserProfileUpdate(long mobileno) {
        int y = 0;
        do {
            System.out.println("\n\n-----------------------------------------------------------");
            System.out.println("\n    ----   Profile Update page   ----");
            System.out.println(" 1.UserName");
            System.out.println(" 2.Password");
            System.out.println(" 3.pincode");
            System.out.println(" 4.Quit");
            try {
                System.out.print("Enter choice : ");
                y = Integer.parseInt(sc.next());
            } catch (NumberFormatException e) {
                System.out.println(y + " is not a valid integer number");
            }
            if (y == 1) {
                System.out.print("\nEnter New Username : ");
                String name = sc.next();
                Boolean bool = up.updateUsername(mobileno, name);
                if (bool)
                    System.out.println("\nSuccessfully Username updated...\n");
                else
                    System.out.println("\nUsername not updated\n");
            } else if (y == 2) {
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
            } else if (y == 3) {
                System.out.println("\nUpdate Pincode ");
                int pincode = UtilsUI.getPincode();
                Boolean bool = up.updatePincode(mobileno, pincode);
                if (bool)
                    System.out.println("\nSuccessfully Pincode updated...\n");
                else
                    System.out.println("\nPincode is not updated\n");
            }
        } while (y < 1 || y > 4 || y != 4);
    }
}
