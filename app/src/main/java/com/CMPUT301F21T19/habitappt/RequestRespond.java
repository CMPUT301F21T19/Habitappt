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
import android.widget.TextView;
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

public class RequestRespond extends DialogFragment {
    private Activity THIS;
    private String tag;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private FirebaseAuth auth;
    private Request request;

    public RequestRespond(Request request) {
        this.request = request;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        THIS = this.getActivity();
        tag = getTag();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();

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
                        db.collection("Users")
                                .document(request.getRequestedEmail())
                                .collection("Requests")
                                .document(request.getRequesterEmail())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(THIS, "Success: follow request denied", Toast.LENGTH_LONG).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(THIS, "Failure: internal error", Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                })
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String requestedEmail = request.getRequestedEmail();
                        String requesterEmail = request.getRequesterEmail();
                        auth.fetchSignInMethodsForEmail(requesterEmail).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                if (task.getResult().getSignInMethods().isEmpty()) {
                                    // user does not exist
                                    Toast.makeText(THIS, "Failure: user does not exist", Toast.LENGTH_LONG).show();
                                } else {
                                    DocumentReference followersDoc = db
                                            .collection("Users")
                                            .document(requesterEmail)
                                            .collection("Followings")
                                            .document(requestedEmail);
                                    DocumentReference followingsDoc = db
                                            .collection("Users")
                                            .document(requestedEmail)
                                            .collection("Followers")
                                            .document(requesterEmail);

                                    HashMap<String,Object> data = new HashMap<>();
                                    data.put("Time", GregorianCalendar.getInstance().getTimeInMillis());

                                    followersDoc.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.i("followersDoc","success");
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.i("followersDoc","failure");
                                        }
                                    });
                                    followingsDoc.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.i("followingsDoc","success");
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.i("followingsDoc","failure");
                                        }
                                    });
                                }
                                db.collection("Users")
                                        .document(request.getRequestedEmail())
                                        .collection("Requests")
                                        .document(request.getRequesterEmail())
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(THIS, "Success: now followed by " + request.getRequesterEmail(), Toast.LENGTH_LONG).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(THIS, "Failure: internal error", Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        });
                    }
                });

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }
}
