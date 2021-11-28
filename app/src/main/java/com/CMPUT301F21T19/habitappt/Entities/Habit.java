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

package com.CMPUT301F21T19.habitappt.Entities;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class Habit {
    private boolean isPrivate;
    private String title;
    private String reason;
    private long dateToStart;
    private ArrayList<Boolean> datesToDo;
    private ArrayList<HabitEvent> habitEvents;
    private String id;
    private long index;


    /**
     * Create a habit object and assign parameters
     *
     * @param title       The title of the habit
     * @param reason      A reason for why the user want to create the habit
     * @param dateToStart The date the users is scheduled to start the habit
     * @param datesToDo   the weekly frequency which specifies when the user is to complete the habit
     * @param id          A unique identifier for the habit object
     * @param isPrivate   Denotes whether this habit is private or not
     */
    public Habit(String title, String reason, long dateToStart, ArrayList<Boolean> datesToDo, String id, boolean isPrivate,long index) {
        this.isPrivate = isPrivate;
        this.title = title;
        this.reason = reason;
        this.dateToStart = dateToStart;
        this.datesToDo = datesToDo;
        this.id = id;
        this.index = index;
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
        for (int i = 0; i < 7; i++) {
            datesToDo.add(false);
        }
        id = "-1";
    }

    /**
     * @return Uniquiely identifiable id of type string for the habit object
     */
    public String getId() {
        return id;
    }

    public void setId(String id){this.id = id; }

    /**
     * @return An arraylist which contains the days of the week which the habit should occur
     */
    public ArrayList<Boolean> getWeekly() {
        return datesToDo;
    }

    /**
     * @return Uniquiely identifiable id of type string for the habit object
     */
    public Boolean getDateSelected(int i) {
        if (i > datesToDo.size() || i < 0) {
            return null;
        }
        return datesToDo.get(i);
    }

    /**
     * @return A bool confirming if the date has been added into datesToDo
     */
    public Boolean setDateSelected(int i, Boolean b) {
        if (i >= datesToDo.size() || i < 0) {
            return false;
        }
        datesToDo.set(i, b);
        return true;
    }

    /**
     * @return A string for the habit title
     */
    public String getTitle() {
        return title;
    }

    /**
     * sets the habit title to the the given parameter
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return A string for the habit reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * sets the habit reason to the the given parameter
     *
     * @param reason
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * @return the date to start a habit of type long
     */
    public long getDateToStart() {
        return dateToStart;
    }

    /**
     * sets the dateToStart to the the given parameter
     *
     * @param dateToStart
     */
    public void setDateToStart(long dateToStart) {
        this.dateToStart = dateToStart;
    }

    /**
     * sets the habit events according to the parameter
     *
     * @param habitEvents
     */
    public void setHabitEvents(ArrayList<HabitEvent> habitEvents) {
        this.habitEvents = habitEvents;
    }

    /**
     * @return the habit event object associated with the habit object
     */
    public ArrayList<HabitEvent> getHabitEvents() {
        return habitEvents;
    }

    /**
     * @return Uniquiely A bool to denote whether the habit is private
     */
    public boolean getIsPrivate() {
        return isPrivate;
    }

    /**
     * sets the habit privacy according the it's parameter
     *
     * @param isPrivate
     */
    public void setIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    /**
     * Gets the index of the habit in the users list
     * @return
     */
    public long getIndex() {
        return index;
    }

    /**
     * Sets the index of the habit in the users list
     * @param index
     */
    public void setIndex(long index) {
        this.index = index;
    }
}



