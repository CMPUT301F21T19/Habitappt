package com.CMPUT301F21T19.habitappt;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class Habit {
    private String title;
    private String reason;
    private long dateToStart;


    public Habit(String title,String reason,long dateToStart){
        this.title = title;
        this.reason = reason;
        this.dateToStart = dateToStart;
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
}
