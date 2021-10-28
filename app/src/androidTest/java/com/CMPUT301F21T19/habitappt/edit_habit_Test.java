package com.CMPUT301F21T19.habitappt;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import android.app.Activity;
import android.util.Log;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.ActivityTestRule;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class edit_habit_Test {
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

    @Test public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void addHabit() {
        solo.assertCurrentActivity("Wrong Activity",MainActivity.class);
        solo.clickOnView(solo.getView(R.id.add_habit_button));
        solo.enterText((EditText) solo.getView(R.id.habit_title),"Habit_1");
        solo.enterText((EditText) solo.getView(R.id.habit_reason),"Reason_1");
        solo.clickOnButton("M");
        solo.clickOnButton("W");
        solo.clickOnButton("F");
        solo.clickOnView(solo.getView(R.id.date_to_start));
        solo.setDatePicker(1, 2021, 10, 10);
        solo.clickOnButton(R.id.dialog_button);
        //solo.clickOnView(solo.getView(R.id.dialog_button));
    }


}

