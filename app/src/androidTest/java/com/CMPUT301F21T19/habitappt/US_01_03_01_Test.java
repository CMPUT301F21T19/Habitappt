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
        solo.enterText((EditText) solo.getView(R.id.habit_title), "TestHabit-01-03-01");
        solo.enterText((EditText) solo.getView(R.id.habit_reason), "TestReason-01-03-01");
        solo.clickOnView(solo.getView(R.id.public_private_button));
        solo.clickOnView(solo.getView(R.id.monday_button));
        solo.clickOnView(solo.getView(R.id.date_to_start));
        solo.clickOnButton("Confirm");

        // Viewing the habit
        solo.clickOnText("TestHabit-01-03-01");
        //solo.waitForText("TestHabit-01-03-01", 1, 2000);
        //solo.waitForText("TestReason-01-03-01", 1, 2000);
        //solo.waitForText("Private Habit", 1, 2000);
        solo.clickOnView(solo.getView(R.id.all_habit_button));

        // Removing the habit
        solo.clickOnText("TestHabit-01-03-01");
        solo.clickOnView(solo.getView(R.id.edit_button));
        solo.clickOnButton("Remove Habit");
    }

    /*
    @Test
    public void checkList() {
        solo.assertCurrentActivity("Wrong Activity!", MainActivity.class);
        solo.clickOnButton("ADD CITY");
        solo.enterText((EditText) solo.getView(R.id.editText_name), "Edmonton");
        solo.clickOnButton("CONFIRM");
        solo.clearEditText((EditText) solo.getView(R.id.editText_name));

        assertTrue(solo.waitForText("Edmonton", 1, 2000));

        solo.clickOnButton("ClEAR ALL");

        assertFalse(solo.searchText("Edmonton"));
    }

    @Test
    public void checkCityListItem() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnButton("ADD CITY");
        solo.enterText((EditText) solo.getView(R.id.editText_name), "Edmonton");
        solo.clickOnButton("CONFIRM");
        solo.waitForText("Edmonton", 1, 2000);

        MainActivity activity = (MainActivity) solo.getCurrentActivity();
        final ListView cityList = activity.cityList;
        String city = (String) cityList.getItemAtPosition(0);

        assertEquals("Edmonton", city);
    }

    @Test
    public void checkActivitySwitch() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnButton("ADD CITY");
        solo.enterText((EditText) solo.getView(R.id.editText_name), "Edmonton");
        solo.clickOnButton("CONFIRM");
        solo.waitForText("Edmonton", 1, 2000);

        MainActivity activity = (MainActivity) solo.getCurrentActivity();
        final ListView cityList = activity.cityList;

        solo.clickOnView(cityList.getChildAt(0));

        assertTrue(solo.waitForActivity(showActivity.class, 2000));
    }

    @Test
    public void checkCityName() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnButton("ADD CITY");
        solo.enterText((EditText) solo.getView(R.id.editText_name), "Edmonton");
        solo.clickOnButton("CONFIRM");
        solo.waitForText("Edmonton", 1, 2000);

        MainActivity activity = (MainActivity) solo.getCurrentActivity();
        final ListView cityList = activity.cityList;

        solo.clickOnView(cityList.getChildAt(0));
        solo.waitForActivity(showActivity.class, 2000);

        assertEquals("Edmonton", ((TextView) solo.getView(R.id.city_text_view)).getText().toString());
    }

    @Test
    public void checkBackButton() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnButton("ADD CITY");
        solo.enterText((EditText) solo.getView(R.id.editText_name), "Edmonton");
        solo.clickOnButton("CONFIRM");
        solo.waitForText("Edmonton", 1, 2000);

        MainActivity activity = (MainActivity) solo.getCurrentActivity();
        final ListView cityList = activity.cityList;

        solo.clickOnView(cityList.getChildAt(0));
        solo.waitForActivity(showActivity.class, 2000);
        solo.clickOnButton("BACK");

        assertTrue(solo.waitForActivity(MainActivity.class, 2000));
        assertTrue(solo.searchText("Edmonton"));
    }
    */

    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }
}