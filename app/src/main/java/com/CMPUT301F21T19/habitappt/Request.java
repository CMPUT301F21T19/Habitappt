package com.CMPUT301F21T19.habitappt;

/**
 * Contains information about a follow request
 */
public class Request {
    /**
     * The email of the user that made the follow request
     */
    String requesterEmail;

    /**
     * The email of the user that the request is sent to
     */
    String requestedEmail;

    long time;

    public Request(String requesterEmail, String requestedEmail, long time) {
        this.requesterEmail = requesterEmail;
        this.requestedEmail = requestedEmail;
        this.time = time;
    }

    public String getRequesterEmail() {
        return requesterEmail;
    }

    public void setRequesterEmail(String requesterEmail) {
        this.requesterEmail = requesterEmail;
    }

    public String getRequestedEmail() {
        return requestedEmail;
    }

    public void setRequestedEmail(String requestedEmail) {
        this.requestedEmail = requestedEmail;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
