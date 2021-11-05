/**
 * Copyright 2021 - 2021 CMPUT301F21T19 (Habitappt). All rights reserved. This document nor any
 * part of it may be reproduced, stored in a retrieval system or transmitted in any for or by any
 * means without prior permission of the members of CMPUT301F21T19 or by the professor and any
 * authorized TAs of the CMPUT301 class at the University of Alberta, fall term 2021.
 *
 * Class: daily_habits
 *
 * Description: Populates a list with habits that need to be completed in the current day
 */

package com.CMPUT301F21T19.habitappt;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class daily_habits extends  abstract_habit_list_fragment {
    /**
     * This class defines a fragment that shows all of the habits a user must complete today.
     */

    /**
     * This method returns all of the habits that must be completed today by the user
     * @param queryDocumentSnapshots
     * @param error
     */
    @Override
    public void parseDataBaseUpdate(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
        habitDataList.clear();

        for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
            String id = doc.getId();
            boolean isPrivate = (boolean) doc.getData().get("isPrivate");
            String title = (String) doc.getData().get("title");
            String reason = (String) doc.getData().get("reason");
            long dateToStart = (long) doc.getData().get("dateToStart");
            ArrayList<Boolean> datesToDo = (ArrayList<Boolean>) doc.getData().get("daysToDo");

            Date todayDate = new Date(GregorianCalendar.getInstance().getTimeInMillis());
            Date startDate = new Date(dateToStart);
            Calendar todayCal = Calendar.getInstance();
            todayCal.setTime(todayDate);
            Calendar startCal = Calendar.getInstance();
            startCal.setTime(startDate);

            if (todayDate.getTime() > startDate.getTime()) {
                for (int i=0; i<datesToDo.size(); i++) {
                    if (datesToDo.get(i) && todayCal.get(Calendar.DAY_OF_WEEK) == ((i+1)%7)+1) {
                        habitDataList.add(new Habit(title, reason, dateToStart, datesToDo, id, isPrivate));
                    }
                }
            }
        }

        habitAdapter.notifyDataSetChanged();
    }


}