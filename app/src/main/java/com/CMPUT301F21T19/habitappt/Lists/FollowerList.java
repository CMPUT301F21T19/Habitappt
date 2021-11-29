/**
 * Copyright 2021 - 2021 CMPUT301F21T19 (Habitappt). All rights reserved. This document nor any
 * part of it may be reproduced, stored in a retrieval system or transmitted in any for or by any
 * means without prior permission of the members of CMPUT301F21T19 or by the professor and any
 * authorized TAs of the CMPUT301 class at the University of Alberta, fall term 2021.
 *
 * Class: FollowerList
 *
 * Description:
 * Encapsulates information regarding a users followers into an arrayadapter
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.CMPUT301F21T19.habitappt.Entities.Follower;
import com.CMPUT301F21T19.habitappt.R;

import java.util.ArrayList;

public class FollowerList extends ArrayAdapter<Follower> {
    /**
     * arraylist of users followers
     */
    private ArrayList<Follower> followers;
    private Context context;

    public FollowerList(Context context, ArrayList<Follower> followers) {
        super(context,0, followers);
        this.followers = followers;
        this.context = context;
    }

    /**
     * displays all followers for a given user in a list
     * @param position
     * @param convertView
     * @param parent
     * @return view
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.follower_list_item, parent,false);
        }

        //get follower
        Follower follower = followers.get(position);

        TextView followerEmail = view.findViewById(R.id.follower_email_text_view);

        //list follower by their email
        followerEmail.setText(follower.getUserEmail());

        return view;
    }
}
