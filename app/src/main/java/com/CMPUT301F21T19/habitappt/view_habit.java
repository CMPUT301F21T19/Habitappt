/**
 * Copyright 2021 - 2021 CMPUT301F21T19 (Habitappt). All rights reserved. This document nor any
 * part of it may be reproduced, stored in a retrieval system or transmitted in any for or by any
 * means without prior permission of the members of CMPUT301F21T19 or by the professor and any
 * authorized TAs of the CMPUT301 class at the University of Alberta, fall term 2021.
 *
 * Class: view_habit
 *
 * Description: Displays habit info as well as event associated with the habit. Handles user
 *              interaction to edit habit and add habit events
 *
 * Changelog:
 * =|Version|=|User(s)|==|Date|========|Description|================================================
 *   1.0       Andrew    Oct-23-2021   View and edit habits done
 *   1.1       Sohaib    Oct-24-2021   Added habit events
 *   1.2       Andrew    Oct-25-2021   Habit events editing and list
 *   1.3       Hamzah    Oct-27-2021   Added event swipe to edit/delete functionality
 *   1.4       Andrew    Oct-27-2021   Images for events
 *   1.5       Andrew    Oct-27-2021   Fixed bug with deleting a habit crashing the app
 *   1.6       Hamzah    Oct-28-2021   Added event deletion functionality
 *   1.7       Andrew    Oct-28-2021   fixed images not updating after editing habit event
 *   1.8       Hamzah    Oct-31-2021   Added getters and setters in habit class for habiteventslist,
 *                                     also modified view habit to use habiteventslist list instead
 *                                     of list created locally, refactored delete image in edit_event
 *
 *   1.9       Logan     Nov-01-2021   Added isPrivate indicator in view_habit
 *   1.10      Logan     Nov-01-2021   Reorganized the view habit fragment
 *   1.11      Andrew    Oct-03-2021   Added shared method to sharedHelper class
 *   1.12      Logan     Nov-04-2021   Fixed catastrophic fragment blunder
 * =|=======|=|======|===|====|========|===========|================================================
 */

package com.CMPUT301F21T19.habitappt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class view_habit extends Fragment {

    private View view;
    public MainActivity main;
    private TextView habitIsPrivate;
    private TextView habitTitle;
    private TextView habitReason;
    private TextView habitDateToStart;
    private ImageButton editButton;

    private view_habit THIS = this;

    View addEventButton;

    private ArrayList<TextView> daysToDo = new ArrayList<>();

    private Habit habit;

    ListView eventListView;
    SwipeMenuListView eventSwipeListView;
    ArrayAdapter<HabitEvent> eventAdapter;
//    protected ArrayList<HabitEvent> eventDataList;

    SwipeMenuItem deleteItem;
    SwipeMenuItem editItem;

    private FirebaseStorage storage;
    private FirebaseAuth auth;

    /**
     * @param habit Habit object in which to display in view
     * Instantiates new view_habit object
     */
    public view_habit(Habit habit){
        this.habit = habit;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            main = (MainActivity) context;
        }
    }
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_habit, container, false);

        auth = FirebaseAuth.getInstance();

        habitIsPrivate = view.findViewById(R.id.isPrivate_text_view);
        habitTitle = view.findViewById(R.id.habit_title_display);
        habitReason = view.findViewById(R.id.habit_reason_display);
        habitDateToStart = view.findViewById(R.id.start_date_display);
        editButton = view.findViewById(R.id.edit_button);

        daysToDo.add(view.findViewById(R.id.monday_display));
        daysToDo.add(view.findViewById(R.id.tuesday_display));
        daysToDo.add(view.findViewById(R.id.wednesday_display));
        daysToDo.add(view.findViewById(R.id.thursday_display));
        daysToDo.add(view.findViewById(R.id.friday_display));
        daysToDo.add(view.findViewById(R.id.saturday_display));
        daysToDo.add(view.findViewById(R.id.sunday_display));


        if (habit.getIsPrivate()) {
            habitIsPrivate.setText("Private Habit");
            habitIsPrivate.setBackgroundColor(Color.RED);
        } else {
            habitIsPrivate.setText("Public Habit");
            habitIsPrivate.setBackgroundColor(Color.GREEN);
        }
        habitTitle.setText(habit.getTitle());
        habitReason.setText(habit.getReason());

        storage = FirebaseStorage.getInstance();

        //editing habit
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new edit_habit(habit).show(getActivity().getSupportFragmentManager(), "EDIT");
            }
        });

