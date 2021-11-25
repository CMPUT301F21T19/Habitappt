package com.CMPUT301F21T19.habitappt;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * Contains information about a user
 */
public class User {
    /**
     * The email of the user
     */
    private String userEmail;

    public User(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Task<DocumentSnapshot> following(User userToQuery) {
        return FirebaseFirestore.getInstance()
                .collection("Users")
                .document(userEmail)
                .collection("Followings")
                .document(userToQuery.getUserEmail())
                .get();
    }

    public void unfollow(User userToUnfollow, Activity currentActivity) {
        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(userEmail)
                .collection("Followings")
                .document(userToUnfollow.getUserEmail())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(currentActivity,userToUnfollow.getUserEmail() + " removed from followers.",Toast.LENGTH_SHORT).show();
                        Log.d("success", "removed from followings (unfollowed)");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(currentActivity,userToUnfollow.getUserEmail() + " could not be removed.",Toast.LENGTH_SHORT).show();
                        Log.d("remove follower",e.toString());
                    }
                });
        FirebaseFirestore.getInstance()
                .collection("Users")
                .document(userToUnfollow.getUserEmail())
                .collection("Followers")
                .document(userEmail)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("success","removed from followers (sync)");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("remove following",e.toString());
                    }
                });
    }

    public void request(User userToRequest, Activity currentActivity) {
        FirebaseAuth.getInstance().fetchSignInMethodsForEmail(userToRequest.getUserEmail()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if (task.getResult().getSignInMethods().isEmpty()) {
                    // user does not exist
                    Toast.makeText(currentActivity, "Failure: user does not exist", Toast.LENGTH_LONG).show();
                } else {
                    DocumentReference doc = FirebaseFirestore.getInstance()
                            .collection("Users")
                            .document(userToRequest.getUserEmail())
                            .collection("Requests")
                            .document(userEmail);

                    HashMap<String, Object> data = new HashMap<>();
                    data.put("Requester", userEmail);
                    data.put("Requested", userToRequest.getUserEmail());
                    data.put("Time", GregorianCalendar.getInstance().getTimeInMillis());

                    doc.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.i("data", "success");
                            // request success
                            Toast.makeText(currentActivity, "Success: requested to follow user " + userToRequest.getUserEmail(), Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("data", e.toString());
                            // request failure
                            Toast.makeText(currentActivity, "Failure: request incomplete", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
}
