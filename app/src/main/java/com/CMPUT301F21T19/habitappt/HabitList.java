package com.CMPUT301F21T19.habitappt;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

public class HabitList extends ArrayAdapter<Habit> {

    public HabitList(@NonNull Context context, int resource) {
        super(context, resource);
    }

}
