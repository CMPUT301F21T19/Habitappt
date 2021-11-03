package com.CMPUT301F21T19.habitappt;

import android.app.Activity;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class HabitTest {
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

//        @Test
//        public void start() throws Exception {
//            Activity activity = rule.getActivity();
//        }

        @Test
        public void addHabit() {


//            solo.clickOnView(solo.getView(R.id.bottom_nav_bar));
            solo.clickOnView(solo.getView(R.id.all_habit_button));

            solo.assertCurrentActivity("Wrong Activity",MainActivity.class);
            solo.clickOnView(solo.getView(R.id.add_habit_button));
            System.out.println("TEST!");
            solo.enterText((EditText) solo.getView(R.id.habit_title),"Habit1");
            solo.enterText((EditText) solo.getView(R.id.habit_reason),"Reason1");
            solo.clickOnButton("M");
            solo.clickOnButton("W");
            solo.clickOnButton("F");
            solo.clickOnView(solo.getView(R.id.date_to_start));
            solo.clickOnButton("Confirm");

        }


        @Test
        public void editHabit() {

            solo.clickOnView(solo.getView(R.id.all_habit_button));

            solo.clickOnView(solo.getView(R.id.add_habit_button));

            solo.enterText((EditText) solo.getView(R.id.habit_title),"Habit1");
            solo.enterText((EditText) solo.getView(R.id.habit_reason),"Reason1");
            solo.clickOnButton("M");
            solo.clickOnButton("W");
            solo.clickOnButton("F");
            solo.clickOnView(solo.getView(R.id.date_to_start));
            solo.clickOnButton("Confirm");

            solo.clickInList(0);
            solo.clickOnView(solo.getView(R.id.edit_button));
            solo.clearEditText((EditText) solo.getView(R.id.habit_title));
            solo.enterText((EditText) solo.getView(R.id.habit_title),"New Habit_1");
            solo.clearEditText((EditText) solo.getView(R.id.habit_reason));
            solo.enterText((EditText) solo.getView(R.id.habit_reason),"New Reason_1");
            solo.clickOnButton("M");
            solo.clickOnButton("S");
            solo.clickOnView(solo.getView(R.id.date_to_start));
            solo.clickOnButton("Confirm");
        }
//
//        @Test
//        public void deleteHabit() {
//            solo.clickOnView(solo.getView(R.id.habit_list));
//            solo.clickInList(0);
//            solo.clickOnView(solo.getView(R.id.edit_button));
//            solo.clickOnButton("Remove Habit");
//        }
}

