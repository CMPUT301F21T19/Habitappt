/**
 *
 *  Class : DragHabits
 *
 *  Description: This class is a subclass of ItemTouchHelper.Callback and implements ItemTouchHelper which can drag and swap items.
 *
 *  Reference: This subclass was constructed from a library with the itemTouchHelper drag and swipe methods.
 *  Code taken from : https://github.com/iPaulPro/Android-ItemTouchHelper-Demo/tree/001dcdc99f75f77a431fe5c303be54ab45cd9a32/app/src/main/java/co/paulburke/android/itemtouchhelperdemo/helper
 *
 */
package com.CMPUT301F21T19.habitappt;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import static androidx.recyclerview.widget.ItemTouchHelper.Callback.makeMovementFlags;

import javax.annotation.Nullable;

public class DragHabits extends ItemTouchHelper.Callback{

    /**
     * Interface for methods used in DragHabits.
     */
    public interface ItemTouchHelperAdapter {
        void OnItemMoved(int fromPosition, int toPosition);

        void onItemSelected(DragMoveAdapter.DragViewHolder myViewHolder);

        void onItemCLear(DragMoveAdapter.DragViewHolder myViewHolder);
    }

    private ItemTouchHelperAdapter touchHelper;

    public DragHabits(ItemTouchHelperAdapter touchHelper){
        this.touchHelper = touchHelper;
    }

    /**
     * This method allows the user to long press on an item to drag it.
     * @return boolean
     */
    @Override
    public boolean isLongPressDragEnabled(){
        return true;
    }


    /**
     * This method allows swiping on the items.
     * @return boolean
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }


    /**
     * This sets the movement flags when an item is dragged.
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
     * This overrides the onItemMoved moved of ItemTouchHelperAdapter.
     * @param recyclerView
     * @param viewHolder
     * @param target
     * @return boolean
     */
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        touchHelper.OnItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }


    /**
     * This sets the onItemSelected method of itemTouchHelperAdapter.
     * @param viewHolder
     * @param actionState
     */
    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if(actionState != ItemTouchHelper.ACTION_STATE_IDLE){
            if(viewHolder instanceof DragMoveAdapter.DragViewHolder){
                DragMoveAdapter.DragViewHolder myViewHolder = (DragMoveAdapter.DragViewHolder) viewHolder;
                touchHelper.onItemSelected(myViewHolder);
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }


    /**
     * This sets the onItemCLear method of itemTouchHelperAdapter
     * @param recyclerView
     * @param viewHolder
     */
    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if(viewHolder instanceof DragMoveAdapter.DragViewHolder){
            DragMoveAdapter.DragViewHolder myViewHolder = (DragMoveAdapter.DragViewHolder) viewHolder;
            touchHelper.onItemCLear(myViewHolder);
        }

    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) { }

}
