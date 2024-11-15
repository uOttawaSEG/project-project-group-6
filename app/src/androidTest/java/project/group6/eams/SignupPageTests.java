package project.group6.eams;

import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.*;
import org.junit.runner.RunWith;

import project.group6.eams.activities.SignUpPage;;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class SignupPageTests {
    @Rule
    public ActivityScenarioRule<SignUpPage> activityRule = new ActivityScenarioRule<>(SignUpPage.class);
    @Test
    public void testInputsShowMessage(){
        ViewInteraction firstName = Espresso.onView(withId(R.id.enterFirstName));
        firstName.perform(typeText("12345"));
        Espresso.onView(withId(R.id.submitButton)).perform(click());
        firstName.check(matches(hasErrorText("First Name must be alphabetic characters only.")));
    }
}