package com.CMPUT301F21T19.habitappt;

import android.app.Activity;
import android.view.KeyEvent;
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

/**
 * Tests followers requests/acceptance/etc and also view followers
 *
 * Make sure to run requestToFollow, acceptFollowRequests, denyFollowRequests, viewFollowing respectively
 */
public class FollowersAndroidTest {
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

    /**
     *  Implements user story US 05.01.01
     */
    @Test
    public void requestToFollow(){

        solo.enterText((EditText) solo.getView(R.id.username),"asdf@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.password),"Happy123!");
        solo.clickOnView(solo.getView(R.id.login_button));

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.make_request));

        solo.enterText((EditText) solo.getView(R.id.requested_user),"tester@test.com");
        solo.clickOnButton("Send");

    }

    /**
     *  Implements user story US 05.02.01
     */
    @Test
    public void acceptFollowRequests()  {

        solo.enterText((EditText) solo.getView(R.id.username),"tester@test.com");
        solo.enterText((EditText) solo.getView(R.id.password),"tester");
        solo.clickOnView(solo.getView(R.id.login_button));

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.requests_button));

        solo.clickOnView(solo.getView(R.id.requests_button));
        solo.clickOnView(solo.getView(R.id.requester_email_text_view));
        solo.clickOnButton("Accept");
        solo.clickOnView(solo.getView(R.id.followers_button));
        solo.sleep(3000);

    }

    /**
     *  Implements user story US 05.02.01
     */
    @Test
    public void denyFollowRequests()  {

        solo.enterText((EditText) solo.getView(R.id.username),"tester@test.com");
        solo.enterText((EditText) solo.getView(R.id.password),"tester");
        solo.clickOnView(solo.getView(R.id.login_button));

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.requests_button));
        solo.clickOnView(solo.getView(R.id.requester_email_text_view));
        solo.clickOnButton("Deny");
        solo.clickOnView(solo.getView(R.id.followers_button));
        solo.sleep(3000);

    }

    /**
     *  Implements user story US 05.03.01
     */
    @Test
    public void viewFollowing() {

        solo.enterText((EditText) solo.getView(R.id.username),"asdf@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.password),"Happy123!");
        solo.clickOnView(solo.getView(R.id.login_button));

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.following_button));

        solo.clickOnView(solo.getView(R.id.profile_list));
        solo.sleep(5000);

    }

    @After
    public void tearDown(){ solo.finishOpenedActivities(); }

}
