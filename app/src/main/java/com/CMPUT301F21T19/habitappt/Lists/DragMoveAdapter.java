/**
 *  Copyright 2021 - 2021 CMPUT301F21T19 (Habitappt). All rights reserved. This document nor any
 *  part of it may be reproduced, stored in a retrieval system or transmitted in any for or by any
 *  means without prior permission of the members of CMPUT301F21T19 or by the professor and any
 *  authorized TAs of the CMPUT301 class at the University of Alberta, fall term 2021.
 *
 *  Class : DragMoveAdapter
 *
 *  Description : This class extends Recycler View Adapter, and tells it what to do when moving habits.
 */
package com.CMPUT301F21T19.habitappt.Lists;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.CMPUT301F21T19.habitappt.Entities.Habit;
import com.CMPUT301F21T19.habitappt.Entities.User;
import com.CMPUT301F21T19.habitappt.R;
import com.CMPUT301F21T19.habitappt.Utils.DragHabits;
import com.CMPUT301F21T19.habitappt.Utils.VisualIndicator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This extends RecyclerView.Adapter and initializes the adapter.
 * uses DragHabits and ItemTouchHelperAdapter.
 */
public class DragMoveAdapter extends RecyclerView.Adapter<DragMoveAdapter.DragViewHolder> implements DragHabits.ItemTouchHelperAdapter {

    private ArrayList<Habit> habitList;
    private DragListener dragListener;

    /**
     * Interface: used when a Habit is clicked within the recycler view.
     */
    public interface DragListener {
        void onHabitClick(int position);}

    /**
     * Constructor for the class.
     * @param habitList
     * @param dragListener : used when clicking or dragging is started.
     */
    public DragMoveAdapter(ArrayList<Habit> habitList, DragListener dragListener) {
        this.habitList = habitList;
        this.dragListener = dragListener;
    }

    /**
     * This view holder sets the attributes that display on the Recycler view.
     */
    public class DragViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView habitTitle;
        private TextView habitReason;
        private ImageView scoreImg;
        private ImageView checkMark;
        DragListener dragListener;
        public DragViewHolder(View view, DragListener dragListener){
            super(view);
            habitTitle = view.findViewById(R.id.habit_name);
            habitReason = view.findViewById(R.id.habit_reason_list_text);
            scoreImg = view.findViewById(R.id.score_image);
            checkMark = view.findViewById(R.id.check_mark);

            this.dragListener = dragListener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            dragListener.onHabitClick(getAdapterPosition());
        }
    }

    /**
     * This method sets the view holder for the Recycler View.
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public DragViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View habitView = LayoutInflater.from(parent.getContext()).inflate(R.layout.habit_list_item, parent, false);
        return new DragViewHolder(habitView, dragListener);
    }

    /**
     * This method binds the attributes to the view holder by getting and setting attributes.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull DragViewHolder holder, int position) {
        String title = habitList.get(position).getTitle();
        String reason = habitList.get(position).getReason();
        holder.habitTitle.setText(title);
        holder.habitReason.setText(reason);

        VisualIndicator visualIndicator = new VisualIndicator(habitList.get(position), false, null);

        visualIndicator.populateEventList();
        Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    final double score = visualIndicator.getScore();
                    final boolean checkToday = visualIndicator.GetIsTodayEventDone();
                    if (score < 20) {
                        holder.scoreImg.setImageResource(R.drawable.ic_disappointed_emoji);
                    } else if (score < 40) {
                        holder.scoreImg.setImageResource(R.drawable.ic_orange_emoji);
                    } else if (score < 60) {
                        holder.scoreImg.setImageResource(R.drawable.ic_yellow_emoji);
                    } else if (score < 80) {
                        holder.scoreImg.setImageResource(R.drawable.ic_light_green_emoji);
                    } else {
                        holder.scoreImg.setImageResource(R.drawable.ic_bright_green_emoji);
                    }

                    if (checkToday) {
                        holder.checkMark.setImageResource(R.drawable.ic_green_checkmark);
                    } else {
                        holder.checkMark.setImageResource(R.drawable.ic_empty);
                    }
                }
            }, 500);
        }

    /**
     * This method returns the size of the habit List.
     * @return int
     */
    @Override
    public int getItemCount() {
        return habitList.size();
    }


    /**
     * This method is used to swap habits in the collections when they are being moved.
     * @param fromPosition
     * @param toPosition
     */
    @Override
    public void OnItemMoved(int fromPosition, int toPosition) {
        Collections.swap(habitList,fromPosition,toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemSelected(DragViewHolder myViewHolder) {
    }

    @Override
    public void onItemCLear(DragViewHolder myViewHolder) { }

    @Override
    public void onDoneDragging() {
        updateDocIndex();
    }

    /**
     * This method updates the index field in the document when a habit is moved.
     */
    public void updateDocIndex() {
        User currentUser = new User();
        CollectionReference collectionReference = currentUser.getHabitReference();
        
        int length = habitList.size();

        for (int i = 0; i < length; i++) {
            int finalIndex = i;
            String id = habitList.get(i).getId();
            collectionReference
                    .whereEqualTo("id",id)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            collectionReference.document(id).update("index", finalIndex);}
                    });
        }
    }

}


