package com.CMPUT301F21T19.habitappt;

import android.app.Activity;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import android.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.CMPUT301F21T19.habitappt.LoginActivity;
import com.robotium.solo.Solo;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class US_01_03_01_Test {
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

    @Test
    public void viewDetailsTest() {
        solo.assertCurrentActivity("wrong activity", LoginActivity.class);
        solo.clickOnView(solo.getView(R.id.login_button));
        solo.assertCurrentActivity("wrong activity", MainActivity.class);

        //view of list item

        // Making a habit
        solo.clickOnView(solo.getView(R.id.all_habit_button));
        solo.clickOnView(solo.getView(R.id.add_habit_button));
        solo.waitForText("Add Habit", 1, 2000);

        //Fragment fragment = solo.getCurrentActivity().getFragmentManager().findFragmentById(R.id.
        //EditText editText = (EditText) fragment.getView().findViewById(R.id.habit_title);

        solo.enterText((EditText) solo.getView(R.id.habit_title), "TestHabit-01-03-01");
        solo.enterText((EditText) solo.getView(R.id.habit_reason), "TestReason-01-03-01");
        solo.clickOnView(solo.getView(R.id.public_private_button));
        solo.clickOnView(solo.getView(R.id.monday_button));
        solo.clickOnView(solo.getView(R.id.date_to_start));
        solo.clickOnButton("Confirm");

        // Viewing the habit
        solo.clickOnText("TestHabit-01-03-01");
        solo.waitForText("title:", 1, 2000);
        solo.waitForText("TestHabit-01-03-01", 1, 2000);
        solo.waitForText("reason:", 1, 2000);
        solo.waitForText("TestReason-01-03-01", 1, 2000);
        solo.waitForText("date:", 1, 2000);
        solo.waitForText("10/", 1, 2000);
        solo.waitForText("days:", 1, 2000);
        // check days of week background colours
        solo.waitForText("access:", 1, 2000);
        solo.waitForText("Private Habit", 1, 2000);
        // check private habit background colours
        solo.clickOnView(solo.getView(R.id.all_habit_button));

        // Removing the habit
        solo.clickOnText("TestHabit-01-03-01");
        solo.clickOnView(solo.getView(R.id.edit_button));
        solo.clickOnButton("Remove Habit");
    }

    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }
}