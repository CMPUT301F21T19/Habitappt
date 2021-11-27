package com.CMPUT301F21T19.habitappt.Entities;

/**
 * Contains information about a follow request
 */
public class Request {
    /**
     * The email of the user that made the follow request
     */
    private String requesterEmail;

    /**
     * The email of the user that the request is sent to
     */
    private String requestedEmail;

    private long time;

    public Request(String requesterEmail, String requestedEmail, long time) {
        this.requesterEmail = requesterEmail;
        this.requestedEmail = requestedEmail;
        this.time = time;
    }

    public String getRequesterEmail() {
        return requesterEmail;
    }

    public String getRequestedEmail() {
        return requestedEmail;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
