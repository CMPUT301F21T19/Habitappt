package com.CMPUT301F21T19.habitappt;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class Habit {
    private String name;

    Habit(String name){
        this.name = name;
    }

    String getName(){
        return this.name;
    }
}