package com.CMPUT301F21T19.habitappt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Transaction;

public class MainActivity extends AppCompatActivity implements nav_bar.nav_bar_switch {

    public FirebaseFirestore db;

    @NonNull
    @Override
    public FragmentManager getSupportFragmentManager() {
        return super.getSupportFragmentManager();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get base firestore instance
        db = FirebaseFirestore.getInstance();

        //TEST: add to it
        FirestoreOps.testDBAddition(db);

        //start bottom navigation bar!
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.bottom_nav_bar,new nav_bar());
        transaction.replace(R.id.main_container,new profile());
        transaction.commit();




    }


    @Override
    public void switchFragment(nav_bar.nav_states nav_to) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if(nav_to == nav_bar.nav_states.PROFILE){
            transaction.replace(R.id.main_container,new profile());
        }
        else if(nav_to == nav_bar.nav_states.ALL_HABITS){
            transaction.replace(R.id.main_container,new all_habits());
        }
        else{
            transaction.replace(R.id.main_container,new daily_habits());
        }

        transaction.commit();
    }
}