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

    /**
     * Constructor for a request
     * @param requesterEmail
     * @param requestedEmail
     * @param time
     */
    public Request(String requesterEmail, String requestedEmail, long time) {
        this.requesterEmail = requesterEmail;
        this.requestedEmail = requestedEmail;
        this.time = time;
    }

    /**
     * Get requester email
     * @return
     */
    public String getRequesterEmail() {
        return requesterEmail;
    }

    /**
     * Get requested email
     * @return
     */
    public String getRequestedEmail() {
        return requestedEmail;
    }

    /**
     * Get time the request was made at
     * @return
     */
    public long getTime() {
        return time;
    }

}
