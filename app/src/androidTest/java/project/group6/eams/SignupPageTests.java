package project.group6.eams;

import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.security.SecureRandom;
import java.util.Base64;

import project.group6.eams.activities.SignUpPage;
import project.group6.eams.execptions.ExistingUserException;
import project.group6.eams.execptions.PendingUserException;
import project.group6.eams.execptions.RejectedUserException;
import project.group6.eams.users.Attendee;
import project.group6.eams.users.User;
import project.group6.eams.utils.RegistrationManager;;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SignupPageTests {
    @Rule
    public ActivityScenarioRule<SignUpPage> activityRule = new ActivityScenarioRule<>(SignUpPage.class);
    /*
    @Test
    public void testInputsShowMessage(){
        ViewInteraction firstName = Espresso.onView(withId(R.id.enterFirstName_signup_page));
        firstName.perform(typeText("12345"));
        Espresso.onView(withId(R.id.submitButton_signup_page)).perform(click());
        Espresso.onIdle();
        firstName.check(matches(hasErrorText("First Name must be alphabetic characters only.")));
    }*/

    @Test
    public void testRegisterUser(){
        String email = generateRandomString(16);
        email += "@test.com";
        Attendee toAdd = new Attendee(email,email,"Test","Test","123 456 7890", "123 Test Test");
        RegistrationManager rm = new RegistrationManager("Users");
        rm.addUser(toAdd, new RegistrationManager.RegistrationCallback(){
            @Override
            public void onSuccess() {};
            @Override
            public void onSuccess (User type) {
                assertTrue("Added user",true);
            }
            @Override
            public void onError (Exception e) {
                if (e instanceof RejectedUserException) {
                    assertTrue("RejectedUser",true);
                }
                else if (e instanceof PendingUserException) {
                    assertTrue("PendingUser",true);
                }
                else if (e instanceof ExistingUserException) {
                    assertTrue("ExistingUser",true);
                }
                else {
                   fail("Failed to add user or get reason for failure");
                }
            }
        });
    }
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