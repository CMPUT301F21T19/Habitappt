/**
 * Copyright 2021 - 2021 CMPUT301F21T19 (Habitappt). All rights reserved. This document nor any
 * part of it may be reproduced, stored in a retrieval system or transmitted in any for or by any
 * means without prior permission of the members of CMPUT301F21T19 or by the professor and any
 * authorized TAs of the CMPUT301 class at the University of Alberta, fall term 2021.
 *
 * Class: Habit
 *
 * Description: An object which contains all of the attributes of a habit as entered by the user
 *
 * Changelog:
 * =|Version|=|User(s)|==|Date|========|Description|================================================
 *   1.0       Sohaib    Oct-20-2021   Halfway through initial UI framework
 *   1.1       Andrew    Oct-21-2021   Implemented nav bar and fragment switching in main container!
 *   1.3       Andrew    Oct-23-2021   Update Habit.java
 *   1.4       Andrew    Oct-23-2021   Adding habits!
 *   1.7       Hamzah    Oct-25-2021   Modified edit Habit
 *   1.9       Hamzah    Oct-31-2021   Added getters and setters in habit class for habiteventslist
 *   1.10      Logan     Nov-01-2021   added isPrivate to habit constructor and removed unused java classes
 *   1.11      Hamzah    Nov-01-2021   isPrivate UI and database implementation
 *   1.13      Andrew    Nov-03-2021   Tests for Habit and HabitEvent
 * =|=======|=|======|===|====|========|===========|================================================
 */

package com.CMPUT301F21T19.habitappt;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Habit {
    private boolean isPrivate;
    private String title;
    private String reason;
    private long dateToStart;
    private ArrayList<Boolean> datesToDo;
    private ArrayList<HabitEvent> habitEvents;
    String id;

    private long score;

    /**
     * Create a habit object and assign parameters
     * @param title The title of the habit
     * @param reason A reason for why the user want to create the habit
     * @param dateToStart The date the users is scheduled to start the habit
     * @param datesToDo the weekly frequency which specifies when the user is to complete the habit
     * @param id A unique identifier for the habit object
     * @param isPrivate Denotes whether this habit is private or not
     */
    public Habit(String title, String reason, long dateToStart, ArrayList<Boolean> datesToDo, String id, boolean isPrivate) {
        this.isPrivate = isPrivate;
        this.title = title;
        this.reason = reason;
        this.dateToStart = dateToStart;
        this.datesToDo = datesToDo;
        this.id = id;
    }
    
    /**
     * Instantiates the Habit object
     */
    public Habit() {
        this.isPrivate = false;
        this.title = "";
        this.reason = "";
        this.dateToStart = GregorianCalendar.getInstance().getTimeInMillis();
        this.datesToDo = new ArrayList<>();
        for(int i=0;i<7;i++){
            datesToDo.add(false);
        }
        id = "-1";
    }

    public String getId(){
        return id;
    }

    public ArrayList<Boolean> getWeekly(){
        return datesToDo;
    }

    public Boolean getDateSelected(int i){
        if(i > datesToDo.size() || i < 0){
            return null;
        }
        return datesToDo.get(i);
    }

    public Boolean setDateSelected(int i,Boolean b){
        if(i >= datesToDo.size() || i < 0){
            return false;
        }
        datesToDo.set(i,b);
        return true;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public long getDateToStart() {
        return dateToStart;
    }

    public void setDateToStart(long dateToStart) {
        this.dateToStart = dateToStart;
    }

    public void setHabitEvents(ArrayList<HabitEvent> habitEvents) {
        this.habitEvents = habitEvents;
    }

    public ArrayList<HabitEvent> getHabitEvents(){
        return habitEvents;
    }

    public boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    /**
     * Calculates the score of a given habit to track progress of how often the habit is being completed
     * @return Percentage of of habit events completed per total number of habit occurrences
     */
    public long calculateScore() {

        Date start_date = new Date(1000L * this.dateToStart);
        Calendar c = Calendar.getInstance();
        c.setTime(start_date);

        Date current_date = new Date();

        long counter = 0; // Tracks total days habit is to be performed

        // iterates through all the dates from start to current
        // Checks to see if habit needs to be performed at each date
        // When loop if done, counter should have the total number of habit that should have been completed
        while (start_date.before(current_date)) {
            if (this.datesToDo.get(c.get(Calendar.DAY_OF_WEEK) - 1)) {
                counter += 1;
            }
            c.add(Calendar.DATE, 1);
            start_date = c.getTime();

        }

        // Checks if habit event is empty
        if (habitEvents == null) {
            return (long) -1;
        }

        return (long) (habitEvents.size() / counter) * 100;
    }

    public long getScore() { return score; }

    public void setScore(int newScore) { this.score = newScore;}

}

