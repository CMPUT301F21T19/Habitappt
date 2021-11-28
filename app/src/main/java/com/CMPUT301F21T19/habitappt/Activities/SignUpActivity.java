/**
 * Copyright 2021 - 2021 CMPUT301F21T19 (Habitappt). All rights reserved. This document nor any
 * part of it may be reproduced, stored in a retrieval system or transmitted in any for or by any
 * means without prior permission of the members of CMPUT301F21T19 or by the professor and any
 * authorized TAs of the CMPUT301 class at the University of Alberta, fall term 2021.
 *
 * Class: SignUpActivity
 *
 * Description:
 * Allows user to signup for application, and thereby have access to application thereon given
 * login
 *
 */
package com.CMPUT301F21T19.habitappt.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.CMPUT301F21T19.habitappt.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    /**
     * xml ref
     */
    EditText usernameField;
    EditText passwordField;
    Button confirmButton;

    /**
     * firebase auth instance
     */
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);

        confirmButton = findViewById(R.id.confirm_signup);

        //initialize auth instance
        auth = FirebaseAuth.getInstance();

        //user selects confirm to attempt to create accoung
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();

                //create user as application user in firesbase
                auth.createUserWithEmailAndPassword(username,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //successfully created user, return to main activity
                        auth.signInWithEmailAndPassword(username,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Intent in = new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(in);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast toast = Toast.makeText(getApplicationContext(),"Login failed. Try again",Toast.LENGTH_SHORT);
                                toast.show();
                                finish();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast toast = Toast.makeText(getApplicationContext(),"Account creation failed. Try again.",Toast.LENGTH_SHORT);
                        e.printStackTrace();
                        toast.show();
                    }
                });

            }
        });
    }
}