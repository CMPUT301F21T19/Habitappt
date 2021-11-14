package com.CMPUT301F21T19.habitappt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public abstract class abstract_habit_list_fragment extends Fragment {

    /**
     * Abstract class for whenever we want to display a list of habits from the database.
     */

    ListView habitListView;
    ArrayAdapter<Habit> habitAdapter;
    ArrayList<Habit> habitDataList;

    View addHabitButton;
    FirebaseFirestore db;
    FirebaseAuth auth;

    private View view;


    /**
     * This method must be implemented by any classes that extend this class. It tells the class how to process the habits in the users collection.
     * @param queryDocumentSnapshots
     * @param error
     */
    public abstract void parseDataBaseUpdate(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error);

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
        view = inflater.inflate(R.layout.fragment_habits_list, container, false);

        addHabitButton = view.findViewById(R.id.add_habit_button);
        habitListView = view.findViewById(R.id.habit_list);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        final CollectionReference collectionReference = db.collection(auth.getCurrentUser().getEmail());

        habitDataList = new ArrayList<>();
        habitAdapter = new HabitList(getContext(), habitDataList);
        habitListView.setAdapter(habitAdapter);

        //list view item listener
        habitListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
                trans.replace(R.id.main_container,new view_habit(habitDataList.get(position)));
                trans.addToBackStack("view_habit");
                trans.commit();
            }
        });

        //listener for pressing the button to add habits.
        addHabitButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new edit_habit().show(getActivity().getSupportFragmentManager(), "ADD");
            }
        });


        //listener for database updates. uses the abstract method defined in this class.
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                parseDataBaseUpdate(queryDocumentSnapshots,error);
            }
        });

        return view;
    }

}
