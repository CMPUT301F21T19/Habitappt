/**
 * Copyright 2021 - 2021 CMPUT301F21T19 (Habitappt). All rights reserved. This document nor any
 * part of it may be reproduced, stored in a retrieval system or transmitted in any for or by any
 * means without prior permission of the members of CMPUT301F21T19 or by the professor and any
 * authorized TAs of the CMPUT301 class at the University of Alberta, fall term 2021.
 *
 * Class: edit_habit
 *
 * Description: Lets users add, edit, delete on the habit event page
 *
 * Changelog:
 * =|Version|=|User(s)|==|Date|========|Description|================================================
 *   1.0       Sohaib    Oct-24-2021   added Habit Events
 *   1.1       Andrew    Oct-25-2021   habit event editing and list
 *   1.2       Hamzah    Oct-31-2021   Added event swipe to edit/delete functionality, yet to actually complete
 *   1.3       Andrew    Oct-27-2021   added image activity and uploading images with events to firestorage
 *   1.4       Andrew    Oct-27-2021   Images for events
 *   1.5       Hamzah    Oct-28-2021   Added event deletion functionality
 *   1.6       Andrew    Oct-28-2021   fixed images not updating after editing habit event
 *   1.7       Hamzah    Oct-31-2021   Remove event image (if existing) from storage complete
 *
 *   1.8       Hamzah    Oct-31-2021   Added Getters/setters in habit class for habiteventlist also
 *                                     modified view habit to use habiteventslist list instead of
 *                                     list created locally, refactored delete image in edit_event
 *
 *   1.9       Hamzah    Oct-31-2021   Refactored removing habit, moved functionality to
 *                                     SharedHelper.java class
 *
 *   1.10      Andrew    Nov-01-2021   fixed event not deleting. both image and event are deleted
 *                                     upon clicking the delete button on a habit event now
 *   1.11      Hamzah    Nov-02-2021   fixed incorrect deletion of habit event when cancel is selected
 * =|=======|=|======|===|====|========|===========|================================================
 */

package com.CMPUT301F21T19.habitappt;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TimeZone;

public class edit_event extends DialogFragment {

    private EditText eventComments;
    //image
    //location
    private CalendarView eventDate;

    private ImageButton imgButton;

    private HabitEvent event;

    private String dialogTitle;

    private String removeTextTitle;
    private String tag;

    long date_selected;

    private FirebaseFirestore db;

    private FirebaseStorage storage;
    private Habit habit;

    protected edit_event THIS;

    /**
     * create a edit_event object with the specified values
     * @param event habit event object
     * @param habit the habit object in which the event will be under
     * @param tag string which contain either the sting "ADD", "EDIT", or "REMOVE" to denote
     *            whether the event already exists or not
     */
    public edit_event(HabitEvent event, Habit habit, String tag){
        this.event = event;
        this.removeTextTitle = this.tag == "EDIT" ? "Edit Event":"Remove Event";
        if(tag == "ADD" || tag == "EDIT"){
            this.removeTextTitle = "Cancel";
        }
        else if (tag == "REMOVE"){
            this.removeTextTitle = "Remove Event";
        }
        this.date_selected = event.getEventDate();
        this.habit = habit;
    }

//    public edit_event(){
//        this.event = new HabitEvent();
//        this.dialogTitle = "Add Habit";
//        this.removeTextTitle = "Cancel";
//        this.date_selected = GregorianCalendar.getInstance().getTimeInMillis();
//    }
    /**
     * Checks to see if event comment input length is less than twenty
     */
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

        storage = FirebaseStorage.getInstance();

        eventComments = view.findViewById(R.id.event_comments);
        eventDate = view.findViewById(R.id.event_date_calendar);
        imgButton = view.findViewById(R.id.event_img);

        if(event.getImg() != null){
            imgButton.setImageBitmap(event.getImg());
        }

        eventComments.setText(event.getComment());
        eventDate.setDate(event.getEventDate());

        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, 1);
            }
        });

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

        if(getTag() == "EDIT"){
            dialogTitle = "Edit Habit Event";
        }
        else if(getTag() == "REMOVE"){
            dialogTitle = "Remove Habit Event";
        }
        else{
            dialogTitle = "Add Habit Event";
        }

        builder.setView(view)
                .setTitle(dialogTitle)
                .setNegativeButton(removeTextTitle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //remove event from firestore database
                        getChildFragmentManager().popBackStack("viewevent", FragmentManager.POP_BACK_STACK_INCLUSIVE);


                        //remove image from firestore storage after deleting event
                        SharedHelper.deleteImage(event.getId(), storage);
                        SharedHelper.removeEvent(event,habit,db);
                    }
                })
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if(getTag() == "EDIT"){
                            DocumentReference doc = db.collection("Default User")
                                    .document(String.valueOf(THIS.event.getParentHabit().getId()))
                                    .collection("Event Collection")
                                    .document(String.valueOf(THIS.event.getId()));

                            HashMap<String,Object> data = new HashMap<>();

                            data.put("comments",THIS.eventComments.getText().toString());
                            data.put("eventDate",THIS.date_selected);

                            //get image and upload to firestorage!
                            if(THIS.event.getImg() != null){

                                Bitmap imageBitmap = THIS.event.getImg();
                                StorageReference ref = THIS.storage.getReferenceFromUrl("gs://habitappt.appspot.com/default_user");

                                StorageReference imgRef = ref.child(event.getId() + ".jpg");

                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                imageBitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                                byte[] imgData = baos.toByteArray();

                                UploadTask uploadTask = imgRef.putBytes(imgData);

                                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Log.d("img upload success","img upload success");
                                        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                Log.d("img dwnload url success","img download url success");
                                                data.put("eventImg",GregorianCalendar.getInstance().getTimeInMillis());

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
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("failed to get dwnld uri","failed to get download uri");
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("img upload failure","img upload failure");
                                    }
                                });



                            }
                            else{
                                data.put("eventImg",0);
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
                        else if(getTag() == "ADD"){

                            event.setId(String.valueOf(GregorianCalendar.getInstance().getTimeInMillis()));

                            DocumentReference doc = db.collection("Default User")
                                    .document(String.valueOf(THIS.event.getParentHabit().getId()))
                                    .collection("Event Collection")
                                    .document(event.getId());

                            HashMap<String,Object> data = new HashMap<>();

                            data.put("comments",THIS.eventComments.getText().toString());
                            data.put("eventDate",THIS.date_selected);

                            if(THIS.event.getImg() != null){

                                Bitmap imageBitmap = THIS.event.getImg();
                                StorageReference ref = THIS.storage.getReferenceFromUrl("gs://habitappt.appspot.com/default_user");

                                StorageReference imgRef = ref.child(event.getId() + ".jpg");

                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                imageBitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                                byte[] imgData = baos.toByteArray();

                                UploadTask uploadTask = imgRef.putBytes(imgData);

                                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Log.d("img upload success","img upload success");
                                        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                Log.d("img dwnld url success","img download url success");
                                                data.put("eventImg",GregorianCalendar.getInstance().getTimeInMillis());

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
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.d("failed to get dnld uri","failed to get download uri");
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("img upload failure","img upload failure");
                                    }
                                });



                            }
                            else{
                                data.put("eventImg",0);
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
                    }
                });
        final AlertDialog alertDialog = builder.create();


        alertDialog.show();

        if(eventComments.getText().length() == 0) {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1 && data != null){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imgButton.setImageBitmap(imageBitmap);
            event.setImg(imageBitmap);


        }
    }
}


