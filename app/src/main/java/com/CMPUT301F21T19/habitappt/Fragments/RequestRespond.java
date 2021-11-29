/**
 * Copyright 2021 - 2021 CMPUT301F21T19 (Habitappt). All rights reserved. This document nor any
 * part of it may be reproduced, stored in a retrieval system or transmitted in any for or by any
 * means without prior permission of the members of CMPUT301F21T19 or by the professor and any
 * authorized TAs of the CMPUT301 class at the University of Alberta, fall term 2021.
 *
 * Class: requests
 *
 * Description: Sends follow requests
 */

package com.CMPUT301F21T19.habitappt.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.CMPUT301F21T19.habitappt.Entities.Request;
import com.CMPUT301F21T19.habitappt.Entities.User;
import com.CMPUT301F21T19.habitappt.R;

/**
 * This fragment is used to respond to a follow request.
 */
public class RequestRespond extends DialogFragment {
    private Activity THIS;

    private Request request;
    private User currentUser;

    /**
     * Constructor for the fragment for responding to a request. Takes in a request object
     * @param request
     */
    public RequestRespond(Request request) {
        this.request = request;
    }

    /**
     * Gets the dialog view
     * @param savedInstanceState
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        THIS = this.getActivity();

        currentUser = new User();

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.request_respond,null);
        TextView requestTextView = view.findViewById(R.id.request_test_view);
        requestTextView.setText(request.getRequesterEmail() + " is requesting to follow you.");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        //build follow request alertdialog
        builder.setView(view)
                .setTitle("Follow Request")
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // "Cancel" button pressed
                    }
                })
                .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //doesnt allow user to be added to current users followers list
                        currentUser.denyRequest(request,THIS);
                    }
                })
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //accepts user into current users following list
                        currentUser.acceptRequest(request,THIS);
                    }
                });

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }
}
