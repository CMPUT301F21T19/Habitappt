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
 * This activity is used for signing up to habitappt. Allows a user to create an account.
 */
public class SignUpActivity extends AppCompatActivity {

    private EditText usernameField;
    private EditText passwordField;
    private Button confirmButton;

    private FirebaseAuth auth;

    /**
     * Get activity view.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);

        confirmButton = findViewById(R.id.confirm_signup);

        auth = FirebaseAuth.getInstance();

        //confirm button logic. creates the new account, returns an error toast if something goes wrong.
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameField.getText().toString();
                String password = passwordField.getText().toString();


                auth.createUserWithEmailAndPassword(username,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

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