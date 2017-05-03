//package edu.umd.cs.jobi;
//
//import android.app.Activity;
//import android.content.Context;
//import android.support.test.InstrumentationRegistry;
//import android.support.test.espresso.Espresso;
//import android.support.test.rule.ActivityTestRule;
//import android.support.test.runner.AndroidJUnit4;
//
//import static android.support.test.espresso.Espresso.onData;
//import static android.support.test.espresso.action.ViewActions.clearText;
//import static android.support.test.espresso.action.ViewActions.typeText;
//
//
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import edu.umd.cs.jobi.BacklogActivity;
//
//import static android.support.test.espresso.Espresso.onView;
//import static android.support.test.espresso.action.ViewActions.click;
//import static android.support.test.espresso.assertion.PositionAssertions.isLeftOf;
//import static android.support.test.espresso.assertion.PositionAssertions.isRightOf;
//import static android.support.test.espresso.assertion.ViewAssertions.matches;
//import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
//import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
//import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
//import static android.support.test.espresso.matcher.ViewMatchers.withId;
//import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
//import static android.support.test.espresso.matcher.ViewMatchers.withText;
//import static org.hamcrest.Matchers.containsString;
//import static org.hamcrest.Matchers.instanceOf;
//import static org.hamcrest.Matchers.is;
//import static org.hamcrest.Matchers.not;
//import static org.hamcrest.core.AllOf.allOf;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//
//
//@RunWith(AndroidJUnit4.class)
//
//public class AgileAndroidEspressoTest extends BaseActivityEspressoTest {
//
//    @Rule
//    public ActivityTestRule<BacklogActivity> activityRule = new ActivityTestRule<BacklogActivity>(BacklogActivity.class);
//
//    @Test
//    public void useAppContext() throws Exception {
//        Context appContext = InstrumentationRegistry.getTargetContext();
//        assertEquals("edu.umd.cs.agileandroid", appContext.getPackageName());
//    }
//
//    @Override
//    public Activity getActivity() {
//        return (Activity)activityRule.getActivity();
//    }
//
//    @Test
//    public void testCreateStories(){
//
//        Espresso.closeSoftKeyboard();
//
//        // Add story icon missing ////////////
//        onView(withId(R.id.menu_item_create_story)).check(matches(isDisplayed()));
//
//        // StoryActivity was not opened //////////
//        onView(withId(R.id.menu_item_create_story)).perform(click());
//
//        Activity currentActivity = getActivityInstance();
//        assertTrue(currentActivity.getClass().isAssignableFrom(StoryActivity.class));
//
//        // Story not added to backlog /////////
//
//        // Create Story to add ---------------------------------------//
//        onView(withId(R.id.summary)).perform(typeText("Story A"));
//        onView(withId(R.id.criteria)).perform(typeText("Implementing and Testing!"));
//        onView(withId(R.id.points)).perform(typeText("10"));
//        Espresso.closeSoftKeyboard();
//        onView(withId(R.id.spinner)).perform(click());
//        onData(allOf(is(instanceOf(String.class)), is("In Progress"))).perform(click());
//        onView(withId(R.id.spinner)).check(matches(withSpinnerText(containsString("In Progress"))));
//        onView(withId(R.id.radio_current)).perform(click());
//        onView(withId(R.id.radio_current)).check(matches(isChecked()));
//        onView(withId(R.id.radio_next)).check(matches(isNotChecked()));
//        onView(withId(R.id.radio_later)).check(matches(isNotChecked()));
//
//        Espresso.closeSoftKeyboard();
//        onView(withId(R.id.save_story_button)).perform(click());
//
//        // Check if it was added to the backlog //
//        onView(withText("Story A")).check(matches(isDisplayed()));
//
//        // Check if information was correctly added //
//        onView(withText("Story A")).perform(click());
//        onView(withId(R.id.summary)).check(matches(withText("Story A")));
//        onView(withId(R.id.criteria)).check(matches(withText("Implementing and Testing!")));
//        onView(withId(R.id.points)).check(matches(withText("10.0")));
//        onView(withId(R.id.spinner)).check(matches(withSpinnerText(containsString("In Progress"))));
//        onView(withId(R.id.radio_current)).check(matches(isChecked()));
//
//        // Go back to backlog //
//        Espresso.closeSoftKeyboard();
//        onView(withId(R.id.cancel_story_button)).perform(click());
//
//        // Create Story to add ---------------------------------------//
//        onView(withId(R.id.menu_item_create_story)).perform(click());
//
//        // Create Story to add //
//        onView(withId(R.id.summary)).perform(typeText("Story B"));
//        onView(withId(R.id.criteria)).perform(typeText("Implementing and Testing2!"));
//        onView(withId(R.id.points)).perform(typeText("11"));
//        Espresso.closeSoftKeyboard();
//        onView(withId(R.id.spinner)).perform(click());
//        onData(allOf(is(instanceOf(String.class)), is("To Do"))).perform(click());
//        onView(withId(R.id.spinner)).check(matches(withSpinnerText(containsString("To Do"))));
//        onView(withId(R.id.radio_next)).perform(click());
//        onView(withId(R.id.radio_next)).check(matches(isChecked()));
//        onView(withId(R.id.radio_current)).check(matches(isNotChecked()));
//        onView(withId(R.id.radio_later)).check(matches(isNotChecked()));
//
//        Espresso.closeSoftKeyboard();
//        onView(withId(R.id.save_story_button)).perform(click());
//
//        // Check if it was added to the backlog //
//        onView(withText("Story B")).check(matches(isDisplayed()));
//
//        // Check if information was correctly added //
//        onView(withText("Story B")).perform(click());
//        onView(withId(R.id.summary)).check(matches(withText("Story B")));
//        onView(withId(R.id.criteria)).check(matches(withText("Implementing and Testing2!")));
//        onView(withId(R.id.points)).check(matches(withText("11.0")));
//        onView(withId(R.id.spinner)).check(matches(withSpinnerText(containsString("To Do"))));
//        onView(withId(R.id.radio_next)).check(matches(isChecked()));
//
//        // Go back to backlog //
//        Espresso.closeSoftKeyboard();
//        onView(withId(R.id.cancel_story_button)).perform(click());
//
//        // Create Story to add ----------------------------------------//
//        onView(withId(R.id.menu_item_create_story)).perform(click());
//
//        onView(withId(R.id.summary)).perform(typeText("Story D"));
//        onView(withId(R.id.criteria)).perform(typeText("Implementing and Testing3!"));
//        onView(withId(R.id.points)).perform(typeText("13"));
//        Espresso.closeSoftKeyboard();
//        onView(withId(R.id.spinner)).perform(click());
//        onData(allOf(is(instanceOf(String.class)), is("To Do"))).perform(click());
//        onView(withId(R.id.spinner)).check(matches(withSpinnerText(containsString("To Do"))));
//        onView(withId(R.id.radio_current)).perform(click());
//        onView(withId(R.id.radio_current)).check(matches(isChecked()));
//        onView(withId(R.id.radio_next)).check(matches(isNotChecked()));
//        onView(withId(R.id.radio_later)).check(matches(isNotChecked()));
//
//        Espresso.closeSoftKeyboard();
//        onView(withId(R.id.save_story_button)).perform(click());
//
//        // Check if it was added to the backlog //
//        onView(withText("Story D")).check(matches(isDisplayed()));
//
//        // Check if information was correctly added //
//        onView(withText("Story D")).perform(click());
//        onView(withId(R.id.summary)).check(matches(withText("Story D")));
//        onView(withId(R.id.criteria)).check(matches(withText("Implementing and Testing3!")));
//        onView(withId(R.id.points)).check(matches(withText("13.0")));
//        onView(withId(R.id.spinner)).check(matches(withSpinnerText(containsString("To Do"))));
//        onView(withId(R.id.radio_current)).check(matches(isChecked()));
//
//        // Go back to backlog //
//        Espresso.closeSoftKeyboard();
//        onView(withId(R.id.cancel_story_button)).perform(click());
//
//        // Create Story to add ----------------------------------------//
//        onView(withId(R.id.menu_item_create_story)).perform(click());
//
//        onView(withId(R.id.summary)).perform(typeText("Story E"));
//        onView(withId(R.id.criteria)).perform(typeText("Implementing and Testing4!"));
//        onView(withId(R.id.points)).perform(typeText("14"));
//        Espresso.closeSoftKeyboard();
//        onView(withId(R.id.spinner)).perform(click());
//        onData(allOf(is(instanceOf(String.class)), is("Done"))).perform(click());
//        onView(withId(R.id.spinner)).check(matches(withSpinnerText(containsString("Done"))));
//        onView(withId(R.id.radio_current)).perform(click());
//        onView(withId(R.id.radio_current)).check(matches(isChecked()));
//        onView(withId(R.id.radio_next)).check(matches(isNotChecked()));
//        onView(withId(R.id.radio_later)).check(matches(isNotChecked()));
//
//        Espresso.closeSoftKeyboard();
//        onView(withId(R.id.save_story_button)).perform(click());
//
//        // Check if it was added to the backlog //
//        onView(withText("Story E")).check(matches(isDisplayed()));
//
//        // Check if information was correctly added //
//        onView(withText("Story E")).perform(click());
//        onView(withId(R.id.summary)).check(matches(withText("Story E")));
//        onView(withId(R.id.criteria)).check(matches(withText("Implementing and Testing4!")));
//        onView(withId(R.id.points)).check(matches(withText("14.0")));
//        onView(withId(R.id.spinner)).check(matches(withSpinnerText(containsString("Done"))));
//        onView(withId(R.id.radio_current)).check(matches(isChecked()));
//
//        // Go back to backlog //
//        Espresso.closeSoftKeyboard();
//        onView(withId(R.id.cancel_story_button)).perform(click());
//
//    }
//
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
//}
