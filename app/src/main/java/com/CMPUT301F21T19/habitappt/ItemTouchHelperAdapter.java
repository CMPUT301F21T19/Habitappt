package com.CMPUT301F21T19.habitappt;

public interface ItemTouchHelperAdapter {
    void onItemMove(int fromPosition, int toPosition);

    void onItemSwipe(int position);
}
