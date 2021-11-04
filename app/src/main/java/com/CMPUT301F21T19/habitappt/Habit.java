package com.CMPUT301F21T19.habitappt;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * Represent a habit that a user wants to implement in their life
 * A Habit is to be completed weekly for a set number of days, and completing them
 * Will increase the given users 'score'
 */
public class Habit {
    /**
     * denotes whether habit is public or private
     */
    private boolean isPrivate;
    /**
     * covers basic info on habit, such as title, reason, and date beginning
     */
    private String title;
    private String reason;
    private long dateToStart;
    /**
     * datesToDo represents the dates selected in the week to complete the given habit
     */
    private ArrayList<Boolean> datesToDo;
    /**
     * list of associated events to given habit
     */
    private ArrayList<HabitEvent> habitEvents;
    /**
     * id associated to db instance of habit
     */
    String id;

    /**
     * score associated to habit completion
     */
    private long score;

    /**
     * creates a habit with its associated info
     * @param title
     * @param reason
     * @param dateToStart
     * @param datesToDo
     * @param id
     * @param isPrivate
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
     * empty constructor habit with base parameters
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

    /**
     * returns habit id
     * @return id
     */
    public String getId(){
        return id;
    }

    /**
     * gets the dates habits are expected to be completed
     * @return list of dates
     */
    public ArrayList<Boolean> getWeekly(){
        return datesToDo;
    }

    /**
     * returns whether or not day passed must be completed or not,
     * or returns null if not expected to
     * @param i
     * @return null, true or false (depending on if date valid)
     */
    public Boolean getDateSelected(int i){
        if(i > datesToDo.size() || i < 0){
            return null;
        }
        return datesToDo.get(i);
    }

    /**
     * sets a date to be completed
     * @param i
     * @param b
     * @return true if date can be set, false if not
     */
    public Boolean setDateSelected(int i,Boolean b){
        if(i >= datesToDo.size() || i < 0){
            return false;
        }
        datesToDo.set(i,b);
        return true;
    }

    /**
     * gets Title
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * sets title
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * gets reason
     * @return reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * sets reason
     * @param reason
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * gets date to start
     * @return date to start
     */
    public long getDateToStart() {
        return dateToStart;
    }

    /**
     * sets date to start
     * @param dateToStart
     */
    public void setDateToStart(long dateToStart) {
        this.dateToStart = dateToStart;
    }

    /**
     * sets habit events data structure with passed arraylist
     * @param habitEvents
     */
    public void setHabitEvents(ArrayList<HabitEvent> habitEvents) {
        this.habitEvents = habitEvents;
    }

    /**
     * gets habit events list
     * @return habitEvents
     */
    public ArrayList<HabitEvent> getHabitEvents(){
        return habitEvents;
    }

    /**
     * checks whether private/public habit
     * @return isPrivate
     */
    public boolean getIsPrivate() {
        return isPrivate;
    }

    /**
     * sets privacy fo habit
     * @param isPrivate
     */
    public void setIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }





//    public void calculateScore() {
//            for (int i = 0; i < 7; i++) {
//                datesToDo.add(false);
//
//            }
//        habitEvents.size();
//        datesToDo.size();
//        //look through each a habits habit events,
//        // find how many days a habit event should have been done,
//        // how many has been done
//        // score returns percent 0-100
//    }

    /**
     * gets associated user score on meeting habit completions
     * @return score
     */
    public long getScore() { return score; }

    /**
     * sets associated user score on meeting habit completions
     * @param newScore
     */
    public void setScore(int newScore) { this.score = newScore; }
}

