package com.CMPUT301F21T19.habitappt.Lists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.CMPUT301F21T19.habitappt.Entities.Follower;
import com.CMPUT301F21T19.habitappt.R;

import java.util.ArrayList;

public class FollowerList extends ArrayAdapter<Follower> {
    private ArrayList<Follower> followers;
    private Context context;

    public FollowerList(Context context, ArrayList<Follower> followers) {
        super(context,0, followers);
        this.followers = followers;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.follower_list_item, parent,false);
        }

        Follower follower = followers.get(position);

        TextView followerEmail = view.findViewById(R.id.follower_email_text_view);

        followerEmail.setText(follower.getUserEmail());

        return view;
    }
}
