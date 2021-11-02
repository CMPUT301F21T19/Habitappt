package com.CMPUT301F21T19.habitappt;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Class that can be used to share methods and instance variables necessary between classes
 */
class SharedHelper {
    /**
     * deletes image given storage instance
     * @param id
     * @param storage
     */
    public static void deleteImage(String id, FirebaseStorage storage) {
        //remove image from firestore storage after deleting event
        StorageReference ref = storage.getReferenceFromUrl("gs://habitappt.appspot.com/default_user/" + id + ".jpg");
        ref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("STORAGE", "onSuccess: deleted image");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("STORAGE", "onFailure: could not delete image");
            }
        });
    }

    /**
     * deletes event from db given the habit, event, and db instance
     * @param event
     * @param habit
     * @param db
     */
    public static void removeEvent(HabitEvent event, Habit habit, FirebaseFirestore db){
        db.collection("Default User")
                .document(String.valueOf(habit.getId()))
                .collection("Event Collection")
                .document(String.valueOf(event.getId()))
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.i("data","event has been removed succesfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("data","event has not been removed succesfully" + e.toString());
                    }
                });
    }

    /**
     * removes habit given firestore db instance and habit
     * @param habit
     * @param db
     */
    public static void removeHabit(Habit habit, FirebaseFirestore db){
        db.collection("Default User")
                .document(String.valueOf(habit.getId()))
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.i("data","Data has been removed succesfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("data","Data could not be removed" + e.toString());
                    }
                });
    }
}
