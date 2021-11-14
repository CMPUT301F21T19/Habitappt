/**
 * Copyright 2021 - 2021 CMPUT301F21T19 (Habitappt). All rights reserved. This document nor any
 * part of it may be reproduced, stored in a retrieval system or transmitted in any for or by any
 * means without prior permission of the members of CMPUT301F21T19 or by the professor and any
 * authorized TAs of the CMPUT301 class at the University of Alberta, fall term 2021.
 *
 * Class: HabitList
 *
 * Description: custom array adapter that represents list of habits
 * for associated user
 *
 * @version "%1%,%5%"
 *
 *
 */
package com.CMPUT301F21T19.habitappt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;


public class HabitList extends ArrayAdapter<Habit> {

    /**
     * actual data structure that holds habits for given user
     */
    private ArrayList<Habit> habits;
    /**
     * context to main activity
     */
    private Context context;

    /**
     * constructs habit list array adpater with passed context and list of habits
     * @param context
     * @param habits
     */
    public HabitList(Context context, ArrayList<Habit> habits) {
        super(context,0, habits);
        this.habits = habits;
        this.context = context;
    }

    /**
     * creates custom array adapter view for habits
     * @param position position of habit
     * @param convertView view to convert
     * @param parent parent
     * @return view
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.habit_list_item, parent,false);
        }

        Habit habit = habits.get(position);

        TextView habitName = view.findViewById(R.id.habit_name);
        TextView habitReason = view.findViewById(R.id.habit_reason_list_text);
        ImageView scoreImg = view.findViewById(R.id.score_image);

        habitName.setText(habit.getTitle());
        habitReason.setText(habit.getReason());

        long score = habit.getScore();

        if (score < 20) {
            scoreImg.setImageResource(R.drawable.ic_disappointed_emoji);
        } else if (score < 40) {
            scoreImg.setImageResource(R.drawable.ic_orange_emoji);
        } else if (score < 60) {
            scoreImg.setImageResource(R.drawable.ic_yellow_emoji);
        } else if (score < 80) {
            scoreImg.setImageResource(R.drawable.ic_light_green_emoji);
        } else {
            scoreImg.setImageResource(R.drawable.ic_bright_green_emoji);
        }


        return view;
    }
}
