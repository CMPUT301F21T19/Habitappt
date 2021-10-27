package com.CMPUT301F21T19.habitappt;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class view_habit extends Fragment {

    private View view;

    private TextView habitTitle;
    private TextView habitReason;
    private TextView habitDateToStart;
    private ImageButton editButton;
    private ImageView emoji;

    View addEventButton;

    private ArrayList<TextView> daysToDo = new ArrayList<>();

    private Habit habit;

    ListView eventListView;
    ArrayAdapter<HabitEvent> eventAdapter;
    ArrayList<HabitEvent> eventDataList;

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
        emoji = view.findViewById(R.id.emoji);

        daysToDo.add(view.findViewById(R.id.monday_display));
        daysToDo.add(view.findViewById(R.id.tuesday_display));
        daysToDo.add(view.findViewById(R.id.wednesday_display));
        daysToDo.add(view.findViewById(R.id.thursday_display));
        daysToDo.add(view.findViewById(R.id.friday_display));
        daysToDo.add(view.findViewById(R.id.saturday_display));
        daysToDo.add(view.findViewById(R.id.sunday_display));

        habitTitle.setText(habit.getTitle());
        habitReason.setText(habit.getReason());


        if (habit.getScore() >=0 && habit.getScore() <=20){
            emoji.setImageResource(R.drawable.ic_dissapointed_emoji);}
        else if (habit.getScore() >20 && habit.getScore() <=40){
            emoji.setImageResource(R.drawable.ic_yellow_emoji);}
        else if (habit.getScore() >40 && habit.getScore() <=60){
            emoji.setImageResource(R.drawable.ic_orange_emoji);}
        else if (habit.getScore() >60 && habit.getScore() <=80){
            emoji.setImageResource(R.drawable.ic_light_green_emoji);}
        else if (habit.getScore() >80 && habit.getScore() <=100){
            emoji.setImageResource(R.drawable.ic_green_emoji);}

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new edit_habit(habit).show(getActivity().getSupportFragmentManager(), "EDIT");
            }
        });

        eventListView = view.findViewById(R.id.event_list);
        eventDataList = new ArrayList<>();
        eventAdapter = new EventList(getContext(), eventDataList);
        eventListView.setAdapter(eventAdapter);

        addEventButton = view.findViewById(R.id.add_event_button);
        addEventButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Info", "Clicked add event");
                new edit_event().show(getActivity().getSupportFragmentManager(), "ADD");
            }
        });

        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Info", "Clicked an event");
                FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
                trans.replace(R.id.main_container,new edit_event(eventDataList.get(position)));
                trans.commit();
            }
        });



        DocumentReference docReference = FirebaseFirestore.getInstance().collection("Default User").document(String.valueOf(habit.getId()));

        docReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if(value.get("title") == null){
                    FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
                    trans.replace(R.id.main_container,new all_habits());
                    trans.commit();
                    return;
                }

                habit.setTitle(value.get("title").toString());
                habit.setReason(value.get("reason").toString());
                habit.setDateToStart((Long.valueOf(value.get("dateToStart").toString())));

                habitTitle.setText(habit.getTitle());
                habitReason.setText(habit.getReason());
                habitDateToStart.setText(getStringDateFromLong(habit.getDateToStart()));

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

        CollectionReference eventCollectionReference = FirebaseFirestore.getInstance().collection("Default User").document(String.valueOf(habit.getId())).collection("Event Collection");
        eventCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                eventDataList.clear();

                for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                    String id = doc.getId();
                    String comments = (String) doc.getData().get("Comments");
                    Long eventDate = (Long) doc.getData().get("Event Date");
                    Log.d("info", comments);
                    Log.d("info", String.valueOf(eventDate));
                    eventDataList.add(new HabitEvent(comments, eventDate, habit, id));

                }

                eventAdapter.notifyDataSetChanged();
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
