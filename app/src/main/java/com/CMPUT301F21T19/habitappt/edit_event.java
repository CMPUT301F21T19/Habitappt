package com.CMPUT301F21T19.habitappt;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TimeZone;

public class edit_event extends DialogFragment {

    private EditText eventComments;
    //image
    //location
    private CalendarView eventDate;

    private HabitEvent event;

    private String dialogTitle;

    private String removeTextTitle;

    long date_selected;

    private FirebaseFirestore db;

    protected edit_event THIS;

    public edit_event(HabitEvent event){
        this.event = event;
        this.dialogTitle = "Edit Habit";
        this.removeTextTitle = "Remove Habit";
        this.date_selected = event.getEventDate();
    }

    public edit_event(){
        this.event = new HabitEvent();
        this.dialogTitle = "Add Habit";
        this.removeTextTitle = "Cancel";
        this.date_selected = GregorianCalendar.getInstance().getTimeInMillis();
    }

    public void checkInput(){
        if(THIS.eventComments.getText().length() == 0){
            THIS.eventComments.setError("Comments cannot be empty");
        }
        //too long
        if(THIS.eventComments.getText().length() > 20){
            THIS.eventComments.setError("Maximum Length 0f 20: Please reduce");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        THIS = this;

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_event,null);

        db = FirebaseFirestore.getInstance();

        eventComments = view.findViewById(R.id.event_comments);
        eventDate = view.findViewById(R.id.event_date);

        eventComments.setText(event.getComment());
        eventDate.setDate(event.getEventDate());

        eventDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
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

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view)
                .setTitle(dialogTitle)
                .setNegativeButton(removeTextTitle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        db.collection("Default User")
                                .document(String.valueOf(THIS.event.getId()))
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
                            DocumentReference doc = db.collection("Default User").document(String.valueOf(THIS.event.getParentHabit())).collection("Event Collection").document(String.valueOf(THIS.event.getId()));

                            HashMap<String,Object> data = new HashMap<>();

                            data.put("comments",THIS.eventComments.getText().toString());
                            data.put("eventDate",THIS.date_selected);

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
                            DocumentReference doc = db.collection("Default User").document(String.valueOf(THIS.event.getParentHabit())).collection("Event Collection").document(String.valueOf(GregorianCalendar.getInstance().getTimeInMillis()));

                            HashMap<String,Object> data = new HashMap<>();

                            data.put("comments",THIS.eventComments.getText().toString());
                            data.put("eventDate",THIS.date_selected);

                            doc.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.i("data","Data for event has been added succesfully!");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.i("data","Data for event could not be added!" + e.toString());
                                }
                            });
                        }
                    }
                });
        final AlertDialog alertDialog = builder.create();

        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                //if habit title changed
                if(editable == THIS.eventComments.getEditableText()){
                    //if good length
                    if(editable.length() >= 1 && editable.length() < 20 && String.valueOf(THIS.eventDate.getDate()).length() >= 1 && String.valueOf(THIS.eventDate.getDate()).length() < 30){
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                    }
                    else{
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                        checkInput();
                    }
                }
                /*
                if(editable == String.valueOf(THIS.eventDate.getDate()).getEditableText()){
                    if(editable.length() >= 1 && editable.length() < 30 && THIS.eventComments.getText().length() >=1 && THIS.eventComments.getText().length() < 20){
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                    }
                    else {
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                        checkInput();
                    }
                }

                 */
            }
        };
        THIS.eventComments.addTextChangedListener(watcher);
        //THIS.eventDate.addTextChangedListener(watcher);

        return alertDialog;

    }
}
