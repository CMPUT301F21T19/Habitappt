package com.CMPUT301F21T19.habitappt;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
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

    public Habit(String title, String reason, long dateToStart, ArrayList<Boolean> datesToDo, String id, boolean isPrivate) {
        this.isPrivate = isPrivate;
        this.title = title;
        this.reason = reason;
        this.dateToStart = dateToStart;
        this.datesToDo = datesToDo;
        this.id = id;
    }

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
        if(i > datesToDo.size()){
            return false;
        }
        return datesToDo.get(i);
    }

    public Boolean setDateSelected(int i,Boolean b){
        if(i >= datesToDo.size() && i < 0){
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





    public void calculateScore() {
            for (int i = 0; i < 7; i++) {
                datesToDo.add(false);

            }
        habitEvents.size();
        datesToDo.size();
        //look through each a habits habit events,
        // find how many days a habit event should have been done,
        // how many has been done
        // score returns percent 0-100
    }

    public long getScore() { return score; }

    public void setScore(int newScore) { this.score = newScore; }
}