//        eventListView = view.findViewById(R.id.event_list);
//        eventDataList = new ArrayList<>();
//        eventAdapter = new EventList(getContext(), eventDataList);
//        eventListView.setAdapter(eventAdapter);


        //new
        eventSwipeListView = view.findViewById(R.id.event_list);
        habit.setHabitEvents(new ArrayList<>());
        eventAdapter = new EventList(getContext(), habit.getHabitEvents());
        eventSwipeListView.setAdapter(eventAdapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                editItem = new SwipeMenuItem(
                        getContext());
                // set item background
                editItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                editItem.setWidth(170);
                // set item title
                editItem.setTitle("Edit");
                // set item title fontsize
                editItem.setTitleSize(18);
                // set item title font color
                editItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(editItem);

                // create "delete" item
                deleteItem = new SwipeMenuItem(
                        getContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(170);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        eventSwipeListView.setMenuCreator(creator);

        eventSwipeListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    //edit selected
                    case 0:
                        Log.d("MENUSELECT", "OnMenuItemClick: selected item" + index);
                        //get event
                        HabitEvent editEvent = (HabitEvent) eventSwipeListView.getItemAtPosition(position);
                        //create fragement
                        new edit_event(editEvent, habit,"EDIT").show(getActivity().getSupportFragmentManager(), "EDIT");
                        break;
                    //delete selected
                    case 1:
                        Log.d("MENUSELECT", "OnMenuItemClick: selected item" + index);

                        //get event
                        HabitEvent delEvent = (HabitEvent) eventSwipeListView.getItemAtPosition(position);
                        //NEED TO DO
                        new edit_event(delEvent, habit, "REMOVE").show(getActivity().getSupportFragmentManager(), "REMOVE");
                        break;
                }
                return false;
            }
        });
        //new


        addEventButton = view.findViewById(R.id.add_event_button);
        addEventButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Info", "Clicked add event");
                new edit_event(new HabitEvent(habit), habit,"ADD").show(getActivity().getSupportFragmentManager(), "ADD");
            }
        });

//        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.d("Info", "Clicked an event");
//                FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
//                trans.replace(R.id.main_container,new edit_event(eventDataList.get(position)));
//                trans.commit();
//            }
//        });



        DocumentReference docReference = FirebaseFirestore.getInstance()
                .collection("Users")
                .document(auth.getCurrentUser().getEmail())
                .collection("Habits")
                .document(String.valueOf(habit.getId()));

        docReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value.get("title") == null){
                    FragmentTransaction trans = main.getSupportFragmentManager().beginTransaction();
                    trans.replace(R.id.main_container,new all_habits());
                    trans.commit();
                    return;
                }

                habit.setIsPrivate((boolean) value.get("isPrivate"));
                habit.setTitle(value.get("title").toString());
                habit.setReason(value.get("reason").toString());
                habit.setDateToStart((Long.valueOf(value.get("dateToStart").toString())));

                if (habit.getIsPrivate()) {
                    habitIsPrivate.setText("Private Habit");
                    habitIsPrivate.setBackgroundColor(Color.RED);
                } else {
                    habitIsPrivate.setText("Public Habit");
                    habitIsPrivate.setBackgroundColor(Color.GREEN);
                }
                habitTitle.setText(habit.getTitle());
                habitReason.setText(habit.getReason());
                habitDateToStart.setText(SharedHelper.getStringDateFromLong(habit.getDateToStart()));


                ArrayList<Boolean> days = (ArrayList<Boolean>) value.get("daysToDo");
                for(int i=0;i<7;i++){
                    if(days.get(i)){
                        daysToDo.get(i).setBackgroundColor(Color.LTGRAY);
                    }
                    else{
                        daysToDo.get(i).setBackgroundColor(Color.WHITE);
                    }
                }
            }
        });

        CollectionReference eventCollectionReference = FirebaseFirestore.getInstance()
                .collection("Users")
                .document(auth.getCurrentUser().getEmail())
                .collection("Habits")
                .document(String.valueOf(habit.getId()))
                .collection("Event Collection");

        eventCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {

                habit.getHabitEvents().clear();

                int i = 0;

                for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                    Log.d("info","adding event entry!");
                    String id = doc.getId();
                    String comments = (String) doc.getData().get("comments");
                    Long eventDate = (Long) doc.getData().get("eventDate");

                    HabitEvent newEvent = new HabitEvent(comments, eventDate, habit, id);

                    //adding to habitEventsList
                    habit.getHabitEvents().add(newEvent);

                    if((Long) doc.getData().get("eventImg") != 0){


                        StorageReference ref = storage.getReferenceFromUrl("gs://habitappt.appspot.com/default_user/" + id + ".jpg");

                        int finalI = i;

                        ref.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                final int index = finalI;
                                Bitmap bitMapImg = BitmapFactory.decodeByteArray(bytes,0,bytes.length);

                                habit.getHabitEvents().get(index).setImg(bitMapImg);
                                eventAdapter.notifyDataSetChanged();


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("image retrieval failure","image retrieval failure");
                            }
                        });
                    }
                    i += 1;
                }

                eventAdapter.notifyDataSetChanged();
            }
        });


        //testing push


        habitDateToStart.setText(SharedHelper.getStringDateFromLong(habit.getDateToStart()));

        for(int i=0;i<7;i++){
            if(habit.getDateSelected(i)){
                daysToDo.get(i).setBackgroundColor(Color.LTGRAY);
            }
            else{
                daysToDo.get(i).setBackgroundColor(Color.WHITE);
            }
        }

        // Inflate the layout for this fragment
        return view;
    }
}
