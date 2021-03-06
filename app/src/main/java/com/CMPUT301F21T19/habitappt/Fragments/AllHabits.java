/**
 * Copyright 2021 - 2021 CMPUT301F21T19 (Habitappt). All rights reserved. This document nor any
 * part of it may be reproduced, stored in a retrieval system or transmitted in any for or by any
 * means without prior permission of the members of CMPUT301F21T19 or by the professor and any
 * authorized TAs of the CMPUT301 class at the University of Alberta, fall term 2021.
 *
 * Class: AllHabits
 *
 * Description: Populates a list with all habits stored in the database
 */

package com.CMPUT301F21T19.habitappt.Fragments;

import androidx.annotation.Nullable;

import com.CMPUT301F21T19.habitappt.Entities.Habit;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class AllHabits extends RecyclerViewFragment {
    /**
     * This class defines a fragment that shows all of a users habits.
     */

    /**
     * This method returns all of the habits in the users collection
     * @param queryDocumentSnapshots
     * @param error
     */
    @Override
    public void parseDataBaseUpdate(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
        //clear habit list as we'll update from firestore
        habitDataList.clear();
        //for every habit in firestore
        for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
            //get attributes
            String id = doc.getId();
            boolean isPrivate = (boolean) doc.getData().get("isPrivate");
            String title = (String) doc.getData().get("title");
            String reason = (String) doc.getData().get("reason");
            long dateToStart = (long) doc.getData().get("dateToStart");
            ArrayList<Boolean> datesToDo = (ArrayList<Boolean>) doc.getData().get("daysToDo");
            long index = (long) doc.getData().get("index");

            //add to habit datalist
            habitDataList.add(new Habit(title, reason, dateToStart, datesToDo, id, isPrivate,index));
        }
        //update adapter
        habitAdapter.notifyDataSetChanged();
    }

}