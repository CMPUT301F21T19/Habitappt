/**
 * Copyright 2021 - 2021 CMPUT301F21T19 (Habitappt). All rights reserved. This document nor any
 * part of it may be reproduced, stored in a retrieval system or transmitted in any for or by any
 * means without prior permission of the members of CMPUT301F21T19 or by the professor and any
 * authorized TAs of the CMPUT301 class at the University of Alberta, fall term 2021.
 *
 * Class : Profile
 *
 * Description: creates and displays view for Profile screen
 *
 *  @version "%1%,%5%"
 *
 * **/
package com.CMPUT301F21T19.habitappt.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.CMPUT301F21T19.habitappt.Entities.Follower;
import com.CMPUT301F21T19.habitappt.Entities.User;
import com.CMPUT301F21T19.habitappt.Lists.FollowerList;
import com.CMPUT301F21T19.habitappt.Entities.Following;
import com.CMPUT301F21T19.habitappt.Lists.FollowingList;
import com.CMPUT301F21T19.habitappt.R;
import com.CMPUT301F21T19.habitappt.Entities.Request;
import com.CMPUT301F21T19.habitappt.Lists.RequestList;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * This class is the fragment for viewing ones own Profile. Displays followers, following, and follow requests
 * and allows a user to send follow requests to others.
 */
public class Profile extends Fragment {


    private User currentUser;

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
     * Creates Profile fragment from saved state
     * @param savedInstanceState Bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    /**
     * Inflates layout for Profile fragment
     * @param inflater LayoutInflater
     * @param container ViewGroup
     * @param savedInstanceState Bundle
     * @return LayoutInflater
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //get current user object
        currentUser = new User();

        profileListView = view.findViewById(R.id.profile_list);
        followingButton = view.findViewById(R.id.following_button);
        followerButton = view.findViewById(R.id.followers_button);
        requestButton = view.findViewById(R.id.requests_button);
        makeRequestButton = view.findViewById(R.id.make_request);
        usernameLabel = view.findViewById(R.id.username_field);

        //change button highlights based on which list is being viewed
        followingButton.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
        followerButton.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
        requestButton.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));

        usernameLabel.setText(currentUser.getUserEmail());

        final CollectionReference requestReference = currentUser.getUserReference()
                .collection("Requests");
        final CollectionReference followerReference = currentUser.getUserReference()
                .collection("Followers");
        final CollectionReference followingReference = currentUser.getUserReference()
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
                //change button highlights and switch listview
                followingButton.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
                followerButton.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
                requestButton.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));

                profileListView.setAdapter(followingAdapter);
            }
        });
        followerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //change button highlights and switch listview
                followingButton.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
                followerButton.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
                requestButton.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));

                profileListView.setAdapter(followerAdapter);
            }
        });
        requestButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //change button highlights and switch listview
                followingButton.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
                followerButton.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
                requestButton.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimaryDark));

                profileListView.setAdapter(requestAdapter);
            }
        });

        makeRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open up make request dialog
                new RequestMake().show(getActivity().getSupportFragmentManager(), "REQUEST");
            }
        });

        requestReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                //update follow request list from db
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
                //update follower list from db
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
                //update following list from db
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
                //decide behavior of clicking on a listview item depending on which listview is open
                if (adapterView.getItemAtPosition(i).getClass().getSimpleName().equals("Request")) {
                    // clicked on request
                    Request clickedRequest = (Request) adapterView.getItemAtPosition(i);
                    new RequestRespond(clickedRequest).show(getActivity().getSupportFragmentManager(), "REQUEST");
                } else if (adapterView.getItemAtPosition(i).getClass().getSimpleName().equals("Follower")) {
                    Follower clickFollower = (Follower) adapterView.getItemAtPosition(i);
                    FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
                    trans.replace(R.id.main_container, new ViewFollowing(clickFollower.getUserEmail()));
                    trans.addToBackStack("ViewFollowing");
                    trans.commit();
                } else if (adapterView.getItemAtPosition(i).getClass().getSimpleName().equals("Following")) {
                    // clicked on following
                    Following clickFollowing = (Following) adapterView.getItemAtPosition(i);
                    FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
                    trans.replace(R.id.main_container, new ViewFollowing(clickFollowing.getUserEmail()));
                    trans.addToBackStack("ViewFollowing");
                    trans.commit();
                } else {
                    Toast.makeText(getActivity(), "Error: " + adapterView.getItemAtPosition(i).getClass().getSimpleName(), Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }
}