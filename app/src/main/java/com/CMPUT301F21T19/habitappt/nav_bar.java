/**
 * Copyright 2021 - 2021 CMPUT301F21T19 (Habitappt). All rights reserved. This document nor any
 * part of it may be reproduced, stored in a retrieval system or transmitted in any for or by any
 * means without prior permission of the members of CMPUT301F21T19 or by the professor and any
 * authorized TAs of the CMPUT301 class at the University of Alberta, fall term 2021.
 *
 * Class : nav_bar
 *
 * Description: Implements the navigation bar at the bottom of the screen to allow users to navigate between
 profile,all habits, and daily habits pages.
 *
 * Changelog:
 * =|Version|=|User(s)|==|Date|========|Description|================================================
 * 1.0          Andrew   Oct 21,2021    Implemented nav bar and fragment switching in main container
 * 1.1          Andrew   Oct 21,2021    Start on profile by default + fixing some code
 * 1.2          Andrew   Oct 21,2021    Refactoring nav bar
 * 1.3          Logan    Nov 1,2021     Navigation bar indicates which tab is currently being viewed
 * 1.4          Andrew   Nov 2,2021     Fixed nav bar UI

 * =|=======|=|======|===|====|========|===========|================================================
**/

package com.CMPUT301F21T19.habitappt;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.Console;

/**
 * initialize navigation bar object with the specifed values
 */

public class nav_bar extends Fragment {
    View profile_button;
    View all_habits_button;
    View daily_habits_button;
    private nav_bar_switch listener;

    /**
     * Interface for the navigation bar.
     * Calls switchFragment
     * @param nav_to Fragment
     */
    public interface nav_bar_switch{
        void switchFragment(Fragment nav_to);
    }

    /**
     * Attaches fragment to UI
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (nav_bar_switch) context;
    }

    /**
     * creates custom array adapter view for events
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

        profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile_button.setBackgroundColor(Color.LTGRAY);
                all_habits_button.setBackgroundColor(Color.WHITE);
                daily_habits_button.setBackgroundColor(Color.WHITE);
                listener.switchFragment(new profile());
            }
        });
        all_habits_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile_button.setBackgroundColor(Color.WHITE);
                all_habits_button.setBackgroundColor(Color.LTGRAY);
                daily_habits_button.setBackgroundColor(Color.WHITE);
                listener.switchFragment(new all_habits());
            }
        });
        daily_habits_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile_button.setBackgroundColor(Color.WHITE);
                all_habits_button.setBackgroundColor(Color.WHITE);
                daily_habits_button.setBackgroundColor(Color.LTGRAY);
                listener.switchFragment(new daily_habits());
            }
        });

        // Inflate the layout for this fragment
        return view;
    }
    
}
