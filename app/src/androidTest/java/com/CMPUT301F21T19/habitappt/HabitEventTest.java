package com.CMPUT301F21T19.habitappt;

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
            solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
            solo.clickOnView(solo.getView(R.id.login_button));
            solo.clickOnView(solo.getView(R.id.bottom_nav_bar));
            solo.clickOnView(solo.getView(R.id.all_habit_button));
            //solo.clickOnButton(R.id.all_habit_button);
        }

//    @Test
//    public void start() throws Exception {
//        Activity activity = rule.getActivity();
//    }

//        @Test
//        public void addHabitEvent() {
//            //solo.assertCurrentActivity("Wrong Activity",MainActivity.class);
//            solo.clickOnView(solo.getView(R.id.habit_list));
//            solo.clickOnView(solo.getView(R.id.add_event_button));
//            solo.enterText((EditText) solo.getView(R.id.event_comments),"Event Comment 1");
//            //add image (not yet done)
//            solo.clickOnView(solo.getView(R.id.event_date_calendar));
//            solo.setDatePicker(0, 2021, 10, 10);
//            solo.clickOnView(solo.getView(android.R.id.button1));
//            //solo.clickOnView(solo.getView(R.id.dialog_button));
//        }
}

