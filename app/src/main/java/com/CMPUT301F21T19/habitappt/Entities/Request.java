/**
 * Copyright 2021 - 2021 CMPUT301F21T19 (Habitappt). All rights reserved. This document nor any
 * part of it may be reproduced, stored in a retrieval system or transmitted in any for or by any
 * means without prior permission of the members of CMPUT301F21T19 or by the professor and any
 * authorized TAs of the CMPUT301 class at the University of Alberta, fall term 2021.
 *
 * Class: Request
 *
 * Description:
 * Contains information about a follow request made by another user to the given users account
 *
 * @version "%1%,%5%"
 *
 *
 */

package com.CMPUT301F21T19.habitappt.Entities;

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
