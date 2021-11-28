package com.CMPUT301F21T19.habitappt;

import com.CMPUT301F21T19.habitappt.Entities.Follower;
import com.CMPUT301F21T19.habitappt.Entities.Following;

import org.junit.Before;
import org.junit.Test;

public class FollowingTest {

    Following mockFollowing;

    String userEmail = "friend@mail.com";

    @Before
    public void setup() {

    }

    //test user following getter, should throw NoClassDefFoundError since db instance not created for user creation
    @Test(expected = NoClassDefFoundError.class)
    public void testGetters(){
        mockFollowing = new Following(userEmail);
        mockFollowing.getUserEmail();
    }
}
