/**
 * Copyright 2021 - 2021 CMPUT301F21T19 (Habitappt). All rights reserved. This document nor any
 * part of it may be reproduced, stored in a retrieval system or transmitted in any for or by any
 * means without prior permission of the members of CMPUT301F21T19 or by the professor and any
 * authorized TAs of the CMPUT301 class at the University of Alberta, fall term 2021.
 *
 * Class: LoginActivity
 *
 * Description: Empty class for login screen
 *
 */

package com.CMPUT301F21T19.habitappt.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.CMPUT301F21T19.habitappt.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Activity for logging in to habitappt
 */
public class LoginActivity extends AppCompatActivity {

    private Button login_button;
    private Button signup_button;
    private EditText usernameField;
    private EditText passwordField;
    private FirebaseAuth auth;

    /**
     * Create activity view
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        login_button = findViewById(R.id.login_button);
        usernameField = findViewById(R.id.username);
        passwordField = findViewById(R.id.password);
        signup_button = findViewById(R.id.signup);

        //login button logic. login if creds are valid, otherwise send an error toast.
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();

                if(username.isEmpty() || password.isEmpty()){
                    Toast toast = Toast.makeText( getApplicationContext(),"Please enter a username and password.",Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

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
                          Toast toast = Toast.makeText( getApplicationContext(),"No account with provided email, or password is incorrect.",Toast.LENGTH_SHORT);
                          toast.show();
                      }
                  }
                );

            }
        });

        //logic for switching to signup activity
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(in);
            }
        });


    }
}