/**
 * Copyright 2021 - 2021 CMPUT301F21T19 (Habitappt). All rights reserved. This document nor any
 * part of it may be reproduced, stored in a retrieval system or transmitted in any for or by any
 * means without prior permission of the members of CMPUT301F21T19 or by the professor and any
 * authorized TAs of the CMPUT301 class at the University of Alberta, fall term 2021.
 *
 * Class: EventList
 *
 * Description: custom array adapter that represents list of events
 * for associated user's habits
 *
 * @version "%1%,%5%"
 *
 *
 */
package com.CMPUT301F21T19.habitappt.Lists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.CMPUT301F21T19.habitappt.Entities.HabitEvent;
import com.CMPUT301F21T19.habitappt.R;
import com.CMPUT301F21T19.habitappt.Utils.SharedHelper;

import java.util.ArrayList;

/**
 * represents list of events for associated users habits, inherits array adapter
 */
public class EventList extends ArrayAdapter<HabitEvent> {

    /**
     * actual data structure that holds events for given users habits
     */
    private ArrayList<HabitEvent> events;
    private Context context;

    /**
     * constructs eventlist given passedcontext and list of events
     * @param context context of main activity
     * @param events list of events
     */
    public EventList(@NonNull Context context, ArrayList<HabitEvent> events) {
        super(context, 0, events);
        this.events = events;
        this.context = context;
    }

    /**
     * creates custom array adapter view for events
     * @param position position on array adapter
     * @param convertView view
     * @param parent parent
     * @return view
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.event_list_item, parent,false);
        }

        HabitEvent event = events.get(position);

        TextView eventComment = view.findViewById(R.id.event_name);
        TextView eventDate = view.findViewById(R.id.event_date);

        ImageView img = view.findViewById(R.id.img);

        if(event.getImg() != null){
            img.setImageBitmap(event.getImg());
        }


        eventComment.setText(event.getComment());
        eventDate.setText(SharedHelper.getStringDateFromLong(event.getEventDate()));

        return view;
    }
}
