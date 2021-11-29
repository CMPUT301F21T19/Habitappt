/**
 * Copyright 2021 - 2021 CMPUT301F21T19 (Habitappt). All rights reserved. This document nor any
 * part of it may be reproduced, stored in a retrieval system or transmitted in any for or by any
 * means without prior permission of the members of CMPUT301F21T19 or by the professor and any
 * authorized TAs of the CMPUT301 class at the University of Alberta, fall term 2021.
 *
 * Class: FollowingList
 *
 * Description:
 * Encapsulates information regarding a users following into an arrayadapter
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

import com.CMPUT301F21T19.habitappt.Entities.Following;
import com.CMPUT301F21T19.habitappt.R;

import java.util.ArrayList;

public class FollowingList extends ArrayAdapter<Following> {
    /**
     * contains all followings (users)
     */
    private ArrayList<Following> followings;
    private Context context;

    public FollowingList(Context context, ArrayList<Following> followings) {
        super(context,0, followings);
        this.followings = followings;
        this.context = context;
    }

    /**
     * displays all following users for a given user in a list
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.following_list_item, parent,false);
        }

        Following following = followings.get(position);

        TextView followingEmail = view.findViewById(R.id.following_email_text_view);

        //lists followers by email
        followingEmail.setText(following.getUserEmail());

        return view;
    }
}
