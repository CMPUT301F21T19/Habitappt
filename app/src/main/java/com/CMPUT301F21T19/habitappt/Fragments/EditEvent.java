/**
 * Copyright 2021 - 2021 CMPUT301F21T19 (Habitappt). All rights reserved. This document nor any
 * part of it may be reproduced, stored in a retrieval system or transmitted in any for or by any
 * means without prior permission of the members of CMPUT301F21T19 or by the professor and any
 * authorized TAs of the CMPUT301 class at the University of Alberta, fall term 2021.
 *
 * Class: EditEvent
 *
 * Description: Lets users add, edit, delete on the habit event page
 * @version
 * 1.2
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
 *                                     list created locally, refactored delete image in EditEvent
 *
 *   1.9       Hamzah    Oct-31-2021   Refactored removing habit, moved functionality to
 *                                     SharedHelper.java class
 *
 *   1.10      Andrew    Nov-01-2021   fixed event not deleting. both image and event are deleted
 *                                     upon clicking the delete button on a habit event now
 *   1.11      Hamzah    Nov-02-2021   fixed incorrect deletion of habit event when cancel is selected
 * =|=======|=|======|===|====|========|===========|================================================
 */

package com.CMPUT301F21T19.habitappt.Fragments;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.CMPUT301F21T19.habitappt.Activities.LocationActivity;
import com.CMPUT301F21T19.habitappt.Entities.Habit;
import com.CMPUT301F21T19.habitappt.Entities.HabitEvent;
import com.CMPUT301F21T19.habitappt.Entities.User;
import com.CMPUT301F21T19.habitappt.R;
import com.CMPUT301F21T19.habitappt.Utils.CustomTextWatcher;
import com.CMPUT301F21T19.habitappt.Utils.DualCustomTextWatcher;
import com.CMPUT301F21T19.habitappt.Utils.SharedHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TimeZone;

/**
 * This fragment is used to edit an event and its associated details.
 */
public class EditEvent extends DialogFragment {

    private EditText eventComments;
    //image
    //location
    private CalendarView eventDate;

    private ImageButton imgButton;

    private Button locationButton;

    private HabitEvent event;

    private String dialogTitle;

    private String removeTextTitle;

    private long dateSelected;

    private User currentUser;

    private FirebaseStorage storage;
    private Habit habit;

    private final int LAUNCH_MAP_ACTIVITY = 1;


    private double oldLat, oldLon;
    private TextView latTextView, lonTextView;

    /**
     * create a EditEvent object with the specified values
     * @param event habit event object
     * @param habit the habit object in which the event will be under
     * @param tag string which contain either the sting "ADD", "EDIT", or "REMOVE" to denote
     *            whether the event already exists or not
     */
    public EditEvent(HabitEvent event, Habit habit, String tag){
        this.event = event;
        if(tag == "ADD" || tag == "EDIT"){
            this.removeTextTitle = "Cancel";
        }
        else if (tag == "REMOVE"){
            this.removeTextTitle = "Remove Event";
        }
        this.dateSelected = event.getEventDate();
        this.habit = habit;

    }


    /**
     * Checks to see if event comment input length is valid
     */
    public void checkInput(){
        if(eventComments.getText().length() == 0){
            eventComments.setError("Comments cannot be empty");
        }
        //too long
        if(eventComments.getText().length() > 20){
            eventComments.setError("Maximum Length 0f 20: Please reduce");
        }
    }

