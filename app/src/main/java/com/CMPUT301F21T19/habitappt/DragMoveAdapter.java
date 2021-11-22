package com.CMPUT301F21T19.habitappt;

import android.graphics.Color;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DragMoveAdapter extends RecyclerView.Adapter<DragMoveAdapter.DragViewHolder> implements DragHabits.ItemTouchHelperAdapter {

    private ArrayList<Habit> habitList;
    private DragListener dragListener;
    private VisualIndicator visualIndicator;

    public interface DragListener {
        void onHabitClick(int position);}


    public DragMoveAdapter(ArrayList<Habit> habitList, DragListener dragListener) {
        this.habitList = habitList;
        this.dragListener = dragListener;
    }

    public class DragViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView habitTitle;
        private TextView habitReason;
        private ImageView visual_indicator;
        DragListener dragListener;
        public DragViewHolder(View view, DragListener dragListener){
            super(view);
            habitTitle = view.findViewById(R.id.habit_name);
            habitReason = view.findViewById(R.id.habit_reason_list_text);
            visual_indicator = view.findViewById(R.id.score_image);
            this.dragListener = dragListener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            dragListener.onHabitClick(getAdapterPosition());
        }
    }
    @NonNull
    @Override
    public DragViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View habitView = LayoutInflater.from(parent.getContext()).inflate(R.layout.habit_list_item, parent, false);
        return new DragViewHolder(habitView, dragListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DragViewHolder holder, int position) {
        String title = habitList.get(position).getTitle();
        String reason = habitList.get(position).getReason();
        //double score = habitList.get(position).getScore();
        holder.habitTitle.setText(title);
        holder.habitReason.setText(reason);
        holder.visual_indicator.setImageResource(R.drawable.ic_bright_green_emoji);

//        if (score < 20) {
//            holder.visual_indicator.setImageResource(R.drawable.ic_disappointed_emoji);
//        } else if (score < 40) {
//            holder.visual_indicator.setImageResource(R.drawable.ic_orange_emoji);
//        } else if (score < 60) {
//            holder.visual_indicator.setImageResource(R.drawable.ic_yellow_emoji);
//        } else if (score < 80) {
//            holder.visual_indicator.setImageResource(R.drawable.ic_light_green_emoji);
//        } else {
//            holder.visual_indicator.setImageResource(R.drawable.ic_bright_green_emoji);
//        }



    }

    @Override
    public int getItemCount() {
        return habitList.size();
    }

    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        if(fromPosition < toPosition){
            for(int i = fromPosition; i < toPosition; i++){
                Collections.swap(habitList, i, i+1);

            }
        }else{
            for(int i = fromPosition; i > toPosition; i--){
                Collections.swap(habitList, i, i-1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        updateDocIndex();
    }

    @Override
    public void onRowSelected(DragViewHolder myViewHolder) {
        myViewHolder.itemView.setBackgroundColor(Color.LTGRAY);
    }

    @Override
    public void onRowClear(DragViewHolder myViewHolder) {
        myViewHolder.itemView.setBackgroundColor(Color.WHITE);
    }

    public void updateDocIndex() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        CollectionReference collectionReference = FirebaseFirestore.getInstance()
                .collection("Habits")
                .document(userId)
                .collection("Habits");
        int length = habitList.size();

        for (int i = 0; i < length; i++) {
            String habitTitle = habitList.get(i).getTitle();
            int finalI = i;
            collectionReference
                    .whereEqualTo("title", habitTitle)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for (QueryDocumentSnapshot document : task.getResult()){
                                    collectionReference.document(document.getId())
                                            .update("Index", finalI);
                                }
                            }
                        }
                    });
        }
    }

}

