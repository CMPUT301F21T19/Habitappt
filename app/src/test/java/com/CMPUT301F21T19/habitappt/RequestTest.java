package com.CMPUT301F21T19.habitappt;

import static org.junit.Assert.assertTrue;

import com.CMPUT301F21T19.habitappt.Entities.Follower;
import com.CMPUT301F21T19.habitappt.Entities.Request;

import org.junit.Before;
import org.junit.Test;

public class RequestTest {

    Request mockRequest;
    String requesterEmail = "friend@mail.com";
    String requestedEmail = "account@mail.com";

    long time = 1638116585691l;

    @Before
    public void setup() {}

    //test all of request getters!, requests do not have setter functionality
    @Test
    public void testGetters(){
        mockRequest = new Request(requesterEmail, requestedEmail, time);
        assertTrue(mockRequest.getRequesterEmail().equals(requesterEmail));
        assertTrue(mockRequest.getRequestedEmail().equals(requestedEmail));
        assertTrue(mockRequest.getTime() == time);
    }
}
