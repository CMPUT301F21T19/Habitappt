package com.CMPUT301F21T19.habitappt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * represents list of habits for associated user, inherits array adapter
 */
public class HabitList extends ArrayAdapter<Habit> {

    /**
     * actual data structure that holds habits for given user
     */
    private ArrayList<Habit> habits;
    /**
     * context to main activity
     */
    private Context context;

    /**
     * constructs habit list array adpater with passed context and list of habits
     * @param context
     * @param habits
     */
    public HabitList(Context context, ArrayList<Habit> habits) {
        super(context,0, habits);
        this.habits = habits;
        this.context = context;
    }

    /**
     * creates custom array adapter view for habits
     * @param position position of habit
     * @param convertView view to convert
     * @param parent parent
     * @return view
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.habit_list_item, parent,false);
        }

        Habit habit = habits.get(position);

        TextView habitName = view.findViewById(R.id.habit_name);
        TextView habitReason = view.findViewById(R.id.habit_reason_list_text);

        habitName.setText(habit.getTitle());
        habitReason.setText(habit.getReason());

//        ImageView habit_score = view.findViewById(R.id.habit_score);
//
//        long score = habit.calculateScore();
//        if (score < 20) {
//            habit_score.setImageResource(R.drawable.ic_disappointed_emoji);
//        } else if (score < 40) {
//            habit_score.setImageResource(R.drawable.ic_orange_emoji);
//        } else if (score < 60) {
//            habit_score.setImageResource(R.drawable.ic_yellow_emoji);
//        } else if (score < 80) {
//            habit_score.setImageResource(R.drawable.ic_light_green_emoji);
//        } else {
//            habit_score.setImageResource(R.drawable.ic_bright_green_emoji);}

        return view;
    }
}
