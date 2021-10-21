package com.CMPUT301F21T19.habitappt;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.Console;

public class nav_bar extends Fragment {

    View profile_button;
    View all_habits_button;
    View daily_habits_button;
    private nav_bar_switch listener;

    enum nav_states{
        PROFILE,ALL_HABITS,DAILY_HABITS;
    }
    public interface nav_bar_switch{
        void switchFragment(nav_states nav_to);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        listener = (nav_bar_switch) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("test!!!!!");
        View view = inflater.inflate(R.layout.nav_bar, container, false);

        profile_button = view.findViewById(R.id.profile_button);
        all_habits_button = view.findViewById(R.id.all_habit_button);
        daily_habits_button = view.findViewById(R.id.daily_habit_button);

        profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.switchFragment(nav_states.PROFILE);
            }
        });
        all_habits_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.switchFragment(nav_states.ALL_HABITS);
            }
        });
        daily_habits_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.switchFragment(nav_states.DAILY_HABITS);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }



}
