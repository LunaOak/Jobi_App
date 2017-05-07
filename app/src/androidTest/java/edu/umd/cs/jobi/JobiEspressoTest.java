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

//    @Test
//    public void testEditStory(){
//
//        /* Existing story's data is incorrect in StoryActivity */
//        onView(withText("Story B")).perform(click());
//        onView(withId(R.id.summary)).check(matches(withText("Story B")));
//        onView(withId(R.id.criteria)).check(matches(withText("Implementing and Testing2!")));
//        onView(withId(R.id.points)).check(matches(withText("11.0")));
//        onView(withId(R.id.spinner)).check(matches(withSpinnerText(containsString("To Do"))));
//        onView(withId(R.id.radio_next)).check(matches(isChecked()));
//
//        /* Changes not reflected in backlog */
//        onView(withId(R.id.summary)).perform(clearText(),typeText("Story C"));
//        onView(withId(R.id.criteria)).perform(clearText(), typeText("Implementing!"));
//        onView(withId(R.id.points)).perform(clearText(), typeText("12"));
//        Espresso.closeSoftKeyboard();
//        onView(withId(R.id.spinner)).perform(click());
//        onData(allOf(is(instanceOf(String.class)), is("Done"))).perform(click());
//        onView(withId(R.id.radio_later)).perform(click());
//        onView(withId(R.id.radio_later)).check(matches(isChecked()));
//        onView(withId(R.id.radio_current)).check(matches(isNotChecked()));
//        onView(withId(R.id.radio_next)).check(matches(isNotChecked()));
//
//        Espresso.closeSoftKeyboard();
//        onView(withId(R.id.save_story_button)).perform(click());
//
//        // Check if it was updated to the backlog //
//        onView(withText("Story C")).check(matches(isDisplayed()));
//
//        // Check if information was correctly added //
//        onView(withText("Story C")).perform(click());
//        onView(withId(R.id.summary)).check(matches(withText("Story C")));
//        onView(withId(R.id.criteria)).check(matches(withText("Implementing!")));
//        onView(withId(R.id.points)).check(matches(withText("12.0")));
//        onView(withId(R.id.spinner)).check(matches(withSpinnerText(containsString("Done"))));
//        onView(withId(R.id.radio_later)).check(matches(isChecked()));
//
//        // Go back to backlog //
//        Espresso.closeSoftKeyboard();
//        onView(withId(R.id.cancel_story_button)).perform(click());
//
//    }
//
//    @Test
//    public void testActiveSprint() {
//
//        // Check that active sprint button is present ////////////
//        onView(withId(R.id.menu_item_active_sprint)).check(matches(isDisplayed()));
//
//        // Make sure that SprintActivity starts //////////
//        onView(withId(R.id.menu_item_active_sprint)).perform(click());
//
//        Activity currentActivity = getActivityInstance();
//        assertTrue(currentActivity.getClass().isAssignableFrom(SprintActivity.class));
//
//        /* No stories displayed */
//        onView(withText(R.string.todo_label)).check(matches(isDisplayed()));
//        onView(withText(R.string.inprogress_label)).check(matches(isDisplayed()));
//        onView(withText(R.string.done_label)).check(matches(isDisplayed()));
//
//        /* Stories not displayed in correct columns */
//        onView((withText("Story D"))).check(isLeftOf(withText("Story A")));
//        onView((withText("Story E"))).check(isRightOf(withText("Story A")));
//    }
}
