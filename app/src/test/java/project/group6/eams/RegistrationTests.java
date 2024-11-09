package project.group6.eams;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.security.SecureRandom;
import java.util.Base64;

import project.group6.eams.utils.Attendee;
import project.group6.eams.utils.RegistrationManager;
import project.group6.eams.utils.User;

//@RunWith(RobolectricTestRunner.class)
public class RegistrationTests {
//    @Test
//    public void testAddUser(){
//        String email = generateRandomString(16);
//        email += "@test.com";
//        Attendee toAdd = new Attendee(email,email,"Test","Test","123 456 7890", "123 Test Test");
//        RegistrationManager rm = new RegistrationManager("Users");
//
//    }

    /**
     * Generates a random string of a given length.
     * <a href="https://stackoverflow.com/questions/41107/how-to-generate-a-random-alpha-numeric-string">...</a>
     *
     * @param byteLength Length of the string to be generated.
     * @return Random String
     */
    public static String generateRandomString(int byteLength) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[byteLength];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes); //base64 encoding
    }


}

