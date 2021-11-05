package com.CMPUT301F21T19.habitappt;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

        @Test
        public void start() throws Exception {
            Activity activity = rule.getActivity();
       }

        @Test
        public void addHabit() {

            solo.clickOnView(solo.getView(R.id.all_habit_button));
            solo.assertCurrentActivity("Wrong Activity",MainActivity.class);

            solo.clickOnView(solo.getView(R.id.add_habit_button));
            solo.enterText((EditText) solo.getView(R.id.habit_title),"Mock Habit");
            solo.clearEditText((EditText) solo.getView(R.id.habit_reason));
            solo.enterText((EditText) solo.getView(R.id.habit_reason),"Reason_1");
            solo.clickOnButton("M");
            solo.clickOnButton("W");
            solo.clickOnButton("F");
            solo.clickOnView(solo.getView(R.id.public_private_button));
            solo.clickOnView(solo.getView(R.id.date_to_start));
            solo.clickOnButton("Confirm");

            assertTrue(solo.waitForText("Habit1",1,2000));

        }


        @Test
        public void editHabit() {

            solo.clickOnView(solo.getView(R.id.all_habit_button));
            solo.assertCurrentActivity("Wrong Activity",MainActivity.class);


            // edit a habit
            solo.clickInList(0);
            solo.clickOnView(solo.getView(R.id.edit_button));
            solo.clearEditText((EditText) solo.getView(R.id.habit_title));
            solo.enterText((EditText) solo.getView(R.id.habit_title),"Mock Habit 1");
            solo.clearEditText((EditText) solo.getView(R.id.habit_reason));
            solo.enterText((EditText) solo.getView(R.id.habit_reason),"New Reason_1");
            solo.clickOnView(solo.getView(R.id.public_private_button));

            solo.clickOnButton("M");
            solo.clickOnButton("S");
            solo.clickOnView(solo.getView(R.id.date_to_start));
            solo.clickOnButton("Confirm");

            assertTrue(solo.waitForText("New Habit_1",1,5000));
        }

        @Test
        public void deleteHabit() {

            solo.clickOnView(solo.getView(R.id.all_habit_button));
            solo.assertCurrentActivity("Wrong Activity",MainActivity.class);

            // add Habit before deleting in case there are none.
            solo.clickOnView(solo.getView(R.id.add_habit_button));
            solo.enterText((EditText) solo.getView(R.id.habit_title),"Habit to delete");
            solo.enterText((EditText) solo.getView(R.id.habit_reason),"delete after");
            solo.clickOnButton("M");
            solo.clickOnButton("W");
            solo.clickOnButton("F");
            solo.clickOnView(solo.getView(R.id.date_to_start));
            solo.clickOnButton("Confirm");

            //delete habit
            solo.clickOnText("Habit to delete");
            solo.clickOnView(solo.getView(R.id.edit_button));
            solo.clickOnButton("Remove Habit");

            assertFalse(solo.searchText("Habit to delete"));
        }
        @Test
        public void viewHabit(){

            solo.clickOnView(solo.getView(R.id.all_habit_button));
            solo.assertCurrentActivity("Wrong Activity",MainActivity.class);

            solo.clickOnView(solo.getView(R.id.habit_list));
            solo.clickInList(0);
            solo.waitForText("Habit",1,5000);
        }

        @Test
        public void title_characterlimit(){

            solo.clickOnView(solo.getView(R.id.all_habit_button));
            solo.assertCurrentActivity("Wrong Activity",MainActivity.class);

            solo.clickOnView(solo.getView(R.id.add_habit_button));
            solo.enterText((EditText) solo.getView(R.id.habit_title),"thisisover20characterlimittt");
            solo.sleep(5000);

        }

      @Test
      public  void reason_characterlimit() {

          solo.clickOnView(solo.getView(R.id.all_habit_button));
          solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

          solo.clickOnView(solo.getView(R.id.add_habit_button));
          solo.enterText((EditText) solo.getView(R.id.habit_title),"title");
          solo.enterText((EditText) solo.getView(R.id.habit_reason), "thisisover30characterlimittttttt");
          solo.sleep(5000);
      }
        @Test
        public void dailyHabits(){
            solo.clickOnView(solo.getView(R.id.daily_habit_button));
            solo.assertCurrentActivity("Wrong Activity",MainActivity.class);

            solo.sleep(5000);
        }

        @After
        public void tearDown(){ solo.finishOpenedActivities(); }

}
