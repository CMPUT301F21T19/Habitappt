package com.CMPUT301F21T19.habitappt;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

/**
 * Responsible for all operations on FireStore db
 */
public class FirestoreOps {

    public FirestoreOps() {
    }

    /**
     * Simple test operation of adding user Steve Nash to User info collection
     * @param db - database instance
     * @return trueorfalse - if insertion successful, true. false otherwise
     *
     */
    public static boolean testDBAddition(FirebaseFirestore db){
        final boolean[] result = {false};
        //Get a top level reference to User Information collection
        final CollectionReference collectionReference = db.collection("User Information");

        //iniializing new data instance
        HashMap<String, String> data = new HashMap<>();

        //setup basic user information
        final String testUserName = "Steve Nash";
        final String testUserAge = "33";

        final String testUserProv = "Alberta";
        final String testUserCity = "Edmonton";

        final String testUserHabitCount = "4";

        //adding basic user information to hm
        data.put("Age", testUserAge);
        data.put("Province", testUserProv);
        data.put("City", testUserCity);
        data.put("Habit Count", testUserHabitCount);

        //add info for given user Steve Nash
        collectionReference.document(testUserName)
                .set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("GOOD UPDATE", "Data addition successful");
                result[0] = true;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("BAD UPDATE", "Data addition failed: " + e.toString());
                result[0] = false;
            }
        });
        return result[0];
    }
}
