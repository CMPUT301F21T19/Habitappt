/**
 * Copyright 2021 - 2021 CMPUT301F21T19 (Habitappt). All rights reserved. This document nor any
 * part of it may be reproduced, stored in a retrieval system or transmitted in any for or by any
 * means without prior permission of the members of CMPUT301F21T19 or by the professor and any
 * authorized TAs of the CMPUT301 class at the University of Alberta, fall term 2021.
 *
 * Class: HabitEvent
 *
 * Description: Represents an event associated to a given habit
 * A habit has an event for the corresponding dates that the user
 * has completed a habit
 *
 * @version "%1%,%5%"
 *
 *
 */
package com.CMPUT301F21T19.habitappt.Entities;

import android.graphics.Bitmap;

import java.util.GregorianCalendar;

public class HabitEvent {

    /**
     * basic information associated to event, including comments,
     * the date of the given habit, and an optional image
     */
    private String comment;
    private long eventDate;
    private Bitmap img;

    /**
     * id of habit
     */
    private String id;

    /**
     * associated parent habit to event
     */
    private Habit parentHabit;

    /**
     * gets image associated to event
     * @return img
     */
    public Bitmap getImg() {
        return img;
    }

    /**
     * sets image assoicated to event
     * @param img image of event
     */
    public void setImg(Bitmap img) {
        this.img = img;
    }



    /**
     * location lat and lon for current habit event location
     */
    double locationLat;
    double locationLon;

    /**
     * constructor that takes the associated habit and basic event info w/out image
     * @param comment comment associates to event
     * @param eventDate date associated to event
     * @param parentHabit parent habit for given event
     * @param id id of event in firestore
     */
    public HabitEvent(String comment, long eventDate,Habit parentHabit, String id, double locationLat, double locationLon){
        if(comment.length() > 20) {
            throw new IllegalArgumentException("Invalid Comment");
        }
        this.comment = comment;
        this.eventDate = eventDate;
        this.parentHabit = parentHabit;
        this.id = id;
        //base no location instantiation
        this.locationLat = locationLat;
        this.locationLon = locationLon;
    }
    /**
     * constructor that takes the associated habit and basic event info w/out image
     * @param comment comment associates to event
     * @param eventDate date associated to event
     * @param parentHabit parent habit for given event
     * @param id id of event in firestore
     */
    public HabitEvent(String comment, long eventDate,Habit parentHabit, String id){
        if(comment.length() > 20) {
            throw new IllegalArgumentException("Invalid Comment");
        }
        this.comment = comment;
        this.eventDate = eventDate;
        this.parentHabit = parentHabit;
        this.id = id;
        //base no location instantiation
        this.locationLat = -1;
        this.locationLon = -1;
    }

    /**
     * constructor that takes the associated habit and basic event info w/ given image
     * @param comment comment associates to event
     * @param eventDate date associated to event
     * @param parentHabit parent habit for given event
     * @param id id of event in firestore
     * @param img image of event
     */
    public HabitEvent(String comment, long eventDate,Habit parentHabit, String id, Bitmap img){
        if(comment.length() > 20) {
            throw new IllegalArgumentException("Invalid Comment");
        }
        this.comment = comment;
        this.eventDate = eventDate;
        this.parentHabit = parentHabit;
        this.id = id;
        this.img = img;
        //base no location instantiation
        this.locationLat = -1;
        this.locationLon = -1;
    }

    /**
     * base constructor that only takes parent habit
     * @param parentHabit
     */
    public HabitEvent(Habit parentHabit){
        this.parentHabit = parentHabit;
        this.eventDate = GregorianCalendar.getInstance().getTimeInMillis();
        //base no location instantiation
        this.locationLat = -1;
        this.locationLon = -1;
    }

    /**
     * gets id associated to habit
     * @return id
     */
    public String getId(){
        return id;
    }

    /**
     * sets id associated to habit
     * @param id id of event
     */
    public void setId(String id){
        this.id = id;
    }

    /**
     * gets comment
     * @return comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * sets comment
     * @param comment comment associated to event
     */
    public void setComment(String comment) {
        if(comment.length() > 20) {
            throw new IllegalArgumentException("Invalid Comment");
        }
        this.comment = comment;
    }

    /**
     * gets event date
     * @return eventDate
     */
    public long getEventDate() {
        return eventDate;
    }

    /**
     * sets event date
     * @param eventDate date of event
     */
    public void setEventDate(long eventDate) {
        this.eventDate = eventDate;
    }

    /**
     * gets habit associated to event
     * @return parentHabit
     */
    public Habit getParentHabit() {
        return parentHabit;
    }

    /**
     * sets parent habit of event
     * @param parentHabit parent habit of event
     */
    public void setParentHabit(Habit parentHabit) {
        this.parentHabit = parentHabit;
    }


    public double getLocationLat() {
        return locationLat;
    }

    public void setLocationLat(double locationLat) {
        this.locationLat = locationLat;
    }

    public double getLocationLon() {
        return locationLon;
    }

    public void setLocationLon(double locationLon) {
        this.locationLon = locationLon;
    }
}