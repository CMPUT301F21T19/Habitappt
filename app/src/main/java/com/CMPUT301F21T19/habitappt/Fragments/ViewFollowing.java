/**
 * Copyright 2021 - 2021 CMPUT301F21T19 (Habitappt). All rights reserved. This document nor any
 * part of it may be reproduced, stored in a retrieval system or transmitted in any for or by any
 * means without prior permission of the members of CMPUT301F21T19 or by the professor and any
 * authorized TAs of the CMPUT301 class at the University of Alberta, fall term 2021.
 *
 * Class: ViewFollowing
 *
 * Description: used for viewing followed users profiles. Shows their public habits and visual indicators,
 * aswell as the habits they have to complete today.
 */
package com.CMPUT301F21T19.habitappt.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.CMPUT301F21T19.habitappt.Activities.MainActivity;
import com.CMPUT301F21T19.habitappt.Entities.Habit;
import com.CMPUT301F21T19.habitappt.Entities.User;
import com.CMPUT301F21T19.habitappt.Lists.HabitList;
import com.CMPUT301F21T19.habitappt.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ViewFollowing extends Fragment {
    private MainActivity main;

    private ListView habitListView;
    private ArrayAdapter<Habit> habitAdapter;
    private ArrayList<Habit> habitDataList;

    private ArrayAdapter<Habit> dailyHabitAdapter;
    private ArrayList<Habit> dailyHabitDataList;

    private Button allHabitsButton;
    private Button dailyHabitsButton;
    private Button followButton;
    private Button removeFollowerButton;

    private TextView userProfileName;

    private User currentUser;

    private User viewedUser;

    private Activity THIS;

    /**
     * Constructor that takes in the user whos Profile is being viewed.
     * @param user
     */
    public ViewFollowing(String user){

        this.viewedUser = new User(user);

        //get current user
        this.currentUser = new User();
    }

    /**
     * Called when fragment is attached to main container. Gets reference to MainActivity
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        main = (MainActivity) context;
    }

    /**
     * Create fragment view
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_following,container,false);


        THIS = getActivity();



        habitListView = view.findViewById(R.id.profile_list);

        habitDataList = new ArrayList<>();
        habitAdapter = new HabitList(getContext(), habitDataList, true, viewedUser.getUserEmail());
        habitListView.setAdapter(habitAdapter);

        dailyHabitDataList = new ArrayList<>();
        dailyHabitAdapter = new HabitList(getContext(),dailyHabitDataList, true, viewedUser.getUserEmail());

        allHabitsButton = view.findViewById(R.id.all_habits_following);
        dailyHabitsButton = view.findViewById(R.id.daily_habits_following);
        followButton = view.findViewById(R.id.follow_button);
        removeFollowerButton = view.findViewById(R.id.remove_follower);

        userProfileName = view.findViewById(R.id.username_field);

        userProfileName.setText(viewedUser.getUserEmail());

        //button highlight changes when pressed
        allHabitsButton.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
        dailyHabitsButton.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));

        removeFollowerButton.setEnabled(false);
        followButton.setEnabled(false);


        // if viewed user is following current user, enable remove follower button
        viewedUser.queryFollowing(currentUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    removeFollowerButton.setEnabled(true);
                }
            }
        });

        // if current user is following viewed user, enable remove follower button
        currentUser.queryFollowing(viewedUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    followButton.setText("UNFOLLOW");
                } else {
                    followButton.setText("FOLLOW");
                }
                followButton.setEnabled(true);
            }
        });

        //button logic for removing a follower
        removeFollowerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewedUser.unfollow(currentUser, THIS);
                removeFollowerButton.setEnabled(false);
            }
        });

        //button logic for switching to all habits list
        allHabitsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //update button colors and listview
                habitListView.setAdapter(habitAdapter);
                allHabitsButton.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
                dailyHabitsButton.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
            }
        });

        //button logic for switching to daily habits list
        dailyHabitsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //update button colors and listview
                habitListView.setAdapter(dailyHabitAdapter);
                allHabitsButton.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
                dailyHabitsButton.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
            }
        });

        //button for sending user a follow request, or unfollowing the user based on your current following status
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (followButton.getText().toString().equals("UNFOLLOW")) {
                    currentUser.unfollow(viewedUser, THIS);
                    followButton.setText("FOLLOW");
                    habitDataList.clear();
                    dailyHabitDataList.clear();
                    habitAdapter.notifyDataSetChanged();
                    dailyHabitAdapter.notifyDataSetChanged();
                } else {
                    currentUser.request(viewedUser, THIS);
                }
            }
        });



        //logic for displaying all habits and daily habits list.
        CollectionReference all_habits = viewedUser.getHabitReference();

        all_habits.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                //update habit list based on privacy rule
                habitDataList.clear();

                for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {

                    currentUser.getUserReference()
                            .collection("Followings")
                            .document(viewedUser.getUserEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            if(documentSnapshot.exists()){
                                String id = doc.getId();
                                boolean isPrivate = (boolean) doc.getData().get("isPrivate");

                                if(isPrivate){
                                    return;
                                }

                                String title = (String) doc.getData().get("title");
                                String reason = (String) doc.getData().get("reason");
                                long dateToStart = (long) doc.getData().get("dateToStart");
                                ArrayList<Boolean> datesToDo = (ArrayList<Boolean>) doc.getData().get("daysToDo");
                                long index = (long) doc.getData().get("index");

                                habitDataList.add(new Habit(title, reason, dateToStart, datesToDo, id, isPrivate,index));
                                habitAdapter.notifyDataSetChanged();
                            }

                        }
                    });

                }
            }
        });

        all_habits.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                //update daily habit list based on privacy rule
                dailyHabitDataList.clear();

                for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                    String id = doc.getId();

                    currentUser.getUserReference()
                            .collection("Followings")
                            .document(viewedUser.getUserEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            if(documentSnapshot.exists()){
                                boolean isPrivate = (boolean) doc.getData().get("isPrivate");
                                if(isPrivate){
                                    return;
                                }
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

                                long index = (long) doc.getData().get("index");

                                if (todayDate.getTime() > startDate.getTime()) {
                                    for (int i=0; i<datesToDo.size(); i++) {
                                        if (datesToDo.get(i) && todayCal.get(Calendar.DAY_OF_WEEK) == ((i+1)%7)+1) {
                                            dailyHabitDataList.add(new Habit(title, reason, dateToStart, datesToDo, id, isPrivate,index));
                                        }
                                    }
                                }
                            }
                        }
                    });

                }

                dailyHabitAdapter.notifyDataSetChanged();
            }
        });




        return view;
    }
}
