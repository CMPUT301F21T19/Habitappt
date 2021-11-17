package com.CMPUT301F21T19.habitappt;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.ads.mediationtestsuite.viewmodels.ItemViewHolder;

import java.util.ArrayList;
import java.util.List;

public class DragMoveAdapter extends RecyclerView.Adapter<DragMoveAdapter.DragViewHolder> implements DragHabits.ItemTouchHelperAdapter {

    private ArrayList<Habit> habitList;
    private ItemTouchHelper touchHelper;
    public interface DragListener {
        void onHabitClick(int position);}

    private DragListener moveHabit;

    public DragMoveAdapter(ArrayList<Habit> habitList, DragListener moveHabit) {
        this.habitList = habitList;
        this.moveHabit = moveHabit;
    }

    public class DragViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        DragListener moveHabit;
        public DragViewHolder(View view, DragListener moveHabit){
            super(view);
            this.moveHabit = moveHabit;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            moveHabit.onHabitClick(getAdapterPosition());
        }
    }
    @NonNull
    @Override
    public DragViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View habitView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_habits_list, parent, false);
        return new DragViewHolder(habitView, moveHabit);
    }

    @Override
    public void onBindViewHolder(@NonNull DragViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void onRowMoved(int fromPosition, int toPosition) {

    }

    @Override
    public void onRowSelected(DragViewHolder myViewHolder) {

    }

    @Override
    public void onRowClear(DragViewHolder myViewHolder) {

    }


}

