package com.CMPUT301F21T19.habitappt.Lists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.CMPUT301F21T19.habitappt.R;
import com.CMPUT301F21T19.habitappt.Entities.Request;

import java.util.ArrayList;

public class RequestList extends ArrayAdapter<Request> {
    private ArrayList<Request> requests;
    private Context context;

    public RequestList(Context context, ArrayList<Request> requests) {
        super(context,0, requests);
        this.requests = requests;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.request_list_item, parent,false);
        }

        Request request = requests.get(position);

        TextView requesterEmail = view.findViewById(R.id.requester_email_text_view);

        requesterEmail.setText(request.getRequesterEmail());

        return view;
    }
}
