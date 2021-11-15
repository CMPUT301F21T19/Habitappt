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

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class profile extends Fragment {
    FirebaseFirestore db;
    FirebaseAuth auth;

    ListView profileListView;
    Button followingButton;
    Button followersButton;
    Button requestsButton;
    FloatingActionButton makeRequestButton;

    ArrayAdapter<Request> requestAdapter;
    ArrayList<Request> requestDataList;
    ArrayAdapter<Follower> followerAdapter;
    ArrayList<Follower> followerDataList;
    ArrayAdapter<Following> followingAdapter;
    ArrayList<Following> followingDataList;

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

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        profileListView = view.findViewById(R.id.profile_list);
        followingButton = view.findViewById(R.id.following_button);
        followersButton = view.findViewById(R.id.followers_button);
        requestsButton = view.findViewById(R.id.requests_button);
        makeRequestButton = view.findViewById(R.id.make_request);
        makeRequestButton.setVisibility(View.VISIBLE);

        final CollectionReference requestReference = db
                .collection("Users")
                .document(auth.getCurrentUser().getEmail())
                .collection("Requests");
        final CollectionReference followerReference = db
                .collection("Users")
                .document(auth.getCurrentUser().getEmail())
                .collection("Followers");
        final CollectionReference followingReference = db
                .collection("Users")
                .document(auth.getCurrentUser().getEmail())
                .collection("Following");

        requestDataList = new ArrayList<>();
        requestAdapter = new RequestList(getContext(), requestDataList);
        followerDataList = new ArrayList<>();
        followerAdapter = new FollowerList(getContext(), followerDataList);
        followingDataList = new ArrayList<>();
        followingAdapter = new FollowingList(getContext(), followingDataList);
        profileListView.setAdapter(followingAdapter);

        followingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeRequestButton.setVisibility(View.VISIBLE);
                profileListView.setAdapter(followingAdapter);
            }
        });
        followersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeRequestButton.setVisibility(View.GONE);
                profileListView.setAdapter(followerAdapter);
            }
        });
        requestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeRequestButton.setVisibility(View.GONE);
                profileListView.setAdapter(requestAdapter);
            }
        });
        makeRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Requests().show(getActivity().getSupportFragmentManager(), "REQUEST");
            }
        });

        requestReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                requestDataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                    String requestedEmail = (String) doc.getData().get("Requested");
                    String requesterEmail = (String) doc.getData().get("Requester");
                    long time = (long) doc.getData().get("Time");
                    requestDataList.add(new Request(requesterEmail, requestedEmail, time));
                }
                requestAdapter.notifyDataSetChanged();
            }
        });
        followerReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                followerDataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                    String followerEmail = doc.getId();
                    followerDataList.add(new Follower(followerEmail));
                }
                followerAdapter.notifyDataSetChanged();
            }
        });
        followingReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                followingDataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                    String followingEmail = doc.getId();
                    followingDataList.add(new Following(followingEmail));
                }
                followingAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }
}