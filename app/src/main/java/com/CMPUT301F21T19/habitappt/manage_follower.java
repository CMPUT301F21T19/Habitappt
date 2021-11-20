package com.CMPUT301F21T19.habitappt;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.GregorianCalendar;
import java.util.HashMap;

public class manage_follower extends DialogFragment {
    FirebaseAuth auth;
    FirebaseFirestore db;
    String tag;
    Activity THIS;
    FirebaseStorage storage;

    TextView username_text;

    String user;

    public manage_follower(String user){
        this.user = user;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        THIS = this.getActivity();
        tag = getTag();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();


        View view = LayoutInflater.from(getActivity()).inflate(R.layout.manage_follower,null);

        username_text = view.findViewById(R.id.manage_follower_username);

        username_text.setText(user);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setView(view)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //remove user from follower list, and from respective users following list

                        db.collection("Users")
                        .document(auth.getCurrentUser().getEmail())
                        .collection("Followers").document(user).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast toast = Toast.makeText(THIS,user + " removed as a follower.",Toast.LENGTH_SHORT);
                                toast.show();

                                db.collection("Users")
                                        .document(user)
                                        .collection("Followings")
                                        .document(auth.getCurrentUser().getEmail()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("success","removed from following");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("error",e.toString());
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("error",e.toString());
                            }
                        });



                    }
                }).setNeutralButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //exit out of dialog...
            }
        });

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }

}
