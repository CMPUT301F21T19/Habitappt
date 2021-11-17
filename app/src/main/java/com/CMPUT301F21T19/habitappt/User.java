package com.CMPUT301F21T19.habitappt;

/**
 * Contains information about a user
 */
public class User {
    /**
     * The email of the user
     */
    private String userEmail;

    public User(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
