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
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    /**
     * Checks if a string matches the password pattern and isn't empty
     * @param password string to check
     * @return true if password is valid, false otherwise
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
