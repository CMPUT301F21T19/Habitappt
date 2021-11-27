package com.CMPUT301F21T19.habitappt;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import android.widget.EditText;

import com.CMPUT301F21T19.habitappt.Activities.LoginActivity;
import com.CMPUT301F21T19.habitappt.Activities.MainActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class US_01_06_01_Test {
    private Solo solo;

    @Rule
    public ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class, true, true);

    @Before
    public void setUp() {
        if (solo != null) {
            solo.finishOpenedActivities();
        }
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    //test max habit title length of 20. type string thats 30 characters, check to make sure text watcher appears.
    @Test
    public void testHabitTitle(){

        solo.assertCurrentActivity("wrong activity", LoginActivity.class);
        solo.clickOnView(solo.getView(R.id.login_button));
        solo.assertCurrentActivity("wrong activity", MainActivity.class);

        //view of list item

        // Making a habit
        solo.clickOnView(solo.getView(R.id.all_habit_button));
        solo.clickOnView(solo.getView(R.id.add_habit_button));

        String inp = "";

        for(int i=0;i<30;i++){
            inp += "a";
        }

        solo.enterText((EditText) solo.getView(R.id.habit_title), inp);
        solo.sleep(200);
        //do this to check if the text watcher appears.
        solo.clickOnText("Maximum Length");

    }

    //test max habit reason length of 30. type string thats 40 characters, check to make sure text watcher appears.
    @Test
    public void testHabitReason(){

        solo.assertCurrentActivity("wrong activity", LoginActivity.class);
        solo.clickOnView(solo.getView(R.id.login_button));
        solo.assertCurrentActivity("wrong activity", MainActivity.class);

        //view of list item

        // Making a habit
        solo.clickOnView(solo.getView(R.id.all_habit_button));
        solo.clickOnView(solo.getView(R.id.add_habit_button));

        String inp = "";

        for(int i=0;i<40;i++){
            inp += "a";
        }
        solo.sleep(500);
        System.out.println(solo.getView(R.id.main_container).getViewTreeObserver());
        solo.enterText((EditText) solo.getView(R.id.habit_reason), inp);
        solo.sleep(200);
        //do this to check if the text watcher appears.
        solo.clickOnText("Maximum Length");

    }


}
