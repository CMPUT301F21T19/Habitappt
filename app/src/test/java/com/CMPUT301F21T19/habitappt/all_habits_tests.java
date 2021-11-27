package com.CMPUT301F21T19.habitappt;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

//cant get firebase instance when app is not running. will have to create mock objects to test this
//but that doesn't seem arbitrary. uh oh.
public class all_habits_tests {
//
//    AllHabits AllHabits;
//
//    FirebaseFirestore db;
//
//    long test_id = -1;
//
//    Habit mockHabit;
//
//
//    @Before
//    public void setup(){
//        AllHabits = new AllHabits();
//        db = FirebaseFirestore.getInstance();
//    }
//
//    //test to see if object is in the database with matching parameters after dataBaseUpdate.
//    @Test
//    public void testDataBaseUpdate(){
//
//        ArrayList<Boolean> datesToDo = new ArrayList<>();
//        for(int i=0;i<7;i++){
//            datesToDo.add(false);
//        }
//
//        mockHabit = new Habit("test habit","test reason",0,datesToDo,String.valueOf(test_id),true);
//
//        HashMap<String,Object> data = new HashMap<>();
//        data.put("isPrivate",true);
//        data.put("id",String.valueOf(test_id));
//        data.put("title","test habit");
//        data.put("reason","test reason");
//        data.put("dateToStart",0);
//
//
//
//        datesToDo.set(0,true);
//
//        data.put("datesToDo",datesToDo);
//
//        db.collection("Default User").document(String.valueOf(test_id)).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//                Log.d("success","success on adding habit.");
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d("failure","failure on adding habit.");
//            }
//        });
//
//        assertTrue(AllHabits.habitDataList.contains(mockHabit));


}
