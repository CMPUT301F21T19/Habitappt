/**
 * Copyright 2021 - 2021 CMPUT301F21T19 (Habitappt). All rights reserved. This document nor any
 * part of it may be reproduced, stored in a retrieval system or transmitted in any for or by any
 * means without prior permission of the members of CMPUT301F21T19 or by the professor and any
 * authorized TAs of the CMPUT301 class at the University of Alberta, fall term 2021.
 *
 * Class: EditHabit
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
 *   1.13      Logan     Nov-01-2021   Added isPrivate indicator in ViewHabit
 *   1.14      Logan     Nov-01-2021   Adjusted edit/add habit UI
 * =|=======|=|======|===|====|========|===========|================================================
 */

package com.CMPUT301F21T19.habitappt.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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
import androidx.fragment.app.FragmentTransaction;

import com.CMPUT301F21T19.habitappt.Activities.MainActivity;
import com.CMPUT301F21T19.habitappt.Entities.Habit;
import com.CMPUT301F21T19.habitappt.Entities.HabitEvent;
import com.CMPUT301F21T19.habitappt.Entities.User;
import com.CMPUT301F21T19.habitappt.R;
import com.CMPUT301F21T19.habitappt.Utils.CustomTextWatcher;
import com.CMPUT301F21T19.habitappt.Utils.DualCustomTextWatcher;
import com.CMPUT301F21T19.habitappt.Utils.SharedHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * This fragment is used to edit a habit and its associated details.
 */
public class EditHabit extends DialogFragment {

    private MainActivity main;

    private Button isPrivateButton;
    private EditText habitTitle;
    private EditText habitReason;
    private CalendarView habitDateToStart;
    private ArrayList<Button> dayOfWeek = new ArrayList<>();

    private Habit habit;

    private String dialogTitle;

    private String removeTextTitle;

    private long date_selected;

    private FirebaseStorage storage;

    private User currentUser;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.main = (MainActivity) context;
    }

    /**
     * Instantiate habit EditHabit object (habit already exists) with specified values
     * @param habit existing habit
     */
    public EditHabit(Habit habit){
        this.habit = habit;
        this.dialogTitle = "Edit Habit";
        this.removeTextTitle = "Remove Habit";
        this.date_selected = habit.getDateToStart();
    }
    /**
     * Instantiates new EditHabit object for new habit
     */
    public EditHabit(){
        this.habit = new Habit();
        this.dialogTitle = "Add Habit";
        this.removeTextTitle = "Cancel";
        this.date_selected = GregorianCalendar.getInstance().getTimeInMillis();
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_habit,null);


        storage = FirebaseStorage.getInstance();



        //get current user
        currentUser = new User();

        isPrivateButton = view.findViewById(R.id.public_private_button);
        habitTitle = view.findViewById(R.id.habit_title);
        habitReason = view.findViewById(R.id.habit_reason);
        habitDateToStart = view.findViewById(R.id.date_to_start);

        habitTitle.setText(habit.getTitle());
        habitReason.setText(habit.getReason());
        habitDateToStart.setDate(habit.getDateToStart());

        dayOfWeek.add(view.findViewById(R.id.monday_button));
        dayOfWeek.add(view.findViewById(R.id.tuesday_button));
        dayOfWeek.add(view.findViewById(R.id.wednesday_button));
        dayOfWeek.add(view.findViewById(R.id.thursday_button));
        dayOfWeek.add(view.findViewById(R.id.friday_button));
        dayOfWeek.add(view.findViewById(R.id.saturday_button));
        dayOfWeek.add(view.findViewById(R.id.sunday_button));

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
                dayOfWeek.get(i).setBackgroundColor(Color.LTGRAY);
            } else {
                dayOfWeek.get(i).setBackgroundColor(Color.WHITE);
            }
            final int index = new Integer(i);
            dayOfWeek.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(habit.getDateSelected(index)){
                        habit.setDateSelected(index,false);
                        dayOfWeek.get(index).setBackgroundColor(Color.WHITE);
                    } else {
                        habit.setDateSelected(index,true);
                        dayOfWeek.get(index).setBackgroundColor(Color.LTGRAY);
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
                            SharedHelper.removeEvent(eachEvent, habit, currentUser);
                        }
                        //remove habit
                        SharedHelper.removeHabit(habit, currentUser);

                    }
                }
            })
            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //logic for when a habit was getting edited and needs to be updated to the db.
                    if(getTag() == "EDIT"){
                        DocumentReference doc = currentUser.getHabitReference()
                                .document(String.valueOf(habit.getId()));

                        HashMap<String,Object> data = new HashMap<>();

                        data.put("isPrivate", habit.getIsPrivate());
                        data.put("title",habitTitle.getText().toString());
                        data.put("reason",habitReason.getText().toString());
                        data.put("dateToStart",date_selected);
                        data.put("daysToDo",habit.getWeekly());
                        data.put("index",habit.getIndex());

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
                        //logic for adding a habit and storing it in the db.

                        String id = String.valueOf(GregorianCalendar.getInstance().getTimeInMillis());
                        DocumentReference doc = currentUser.getHabitReference()
                                .document(id);

                        HashMap<String,Object> data = new HashMap<>();

                        data.put("isPrivate", habit.getIsPrivate());
                        data.put("title",habitTitle.getText().toString());
                        data.put("reason",habitReason.getText().toString());
                        data.put("dateToStart",date_selected);
                        data.put("daysToDo",habit.getWeekly());
                        data.put("index", 9999999);

                        habit.setDateToStart(date_selected);
                        habit.setTitle(habitTitle.getText().toString());
                        habit.setReason(habitReason.getText().toString());
                        habit.setId(id);


                        doc.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.i("data","Data has been added succesfully!");

                                FragmentTransaction trans = main.getSupportFragmentManager().beginTransaction();
                                trans.replace(R.id.main_container,new ViewHabit(habit));
                                trans.addToBackStack(null);
                                trans.commit();

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
        DualCustomTextWatcher dualTextWatcher = new DualCustomTextWatcher(THIS.habitTitle, THIS.habitReason, alertDialog.getButton(AlertDialog.BUTTON_POSITIVE), 0, 20, 0, 30);

        //set both edit text watchers
        THIS.habitTitle.addTextChangedListener(dualTextWatcher);
        THIS.habitReason.addTextChangedListener(dualTextWatcher);

        return alertDialog;
    }

}
