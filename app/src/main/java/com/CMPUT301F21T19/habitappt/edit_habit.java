package com.CMPUT301F21T19.habitappt;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TimeZone;

public class edit_habit extends DialogFragment {

    private EditText habitTitle;
    private EditText habitReason;
    private CalendarView habitDateToStart;
    private ArrayList<Button> days_of_week = new ArrayList<>();

    private Habit habit;

    private String dialogTitle;

    private String removeTextTitle;

    long date_selected;

    private FirebaseFirestore db;

    protected edit_habit THIS;

    public edit_habit(Habit habit){
        this.habit = habit;
        this.dialogTitle = "Edit Habit";
        this.removeTextTitle = "Remove Habit";
    }

    public edit_habit(){
        this.habit = new Habit();
        this.dialogTitle = "Add Habit";
        this.removeTextTitle = "Cancel";
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        THIS = this;

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_habit,null);

        db = FirebaseFirestore.getInstance();

        habitTitle = view.findViewById(R.id.habit_title);
        habitReason = view.findViewById(R.id.habit_reason);
        habitDateToStart = view.findViewById(R.id.date_to_start);

        habitTitle.setText(habit.getTitle());
        habitReason.setText(habit.getReason());
        habitDateToStart.setDate(habit.getDateToStart());

        days_of_week.add(view.findViewById(R.id.monday_button));
        days_of_week.add(view.findViewById(R.id.tuesday_button));
        days_of_week.add(view.findViewById(R.id.wednesday_button));
        days_of_week.add(view.findViewById(R.id.thursday_button));
        days_of_week.add(view.findViewById(R.id.friday_button));
        days_of_week.add(view.findViewById(R.id.saturday_button));
        days_of_week.add(view.findViewById(R.id.sunday_button));

        for(int i=0;i<7;i++){
            if(habit.getDateSelected(i)){
                days_of_week.get(i).setBackgroundColor(Color.GREEN);
            } else {
                days_of_week.get(i).setBackgroundColor(Color.WHITE);
            }

            final int index = new Integer(i);
            days_of_week.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(habit.getDateSelected(index)){
                        habit.setDateSelected(index,false);
                        days_of_week.get(index).setBackgroundColor(Color.WHITE);
                    }
                    else{
                        habit.setDateSelected(index,true);
                        days_of_week.get(index).setBackgroundColor(Color.GREEN);
                    }
                }
            });
        }

        habitDateToStart.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                /*
                update calendar selection.
                 */
                GregorianCalendar cal = new GregorianCalendar(TimeZone.getDefault());
                cal.set(year,month,day);

                date_selected = cal.getTimeInMillis();
            }
        });

        return new AlertDialog.Builder(requireContext())
                .setView(view)
                .setTitle(dialogTitle)
                .setNegativeButton(removeTextTitle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        db.collection("Default User")
                                .document(String.valueOf(THIS.habit.getId()))
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.i("data","Data has been added succesfully!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.i("data","Data could not be added!" + e.toString());
                                    }
                                });
                    }
                })
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if(getTag() == "EDIT"){
                            DocumentReference doc = db.collection("Default User").document(String.valueOf(THIS.habit.getId()));

                            HashMap<String,Object> data = new HashMap<>();

                            data.put("title",THIS.habitTitle.getText().toString());
                            data.put("reason",THIS.habitReason.getText().toString());
                            data.put("dateToStart",THIS.date_selected);
                            data.put("daysToDo",THIS.habit.getWeekly());

                            doc.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.i("data","Data has been added succesfully!");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.i("data","Data could not be added!" + e.toString());
                                }
                            });
                        }
                        else if(getTag() == "ADD"){
                            DocumentReference doc = db.collection("Default User").document(String.valueOf(GregorianCalendar.getInstance().getTimeInMillis()));

                            HashMap<String,Object> data = new HashMap<>();

                            data.put("title",THIS.habitTitle.getText().toString());
                            data.put("reason",THIS.habitReason.getText().toString());
                            data.put("dateToStart",THIS.date_selected);
                            data.put("daysToDo",THIS.habit.getWeekly());

                            doc.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.i("data","Data has been added succesfully!");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.i("data","Data could not be added!" + e.toString());
                                }
                            });
                        }
                    }
                }).create();
    }
}
