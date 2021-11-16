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

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class profile extends Fragment {
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    private ListView profileListView;
    private Button followingButton;
    private Button followerButton;
    private Button requestButton;
    private FloatingActionButton makeRequestButton;
    private TextView usernameLabel;

    private ArrayAdapter<Request> requestAdapter;
    private ArrayList<Request> requestDataList;
    private ArrayAdapter<Follower> followerAdapter;
    private ArrayList<Follower> followerDataList;
    private ArrayAdapter<Following> followingAdapter;
    private ArrayList<Following> followingDataList;

    /**
     * Creates profile fragment from saved state
     * @param savedInstanceState Bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

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
        followerButton = view.findViewById(R.id.followers_button);
        requestButton = view.findViewById(R.id.requests_button);
        makeRequestButton = view.findViewById(R.id.make_request);
        usernameLabel = view.findViewById(R.id.username_field);
        //makeRequestButton.setVisibility(View.VISIBLE);

        usernameLabel.setText(auth.getCurrentUser().getEmail());

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
                .collection("Followings");

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
                //makeRequestButton.setVisibility(View.VISIBLE);

                //changing button tints programmatically isn't available in older apis so we gotta do this.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    followingButton.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.colorPrimaryDark));
                    followerButton.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.colorPrimary));
                    requestButton.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.colorPrimary));
                }

                profileListView.setAdapter(followingAdapter);
            }
        });
        followerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //makeRequestButton.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    followingButton.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.colorPrimary));
                    followerButton.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.colorPrimaryDark));
                    requestButton.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.colorPrimary));
                }

                profileListView.setAdapter(followerAdapter);
            }
        });
        requestButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //makeRequestButton.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    followingButton.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.colorPrimary));
                    followerButton.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.colorPrimary));
                    requestButton.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.colorPrimaryDark));
                }

                profileListView.setAdapter(requestAdapter);
            }
        });

        makeRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new RequestMake().show(getActivity().getSupportFragmentManager(), "REQUEST");
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

        profileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getItemAtPosition(i).getClass().getSimpleName().equals("Request")) {
                    // clicked on request
                    Request clickedRequest = (Request) adapterView.getItemAtPosition(i);
                    new RequestRespond(clickedRequest).show(getActivity().getSupportFragmentManager(), "REQUEST");
                } else if (adapterView.getItemAtPosition(i).getClass().getSimpleName().equals("Follower")) {
                    Follower clickFollower = (Follower) adapterView.getItemAtPosition(i);
                    new manage_follower(clickFollower.getUserEmail()).show(getActivity().getSupportFragmentManager(),"MANAGE");
                } else if (adapterView.getItemAtPosition(i).getClass().getSimpleName().equals("Following")) {
                    // clicked on following
                } else {
                    Toast.makeText(getActivity(), "Error: " + adapterView.getItemAtPosition(i).getClass().getSimpleName(), Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }
}