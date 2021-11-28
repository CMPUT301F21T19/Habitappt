/**
 * Copyright 2021 - 2021 CMPUT301F21T19 (Habitappt). All rights reserved. This document nor any
 * part of it may be reproduced, stored in a retrieval system or transmitted in any for or by any
 * means without prior permission of the members of CMPUT301F21T19 or by the professor and any
 * authorized TAs of the CMPUT301 class at the University of Alberta, fall term 2021.
 *
 * Class: RequestList
 *
 * Description:
 * Encapsulates information regarding a users follow requests into an arrayadapter
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

import com.CMPUT301F21T19.habitappt.R;
import com.CMPUT301F21T19.habitappt.Entities.Request;

import java.util.ArrayList;

public class RequestList extends ArrayAdapter<Request> {
    /**
     * contains user requests
     */
    private ArrayList<Request> requests;
    private Context context;

    public RequestList(Context context, ArrayList<Request> requests) {
        super(context,0, requests);
        this.requests = requests;
        this.context = context;
    }

    /**
     * displays all follower requests for a given user in a list
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
            view = LayoutInflater.from(context).inflate(R.layout.request_list_item, parent,false);
        }

        Request request = requests.get(position);

        TextView requesterEmail = view.findViewById(R.id.requester_email_text_view);

        //list requesting follower by their email
        requesterEmail.setText(request.getRequesterEmail());

        return view;
    }
}
