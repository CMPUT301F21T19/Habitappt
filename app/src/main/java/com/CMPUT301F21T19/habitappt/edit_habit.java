/**
 * Copyright 2021 - 2021 CMPUT301F21T19 (Habitappt). All rights reserved. This document nor any
 * part of it may be reproduced, stored in a retrieval system or transmitted in any for or by any
 * means without prior permission of the members of CMPUT301F21T19 or by the professor and any
 * authorized TAs of the CMPUT301 class at the University of Alberta, fall term 2021.
 *
 * Class: edit_habit
 *
 * Description: Lets users to add & edit a habit
 *
 * Changelog:
 * =|Version|=|User(s)|==|Date|========|Description|================================================
 *   1.0       Andrew    Oct-23-2021   Added habits!
 *   1.1       Logan     Oct-23-2021   Fix add/edit/remove bugs
 *   1.2       Logan     Oct-23-2021   Display daily habits
 *   1.3       Andrew    Oct-23-2021   Viewing and editing habits done
 *   1.4       Hamzah    Oct-23-2021   Added checks for min and max input for habit title and reason
 *   1.5       Sohaib    Oct-24-2021   Added habit events
 *   1.6       Hamzah    Oct-25-2021   Added some comments
 *   1.7       Hamzah    Oct-25-2021   Modified edit Habit
 *   1.8       Hamzah    Oct-25-2021   Small fix
 *   1.9       Andrew    Oct-26-2021   Fixed back button not working correctly after deleting a habit
 *   1.10      Andrew    Oct-27-2021   Added image activity and uploading images with events to firestorage
 *   1.11      Hamzah    Oct-31-2021   Refactored removing habit, moved functionality to SharedHelper.java class
 *   1.12      Logan     Nov-01-2021   isPrivate UI and database implementation
 *   1.13      Logan     Nov-01-2021   Added isPrivate indicator in view_habit
 *   1.14      Logan     Nov-01-2021   Adjusted edit/add habit UI
 * =|=======|=|======|===|====|========|===========|================================================
 */

package com.CMPUT301F21T19.habitappt;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TimeZone;

public class edit_habit extends DialogFragment {

    private Button isPrivateButton;
    private EditText habitTitle;
    private EditText habitReason;
    private CalendarView habitDateToStart;
    private ArrayList<Button> days_of_week = new ArrayList<>();

    private Habit habit;

    private String dialogTitle;

    private String removeTextTitle;

    long date_selected;

    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private FirebaseAuth auth;


    protected edit_habit THIS;

    /**
     * Instantiate habit edit_habit object (habit already exists) with specified values
     * @param habit existing habit
     */
    public edit_habit(Habit habit){
        this.habit = habit;
        this.dialogTitle = "Edit Habit";
        this.removeTextTitle = "Remove Habit";
        this.date_selected = habit.getDateToStart();
    }
    /**
     * Instantiates new edit_habit object for new habit
     */
    public edit_habit(){
        this.habit = new Habit();
        this.dialogTitle = "Add Habit";
        this.removeTextTitle = "Cancel";
        this.date_selected = GregorianCalendar.getInstance().getTimeInMillis();
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        THIS = this;

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_habit,null);

        db = FirebaseFirestore.getInstance();

        storage = FirebaseStorage.getInstance();

        auth = FirebaseAuth.getInstance();

        isPrivateButton = view.findViewById(R.id.public_private_button);
        habitTitle = view.findViewById(R.id.habit_title);
        habitReason = view.findViewById(R.id.habit_reason);
        habitDateToStart = view.findViewById(R.id.date_to_start);

        habitTitle.setText(habit.getTitle());
        habitReason.setText(habit.getReason());
        habitDateToStart.setDate(habit.getDateToStart());

        days_of_week.add(view.findViewById(R.id.monday_button));
        days_of_week.add(view.findViewById(R.id.tuesday_button));
        days_of_week.add(view.findViewById(R.id.wednesday_button));
        days_of_week.add(view.findViewById(R.id.thursday_button));
        days_of_week.add(view.findViewById(R.id.friday_button));
        days_of_week.add(view.findViewById(R.id.saturday_button));
        days_of_week.add(view.findViewById(R.id.sunday_button));

