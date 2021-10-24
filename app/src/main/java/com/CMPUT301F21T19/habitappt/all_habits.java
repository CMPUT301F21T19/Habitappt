package com.CMPUT301F21T19.habitappt;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class all_habits extends Fragment {
    ListView habitListView;
    ArrayAdapter<Habit> habitAdapter;
    ArrayList<Habit> habitDataList;

    View addHabitButton;
    FirebaseFirestore db;

    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_habits_list, container, false);

        addHabitButton = view.findViewById(R.id.add_habit_button);
        habitListView = view.findViewById(R.id.habit_list);

        db = FirebaseFirestore.getInstance();

        final CollectionReference collectionReference = db.collection("Default User");

        habitDataList = new ArrayList<>();
        habitAdapter = new HabitList(getContext(), habitDataList);
        habitListView.setAdapter(habitAdapter);

        habitListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new edit_habit(habitDataList.get(position)).show(getActivity().getSupportFragmentManager(),"EDIT");
            }
        });

        addHabitButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new edit_habit().show(getActivity().getSupportFragmentManager(), "ADD");
            }
        });

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                habitDataList.clear();

                for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                    String id = doc.getId();
                    String title = (String) doc.getData().get("title");
                    String reason = (String) doc.getData().get("reason");
                    long dateToStart = (long) doc.getData().get("dateToStart");
                    ArrayList<Boolean> datesToDo = (ArrayList<Boolean>) doc.getData().get("daysToDo");

                    habitDataList.add(new Habit(title, reason, dateToStart, datesToDo, id));
                }

                habitAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }
}