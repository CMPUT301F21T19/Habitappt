package com.CMPUT301F21T19.habitappt;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * Represents an event associated to a given habit
 * A habit has an event for the corresponding dates that the user
 * has completed a habit
 */
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
    String id;

    /**
     * associated parent habit to event
     */
    private Habit parentHabit;

    /**
     * gets image assoicated to event
     * @return img
     */
    public Bitmap getImg() {
        return img;
    }

    /**
     * sets image assoicated to event
     * @param img
     */
    public void setImg(Bitmap img) {
        this.img = img;
    }

    /**
     * constructor that takes the associated habit and basic event info w/out image
     * @param comment
     * @param eventDate
     * @param parentHabit
     * @param id
     */
    public HabitEvent(String comment, long eventDate,Habit parentHabit, String id){
        this.comment = comment;
        this.eventDate = eventDate;
        this.parentHabit = parentHabit;
        this.id = id;
    }

    /**
     * constructor that takes the associated habit and basic event info w/ given image
     * @param comment
     * @param eventDate
     * @param parentHabit
     * @param id
     * @param img
     */
    public HabitEvent(String comment, long eventDate,Habit parentHabit, String id, Bitmap img){
        this.comment = comment;
        this.eventDate = eventDate;
        this.parentHabit = parentHabit;
        this.id = id;
        this.img = img;
    }

    /**
     * base constructor that only takes parent habit
     * @param parentHabit
     */
    public HabitEvent(Habit parentHabit){
        this.parentHabit = parentHabit;
        this.eventDate = GregorianCalendar.getInstance().getTimeInMillis();
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
     * @param id
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
     * @param comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * gets event date
     * @return event date
     */
    public long getEventDate() {
        return eventDate;
    }

    /**
     * sets event date
     * @param eventDate
     */
    public void setEventDate(long eventDate) {
        this.eventDate = eventDate;
    }

    /**
     * gets habit associated to event
     * @return habit
     */
    public Habit getParentHabit() {
        return parentHabit;
    }

    /**
     * sets parent habit of event
     * @param parentHabit
     */
    public void setParentHabit(Habit parentHabit) {
        this.parentHabit = parentHabit;
    }
}
