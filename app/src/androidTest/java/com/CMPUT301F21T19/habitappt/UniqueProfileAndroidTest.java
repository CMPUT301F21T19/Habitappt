package com.CMPUT301F21T19.habitappt;

import android.app.Activity;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.CMPUT301F21T19.habitappt.Activities.LoginActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

public class UniqueProfileAndroidTest {

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
    }

    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void createUniqueAccount(){
        solo.clickOnView(solo.getView(R.id.signup));

        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(10);
        final String alphabet = "abcdefghijklmnopqrstuvwxyz";
        for(int i=0;i<10;++i)
            sb.append(alphabet.charAt(random.nextInt(alphabet.length())));

        solo.enterText((EditText) solo.getView(R.id.email),sb.toString() + "@test.com");
        solo.enterText((EditText) solo.getView(R.id.password),"tester");
        solo.clickOnButton("Confirm");
        solo.sleep(3000);

    }

    @Test
    public void createUsedAccount(){
        solo.clickOnView(solo.getView(R.id.signup));
        solo.enterText((EditText) solo.getView(R.id.email),"asdf@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.password),"Happy123!");
        solo.clickOnButton("Confirm");
        solo.sleep(3000);
    }


    @After
    public void tearDown(){ solo.finishOpenedActivities(); }

}
