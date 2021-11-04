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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EventList extends ArrayAdapter<HabitEvent> {

    private ArrayList<HabitEvent> events;
    private Context context;

    public EventList(@NonNull Context context, ArrayList<HabitEvent> events) {
        super(context, 0, events);
        this.events = events;
        this.context = context;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.event_list_item, parent,false);
        }

        HabitEvent event = events.get(position);

        TextView eventComment = view.findViewById(R.id.event_name);
        TextView eventDate = view.findViewById(R.id.event_date);

        ImageView img = view.findViewById(R.id.img);

        if(event.getImg() != null){
            img.setImageBitmap(event.getImg());
        }


        eventComment.setText(event.getComment());
        eventDate.setText(SharedHelper.getStringDateFromLong(event.getEventDate()));

        return view;
    }
}
