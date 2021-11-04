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

/**
 * represents list of events for associated users habits, inherits array adapter
 */
public class EventList extends ArrayAdapter<HabitEvent> {

    /**
     * actual data structure that holds events for given users habits
     */
    private ArrayList<HabitEvent> events;
    private Context context;

    /**
     * constructs eventlist given passedcontext and list of events
     * @param context
     * @param events
     */
    public EventList(@NonNull Context context, ArrayList<HabitEvent> events) {
        super(context, 0, events);
        this.events = events;
        this.context = context;
    }

    /**
     * creates custom array adapter view for events
     * @param position
     * @param convertView
     * @param parent
     * @return view
     */
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