    /**
     * Gets the view for the fragment
     * @param savedInstanceState
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        currentUser = new User();

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_event,null);


        storage = FirebaseStorage.getInstance();

        eventComments = view.findViewById(R.id.event_comments);
        eventDate = view.findViewById(R.id.event_date_calendar);
        imgButton = view.findViewById(R.id.event_img);

        //location button ref
        locationButton = view.findViewById(R.id.location_button);

        //get textviews
        latTextView = view.findViewById(R.id.current_lat_disp);
        lonTextView = view.findViewById(R.id.current_lon_disp);

        //if presaved valid #'s, set them
        if(event.getLocationLat() != -1 && event.getLocationLon() != -1){
            //set textview
            latTextView.setText("Latitude: " + (int) event.getLocationLat() +  "\u00B0");
            lonTextView.setText("Longitude: " + (int) event.getLocationLon() +  "\u00B0");
        }

        //keep reference to old lat and lon
        oldLat = event.getLocationLat();
        oldLon = event.getLocationLon();

        if(event.getImg() != null){
            imgButton.setImageBitmap(event.getImg());
        }

        eventComments.setText(event.getComment());
        eventDate.setDate(event.getEventDate());

        //logic for setting a new image for the habit event
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePictureIntent, 1);
            }
        });

        //button for setting the location for an event
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getLocationIntent = new Intent(getContext(), LocationActivity.class);

                startActivityForResult(getLocationIntent , LAUNCH_MAP_ACTIVITY);
            }
        });


        //button logic for setting event date
        eventDate.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                /*
                update calendar selection.
                 */
                GregorianCalendar cal = new GregorianCalendar(TimeZone.getDefault());
                cal.set(year,month,day);

                dateSelected = cal.getTimeInMillis();
            }
        });

        //below is the logic for updating the db with the new habit event details

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


                        if(removeTextTitle.equals("Remove Event")) {
                            //remove image from firestore storage after deleting event
                            SharedHelper.deleteImage(event.getId(), storage);
                            SharedHelper.removeEvent(event, habit, currentUser);
                        }
                        //cancel selected
                        else{
                            //put back old locations
                            event.setLocationLat(oldLat);
                            event.setLocationLon(oldLon);
                        }

                    }
                })
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if(getTag() == "EDIT"){
                            DocumentReference doc = currentUser.getHabitEventReference(event.getParentHabit())
                                    .document(String.valueOf(event.getId()));

                            HashMap<String,Object> data = new HashMap<>();

                            data.put("comments",eventComments.getText().toString());
                            data.put("eventDate", dateSelected);
                            //if real lat and lon, SAVE
                            if(event.getLocationLon() != -1 && event.getLocationLat() != -1) {
                                data.put("latitude", event.getLocationLat());
                                data.put("longitude", event.getLocationLon());
                            }

                            //get image and upload to firestorage!
                            if( event.getImg() != null){

                                Bitmap imageBitmap =  event.getImg();
                                StorageReference ref =  storage.getReferenceFromUrl("gs://habitappt.appspot.com/default_user");

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
                            habit.getHabitEvents().add( event); //////////////////////////////////////////////////////////////////////
                            DocumentReference doc = currentUser.getHabitEventReference(event.getParentHabit())
                                    .document(event.getId());

                            HashMap<String,Object> data = new HashMap<>();

                            data.put("comments", eventComments.getText().toString());
                            data.put("eventDate", dateSelected);

                            //if real lat and lon, SAVE
                            if(event.getLocationLon() != -1 && event.getLocationLat() != -1) {
                                data.put("latitude", event.getLocationLat());
                                data.put("longitude", event.getLocationLon());
                            }

                            if( event.getImg() != null){

                                Bitmap imageBitmap =  event.getImg();
                                StorageReference ref =  storage.getReferenceFromUrl("gs://habitappt.appspot.com/default_user");

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

        CustomTextWatcher textWatcher = new CustomTextWatcher(THIS.eventComments, alertDialog.getButton(AlertDialog.BUTTON_POSITIVE), 0, 20);
        eventComments.addTextChangedListener(textWatcher);
        

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

        //if returning from map activity
        if(requestCode == LAUNCH_MAP_ACTIVITY){
            if(resultCode == Activity.RESULT_OK){
                double latitude = data.getDoubleExtra("latitude", Activity.RESULT_CANCELED);
                double longitude = data.getDoubleExtra("longitude", Activity.RESULT_CANCELED);

                //update current coordinates
                event.setLocationLat(latitude);
                event.setLocationLon(longitude);

                //set textview
                latTextView.setText("Latitude: " + (int) latitude + "\u00B0");
                lonTextView.setText("Longitude: " + (int) longitude + "\u00B0");
            }
        }
    }
}