        // click listeners for public/private button
        if (habit.getIsPrivate()) {
            isPrivateButton.setBackgroundColor(Color.RED);
            isPrivateButton.setText("private habit");
        } else {
            isPrivateButton.setBackgroundColor(Color.GREEN);
            isPrivateButton.setText("public habit");
        }
        isPrivateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (habit.getIsPrivate()) {
                    habit.setIsPrivate(false);
                    isPrivateButton.setBackgroundColor(Color.GREEN);
                    isPrivateButton.setText("public habit");
                } else {
                    habit.setIsPrivate(true);
                    isPrivateButton.setBackgroundColor(Color.RED);
                    isPrivateButton.setText("private habit");
                }
            }
        });

        // click listeners for weekday buttons
        for(int i=0;i<7;i++){
            if(habit.getDateSelected(i)){
                days_of_week.get(i).setBackgroundColor(Color.LTGRAY);
            } else {
                days_of_week.get(i).setBackgroundColor(Color.WHITE);
            }
            final int index = new Integer(i);
            days_of_week.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(habit.getDateSelected(index)){
                        habit.setDateSelected(index,false);
                        days_of_week.get(index).setBackgroundColor(Color.WHITE);
                    } else {
                        habit.setDateSelected(index,true);
                        days_of_week.get(index).setBackgroundColor(Color.LTGRAY);
                    }
                }
            });
        }

        // click listener for the calendar
        habitDateToStart.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                /*
                update calendar selection.
                 */
                GregorianCalendar cal = new GregorianCalendar(TimeZone.getDefault());
                cal.set(year,month,day);

                date_selected = cal.getTimeInMillis();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view)
            .setTitle(dialogTitle)
            .setNegativeButton(removeTextTitle, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    getChildFragmentManager().popBackStack("viewhabit", FragmentManager.POP_BACK_STACK_INCLUSIVE);

                    //if remove and not cancel
                    if(removeTextTitle.equals("Remove Habit")) {

                        //remove all images associated to each habit event and events first
                        for (HabitEvent eachEvent : habit.getHabitEvents()) {
                            //remove image from firestore storage after deleting event
                            SharedHelper.deleteImage(eachEvent.getId(), storage);
                            //remove event
                            SharedHelper.removeEvent(eachEvent, THIS.habit, db);
                        }
                        //remove habit
                        SharedHelper.removeHabit(THIS.habit, db);

                    }
                }
            })
            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    if(getTag() == "EDIT"){
                        DocumentReference doc = db.collection(auth.getCurrentUser().getEmail()).document(String.valueOf(THIS.habit.getId()));

                        HashMap<String,Object> data = new HashMap<>();

                        data.put("isPrivate", THIS.habit.getIsPrivate());
                        data.put("title",THIS.habitTitle.getText().toString());
                        data.put("reason",THIS.habitReason.getText().toString());
                        data.put("dateToStart",THIS.date_selected);
                        data.put("daysToDo",THIS.habit.getWeekly());

                        doc.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.i("data","Data has been added succesfully!");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i("data","Data could not be added!" + e.toString());
                            }
                        });
                    }
                    else if(getTag() == "ADD"){
                        DocumentReference doc = db.collection(auth.getCurrentUser().getEmail()).document(String.valueOf(GregorianCalendar.getInstance().getTimeInMillis()));

                        HashMap<String,Object> data = new HashMap<>();

                        data.put("isPrivate", THIS.habit.getIsPrivate());
                        data.put("title",THIS.habitTitle.getText().toString());
                        data.put("reason",THIS.habitReason.getText().toString());
                        data.put("dateToStart",THIS.date_selected);
                        data.put("daysToDo",THIS.habit.getWeekly());

                        doc.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.i("data","Data has been added succesfully!");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.i("data","Data could not be added!" + e.toString());
                            }
                        });
                    }
                }
            });


        //create the alertdialog object
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        //disable confirm button until fields are correctly filled (if empty)
        if(habitTitle.length() == 0 && habitReason.length() == 0) {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        }

        //custom text watcher that will check the given inputs before enabling
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                //if habit title changed
                if(editable == THIS.habitTitle.getEditableText()){
                    //if good length
                    if(editable.length() >= 1 && editable.length() < 20 && THIS.habitReason.getText().length() >= 1 && THIS.habitReason.getText().length() < 30){
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                    }
                    else{
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                        //if false, update error reason
                        checkInput();
                    }
                }
                if(editable == THIS.habitReason.getEditableText()){
                    if(editable.length() >= 1 && editable.length() < 30 && THIS.habitTitle.getText().length() >=1 && THIS.habitTitle.getText().length() < 20){
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                    }
                    else {
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                        //if false, update error reason
                        checkInput();
                    }
                }
            }
        };
        //set both edit text watchers
        THIS.habitTitle.addTextChangedListener(watcher);
        THIS.habitReason.addTextChangedListener(watcher);

        return alertDialog;
    }

    /**
     * Displays error when habit title and reason text input is empty or greater than 20 characters
     */
    public void checkInput(){
        if(THIS.habitTitle.getText().length() == 0){
            THIS.habitTitle.setError("Title cannot be empty");
        }
        //too long
        if(THIS.habitTitle.getText().length() > 19){
            THIS.habitTitle.setError("Maximum Length 0f 20: Please reduce");
        }
        //empty reason
        if(THIS.habitReason.getText().length() == 0){
            THIS.habitReason.setError("Reason cannot be empty");
        }
        if(THIS.habitReason.getText().length() > 29){
            THIS.habitReason.setError("Maximum Length 0f 30: Please reduce");
        }
    }
}
