package com.CMPUT301F21T19.habitappt;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class VisualIndicator {
    private Habit habit;
    private long score;
    private long eventListSize;
    ArrayList<HabitEvent> eventList;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseStorage storage;


    public VisualIndicator(Habit habit) {
        this.habit = habit;
    }

    public long getScore() {
        populateEventList();
        // Not sure how to properly instantiate the correct date
        Date start_date = new Date(this.habit.getDateToStart());
        Calendar c = Calendar.getInstance();
        c.setTime(start_date);

        Date current_date = new Date();

        long counter = 0; // Tracks total days habit is to be performed
        System.out.println("\n\n\n " + "Habit Title: " + this.habit.getTitle());
        System.out.println("Start Date: " + start_date);
        System.out.println("Current Date: " + current_date);
        // iterates through all the dates from start to current
        // Checks to see if habit needs to be performed at each date
        // When loop if done, counter should have the total number of habit that should have been completed
        while (start_date.before(current_date)) {
            if (c.get(Calendar.DAY_OF_WEEK) == 1) {
                if (this.habit.getDateSelected(6)) {
                    counter += 1;
                }
            } else if (this.habit.getDateSelected(c.get(Calendar.DAY_OF_WEEK) - 2)) {
                counter += 1;
            }
            c.add(Calendar.DATE, 1);
            start_date = c.getTime();
        }
        System.out.println("Counter: " + counter);
        System.out.println("number of events: " + getEventListSize());


        // Checks if there is no habits that need to be done yet
        if (counter == 0) {
            this.score = 100;
            return this.score;
            //more than one event shouldve been done and none were completed
        } else if (counter > 0 && getEventListSize() == 0) {
            System.out.println("score " + score);
            this.score = 0;
            return this.score;
        }
        this.score = (getEventListSize() / counter) * 100;
        return this.score;
    }

    public void setScore(long score) {
        this.score = score;
    }


    public void populateEventList() {
        auth = FirebaseAuth.getInstance();
        CollectionReference eventCollectionReference = FirebaseFirestore.getInstance()
                .collection("Users")
                .document(auth.getCurrentUser().getEmail())
                .collection("Habits")
                .document(String.valueOf(this.habit.getId()))
                .collection("Event Collection");
        //System.out.println("First");
        eventCollectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    InitializeEventListSize();
                    int size;
                    size = task.getResult().size();
                    System.out.println("The number of the events corresponding to habit title (" + habit.getTitle() + ") is " + size);
                    EventListSize(size);

                } else {
                    System.out.println("error");
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }

        });
//
//        eventCollectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                int size = 0;
//                habit.InitializeEventListSize();
//                for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
//                    size++;
//                    habit.IncrementEventListSize();
//                }
//                System.out.println("The number of the events corresponding to habit title (" + habit.getTitle() + ") is " + size);
//            }
//        });
//
//    }

    }
    public void InitializeEventListSize() {
        this.eventListSize = 0;
    }

    public void EventListSize(long size) {
        this.eventListSize = size;
    }

    public long getEventListSize() {
        return this.eventListSize;
    }
}