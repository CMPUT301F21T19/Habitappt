/**
 * Copyright 2021 - 2021 CMPUT301F21T19 (Habitappt). All rights reserved. This document nor any
 * part of it may be reproduced, stored in a retrieval system or transmitted in any for or by any
 * means without prior permission of the members of CMPUT301F21T19 or by the professor and any
 * authorized TAs of the CMPUT301 class at the University of Alberta, fall term 2021.
 *
 * Class: MainActivity
 *
 * Description:
 * the Main activity creates context to the db instance for the application,
 * then moves to the profile screen for the user
 *
 */

package com.CMPUT301F21T19.habitappt.Activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.CMPUT301F21T19.habitappt.Fragments.NavBar;
import com.CMPUT301F21T19.habitappt.Fragments.Profile;
import com.CMPUT301F21T19.habitappt.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements NavBar.nav_bar_switch {

    /**
     * continuously used db instance
     */
    public FirebaseFirestore db;

    @NonNull
    @Override
    public FragmentManager getSupportFragmentManager() {
        return super.getSupportFragmentManager();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get base firestore instance
        db = FirebaseFirestore.getInstance();

        //start bottom navigation bar!
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //always starts in Profile screen
        transaction.replace(R.id.bottom_nav_bar,new NavBar());
        transaction.commit();

        //switch to profile fragment
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container,new Profile());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * Switches the fragment in the main container. This is an implementation of an interface that gets used by
     * the NavBar fragment.
     * @param frag
     */
    @Override
    public void switchFragment(Fragment frag) {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        for(int i=0;i < count;i++){
            getSupportFragmentManager().popBackStackImmediate();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //replace current activity with fragment
        transaction.replace(R.id.main_container,frag);
        transaction.addToBackStack("base");
        transaction.commit();
    }

    /**
     * Manages application when back is selected
     */
    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount() > 1){
            getSupportFragmentManager().popBackStackImmediate();
        }
        else{
            finish();
        }

    }
}