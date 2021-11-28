/**
 * Copyright 2021 - 2021 CMPUT301F21T19 (Habitappt). All rights reserved. This document nor any
 * part of it may be reproduced, stored in a retrieval system or transmitted in any for or by any
 * means without prior permission of the members of CMPUT301F21T19 or by the professor and any
 * authorized TAs of the CMPUT301 class at the University of Alberta, fall term 2021.
 *
 * Class: LoginActivity
 *
 * Description:
 * Main login screen for user: User must enter correct username and password in order to
 * login and have access to all their data
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

public class LoginActivity extends AppCompatActivity {

    /**
     * xml attribute ref
     */
    Button login_button;
    Button signup_button;
    EditText usernameField;
    EditText passwordField;

    /**
     * firebase auth instance
     */
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initialize firebase auth
        auth = FirebaseAuth.getInstance();

        login_button = findViewById(R.id.login_button);
        usernameField = findViewById(R.id.username);
        passwordField = findViewById(R.id.password);
        signup_button = findViewById(R.id.signup);

        //when login selected, check that user exists and has valid credentials
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();

                //if no username/password entered, then do not sign in
                if(username.isEmpty() || password.isEmpty()){
                    Toast toast = Toast.makeText( getApplicationContext(),"Please enter a username and password.",Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                //valid data entered, check if they exists in firebase
                auth.signInWithEmailAndPassword(username,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //valid user, move on to following profile fragment
                        Intent in = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(in);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception e) {
                          Toast toast = Toast.makeText( getApplicationContext(),"No account with provided email, or password is incorrect.",Toast.LENGTH_SHORT);
                          toast.show();
                      }
                  }
                );

            }
        });

        //if signup is selected, user can create an account
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(in);
            }
        });


    }
}