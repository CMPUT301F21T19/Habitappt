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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.GregorianCalendar;
import java.util.HashMap;

import com.CMPUT301F21T19.habitappt.Entities.User;
import com.CMPUT301F21T19.habitappt.R;
import com.CMPUT301F21T19.habitappt.Entities.Request;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class RequestRespond extends DialogFragment {
    private Activity THIS;

    private Request request;
    private User currentUser;

    public RequestRespond(Request request) {
        this.request = request;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        THIS = this.getActivity();

        currentUser = new User();

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.request_respond,null);
        TextView requestTextView = view.findViewById(R.id.request_test_view);
        requestTextView.setText(request.getRequesterEmail() + " is requesting to follow you.");
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

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
                        currentUser.denyRequest(request,THIS);
                    }
                })
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        currentUser.acceptRequest(request,THIS);
                    }
                });

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }
}
