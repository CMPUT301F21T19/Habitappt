package com.CMPUT301F21T19.habitappt;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class DragViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener, GestureDetector.OnGestureListener {


    private ItemTouchHelper touchHelper;
    private DragMoveAdapter.DragListener moveHabit;
    GestureDetector gestureDetector;


    public DragViewHolder(View habitView, ItemTouchHelper helper, DragMoveAdapter.DragListener habitListener) {
        super(habitView);
        touchHelper = helper;
        moveHabit = habitListener;
        gestureDetector = new GestureDetector(habitView.getContext(), this);

        habitView.setOnTouchListener(this);

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
        moveHabit.onHabitClick(getAdapterPosition());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        touchHelper.startDrag(this);
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

}
