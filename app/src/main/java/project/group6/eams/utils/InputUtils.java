package project.group6.eams.utils;

import android.text.TextUtils;
import android.util.Patterns;

public class InputUtils {

    /**
     * Checks if a string matches an email address pattern and isn't empty
     * Pattern code found at:
     * <a href="https://stackoverflow.com/questions/12947620/email-address-validation-in-android-on-edittext">...</a>
     * @param email string to check
     * @return true if email is valid, false otherwise
     */
    public static boolean isValidEmail(String email){
        return (email.length()>0 && !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    /**
     * Checks if a string matches an phone number pattern and isn't empty
     * Pattern code found at:
     * <a href="https://stackoverflow.com/questions/6358380/phone-number-validation-android">...</a>
     * @param phoneNumber string to check
     * @return true if phone number is valid, false otherwise
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        return (!TextUtils.isEmpty(phoneNumber) && Patterns.PHONE.matcher(phoneNumber).matches());
    }

    /**
     * Checks to see if chosen password and reentered password are the same
     * @param passwordEntry1 initial password
     * @param passwordEntry2 second entry of password
     * @return true if the two passwords are the same, false otherwise.
     */
    public static boolean verifyPassword(String passwordEntry1, String passwordEntry2) {
        return passwordEntry2.equals(passwordEntry1);
    }

    /**
     * Checks to see if name inputs are alphabetic only.
     *
     * @param name inputted name
     * @return true if it is a valid name with alphabetic characters, false otherwise.
     */
    public static boolean isValidName(String name ) {
        if (name == null || name.length() <= 1) {
            return false;
        }

        for (int i = 0; i<name.length(); i++) {
            if (!Character.isLetter(name.charAt(i))){
                return false;
            }
        }

        return true;
    }
    /**
     * Checks if a string matches the password pattern and isn't empty
     * @param password string to check
     * @return empty string if password is valid, string with length >0 otherwise
     */
    public static String passwordChecker(String password){
        if (!TextUtils.isEmpty(password)){
            int numOfCapitals = 0;
            int numOfLowerCase = 0;
            int numOfSpecialChar = 0;
            int numOfNum = 0;
            String message = "Password must contain: ";
            for (int i = 0; i<password.length(); i++){
                char character = password.charAt(i);
                if (Character.isUpperCase(character)){
                    numOfCapitals++;
                }
                else if (Character.isLowerCase(character)){
                    numOfLowerCase++;
                }
                else if (Character.isDigit(character)){
                    numOfNum++;
                }
                else{
                    numOfSpecialChar++;
                }
            }
            if(numOfCapitals < 1) {
                message = message + "\nAt least one upper case letter";
            }
            if(numOfLowerCase < 1) {
                message = message + "\nAt least one lower case letter";
            }
            if(numOfNum < 1) {
                message = message + "\nAt least one number";
            }
            if(numOfSpecialChar < 1) {
                message = message + "\nAt least one special character. Eg: @,!,?";
            }
            if(password.length() < 8) {
                message = message + "\nAt least 8 characters";
            }
            if(message.equals("Password must contain: ")) {
                message = "";
            }
            return message;
        }
        return "Password must contain: \nAt least one upper case letter \nAt least one lower case letter" +
                "\nAt least one number \nAt least one special character. Eg: @,!,? \nAt least 8 characters";
    }

}
