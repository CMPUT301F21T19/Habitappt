package com.CMPUT301F21T19.habitappt;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * This class is the fragment used for viewing followed users profiles. Shows their public habits and visual indicators,
 * aswell as the habits they have to complete today.
 */
public class view_following extends Fragment {
    MainActivity main;

    ListView habitListView;
    ArrayAdapter<Habit> habitAdapter;
    ArrayList<Habit> habitDataList;

    ArrayAdapter<Habit> dailyHabitAdapter;
    ArrayList<Habit> dailyHabitDataList;

    Button allHabitsButton;
    Button dailyHabitsButton;
    Button followButton;
    Button removeFollowerButton;

    TextView userProfileName;

    FirebaseFirestore db;
    FirebaseAuth auth;

    String user;

    Activity THIS;

    /**
     * Constructor that takes in the user whos profile is being viewed.
     * @param user
     */
    public view_following(String user){
        this.user = user;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        main = (MainActivity) context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_following,container,false);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        THIS = getActivity();

        habitListView = view.findViewById(R.id.profile_list);

        habitDataList = new ArrayList<>();
        habitAdapter = new HabitList(getContext(), habitDataList, true, this.user);
        habitListView.setAdapter(habitAdapter);

        dailyHabitDataList = new ArrayList<>();
        dailyHabitAdapter = new HabitList(getContext(),dailyHabitDataList, true, this.user);

        allHabitsButton = view.findViewById(R.id.all_habits_following);
        dailyHabitsButton = view.findViewById(R.id.daily_habits_following);
        followButton = view.findViewById(R.id.follow_button);
        removeFollowerButton = view.findViewById(R.id.remove_follower);

        userProfileName = view.findViewById(R.id.username_field);

        userProfileName.setText(user);

        //button highlight changes when pressed
        allHabitsButton.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
        dailyHabitsButton.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));

        removeFollowerButton.setVisibility(View.GONE);
        followButton.setVisibility(View.GONE);

        db.collection("Users")
                .document(auth.getCurrentUser().getEmail())
                .collection("Followers")
                .document(user).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    removeFollowerButton.setVisibility(View.VISIBLE);
                }
            }
        });

        //change follow button functionality if you are following the user or not.
        db.collection("Users")
                .document(auth.getCurrentUser().getEmail())
                .collection("Followings")
                .document(user).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.exists()){
                    followButton.setText("UNFOLLOW");
                }
                else{
                    followButton.setText("FOLLOW");
                }
                followButton.setVisibility(View.VISIBLE);
            }
        });

        removeFollowerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(THIS,user + " removed as a follower.",Toast.LENGTH_SHORT);
                toast.show();

                removeFollowerButton.setVisibility(View.GONE);

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

        });

        allHabitsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //update button colors and listview
                habitListView.setAdapter(habitAdapter);
                allHabitsButton.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
                dailyHabitsButton.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
            }
        });

        dailyHabitsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //update button colors and listview
                habitListView.setAdapter(dailyHabitAdapter);
                allHabitsButton.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimary));
                dailyHabitsButton.setBackgroundColor(getContext().getResources().getColor(R.color.colorPrimaryDark));
            }
        });

        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(followButton.getText().toString().equals("UNFOLLOW")){
                    //remove user from following, and remove current user from users follower list
                    db.collection("Users")
                            .document(auth.getCurrentUser().getEmail())
                            .collection("Followings").document(user).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast toast = Toast.makeText(THIS,user + " unfollowed.",Toast.LENGTH_SHORT);
                            toast.show();

                            db.collection("Users")
                                    .document(user)
                                    .collection("Followers")
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
                    main.getSupportFragmentManager().popBackStackImmediate();
                }
                else{
                    auth.fetchSignInMethodsForEmail(user).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                            if (task.getResult().getSignInMethods().isEmpty()) {
                                // user does not exist
                                Toast.makeText(THIS, "Failure: user does not exist", Toast.LENGTH_LONG).show();
                            } else {
                                DocumentReference doc = db
                                        .collection("Users")
                                        .document(user)
                                        .collection("Requests")
                                        .document(auth.getCurrentUser().getEmail());

                                HashMap<String, Object> data = new HashMap<>();
                                data.put("Requester", auth.getCurrentUser().getEmail());
                                data.put("Requested", user);
                                data.put("Time", GregorianCalendar.getInstance().getTimeInMillis());

                                doc.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.i("data", "success");
                                        // request success
                                        Toast.makeText(THIS, "Success: requested to follow user " + user, Toast.LENGTH_LONG).show();
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
                }

            }
        });



        habitListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //go to view user habit activity?
            }
        });

        CollectionReference all_habits = db.collection("Users")
                                            .document(user)
                                            .collection("Habits");

        all_habits.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                //update habit list based on privacy rule
                habitDataList.clear();

                for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {

                    db.collection("Users")
                            .document(auth.getCurrentUser().getEmail())
                            .collection("Followings")
                            .document(user).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            if(documentSnapshot.exists()){
                                String id = doc.getId();
                                boolean isPrivate = (boolean) doc.getData().get("isPrivate");

                                if(isPrivate){
                                    return;
                                }

                                String title = (String) doc.getData().get("title");
                                String reason = (String) doc.getData().get("reason");
                                long dateToStart = (long) doc.getData().get("dateToStart");
                                ArrayList<Boolean> datesToDo = (ArrayList<Boolean>) doc.getData().get("daysToDo");

                                habitDataList.add(new Habit(title, reason, dateToStart, datesToDo, id, isPrivate));
                                habitAdapter.notifyDataSetChanged();
                            }

                        }
                    });

                }
            }
        });

        all_habits.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                //update daily habit list based on privacy rule
                dailyHabitDataList.clear();

                for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                    String id = doc.getId();

                    db.collection("Users")
                            .document(auth.getCurrentUser().getEmail())
                            .collection("Followings")
                            .document(user).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            if(documentSnapshot.exists()){
                                boolean isPrivate = (boolean) doc.getData().get("isPrivate");
                                if(isPrivate){
                                    return;
                                }
                                String title = (String) doc.getData().get("title");
                                String reason = (String) doc.getData().get("reason");
                                long dateToStart = (long) doc.getData().get("dateToStart");
                                ArrayList<Boolean> datesToDo = (ArrayList<Boolean>) doc.getData().get("daysToDo");

                                Date todayDate = new Date(GregorianCalendar.getInstance().getTimeInMillis());
                                Date startDate = new Date(dateToStart);
                                Calendar todayCal = Calendar.getInstance();
                                todayCal.setTime(todayDate);
                                Calendar startCal = Calendar.getInstance();
                                startCal.setTime(startDate);

                                if (todayDate.getTime() > startDate.getTime()) {
                                    for (int i=0; i<datesToDo.size(); i++) {
                                        if (datesToDo.get(i) && todayCal.get(Calendar.DAY_OF_WEEK) == ((i+1)%7)+1) {
                                            dailyHabitDataList.add(new Habit(title, reason, dateToStart, datesToDo, id, isPrivate));
                                        }
                                    }
                                }
                            }
                        }
                    });

                }

                dailyHabitAdapter.notifyDataSetChanged();
            }
        });




        return view;
    }
}
