/**
 *  Copyright 2021 - 2021 CMPUT301F21T19 (Habitappt). All rights reserved. This document nor any
 *  part of it may be reproduced, stored in a retrieval system or transmitted in any for or by any
 *  means without prior permission of the members of CMPUT301F21T19 or by the professor and any
 *  authorized TAs of the CMPUT301 class at the University of Alberta, fall term 2021.
 *
 *  Class : RecyclerViewFragment
 *
 *  Description : This is an abstract class for the Recycler View to display the habits on the view
 *  and order them by their index.
 *
 * @version "%1 %5"
 *
 */
package com.CMPUT301F21T19.habitappt.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.CMPUT301F21T19.habitappt.Entities.User;
import com.CMPUT301F21T19.habitappt.Utils.DragHabits;
import com.CMPUT301F21T19.habitappt.Lists.DragMoveAdapter;
import com.CMPUT301F21T19.habitappt.Entities.Habit;
import com.CMPUT301F21T19.habitappt.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


/**
 * Abstract class for whenever we want to display a list of habits from the database.
 */

public abstract class RecyclerViewFragment extends Fragment implements DragMoveAdapter.DragListener {

    protected DragMoveAdapter habitAdapter;
    protected ArrayList<Habit> habitDataList;

    private RecyclerView habitView;
    private View addHabitButton;

    private User currentUser;
    private View view;

    /**
     * This method must be implemented by any classes that extend this class.
     * It tells the class how to process the habits in the users collection and order them by their index.
     */
    public abstract void parseDataBaseUpdate(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error);

    /**
     * Called on creation of the fragment.
     * @param savedInstanceState
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Defines the habit list view
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentUser = new User();

        view = inflater.inflate(R.layout.recycler_view, container, false);

        final CollectionReference userHabitReference = currentUser.getHabitReference();

        addHabitButton = view.findViewById(R.id.add_habit_button);

        //listener for pressing the button to add habits.
        addHabitButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new EditHabit().show(getActivity().getSupportFragmentManager(), "ADD"); }
        });


        habitView = view.findViewById(R.id.recycler_habitList);
        habitDataList = new ArrayList<>();
        habitAdapter = new DragMoveAdapter(habitDataList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        habitView.setLayoutManager(layoutManager);
        initHabitOrder();

        // orders the habitList by it's index position
        Query referenceQuery = userHabitReference.orderBy("index", Query.Direction.ASCENDING);
        referenceQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                parseDataBaseUpdate(queryDocumentSnapshots,error);
            }
        });

        habitView.setAdapter(habitAdapter);
        return view;
    }

    /**
     * This overrides the onHabitClick method to display the view habit fragment when a habit is clicked on.
     * @param position
     */
    @Override
    public void onHabitClick(int position) {
        FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.main_container,new ViewHabit(habitDataList.get(position)));
        trans.addToBackStack("view_habit");
        trans.commit();
    };

    /**
     * This method attaches the DragHabits subclass to the RecyclerView.
     */

    public void initHabitOrder(){
        ItemTouchHelper.Callback callback = new DragHabits(habitAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(habitView);
    }

}
