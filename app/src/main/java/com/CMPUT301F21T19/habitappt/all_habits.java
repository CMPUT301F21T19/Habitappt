package com.CMPUT301F21T19.habitappt;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

public class all_habits extends abstract_habit_list_fragment {

    @Override
    public void parseDataBaseUpdate(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
        habitDataList.clear();

        for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
            String id = doc.getId();
            boolean isPrivate = (boolean) doc.getData().get("isPrivate");
            String title = (String) doc.getData().get("title");
            String reason = (String) doc.getData().get("reason");
            long dateToStart = (long) doc.getData().get("dateToStart");
            ArrayList<Boolean> datesToDo = (ArrayList<Boolean>) doc.getData().get("daysToDo");

            habitDataList.add(new Habit(title, reason, dateToStart, datesToDo, id, isPrivate));
        }

        habitAdapter.notifyDataSetChanged();
    }

}