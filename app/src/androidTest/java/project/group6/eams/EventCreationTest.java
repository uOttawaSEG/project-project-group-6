package project.group6.eams;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;


import android.content.Context;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import project.group6.eams.activities.*;

@RunWith(AndroidJUnit4.class)
public class EventCreationTest {

    @Rule
    public ActivityScenarioRule<CreateEventPage> activityRule = new ActivityScenarioRule<>(CreateEventPage.class);

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("project.group6.eams", appContext.getPackageName());
    }

   // @Test
    /*public void testEmptyEventName() {

        onView(withId(R.id.eventTitle)).perform(typeText("      "));
        onView(withId(R.id.createEvent)).perform(click());
        onView(withId(R.id.eventTitle)).check(matches(hasErrorText("Please enter an event name.")));
    }*/


}
