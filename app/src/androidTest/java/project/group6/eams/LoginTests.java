package project.group6.eams;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Context;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import project.group6.eams.activities.LoginPage;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class LoginTests {
    @Rule
    public ActivityScenarioRule<LoginPage> activityRule = new ActivityScenarioRule<>(LoginPage.class);
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("project.group6.eams", appContext.getPackageName());
    }

    @Test
    public void testLoginEmpty(){
        Espresso.onView(withId(R.id.loginButton_login_page)).perform(click());
        Espresso.onView(withId(R.id.editTextTextEmailAddress_login_page)).check(matches(hasErrorText("Invalid Email.")));
    }
}