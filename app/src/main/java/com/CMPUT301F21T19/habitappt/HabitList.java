package com.CMPUT301F21T19.habitappt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class HabitList extends ArrayAdapter<Habit> {

    private ArrayList<Habit> habits;
    private Context context;

    public HabitList(Context context, ArrayList<Habit> habits) {
        super(context,0, habits);
        this.habits = habits;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.habit_list_item, parent,false);
        }

        Habit habit = habits.get(position);

        TextView habitName = view.findViewById(R.id.habit_name);

        habitName.setText(habit.getTitle());

        ImageView emoji = view.findViewById(R.id.emoji);

        if (habit.getScore() >=0 && habit.getScore() <=20){
            emoji.setImageResource(R.drawable.ic_disappointed_emoji);}
        else if (habit.getScore() >20 && habit.getScore() <=40){
            emoji.setImageResource(R.drawable.ic_yellow_emoji);}
        else if (habit.getScore() >40 && habit.getScore() <=60){
            emoji.setImageResource(R.drawable.ic_orange_emoji);}
        else if (habit.getScore() >60 && habit.getScore() <=80){
            emoji.setImageResource(R.drawable.ic_light_green_emoji);}
        else if (habit.getScore() >80 && habit.getScore() <=100){
            emoji.setImageResource(R.drawable.ic_green_emoji);}

        return view;
    }
}
