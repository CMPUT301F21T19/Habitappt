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

package com.CMPUT301F21T19.habitappt.Utils;


import android.util.Log;

import androidx.annotation.NonNull;

import com.CMPUT301F21T19.habitappt.Entities.Habit;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class VisualIndicator {
    private Habit habit;
    private double score;
    private long eventListSize = 0;
    private FirebaseAuth auth;
    private Boolean isFollowing;
    private String user;
    private ArrayList<Long> recordedEventDates = new ArrayList<Long>();
    private long eventDate;
    private boolean getIsTodayEventDone = false;

    /**
     * Instantiates a new visual Indicator object
     * @param habit         Used to retrieve dateToStart & datesToDo in order to calculate the score
     * @param isFollowing   A bool which return determine we are trying to view a followers habits
     * @param user          Retrieve username in order to access the habit info (used to calculate
     *                      score for user that you are following)
     */
    public VisualIndicator(Habit habit, Boolean isFollowing, String user) {
        this.habit = habit;
        this.isFollowing = isFollowing;
        this.user = user;
    }

    /**
     * Calculates the number of events the user has done (according to datesToDo) per total number
     * of habits that were supposed to be done
     * @return score
     */
    public double getScore() {

        // Create a start date and use it to instantiate a calender object (which we will be using more)
        Date start_date = new Date(this.habit.getDateToStart());
        Calendar c = Calendar.getInstance();
        c.setTime(start_date);

        Date current_date = new Date();

        long totalNumHabits = 0; // Tracks total days habit is to be performed

        // Iterates through all the dates from start to current
        // Checks to see if habit needs to be performed at each date and if event has been completed
        //      at a specified date according to datesToDo
        // When loop is done, counter should have the total number of habit that should have been
        //      completed as well as the total events the user has recorded
        while (start_date.before(current_date)) {
            // Checks if the habit should be done on a Sunday
            if (c.get(Calendar.DAY_OF_WEEK) == 1) {
                if (this.habit.getDateSelected(6)) {
                    totalNumHabits += 1;
                    // Loops through all habit events for a given habit
                    for (int i = 0; i < recordedEventDates.size(); i++) {
                        Date eventDate = new Date(recordedEventDates.get(i));
                        // Checks if the habit event that was supposed to be done for sunday
                        //      (according to datesToDo) has been done
                        if (start_date.getDate() == eventDate.getDate()) {
                            eventListSize += 1;
                        }
                        // Checks if the habit event that was supposed to be done today has been
                        //      done today
                        if (current_date.getDate() == start_date.getDate() && eventDate.getDate() == current_date.getDate()) {
                            this.getIsTodayEventDone = true;
                        }
                    }
                }
              // Checks if habit falls under all other days of the week
            } else if (this.habit.getDateSelected(c.get(Calendar.DAY_OF_WEEK) - 2)) {
                totalNumHabits += 1;
                for (int i = 0; i < recordedEventDates.size(); i++) {
                    Date eventDate = new Date(recordedEventDates.get(i));
                    // Checks if the habit event that was supposed to be done for every other day
                    //      (according to datesToDo) has been done
                    if (start_date.getDate() == eventDate.getDate()) {
                        eventListSize += 1;
                    }
                    // Checks if the habit event that was supposed to be done today has been
                    //      done today
                    if (current_date.getDate() == start_date.getDate() && eventDate.getDate() == current_date.getDate()) {
                        this.getIsTodayEventDone = true;
                    }
                }
            }

            c.add(Calendar.DATE, 1); // Increment the start date to one day forward
            start_date = c.getTime();
        }


        // Checks if there is no habits that need to be done yet
        if (totalNumHabits == 0) {
            this.score = 100;
            return this.score;
        }
        this.score = (((float) eventListSize * 1/totalNumHabits) * 100);
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
     * Retrieves an array of dates of habit events of a given habit and stores it into eventListSize
     */
    public void populateEventList() {
        auth = FirebaseAuth.getInstance();
        // Accesses and stores the event dates of a follower from the database in order to calculate
        //      the score for the followers habit progress
        if (isFollowing) {
            CollectionReference eventCollectionReference = FirebaseFirestore.getInstance()
                    .collection("Users")
                    .document(this.user)
                    .collection("Habits")
                    .document(String.valueOf(this.habit.getId()))
                    .collection("Event Collection");

            eventCollectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("info", document.getId() + " => " + document.getData());
                            Long eventDate = (Long) document.getData().get("eventDate");
                            recordedEventDates.add(eventDate);
                        }
                    } else {
                        Log.d("TAG", "Error getting documents: ", task.getException());

                    }
                }
            });
            // Accessing the event dates of the user themselves from the database in order to
            //      calculate the score of the the users habit progress
        } else {
            CollectionReference eventCollectionReference = FirebaseFirestore.getInstance()
                    .collection("Users")
                    .document(auth.getCurrentUser().getEmail())
                    .collection("Habits")
                    .document(String.valueOf(this.habit.getId()))
                    .collection("Event Collection");

            eventCollectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("info", document.getId() + " => " + document.getData());
                            Long eventDate = (Long) document.getData().get("eventDate");
                            recordedEventDates.add(eventDate);
                        }
                    } else {
                        Log.d("TAG", "Error getting documents: ", task.getException());

                    }
                }
            });
        }


    }

    /**
     * return
     * @return      A boolean to denote whether the user has completed a habit event for a habit
     *              that was supposed to be done today
     */
    public boolean GetIsTodayEventDone() {
        return getIsTodayEventDone;
    }
}