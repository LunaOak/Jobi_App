package edu.umd.cs.jobi;

import android.app.Activity;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
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

    // This test will test using and updating changes in settings and checking if changes are being //
    // reflected in home and preserved in settings //
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

        // Check right status is being displayed //
        onView(withId(R.id.status_text)).check(matches(withText(R.string.status_interviewing)));

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
        onView(withId(R.id.status_text)).check(matches(withText(R.string.status_searching)));

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
        onView(withId(R.id.status_text)).check(matches(withText(R.string.status_searching)));

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

    @Test
    public void testEventListUI(){

        // Test Event List //////////
        onView(withId(R.id.event_list_button)).perform(click());

        Activity currentActivity = getActivityInstance();
        assertTrue(currentActivity.getClass().isAssignableFrom(EventListActivity.class));

        // Check that menu buttons are present ////////////
        onView(withId(R.id.menu_item_home)).check(matches(isDisplayed()));
        onView(withId(R.id.menu_item_settings)).check(matches(isDisplayed()));

        // Check that labels are present //
        onView(withText(R.string.event_list_label)).check(matches(isDisplayed()));
        onView(withText(R.string.list_all)).check(matches(isDisplayed()));

        onView(withText(R.string.event_interview_label)).check(matches(isDisplayed()));
        onView(withText(R.string.event_email_label)).check(matches(isDisplayed()));
        onView(withText(R.string.event_deadline_label)).check(matches(isDisplayed()));

        // Add new event to list //
        onView(withId(R.id.add_new_event_button)).check(matches(ViewMatchers.isDisplayed()));
        onView(withId(R.id.add_new_event_button)).perform(
                new ViewAction() {
                    @Override
                    public Matcher<View> getConstraints() {
                        return ViewMatchers.isEnabled(); // no constraints, they are checked above
                    }

                    @Override
                    public String getDescription() {
                        return "click add event";
                    }

                    @Override
                    public void perform(UiController uiController, View view) {
                        view.performClick();
                    }
                }
        );

        Espresso.closeSoftKeyboard();

        onView(withId(R.id.event_name)).perform(typeText("First Round Phone Call"));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.event_company)).perform(typeText("Pinterest"));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.event_position)).perform(typeText("Cloud Engineer"));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.event_location)).perform(typeText("San Francisco"));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.event_date_button)).perform(click());

        // PickerActions not supported
        // onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2017, 5, 12));
        onView(withText("OK")).perform(click());

        onView(withId(R.id.event_time_button)).perform(click());
        onView(withText("OK")).perform(click());

        onView(withId(R.id.save_event_button)).perform(click());

        // Check that added event is present and in correct tab
        onView(withText("Pinterest")).check(matches(ViewMatchers.isDisplayed()));

        onView(withText(R.string.event_interview_label)).perform(
                new ViewAction() {

                    @Override
                    public Matcher<View> getConstraints() {
                        return ViewMatchers.isEnabled(); // no constraints, they are checked above
                    }
                    @Override
                    public String getDescription() {
                        return "click interviews tab";
                    }
                    @Override
                    public void perform(UiController uiController, View view) {
                        view.performClick();
                    }
                }
        );
        onView(withText("Pinterest")).check(matches(ViewMatchers.isDisplayed()));


    }

    @Test
    public void testCompanyListUI(){

        // Test Company List //////////
        onView(withId(R.id.company_list_button)).perform(click());

        Activity currentActivity = getActivityInstance();
        assertTrue(currentActivity.getClass().isAssignableFrom(CompanyListActivity.class));

        // Check that menu buttons are present ////////////
        onView(withId(R.id.menu_item_home)).check(matches(isDisplayed()));
        onView(withId(R.id.menu_item_settings)).check(matches(isDisplayed()));

        // Check that labels are present //
        onView(withText(R.string.company_list_label)).check(matches(isDisplayed()));
        onView(withText(R.string.list_all)).check(matches(isDisplayed()));
        onView(withText(R.string.current_companies)).check(matches(isDisplayed()));

        // Add new company to list //
        onView(withId(R.id.add_new_company_button)).check(matches(ViewMatchers.isDisplayed()));
        onView(withId(R.id.add_new_company_button)).perform(
                new ViewAction() {
                    @Override
                    public Matcher<View> getConstraints() {
                        return ViewMatchers.isEnabled(); // no constraints, they are checked above
                    }

                    @Override
                    public String getDescription() {
                        return "click add company";
                    }

                    @Override
                    public void perform(UiController uiController, View view) {
                        view.performClick();
                    }
                }
        );

        Espresso.closeSoftKeyboard();

        onView(withId(R.id.company_name)).perform(typeText("Yahoo"));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.company_location)).perform(typeText("New York"));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.company_description)).perform(typeText("A search engine"));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.save_company_button)).perform(click());

        // Check that added company is present and in correct tab
        onView(withText("Yahoo")).check(matches(ViewMatchers.isDisplayed()));

    }


    // This test will test creating various positions, updating them, and checking if changes are //
    // being reflected //

    @Test
    public void testPositionListUI(){

        // Test Position List //////////
        onView(withId(R.id.position_list_button)).perform(click());

        Activity currentActivity = getActivityInstance();
        assertTrue(currentActivity.getClass().isAssignableFrom(PositionListActivity.class));

        // Create New Positions //
        onView(withId(R.id.add_new_position_button)).check(matches(ViewMatchers.isDisplayed()));
        onView(withId(R.id.add_new_position_button)).perform(
                new ViewAction() {
                    @Override
                    public Matcher<View> getConstraints() {
                        return ViewMatchers.isEnabled(); // no constraints, they are checked above
                    }

                    @Override
                    public String getDescription() {
                        return "click add position";
                    }

                    @Override
                    public void perform(UiController uiController, View view) {
                        view.performClick();
                    }
                }
        );

        // Position One //
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.position)).perform(typeText("Software Engineer"));
        onView(withId(R.id.position_company)).perform(typeText("Google"));
        onView(withId(R.id.position_location)).perform(typeText("Seattle, Washington"));

        Espresso.closeSoftKeyboard();

        onView(withId(R.id.position_type_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Full-time"))).perform(click());
        onView(withId(R.id.position_type_spinner)).check(matches(withSpinnerText(containsString("Full-time"))));
        onView(withId(R.id.position_todo)).perform(click());
        onView(withId(R.id.position_todo)).check(matches(isChecked()));
        onView(withId(R.id.position_inProgress)).check(matches(isNotChecked()));
        onView(withId(R.id.position_done)).check(matches(isNotChecked()));

        Espresso.closeSoftKeyboard();

        onView(withId(R.id.position_description)).perform(typeText("Mobile App Development"));

        Espresso.closeSoftKeyboard();
        onView(withId(R.id.save_position_button)).perform(click());

        // Go back to Position List and check that it was created and in right tab //
        onView(withText(R.string.list_all)).perform(
                new ViewAction() {
                @Override
                public Matcher<View> getConstraints() {
                    return ViewMatchers.isEnabled(); // no constraints, they are checked above
                }

                @Override
                public String getDescription() {
                   return "click all tab";
                }

                @Override
                public void perform(UiController uiController, View view) {
                 view.performClick();
              }
            }
        );

        onView(withText("Software Engineer")).check(matches(isDisplayed()));
        onView(withText(R.string.positions_todo)).perform(
                new ViewAction() {

                    @Override
                    public Matcher<View> getConstraints() {
                        return ViewMatchers.isEnabled(); // no constraints, they are checked above
                    }
                    @Override
                    public String getDescription() {
                        return "click all tab";
                    }
                    @Override
                    public void perform(UiController uiController, View view) {
                        view.performClick();
                    }
                }
        );

        onView(withText("Software Engineer")).check(matches(isDisplayed()));

        // Check if information was correctly added //
        onView(withText("Software Engineer")).perform(click());
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.positionTitle)).check(matches(withText("Software Engineer")));
        onView(withId(R.id.companyName)).check(matches(withText("Google")));
        onView(withId(R.id.companyLocation)).check(matches(withText("Seattle, Washington")));
        onView(withId(R.id.positionType)).check(matches(withText("FULL_TIME")));
        onView(withId(R.id.positionStatus)).check(matches(withText("TODO")));
        onView(withId(R.id.positionDescription)).check(matches(withText("Mobile App Development")));

        Espresso.pressBack();

        // Position Two //
        onView(withId(R.id.add_new_position_button)).perform(
                new ViewAction() {
                    @Override
                    public Matcher<View> getConstraints() {
                        return ViewMatchers.isEnabled(); // no constraints, they are checked above
                    }

                    @Override
                    public String getDescription() {
                        return "click add position";
                    }

                    @Override
                    public void perform(UiController uiController, View view) {
                        view.performClick();
                    }
                }
        );

        Espresso.closeSoftKeyboard();

        onView(withId(R.id.position)).perform(typeText("Software Engineer 2"));
        onView(withId(R.id.position_company)).perform(typeText("Microsoft"));
        onView(withId(R.id.position_location)).perform(typeText("San Francisco, California"));

        Espresso.closeSoftKeyboard();

        onView(withId(R.id.position_type_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Part-time"))).perform(click());
        onView(withId(R.id.position_type_spinner)).check(matches(withSpinnerText(containsString("Part-time"))));
        onView(withId(R.id.position_inProgress)).perform(click());
        onView(withId(R.id.position_todo)).check(matches(isNotChecked()));
        onView(withId(R.id.position_inProgress)).check(matches(isChecked()));
        onView(withId(R.id.position_done)).check(matches(isNotChecked()));

        Espresso.closeSoftKeyboard();

        onView(withId(R.id.position_description)).perform(typeText("Software Development"));

        Espresso.closeSoftKeyboard();
        onView(withId(R.id.save_position_button)).perform(click());

        // Go back to Position List and check that it was created //
        onView(withText(R.string.list_all)).perform(
                new ViewAction() {
                    @Override
                    public Matcher<View> getConstraints() {
                        return ViewMatchers.isEnabled(); // no constraints, they are checked above
                    }

                    @Override
                    public String getDescription() {
                        return "click all tab";
                    }

                    @Override
                    public void perform(UiController uiController, View view) {
                        view.performClick();
                    }
                }
        );
        onView(withText("Software Engineer 2")).check(matches(isDisplayed()));

        onView(withText(R.string.positions_ongoing)).perform(
                new ViewAction() {
                    @Override
                    public Matcher<View> getConstraints() {
                        return ViewMatchers.isEnabled(); // no constraints, they are checked above
                    }

                    @Override
                    public String getDescription() {
                        return "click all tab";
                    }

                    @Override
                    public void perform(UiController uiController, View view) {
                        view.performClick();
                    }
                }
        );
        onView(withText("Software Engineer 2")).check(matches(isDisplayed()));

        // Check if information was correctly added //
        onView(withText("Software Engineer 2")).perform(click());
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.positionTitle)).check(matches(withText("Software Engineer 2")));
        onView(withId(R.id.companyName)).check(matches(withText("Microsoft")));
        onView(withId(R.id.companyLocation)).check(matches(withText("San Francisco, California")));
        onView(withId(R.id.positionType)).check(matches(withText("PART_TIME")));
        onView(withId(R.id.positionStatus)).check(matches(withText("IN_PROGRESS")));
        onView(withId(R.id.positionDescription)).check(matches(withText("Software Development")));

        // Perform Changes //
        onView(withId(R.id.edit_position_button)).perform(click());

        onView(withId(R.id.position)).perform(clearText(),typeText("Software Engineer 2A"));
        onView(withId(R.id.position_company)).perform(clearText(),typeText("Microsoft 2"));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.position_type_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Full-time"))).perform(click());
        onView(withId(R.id.position_type_spinner)).check(matches(withSpinnerText(containsString("Full-time"))));
        onView(withId(R.id.position_done)).perform(click());
        onView(withId(R.id.position_todo)).check(matches(isNotChecked()));
        onView(withId(R.id.position_inProgress)).check(matches(isNotChecked()));
        onView(withId(R.id.position_done)).check(matches(isChecked()));

        Espresso.closeSoftKeyboard();
        onView(withId(R.id.save_position_button)).perform(
                new ViewAction() {
                    @Override
                    public Matcher<View> getConstraints() {
                        return ViewMatchers.isEnabled(); // no constraints, they are checked above
                    }

                    @Override
                    public String getDescription() {
                        return "click save";
                    }

                    @Override
                    public void perform(UiController uiController, View view) {
                        view.performClick();
                    }
                }
        );

        // Check if changes were applied //
        onView(withId(R.id.positionTitle)).check(matches(withText("Software Engineer 2A")));
        onView(withId(R.id.companyName)).check(matches(withText("Microsoft 2")));
        onView(withId(R.id.companyLocation)).check(matches(withText("San Francisco, California")));
        onView(withId(R.id.positionType)).check(matches(withText("FULL_TIME")));
        onView(withId(R.id.positionStatus)).check(matches(withText("DONE")));
        onView(withId(R.id.positionDescription)).check(matches(withText("Software Development")));

        Espresso.pressBack();

        // Check that it has been modified in the list //
        onView(withText(R.string.list_all)).perform(
                new ViewAction() {
                    @Override
                    public Matcher<View> getConstraints() {
                        return ViewMatchers.isEnabled(); // no constraints, they are checked above
                    }

                    @Override
                    public String getDescription() {
                        return "click all tab";
                    }

                    @Override
                    public void perform(UiController uiController, View view) {
                        view.performClick();
                    }
                }
        );
        onView(withText("Software Engineer 2A")).check(matches(isDisplayed()));
        onView(withText("Software Engineer")).check(matches(isDisplayed()));

        onView(withText(R.string.positions_todo)).perform(
                new ViewAction() {
                    @Override
                    public Matcher<View> getConstraints() {
                        return ViewMatchers.isEnabled(); // no constraints, they are checked above
                    }

                    @Override
                    public String getDescription() {
                        return "click all tab";
                    }

                    @Override
                    public void perform(UiController uiController, View view) {
                        view.performClick();
                    }
                }
        );
        onView(withText("Software Engineer")).check(matches(isDisplayed()));

        onView(withText(R.string.positions_done)).perform(
                new ViewAction() {
                    @Override
                    public Matcher<View> getConstraints() {
                        return ViewMatchers.isEnabled(); // no constraints, they are checked above
                    }

                    @Override
                    public String getDescription() {
                        return "click all tab";
                    }

                    @Override
                    public void perform(UiController uiController, View view) {
                        view.performClick();
                    }
                }
        );
        onView(withText("Software Engineer 2A")).check(matches(isDisplayed()));
  
        // Position 3 //

    }

//    @Test
//    public void testCreatePositionUI(){
//
//        // Test Position List //////////
//        onView(withId(R.id.create_position_button)).perform(click());
//
//        Activity currentActivity = getActivityInstance();
//        assertTrue(currentActivity.getClass().isAssignableFrom(EnterPositionActivity.class));
//
//    }
}
