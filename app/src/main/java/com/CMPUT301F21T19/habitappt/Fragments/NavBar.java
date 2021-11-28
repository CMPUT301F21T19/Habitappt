/**
 * Copyright 2021 - 2021 CMPUT301F21T19 (Habitappt). All rights reserved. This document nor any
 * part of it may be reproduced, stored in a retrieval system or transmitted in any for or by any
 * means without prior permission of the members of CMPUT301F21T19 or by the professor and any
 * authorized TAs of the CMPUT301 class at the University of Alberta, fall term 2021.
 *
 * Class : NavBar
 *
 * Description: Implements the navigation bar at the bottom of the screen to allow users to navigate between
 Profile,all habits, and daily habits pages.
 *
 * Changelog:
 * =|Version|=|User(s)|==|Date|========|Description|================================================
 * 1.0          Andrew   Oct 21,2021    Implemented nav bar and fragment switching in main container
 * 1.1          Andrew   Oct 21,2021    Start on Profile by default + fixing some code
 * 1.2          Andrew   Oct 21,2021    Refactoring nav bar
 * 1.3          Logan    Nov 1,2021     Navigation bar indicates which tab is currently being viewed
 * 1.4          Andrew   Nov 2,2021     Fixed nav bar UI

 * =|=======|=|======|===|====|========|===========|================================================
**/

package com.CMPUT301F21T19.habitappt.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.CMPUT301F21T19.habitappt.R;

/**
 * Navigation bar for switching between main fragments (Profile, All Habits, and Daily Habits)
 */
public class NavBar extends Fragment {
    private View profile_button;
    private View all_habits_button;
    private View daily_habits_button;
    private nav_bar_switch listener;


    public interface nav_bar_switch{
        /**
         * Interface method for switching fragments in main container.
         * @param nav_to Fragment
         */
        void switchFragment(Fragment nav_to);
    }

    /**
     * Called when fragment gets attached to main container
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (nav_bar_switch) context;
    }

    /**
     * Creates view for the navigation bar.
     * @param inflater inflates the layout to match view
     * @param container ViewGroup
     * @param savedInstanceState Bundle
     * @return view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nav_bar, container, false);

        profile_button = view.findViewById(R.id.profile_button);
        all_habits_button = view.findViewById(R.id.all_habit_button);
        daily_habits_button = view.findViewById(R.id.daily_habit_button);

        profile_button.setBackgroundColor(Color.LTGRAY);
        all_habits_button.setBackgroundColor(Color.WHITE);
        daily_habits_button.setBackgroundColor(Color.WHITE);

        //profile button logic
        profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile_button.setBackgroundColor(Color.LTGRAY);
                all_habits_button.setBackgroundColor(Color.WHITE);
                daily_habits_button.setBackgroundColor(Color.WHITE);
                listener.switchFragment(new Profile());
            }
        });
        //all habits button logic
        all_habits_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile_button.setBackgroundColor(Color.WHITE);
                all_habits_button.setBackgroundColor(Color.LTGRAY);
                daily_habits_button.setBackgroundColor(Color.WHITE);
                listener.switchFragment(new AllHabits());
            }
        });

        //daily habits button logic
        daily_habits_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile_button.setBackgroundColor(Color.WHITE);
                all_habits_button.setBackgroundColor(Color.WHITE);
                daily_habits_button.setBackgroundColor(Color.LTGRAY);
                listener.switchFragment(new DailyHabits());
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

}
