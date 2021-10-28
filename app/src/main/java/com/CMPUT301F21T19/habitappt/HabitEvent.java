package com.CMPUT301F21T19.habitappt;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class HabitEvent {

    private String comment;
    //image
    //location
    private long eventDate;

    String id;

    private Habit parentHabit;

    private Bitmap img;


    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }




    public HabitEvent(String comment, long eventDate,Habit parentHabit, String id){
        this.comment = comment;
        this.eventDate = eventDate;
        this.parentHabit = parentHabit;
        this.id = id;
    }

    public HabitEvent(String comment, long eventDate,Habit parentHabit, String id, Bitmap img){
        this.comment = comment;
        this.eventDate = eventDate;
        this.parentHabit = parentHabit;
        this.id = id;
        this.img = img;
    }

    public HabitEvent(Habit parentHabit){
        this.parentHabit = parentHabit;
        this.eventDate = GregorianCalendar.getInstance().getTimeInMillis();
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public long getEventDate() {
        return eventDate;
    }

    public void setEventDate(long eventDate) {
        this.eventDate = eventDate;
    }

    public Habit getParentHabit() {
        return parentHabit;
    }

    public void setParentHabit(Habit parentHabit) {
        this.parentHabit = parentHabit;
    }
}
