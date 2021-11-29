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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.CMPUT301F21T19.habitappt.Entities.User;
import com.CMPUT301F21T19.habitappt.R;

/**
 * This fragment is used to make follow requests.
 */
public class RequestMake extends DialogFragment {

    private User currentUser;

    /**
     * Gets the view for the fragment
     * @param savedInstanceState
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        //get current user object
        currentUser = new User();

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.request_make,null);
        EditText requestedUserEditText = view.findViewById(R.id.requested_user);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        //create alertdialog allowing user to make a follow request
        builder.setView(view)
                .setTitle("Make Follow Request")
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // "Cancel" button pressed
                    }
                })
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //send a request to user with current users email
                        String requestedEmail = requestedUserEditText.getText().toString();
                        currentUser.request(new User(requestedEmail),getActivity());
                    }
                });
        //create dialog
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }
}
