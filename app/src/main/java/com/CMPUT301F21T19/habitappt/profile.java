/**
 * Copyright 2021 - 2021 CMPUT301F21T19 (Habitappt). All rights reserved. This document nor any
 * part of it may be reproduced, stored in a retrieval system or transmitted in any for or by any
 * means without prior permission of the members of CMPUT301F21T19 or by the professor and any
 * authorized TAs of the CMPUT301 class at the University of Alberta, fall term 2021.
 *
 * Class : profile
 *
 * Description: creates and displays view for Profile screen
 *
 *  @version "%1%,%5%"
 *
 * **/
package com.CMPUT301F21T19.habitappt;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class profile extends Fragment {

    Button followingButton;
    Button followersButton;
    Button requestsButton;
    FloatingActionButton makeRequestButton;
    /**
     * Creates profile fragment from saved state
     * @param savedInstanceState Bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Inflates layout for profile fragment
     * @param inflater LayoutInflater
     * @param container ViewGroup
     * @param savedInstanceState Bundle
     * @return LayoutInflater
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        followingButton = view.findViewById(R.id.following_button);
        followersButton = view.findViewById(R.id.followers_button);
        requestsButton = view.findViewById(R.id.requests_button);
        makeRequestButton = view.findViewById(R.id.make_request);

        makeRequestButton.setVisibility(View.VISIBLE);

        followingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeRequestButton.setVisibility(View.VISIBLE);
            }
        });
        followersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeRequestButton.setVisibility(View.GONE);
            }
        });
        requestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeRequestButton.setVisibility(View.GONE);
            }
        });
        makeRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Requests().show(getActivity().getSupportFragmentManager(), "REQUEST");
            }
        });

        return view;
    }
}