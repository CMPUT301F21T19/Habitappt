package com.CMPUT301F21T19.habitappt;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
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

    SwipeMenuListView habitListView;
    ArrayAdapter<Habit> habitAdapter;
    ArrayList<Habit> habitDataList;

    View addHabitButton;
    FirebaseFirestore db;
    FirebaseAuth auth;

    SwipeMenuItem deleteItem;
    SwipeMenuItem editItem;

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

        final CollectionReference collectionReference = db
                .collection("Users")
                .document(auth.getCurrentUser().getEmail())
                .collection("Habits");

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

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                editItem = new SwipeMenuItem(
                        getContext());
                // set item background
                editItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                editItem.setWidth(170);
                // set item title
                editItem.setTitle("Edit");
                // set item title fontsize
                editItem.setTitleSize(18);
                // set item title font color
                editItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(editItem);

                // create "delete" item
                deleteItem = new SwipeMenuItem(
                        getContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(170);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        habitListView.setMenuCreator(creator);

        habitListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    //edit selected
                    case 0:
                        Log.d("MENUSELECT", "OnMenuItemClick: selected item" + index);
                        //get event
                        Habit editHabit = (Habit) habitListView.getItemAtPosition(position);
                        //create fragement
                        new edit_habit(editHabit).show(getActivity().getSupportFragmentManager(), "EDIT");
                        break;
                    //delete selected
                    case 1:
                        Log.d("MENUSELECT", "OnMenuItemClick: selected item" + index);


                        Habit delHabit = (Habit) habitListView.getItemAtPosition(position);

                        SharedHelper.removeHabit(delHabit,db);
                        break;
                }
                return false;
            }
        });


        return view;
    }

}
