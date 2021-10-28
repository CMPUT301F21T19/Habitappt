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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
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

    long date_selected;

    private FirebaseFirestore db;

    private FirebaseStorage storage;

    protected edit_event THIS;

    public edit_event(HabitEvent event){
        this.event = event;
        this.removeTextTitle = "Cancel";
        this.date_selected = event.getEventDate();
    }

//    public edit_event(){
//        this.event = new HabitEvent();
//        this.dialogTitle = "Add Habit";
//        this.removeTextTitle = "Cancel";
//        this.date_selected = GregorianCalendar.getInstance().getTimeInMillis();
//    }

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
                                                Log.d("img download url success","img download url success");
                                                data.put("eventImg",true);

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
                                                Log.d("failed to get download uri","failed to get download uri");
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
                                data.put("eventImg",false);
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
                                                Log.d("img download url success","img download url success");
                                                data.put("eventImg",true);

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
                                                Log.d("failed to get download uri","failed to get download uri");
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
                                data.put("eventImg",false);
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
