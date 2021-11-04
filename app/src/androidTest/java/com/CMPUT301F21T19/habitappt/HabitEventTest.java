package com.CMPUT301F21T19.habitappt;

import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class HabitEventTest {

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
            solo.clickOnView(solo.getView(R.id.login_button));


        }

        @Test
        public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

        @Test
        public void addHabitEvent() {
            solo.assertCurrentActivity("Wrong Activity",MainActivity.class);
            solo.clickOnView(solo.getView(R.id.all_habit_button));

            solo.clickOnView(solo.getView(R.id.habit_list));
            solo.clickInList(0);
            solo.clickOnView(solo.getView(R.id.add_event_button));
            solo.enterText((EditText) solo.getView(R.id.event_comments),"Event Comment 1");
            solo.clickOnView(solo.getView(R.id.event_date_calendar));
            solo.clickOnButton("Confirm");
            assertTrue(solo.waitForText("Event Comment 1", 1,2000 ));

        }

        @Test
        public void characterlimit(){
            solo.clickOnView(solo.getView(R.id.all_habit_button));
            solo.clickOnView(solo.getView(R.id.habit_list));
            solo.clickInList(0);
            solo.clickOnView(solo.getView(R.id.add_event_button));
            solo.enterText((EditText) solo.getView(R.id.event_comments),"20characterlimittttt");
            solo.clickOnView(solo.getView(R.id.event_date_calendar));
            solo.clickOnButton("Confirm");
            assertTrue(solo.waitForText("20characterlimittttt", 1,2000 ));


        }
}

