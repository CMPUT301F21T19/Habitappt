package com.CMPUT301F21T19.habitappt;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class view_habit extends Fragment {

    private View view;

    private TextView habitTitle;
    private TextView habitReason;
    private TextView habitDateToStart;
    private ImageButton editButton;

    private ArrayList<TextView> daysToDo = new ArrayList<>();

    private Habit habit;

    public view_habit(Habit habit){
        this.habit = habit;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private String getStringDateFromLong(long l){
        Date date= new Date(l);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String dateText = df.format(date);
        return dateText;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_habit, container, false);

        habitTitle = view.findViewById(R.id.habit_title_display);
        habitReason = view.findViewById(R.id.habit_reason_display);
        habitDateToStart = view.findViewById(R.id.start_date_display);
        editButton = view.findViewById(R.id.edit_button);

        daysToDo.add(view.findViewById(R.id.monday_display));
        daysToDo.add(view.findViewById(R.id.tuesday_display));
        daysToDo.add(view.findViewById(R.id.wednesday_display));
        daysToDo.add(view.findViewById(R.id.thursday_display));
        daysToDo.add(view.findViewById(R.id.friday_display));
        daysToDo.add(view.findViewById(R.id.saturday_display));
        daysToDo.add(view.findViewById(R.id.sunday_display));

        habitTitle.setText(habit.getTitle());
        habitReason.setText(habit.getReason());

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new edit_habit(habit).show(getActivity().getSupportFragmentManager(), "EDIT");
            }
        });

        DocumentReference docReference = FirebaseFirestore.getInstance().collection("Default User").document(String.valueOf(habit.getId()));

        docReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                habitTitle.setText(value.get("title").toString());
                habitReason.setText(value.get("reason").toString());
                habitDateToStart.setText(getStringDateFromLong(Long.valueOf(value.get("dateToStart").toString())));

                ArrayList<Boolean> days = (ArrayList<Boolean>) value.get("daysToDo");
                for(int i=0;i<7;i++){
                    if(days.get(i)){
                        daysToDo.get(i).setBackgroundColor(Color.GREEN);
                    }
                    else{
                        daysToDo.get(i).setBackgroundColor(Color.WHITE);
                    }
                }

            }

        });

        habitDateToStart.setText(getStringDateFromLong(habit.getDateToStart()));

        for(int i=0;i<7;i++){
            if(habit.getDateSelected(i)){
                daysToDo.get(i).setBackgroundColor(Color.GREEN);
            }
            else{
                daysToDo.get(i).setBackgroundColor(Color.WHITE);
            }
        }

        // Inflate the layout for this fragment
        return view;
    }
}
