package project.group6.eams.utils;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.Date;

public class InputUtils {

    /**
     * Checks if a string matches an email address pattern and isn't empty
     * Pattern code found at:
     * <a href="https://stackoverflow.com/questions/12947620/email-address-validation-in-android-on-edittext">...</a>
     *
     * @param email string to check
     * @return true if email is valid, false otherwise
     */
    public static boolean isValidEmail (String email) {
        if (email == null) {return false;}
        return (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    /**
     * Checks if a string matches an phone number pattern and isn't empty
     * Pattern code found at:
     * <a href="https://stackoverflow.com/questions/6358380/phone-number-validation-android">...</a>
     *
     * @param phoneNumber string to check
     * @return true if phone number is valid, false otherwise
     */
    public static boolean isValidPhoneNumber (String phoneNumber) {
        if (phoneNumber == null) {return false;}
        return (!phoneNumber.isEmpty() && phoneNumber.length() >= 7 && Patterns.PHONE.matcher(phoneNumber).matches());
    }

    /**
     * Checks to see if chosen password and reentered password are the same
     *
     * @param passwordEntry1 initial password
     * @param passwordEntry2 second entry of password
     * @return true if the two passwords are the same, false otherwise.
     */
    public static boolean verifyPassword (String passwordEntry1, String passwordEntry2) {
        if (passwordEntry1 == null || passwordEntry2 == null) {return false;}
        return (!passwordEntry1.isEmpty() && !passwordEntry2.isEmpty() && passwordEntry2.equals(passwordEntry1));
    }

    /**
     * Checks to see if name inputs are alphabetic only.
     *
     * @param name inputted name
     * @return true if it is a valid name with alphabetic characters, false otherwise.
     */
    public static boolean isValidName (String name) {
        if (name == null) {
            return false;
        } else {
            String nameW = name.replaceAll(" ", "");
            if (nameW.length() <= 1) {
                return false;
            }
        }
        for (int i = 0; i < name.length(); i++) {
            if (!Character.isLetter(name.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Concatenates street, city, province, and postalCode for easy storage
     *
     * @param street, city, province, postalCode
     * @return String address
     */
    public static String addressCreator (String street, String city, String province,
                                         String postalCode) {
        return street + ", " + city + ", " + province + ", " + postalCode;
    }

    /**
     * Checks if a string matches the street pattern and isn't empty
     *
     * @param street must be input: StreetNumber and StreetName
     * @param street string to check
     * @return true if address is valid, false otherwise
     */
    public static boolean isValidStreet (String street) {
        if (street == null){return false;}
        if (!street.isEmpty()) {
            String components[] = street.split(" ");
            if (components.length < 2 || components.length > 3) {
                return false;
            }
            for (int j = 0; j < components.length; j++) {
                components[j] = components[j].replaceAll("\\s", "");
            }
            for (int i = 0; i < components[0].length(); i++) {
                if (!Character.isDigit(components[0].charAt(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Checks if a string matches the postal code pattern and isn't empty
     *
     * @param postalCode must be input: PostalCode(canadian)
     * @param postalCode string to check
     * @return true if address is valid, false otherwise
     */
    public static boolean isValidPostalCode (String postalCode) {
        if (postalCode == null) {
            return false;
        }
        postalCode = postalCode.toUpperCase();
        if (!postalCode.isEmpty()) {
            postalCode = postalCode.replaceAll("\\s", "");

            if (postalCode.length() != 6) {
                return false;
            }

            for (int i = 0; i < postalCode.length(); i++) {
                if (!Character.isLetter(postalCode.charAt(i))) {
                    return false;
                }
                i++;
            }

            for (int j = 0; j < postalCode.length(); j++) {
                if (!Character.isDigit(postalCode.charAt(j + 1))) {
                    return false;
                }
                j++;
            }

            return true;
        }
        return false;
    }

    /**
     * Checks if a string matches the password pattern and isn't empty
     * At least one upper case letter
     * At least one lower case letter
     * At least one number
     * At least one special character. Eg: @,!,?
     * At least 8 characters
     *
     * @param password string to check
     * @return empty string if password is valid, string with length >0 otherwise
     */
    public static String passwordChecker (String password) {
        if (password == null) {
            return "Password must contain: \n" +
                    "At least one upper case letter \n" +
                    "At least one lower case letter" +
                    "\nAt least one number \n" +
                    "At least one special character. Eg: @,!,? \n" +
                    "At least 8 characters";
        }
        if (!password.isEmpty()) {
            int numOfCapitals = 0;
            int numOfLowerCase = 0;
            int numOfSpecialChar = 0;
            int numOfNum = 0;
            String message = "Password must contain: ";
            for (int i = 0; i < password.length(); i++) {
                char character = password.charAt(i);
                if (Character.isUpperCase(character)) {
                    numOfCapitals++;
                } else if (Character.isLowerCase(character)) {
                    numOfLowerCase++;
                } else if (Character.isDigit(character)) {
                    numOfNum++;
                } else {
                    numOfSpecialChar++;
                }
            }
            if (numOfCapitals < 1) {
                message = message + "\nAt least one upper case letter";
            }
            if (numOfLowerCase < 1) {
                message = message + "\nAt least one lower case letter";
            }
            if (numOfNum < 1) {
                message = message + "\nAt least one number";
            }
            if (numOfSpecialChar < 1) {
                message = message + "\nAt least one special character. Eg: @,!,?";
            }
            if (password.length() < 8) {
                message = message + "\nAt least 8 characters";
            }
            if (message.equals("Password must contain: ")) {
                message = "";
            }
            return message;
        }
        return "Password must contain: \n" +
                "At least one upper case letter \n" +
                "At least one lower case letter" +
                "\nAt least one number \n" +
                "At least one special character. Eg: @,!,? \n" +
                "At least 8 characters";
    }

    /**
     * Method that returns a boolean that is true if date has already passed in comparison to current date,
     * false if it is same date OR after current date.
     * @param date is the Date that is to be compared to current date
     * @return boolean value htat represents if the date has passed or not.
     */
    public static boolean dateHasPassed(Date date) {
        Date currentDate = new Date(); // defaults to current date
        return date.before(currentDate);
    }

    /**
     * Method used to check whether event times are valid
     * startTime must be before endTime.
     * @param startDate
     * @param endDate
     * @return
     */
    public static boolean isValidEventRuntime(Date startDate, Date endDate) {
        return startDate.before(endDate);
    }

}
