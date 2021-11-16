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

public class FollowingList extends ArrayAdapter<Following> {
    private ArrayList<Following> followings;
    private Context context;

    public FollowingList(Context context, ArrayList<Following> followings) {
        super(context,0, followings);
        this.followings = followings;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.following_list_item, parent,false);
        }

        Following following = followings.get(position);

        TextView followingEmail = view.findViewById(R.id.following_email_text_view);

        followingEmail.setText(following.getUserEmail());

        return view;
    }
}
