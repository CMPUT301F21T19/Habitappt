package com.CMPUT301F21T19.habitappt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.ads.mediationtestsuite.viewmodels.ItemViewHolder;

import java.util.ArrayList;
import java.util.List;

public class DragMoveAdapter extends RecyclerView.Adapter implements ItemTouchHelperAdapter {

    private ArrayList<Habit> habitList;
    private ItemTouchHelper touchHelper;
    private DragListener moveHabit;

    public DragMoveAdapter(ArrayList<Habit> habitList, DragListener moveHabit) {
        this.habitList = habitList;
        this.moveHabit = moveHabit;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.habit_list_item, parent, false);
        return new DragViewHolder(view, touchHelper, moveHabit);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((DragViewHolder) holder).attachData(habitList.get(position));

    }

    @Override
    public int getItemCount() {
        return habitList.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Habit fromHabit = habitList.get(fromPosition);
        habitList.remove(fromHabit);
        habitList.add(toPosition, fromHabit);   //Removed habit is re-added at the new position.
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemSwipe(int position) {
        habitList.remove(position);
        notifyItemRemoved(position);
    }

    public void setTouchHelper(ItemTouchHelper helper) {
        this.touchHelper = helper;
    }

}
