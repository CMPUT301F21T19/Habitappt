package com.CMPUT301F21T19.habitappt;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public abstract class recycler_view_fragment extends Fragment implements DragMoveAdapter.DragListener {

    /**
     * Abstract class for whenever we want to display a list of habits from the database.
     */

    DragMoveAdapter habitAdapter;
    ArrayList<Habit> habitDataList;

    RecyclerView habitView;
    View addHabitButton;
    FirebaseFirestore db;
    FirebaseAuth auth;
    String emailID;

    CollectionReference habitCollection;
    DocumentReference userDocument;
    CollectionReference currentUserHabits;

    private View view;

    /**
     * Called on creation of the fragment.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Defines the habit list view
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.recycler_view, container, false);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        emailID = user.getEmail();

        habitCollection = db.collection("Users");
        userDocument = habitCollection.document(emailID);
        currentUserHabits = userDocument.collection("Habits");

        addHabitButton = view.findViewById(R.id.add_habit_button);

        final CollectionReference collectionReference = db
                .collection("Users")
                .document(auth.getCurrentUser().getEmail())
                .collection("Habits");

        //listener for pressing the button to add habits.
        addHabitButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new edit_habit().show(getActivity().getSupportFragmentManager(), "ADD"); }
        });


        habitView = view.findViewById(R.id.recycler_habitList);
        habitDataList = new ArrayList<>();
        habitAdapter = new DragMoveAdapter(habitDataList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        habitView.setLayoutManager(layoutManager);
        initHabitOrder();
        parseDataBaseUpdate();
        habitView.setAdapter(habitAdapter);
        
        return view;
    }

    @Override
    public void onHabitClick(int position) {
        FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.main_container,new view_habit(habitDataList.get(position)));
        trans.addToBackStack("view_habit");
        trans.commit();
    };

    public void initHabitOrder(){
        ItemTouchHelper.Callback callback = new DragHabits(habitAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(habitView);
    }


    /**
     * This method must be implemented by any classes that extend this class. It tells the class how to process the habits in the users collection.
     *
     */
    public void parseDataBaseUpdate(){
        Query currentUser = currentUserHabits.orderBy("index", Query.Direction.ASCENDING);
        currentUser.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                habitDataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                    String id = doc.getId();
                    boolean isPrivate = (boolean) doc.getData().get("isPrivate");
                    String title = (String) doc.getData().get("title");
                    String reason = (String) doc.getData().get("reason");
                    long dateToStart = (long) doc.getData().get("dateToStart");
                    ArrayList<Boolean> datesToDo = (ArrayList<Boolean>) doc.getData().get("daysToDo");
                    habitDataList.add(new Habit(title, reason, dateToStart, datesToDo,id, isPrivate));
                }
                habitAdapter.notifyDataSetChanged();
            }
        });

        }

}
