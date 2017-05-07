package edu.umd.cs.jobi;

import android.app.Activity;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.typeText;


import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.umd.cs.jobi.HomeActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.PositionAssertions.isLeftOf;
import static android.support.test.espresso.assertion.PositionAssertions.isRightOf;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)

public class JobiEspressoTest extends BaseActivityEspressoTest {

    @Rule
    public ActivityTestRule<HomeActivity> activityRule = new ActivityTestRule<HomeActivity>(HomeActivity.class);

    @Test
    public void useAppContext() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("edu.umd.cs.jobi", appContext.getPackageName());
    }

    @Override
    public Activity getActivity() {
        return (Activity)activityRule.getActivity();
    }

    @Test
    public void testHomeAndSettingsUI(){

        Espresso.closeSoftKeyboard();

        // Check that menu buttons are present ////////////
        onView(withId(R.id.menu_item_home)).check(matches(isDisplayed()));
        onView(withId(R.id.menu_item_settings)).check(matches(isDisplayed()));

        // Check Status and Manage Buttons are present //
        onView(withId(R.id.status_color)).check(matches(isDisplayed()));
        onView(withId(R.id.status_text)).check(matches(isDisplayed()));

        onView(withId(R.id.event_list_button)).check(matches(isDisplayed()));
        onView(withId(R.id.position_list_button)).check(matches(isDisplayed()));
        onView(withId(R.id.company_list_button)).check(matches(isDisplayed()));
        onView(withId(R.id.create_position_button)).check(matches(isDisplayed()));

        // Check right color of status being displayed //
        //onView(withId(R.id.status_color)).check(matches(withId(R.color.interviewing)));

        // Test Settings //////////
        onView(withId(R.id.menu_item_settings)).perform(click());

        Activity currentActivity = getActivityInstance();
        assertTrue(currentActivity.getClass().isAssignableFrom(SettingsActivity.class));

        // Make sure all UI elements are present /////////
        onView(withId(R.id.settings_status_radio_group)).check(matches(isDisplayed()));
        onView(withId(R.id.settings_notification_switch)).check(matches(isDisplayed()));
        onView(withId(R.id.settings_up_interviews)).check(matches(isDisplayed()));
        onView(withId(R.id.settings_emails)).check(matches(isDisplayed()));
        onView(withId(R.id.settings_deadlines)).check(matches(isDisplayed()));

        // Test First Instance of Settings ///
        onView(withId(R.id.settings_status_interviewing)).check(matches(isChecked()));
        onView(withId(R.id.settings_status_searching)).check(matches(isNotChecked()));
        onView(withId(R.id.settings_status_not_searching)).check(matches(isNotChecked()));

        onView(withId(R.id.settings_notification_switch)).check(matches(isChecked()));
        onView(withId(R.id.settings_up_interviews)).check(matches(isChecked()));
        onView(withId(R.id.settings_emails)).check(matches(isChecked()));
        onView(withId(R.id.settings_deadlines)).check(matches(isChecked()));

        // Perform Changes and Save //
        onView(withId(R.id.settings_status_searching)).perform(click());
        onView(withId(R.id.settings_notification_switch)).perform(click());
        onView(withId(R.id.settings_emails)).perform(click());

        onView(withId(R.id.settings_save_button)).perform(click());

        // Back in Home //
        //onView(withId(R.id.status_color)).check(matches(withId(R.color.searching)));

        // Go Back to Settings and check if changes are reflected //
        onView(withId(R.id.menu_item_settings)).perform(click());

        onView(withId(R.id.settings_status_searching)).check(matches(isChecked()));
        onView(withId(R.id.settings_status_interviewing)).check(matches(isNotChecked()));
        onView(withId(R.id.settings_status_not_searching)).check(matches(isNotChecked()));

        onView(withId(R.id.settings_notification_switch)).check(matches(isNotChecked()));
        onView(withId(R.id.settings_up_interviews)).check(matches(isChecked()));
        onView(withId(R.id.settings_emails)).check(matches(isNotChecked()));
        onView(withId(R.id.settings_deadlines)).check(matches(isChecked()));

        // Perform Changes but Cancel //
        onView(withId(R.id.settings_status_not_searching)).perform(click());
        onView(withId(R.id.settings_notification_switch)).perform(click());
        onView(withId(R.id.settings_deadlines)).perform(click());

        onView(withId(R.id.settings_cancel_button)).perform(click());

        // Back at Home //
        //onView(withId(R.id.status_color)).check(matches(withId(R.color.searching)));

        // Go Back to Settings and check if changes are not reflected //
        onView(withId(R.id.menu_item_settings)).perform(click());

        onView(withId(R.id.settings_status_searching)).check(matches(isChecked()));
        onView(withId(R.id.settings_status_interviewing)).check(matches(isNotChecked()));
        onView(withId(R.id.settings_status_not_searching)).check(matches(isNotChecked()));

        onView(withId(R.id.settings_notification_switch)).check(matches(isNotChecked()));
        onView(withId(R.id.settings_up_interviews)).check(matches(isChecked()));
        onView(withId(R.id.settings_emails)).check(matches(isNotChecked()));
        onView(withId(R.id.settings_deadlines)).check(matches(isChecked()));

    }
    
}
