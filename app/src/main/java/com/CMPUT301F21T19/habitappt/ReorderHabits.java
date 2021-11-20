package com.CMPUT301F21T19.habitappt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * This is a subclass of ItemToucheHelper.Callback which can construct drag and swipe
 * @author Shanshan Wei/swei3
 * @see ItemTouchHelper
 * @see RecyclerTouchHelper
 */
public class ReorderHabits extends ItemTouchHelper.Callback {

    private RecyclerTouchHelper recyclerTouchHelper;

    /**
     * This is the constructor
     * @author Shanshan Wei/swei3
     * @param recyclerTouchHelper
     */
    public ReorderHabits(RecyclerTouchHelper recyclerTouchHelper) {
        this.recyclerTouchHelper = recyclerTouchHelper;
    }

    /**
     * This means that the user has to long click on the item, so that the drag will function
     * @author Shanshan Wei/swei3
     * @return boolean
     */
    @Override
    public boolean isLongPressDragEnabled(){
        return true;
    }

    /**
     * This discards the swipe action
     * @author Shanshan Wei/swei3
     * @return boolean
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    /**
     * This sets a drag flag for movement
     * @author Shanshan Wei/swei3
     * @param recyclerView
     * @param viewHolder
     * @return
     */
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(dragFlag,0);
    }

    /**
     * This overrides the onMove method of RecyclerTouchHelper
     * @author Shanshan Wei/swei3
     * @param recyclerView
     * @param viewHolder
     * @param target
     * @return boolean
     */
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        this.recyclerTouchHelper.onRowMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    /**
     * touch helper will help to identify the drag and drop and implements onRowSelected method
     * @author Shanshan Wei/swei3
     * @param viewHolder
     * @param actionState
     */
    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if(actionState != ItemTouchHelper.ACTION_STATE_IDLE){
            if(viewHolder instanceof DragMoveAdapter.DragViewHolder){
                DragMoveAdapter.DragViewHolder myViewHolder = (DragMoveAdapter.DragViewHolder) viewHolder;
                recyclerTouchHelper.onRowSelected(myViewHolder);
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    /**
     * This overrides the clearView method and implements the onRowClear method
     * @author Shanshan Wei/swei3
     * @param recyclerView
     * @param viewHolder
     */
    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if(viewHolder instanceof DragMoveAdapter.DragViewHolder){
            DragMoveAdapter.DragViewHolder myViewHolder = (DragMoveAdapter.DragViewHolder) viewHolder;
            recyclerTouchHelper.onRowClear(myViewHolder);
        }

    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

    /**
     * Interfaces for above three overrided methods
     * @author Shanshan Wei/swei3
     */
    public interface RecyclerTouchHelper{
        void onRowMoved(int from, int to);
        void onRowSelected(DragMoveAdapter.DragViewHolder myViewHolder);
        void onRowClear(DragMoveAdapter.DragViewHolder myViewHolder);
    }
}
