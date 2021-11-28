/**
 * Copyright 2021 - 2021 CMPUT301F21T19 (Habitappt). All rights reserved. This document nor any
 * part of it may be reproduced, stored in a retrieval system or transmitted in any for or by any
 * means without prior permission of the members of CMPUT301F21T19 or by the professor and any
 * authorized TAs of the CMPUT301 class at the University of Alberta, fall term 2021.
 *
 * Class: ViewHabit
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
 *                                     of list created locally, refactored delete image in EditEvent
 *
 *   1.9       Logan     Nov-01-2021   Added isPrivate indicator in ViewHabit
 *   1.10      Logan     Nov-01-2021   Reorganized the view habit fragment
 *   1.11      Andrew    Oct-03-2021   Added shared method to sharedHelper class
 *   1.12      Logan     Nov-04-2021   Fixed catastrophic fragment blunder
 * =|=======|=|======|===|====|========|===========|================================================
 */

package com.CMPUT301F21T19.habitappt.Fragments;

import android.app.Activity;
import android.content.Context;
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
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.CMPUT301F21T19.habitappt.Activities.MainActivity;
import com.CMPUT301F21T19.habitappt.Entities.User;
import com.CMPUT301F21T19.habitappt.Lists.EventList;
import com.CMPUT301F21T19.habitappt.Entities.Habit;
import com.CMPUT301F21T19.habitappt.Entities.HabitEvent;
import com.CMPUT301F21T19.habitappt.R;
import com.CMPUT301F21T19.habitappt.Utils.SharedHelper;
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

import java.util.ArrayList;

/**
 * This fragment is used to view a habit and its associated habit events.
 */
public class ViewHabit extends Fragment {

    private View view;

    private MainActivity main;

    private TextView habitIsPrivate;
    private TextView habitTitle;
    private TextView habitReason;
    private TextView habitDateToStart;
    private ImageButton editButton;


    private View addEventButton;

    private ArrayList<TextView> daysToDo = new ArrayList<>();

    private Habit habit;

    private SwipeMenuListView eventSwipeListView;
    private ArrayAdapter<HabitEvent> eventAdapter;

    private SwipeMenuItem deleteItem;
    private SwipeMenuItem editItem;

    private FirebaseStorage storage;

    private User currentUser;

    /**
     * @param habit Habit object in which to display in view
     * Instantiates new ViewHabit object
     */
    public ViewHabit(Habit habit){
        this.habit = habit;

    }

    /**
     * Method called when fragment is created
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * When the fragment gets attached to its container, get a reference to MainActivity
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            main = (MainActivity) context;
        }
    }

    /**
     * Create the view for the fragment.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_habit, container, false);



        //get current user object
        currentUser = new User();

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
                new EditHabit(habit).show(getActivity().getSupportFragmentManager(), "EDIT");
            }
        });


        eventSwipeListView = view.findViewById(R.id.event_list);
        habit.setHabitEvents(new ArrayList<>());
        eventAdapter = new EventList(getContext(), habit.getHabitEvents());
        eventSwipeListView.setAdapter(eventAdapter);

        eventSwipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FragmentTransaction trans = main.getSupportFragmentManager().beginTransaction();
                trans.replace(R.id.main_container,new ViewEvent(habit.getHabitEvents().get(i)));
                trans.addToBackStack("ViewEvent");
                trans.commit();

            }
        });

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

        //set the menu creator to created config
        eventSwipeListView.setMenuCreator(creator);

        //set listener for swiped item menu
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
                        new EditEvent(editEvent, habit,"EDIT").show(getActivity().getSupportFragmentManager(), "EDIT");
                        break;
                    //delete selected
                    case 1:
                        Log.d("MENUSELECT", "OnMenuItemClick: selected item" + index);

                        //get event
                        HabitEvent delEvent = (HabitEvent) eventSwipeListView.getItemAtPosition(position);
                        //NEED TO DO
                        SharedHelper.deleteImage(delEvent.getId(), storage);
                        SharedHelper.removeEvent(delEvent, habit, currentUser);
                        break;
                }
                return false;
            }
        });

        //button logic for creating a new event
        addEventButton = view.findViewById(R.id.add_event_button);
        addEventButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Info", "Clicked add event");
                new EditEvent(new HabitEvent(habit), habit,"ADD").show(getActivity().getSupportFragmentManager(), "ADD");
            }
        });


        //logic for getting db updates
        DocumentReference docReference = currentUser.getHabitReference()
                .document(String.valueOf(habit.getId()));

        docReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                //habit has been deleted if title is null. back out of view habit fragment
                if(value.get("title") == null){
                    FragmentTransaction trans = main.getSupportFragmentManager().beginTransaction();
                    trans.replace(R.id.main_container,new AllHabits());
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

        CollectionReference eventCollectionReference = currentUser.getHabitEventReference(habit);

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

                    double locationLat = -1;
                    double locationLon = -1;
                    if(doc.getData().get("latitude") != null && doc.getData().get("longitude") != null) {
                        //get lat and lon
                        locationLat = (double) doc.getData().get("latitude");
                        locationLon = (double) doc.getData().get("longitude");
                    }

                    HabitEvent newEvent = new HabitEvent(comments, eventDate, habit, id, locationLat, locationLon);

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
