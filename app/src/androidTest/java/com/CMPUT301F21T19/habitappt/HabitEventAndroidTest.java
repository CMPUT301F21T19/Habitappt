package com.CMPUT301F21T19.habitappt;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.CMPUT301F21T19.habitappt.Activities.LoginActivity;
import com.CMPUT301F21T19.habitappt.Activities.MainActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class HabitEventAndroidTest {

        private Solo solo;

        @Rule
        public ActivityTestRule<LoginActivity> rule =
                new ActivityTestRule<>(LoginActivity.class,true,true);

        @Before
        public void setUp() throws Exception{
            if(solo != null){
                solo.finishOpenedActivities();
            }
            solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());

            solo.enterText((EditText) solo.getView(R.id.username),"asdf@gmail.com");
            solo.enterText((EditText) solo.getView(R.id.password),"Happy123!");

            solo.clickOnView(solo.getView(R.id.login_button));
        }

        @Test
        public void start() throws Exception {
        Activity activity = rule.getActivity();
        }

        @Test
        public void addHabitEvent() {
            solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
            solo.clickOnView(solo.getView(R.id.all_habit_button));

            solo.clickOnText("Test Habit Event");
            solo.clickOnView(solo.getView(R.id.add_event_button));
            solo.enterText((EditText) solo.getView(R.id.event_comments),"Event Comment 1");
            solo.clickOnView(solo.getView(R.id.event_date_calendar));
            solo.clickOnButton("Confirm");
            assertTrue(solo.waitForText("Event Comment 1", 1,2000 ));

        }

        @Test
        public void eventcharacterlimit(){
            solo.assertCurrentActivity("Wrong Activity",MainActivity.class);

            solo.clickOnView(solo.getView(R.id.all_habit_button));
            solo.clickOnView(solo.getView(R.id.recycler_habitList));

            solo.clickOnView(solo.getView(R.id.add_event_button));
            solo.clearEditText((EditText) solo.getView(R.id.event_comments));
            solo.enterText((EditText) solo.getView(R.id.event_comments),"20characterlimitttttttt");
            solo.sleep(3000);
        }

        @Test
        public void viewHabitEvent(){
            solo.assertCurrentActivity("Wrong Activity",MainActivity.class);

            solo.clickOnView(solo.getView(R.id.all_habit_button));
            solo.clickOnView(solo.getView(R.id.recycler_habitList));
            solo.clickOnView(solo.getView(R.id.event_name));

            solo.sleep(5000);
        }
        @Test
        public void editHabitEvent(){
            solo.assertCurrentActivity("Wrong Activity",MainActivity.class);

            solo.clickOnView(solo.getView(R.id.all_habit_button));
            solo.clickOnText("Test Habit Event");

            int fromX, toX, fromY, toY;
            int[] location = new int[2];

            View row = solo.getText("Event 1");
            row.getLocationInWindow(location);

            fromX = location[0] + 300;
            fromY = location[1];
            toX = location[0];
            toY = fromY;
            solo.drag(fromX, toX, fromY, toY, 10);

            solo.sleep(1000);
            solo.clickOnText("Edit");
            solo.clearEditText((EditText) solo.getView(R.id.event_comments));
            solo.enterText((EditText) solo.getView(R.id.event_comments),"New Event 1");
            solo.clickOnView(solo.getView(R.id.event_date_calendar));
            solo.clickOnButton("Confirm");
            assertTrue(solo.waitForText("New Event 1"));

        }
        @Test
        public void deleteHabitEvent(){
            solo.assertCurrentActivity("Wrong Activity",MainActivity.class);

            solo.clickOnView(solo.getView(R.id.all_habit_button));
            solo.clickOnText("Test Habit Event");

            int fromX, toX, fromY, toY;
            int[] location = new int[2];
            View row = solo.getText("Event 1");
            row.getLocationInWindow(location);

            fromX = location[0] + 300;
            fromY = location[1];
            toX = location[0];
            toY = fromY;
            solo.drag(fromX, toX, fromY, toY, 10);

            solo.sleep(1000);
            solo.clickOnText("Delete");
            solo.sleep(3000);
        }

        @Test
        public void addHabitEventWithLocation() {
            solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
            solo.clickOnView(solo.getView(R.id.all_habit_button));

            solo.clickOnText("Mock Habit 1");
            solo.clickOnView(solo.getView(R.id.add_event_button));
            solo.enterText((EditText) solo.getView(R.id.event_comments),"comment");
            solo.clickOnView(solo.getView(R.id.event_date_calendar));
            solo.clickOnView(solo.getView(R.id.location_button));
            solo.clickLongOnScreen(250,400);
            solo.clickOnText("Save Location");
            solo.clickOnButton("Confirm");

        }

    @Test
    public void addHabitEventWithpicture() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.all_habit_button));

        solo.clickOnText("Mock Habit 1");
        solo.clickOnView(solo.getView(R.id.add_event_button));
        solo.enterText((EditText) solo.getView(R.id.event_comments),"comment");
        solo.clickOnView(solo.getView(R.id.event_img));
        //solo.clickOnButton(R.id.event_img);
        solo.clickLongOnScreen(300,700);
        solo.clickOnView(solo.getView(R.id.event_date_calendar));
        solo.clickOnView(solo.getView(R.id.location_button));
        solo.clickOnButton("Confirm");
    }

        @After
        public void tearDown() { solo.finishOpenedActivities(); }

}

