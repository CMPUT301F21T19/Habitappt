/**
 * Copyright 2021 - 2021 CMPUT301F21T19 (Habitappt). All rights reserved. This document nor any
 * part of it may be reproduced, stored in a retrieval system or transmitted in any for or by any
 * means without prior permission of the members of CMPUT301F21T19 or by the professor and any
 * authorized TAs of the CMPUT301 class at the University of Alberta, fall term 2021.
 *
 * Class: VisualIndicator
 *
 * Description: Creates a score denoting the progress of a given habit
 */

package com.CMPUT301F21T19.habitappt;


import static io.grpc.okhttp.internal.Platform.logger;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;

public class VisualIndicator {
    private Habit habit;
    private double score;
    private long eventListSize;
    private FirebaseAuth auth;
    private Boolean isFollowing;
    private String user;

    /**
     * Instantiates a new visual Indicator object
     * @param habit
     */
    public VisualIndicator(Habit habit, Boolean isFollowing, String user) {
        this.habit = habit;
        this.isFollowing = isFollowing;
        this.user = user;
    }

    /**
     * Calculates the the number of events associated with the habit by the total number of habits
     * that were supposed to be done
     * @return score
     */
    public double getScore() {
        // Create a start date and use it to instantiate calender object (which we will be using more)
        Date start_date = new Date(this.habit.getDateToStart());
        Calendar c = Calendar.getInstance();
        c.setTime(start_date);

        Date current_date = new Date();

        long totalNumHabits = 0; // Tracks total days habit is to be performed

        // iterates through all the dates from start to current
        // Checks to see if habit needs to be performed at each date
        // When loop if done, counter should have the total number of habit that should have been completed
        while (start_date.before(current_date)) {
            // Checks if the habit falls on a Sunday
            if (c.get(Calendar.DAY_OF_WEEK) == 1) {
                if (this.habit.getDateSelected(6)) {
                    totalNumHabits += 1;
                }
              // Checks if habit falls under all other days of the week
            } else if (this.habit.getDateSelected(c.get(Calendar.DAY_OF_WEEK) - 2)) {
                totalNumHabits += 1;
            }
            c.add(Calendar.DATE, 1); // Increment the start date to one day forward
            start_date = c.getTime();
        }

        // Checks if there is no habits that need to be done yet
        if (totalNumHabits == 0) {
            this.score = 100;
            return this.score;
        }
        this.score = (((float) getEventListSize() * 1/totalNumHabits) * 100);
        return this.score;
    }

    /**
     * Sets the score of a given habit (Primarily used for testing)
     * @param score
     */
    public void setScore(long score) {
        this.score = score;
    }

    /**
     * Retrieves the number of habit events of a given habit and stores that value into eventListSize
     */
    public void populateEventList() {
        auth = FirebaseAuth.getInstance();
        if (isFollowing) {
            CollectionReference eventCollectionReference = FirebaseFirestore.getInstance()
                    .collection("Users")
                    .document(this.user)
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
                        size = task.getResult().size(); // gets the number of events in the collection
                        setEventListSize(size);

                    } else {
                        Log.d("TAG", "Error getting documents: ", task.getException());
                    }
                }
            });
        } else {
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
                        size = task.getResult().size(); // gets the number of events in the collection
                        setEventListSize(size);

                    } else {
                        Log.d("TAG", "Error getting documents: ", task.getException());
                    }
                }
            });
        }


    }

    /**
     * Initializes eventListSize to be 0
     */
    public void InitializeEventListSize() {
        this.eventListSize = 0;
    }

    /**
     * Sets the eventListSize to the given parameter
     * @param size
     */
    public void setEventListSize(long size) {
        this.eventListSize = size;
    }

    /**
     * Returns the size of the eventListSize
     * @return eventListSize
     */
    public long getEventListSize() {
        return this.eventListSize;
    }
}