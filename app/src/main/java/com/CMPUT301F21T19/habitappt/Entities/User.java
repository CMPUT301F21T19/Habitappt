package com.CMPUT301F21T19.habitappt.Entities;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.CMPUT301F21T19.habitappt.Activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

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

    private DocumentReference userReference;


    //get user object for desired user
    public User(String userEmail) {
        this.userEmail = userEmail;
        this.userReference = FirebaseFirestore.getInstance().collection("Users").document(this.userEmail);
    }

    //if no email is provided, get current users user object
    public User(){
        this(FirebaseAuth.getInstance().getCurrentUser().getEmail());
    }

    public String getUserEmail() {
        return userEmail;
    }


    public DocumentReference getUserReference(){
        return userReference;
    }

    public CollectionReference getHabitReference(){
        return userReference.collection("Habits");
    }

    public CollectionReference getHabitEventReference(Habit habit){
        return getHabitReference().document(habit.getId()).collection("Event Collection");
    }

    public Task<DocumentSnapshot> queryFollowing(User userToQuery) {
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

    public void acceptRequest(Request request,Activity currentActivity){
        String requestedEmail = request.getRequestedEmail();
        String requesterEmail = request.getRequesterEmail();
        FirebaseAuth.getInstance().fetchSignInMethodsForEmail(requesterEmail).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if (task.getResult().getSignInMethods().isEmpty()) {
                    // user does not exist
                    Toast.makeText(currentActivity, "Failure: user does not exist", Toast.LENGTH_LONG).show();
                } else {

                    User requester = new User(requesterEmail);
                    DocumentReference followingsDoc = requester.userReference
                            .collection("Followings")
                            .document(requestedEmail);

                    DocumentReference followersDoc = userReference
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

                userReference
                        .collection("Requests")
                        .document(request.getRequesterEmail())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(currentActivity, "Success: now followed by " + request.getRequesterEmail(), Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(currentActivity, "Failure: internal error", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }

    public void denyRequest(Request request,Activity currentActivity){
                userReference
                .collection("Requests")
                .document(request.getRequesterEmail())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(currentActivity, "Success: follow request denied", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(currentActivity, "Failure: internal error", Toast.LENGTH_LONG).show();
                    }
                });
    }
}
