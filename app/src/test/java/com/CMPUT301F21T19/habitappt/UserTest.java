package com.CMPUT301F21T19.habitappt;

import com.CMPUT301F21T19.habitappt.Entities.Follower;
import com.CMPUT301F21T19.habitappt.Entities.User;

import org.junit.Before;
import org.junit.Test;

public class UserTest {

    User mockUser;
    String userEmail = "account@mail.com";

    @Before
    public void setup() {}

    //test user following getter, should throw NoClassDefFoundError since db instance not created for user creation
    @Test(expected = NoClassDefFoundError.class)
    public void testGetters(){
        mockUser = new User(userEmail);
        mockUser.getUserEmail();
    }


}
