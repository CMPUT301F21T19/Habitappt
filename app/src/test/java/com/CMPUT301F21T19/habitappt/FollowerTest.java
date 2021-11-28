package com.CMPUT301F21T19.habitappt;

import static org.junit.Assert.assertTrue;

import com.CMPUT301F21T19.habitappt.Entities.Follower;
import com.CMPUT301F21T19.habitappt.Entities.Habit;
import com.CMPUT301F21T19.habitappt.Entities.HabitEvent;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class FollowerTest {

    Follower mockFollower;
    String userEmail = "friend@mail.com";

    @Before
    public void setup() {}

    //test user following getter, should throw ExceptionInInitializerError since db instance not created for user creation
    @Test(expected = ExceptionInInitializerError.class)
    public void testGetters(){
        mockFollower = new Follower(userEmail);
        mockFollower.getUserEmail();
    }

}
