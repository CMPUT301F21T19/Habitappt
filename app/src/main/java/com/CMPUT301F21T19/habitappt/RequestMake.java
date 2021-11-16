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

package com.CMPUT301F21T19.habitappt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.GregorianCalendar;
import java.util.HashMap;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class RequestMake extends DialogFragment {
    protected Activity THIS;
    private String tag;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private FirebaseAuth auth;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        THIS = this.getActivity();
        tag = getTag();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.request_make,null);
        EditText requestedUserEditText = view.findViewById(R.id.requested_user);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

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
                        String requestedEmail = requestedUserEditText.getText().toString();
                        if (requestedEmail.equals(auth.getCurrentUser().getEmail())) {
                            // user requested self
                            Toast.makeText(THIS, "Failure: cannot request self", Toast.LENGTH_LONG).show();
                        } else if (Patterns.EMAIL_ADDRESS.matcher(requestedEmail).matches()) {
                            auth.fetchSignInMethodsForEmail(requestedEmail).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                @Override
                                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                    if (task.getResult().getSignInMethods().isEmpty()) {
                                        // user does not exist
                                        Toast.makeText(THIS, "Failure: user does not exist", Toast.LENGTH_LONG).show();
                                    } else {
                                        DocumentReference doc = db
                                                .collection("Users")
                                                .document(requestedEmail)
                                                .collection("Requests")
                                                .document(auth.getCurrentUser().getEmail());

                                        HashMap<String,Object> data = new HashMap<>();
                                        data.put("Requester", auth.getCurrentUser().getEmail());
                                        data.put("Requested", requestedEmail);
                                        data.put("Time", GregorianCalendar.getInstance().getTimeInMillis());

                                        doc.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.i("data","success");
                                                // request success
                                                Toast.makeText(THIS, "Success: requested to follow user " + requestedEmail, Toast.LENGTH_LONG).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.i("data", e.toString());
                                                // request failure
                                                Toast.makeText(THIS, "Failure: request incomplete", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                }
                            });
                        } else {
                            // incorrect email format
                            Toast.makeText(THIS, "Failure: requests must be made to an email", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }
}
