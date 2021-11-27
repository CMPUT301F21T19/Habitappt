/**
 * Copyright 2021 - 2021 CMPUT301F21T19 (Habitappt). All rights reserved. This document nor any
 * part of it may be reproduced, stored in a retrieval system or transmitted in any for or by any
 * means without prior permission of the members of CMPUT301F21T19 or by the professor and any
 * authorized TAs of the CMPUT301 class at the University of Alberta, fall term 2021.
 *
 * Class: SharedHelper
 *
 * Description: Used to share methods and instance variables necessary between classes
 *
 * Changelog:
 * =|Version|=|User(s)|==|Date|========|Description|================================================
 *   1.0       Hamzah     Oct-31-2021   Refactored removing habit, moved functionality to SharedHelper.java class
 *   1.1       Hamzah     Oct-31-2021   Fixed incorrect deletion of habit event when cancel is selected
 *   1.2       Andrew     Nov-03-2021   Added shared method to sharedHelper class
 * =|=======|=|======|===|====|========|===========|================================================
 */

package com.CMPUT301F21T19.habitappt.Utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.CMPUT301F21T19.habitappt.Entities.Habit;
import com.CMPUT301F21T19.habitappt.Entities.HabitEvent;
import com.CMPUT301F21T19.habitappt.Entities.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SharedHelper {
    /**
     * deletes image given storage instance (after deleting event)
     * @param id
     * @param storage
     */
    public static void deleteImage(String id, FirebaseStorage storage) {
        StorageReference ref = storage.getReferenceFromUrl("gs://habitappt.appspot.com/default_user/" + id + ".jpg");
        ref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("STORAGE", "onSuccess: deleted image");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("STORAGE", "onFailure: could not delete image");
            }
        });
    }

    /**
     * deletes event from db given the habit, event, and db instance
     * @param event
     * @param habit
     * @param db
     */
    public static void removeEvent(HabitEvent event, Habit habit, User user){

        user.getHabitEventReference(habit)
                .document(String.valueOf(event.getId()))
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.i("data","event has been removed succesfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("data","event has not been removed succesfully" + e.toString());
                    }
                });
    }

    /**
     * removes habit given firestore db instance and habit
     * @param habit
     * @param db
     */
    public static void removeHabit(Habit habit, User user){

        user.getHabitReference()
                .document(String.valueOf(habit.getId()))
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.i("data","Data has been removed succesfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("data","Data could not be removed" + e.toString());
                    }
                });
    }

    /**
     * Gets the string date of a date in long integer format.
     * @param l Unix epoch
     * @return The date in the form of a string (dd/MM/yyyy)
     */
    public static String getStringDateFromLong(long l){
        Date date= new Date(l);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String dateText = df.format(date);
        return dateText;
    }
}
