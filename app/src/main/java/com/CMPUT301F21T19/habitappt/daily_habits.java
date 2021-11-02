package com.CMPUT301F21T19.habitappt;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class daily_habits extends Fragment {
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
                FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
                trans.replace(R.id.main_container,new view_habit(habitDataList.get(position)));
                trans.addToBackStack(null);
                trans.commit();
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
                    boolean isPrivate = (boolean) doc.getData().get("isPrivate");
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
                                habitDataList.add(new Habit(title, reason, dateToStart, datesToDo, id, isPrivate));
                            }
                        }
                    }
                }

                habitAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }
}