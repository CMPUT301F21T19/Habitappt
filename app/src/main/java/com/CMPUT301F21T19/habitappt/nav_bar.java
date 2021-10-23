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

    public interface nav_bar_switch{
        void switchFragment(Fragment nav_to);
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        listener = (nav_bar_switch) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nav_bar, container, false);

        profile_button = view.findViewById(R.id.profile_button);
        all_habits_button = view.findViewById(R.id.all_habit_button);
        daily_habits_button = view.findViewById(R.id.daily_habit_button);

        profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.switchFragment(new profile());
            }
        });
        all_habits_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.switchFragment(new all_habits());
            }
        });
        daily_habits_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.switchFragment(new daily_habits());
            }
        });

        // Inflate the layout for this fragment
        return view;
    }



}
