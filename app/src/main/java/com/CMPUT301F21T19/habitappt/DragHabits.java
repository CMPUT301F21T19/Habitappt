package com.CMPUT301F21T19.habitappt;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import static androidx.recyclerview.widget.ItemTouchHelper.Callback.makeMovementFlags;

import javax.annotation.Nullable;

public class DragHabits extends ItemTouchHelper.Callback{

    private ItemTouchHelperAdapter touchHelper;

    public DragHabits(ItemTouchHelperAdapter touchHelper){
        this.touchHelper = touchHelper;
    }

    @Override
    public boolean isLongPressDragEnabled(){
        return true;
    }


    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }


    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(dragFlag,0);
    }


    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        touchHelper.OnItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }


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


    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if(viewHolder instanceof DragMoveAdapter.DragViewHolder){
            DragMoveAdapter.DragViewHolder myViewHolder = (DragMoveAdapter.DragViewHolder) viewHolder;
            touchHelper.onItemCLear(myViewHolder);
        }

    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

    public interface ItemTouchHelperAdapter {
        void OnItemMoved(int fromPosition, int toPosition);

        void onItemSelected(DragMoveAdapter.DragViewHolder myViewHolder);

        void onItemCLear(DragMoveAdapter.DragViewHolder myViewHolder);

    }
}
