package com.CMPUT301F21T19.habitappt;

import static android.provider.SyncStateContract.Helpers.update;

import static io.grpc.okhttp.internal.Platform.get;

import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DragMoveAdapter extends RecyclerView.Adapter<DragMoveAdapter.DragViewHolder> implements DragHabits.ItemTouchHelperAdapter {

    private ArrayList<Habit> habitList;
    private DragListener dragListener;

    public interface DragListener {
        void onHabitClick(int position);}


    public DragMoveAdapter(ArrayList<Habit> habitList, DragListener dragListener) {
        this.habitList = habitList;
        this.dragListener = dragListener;
    }

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

    @Override
    public int getItemCount() {
        return habitList.size();
    }

    @Override
    public void OnItemMoved(int fromPosition, int toPosition) {
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
    public void onItemSelected(DragViewHolder myViewHolder) { }

    @Override
    public void onItemCLear(DragViewHolder myViewHolder) { }

    public void updateDocIndex() {
        String emailID = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        CollectionReference collectionReference = FirebaseFirestore.getInstance()
                .collection("Users")
                .document(emailID)
                .collection("Habits");
        
        int length = habitList.size();

            for (int i = 0; i < length; i++) {
                int finalI = i;
                String id = habitList.get(i).getId();
                collectionReference
                        .whereEqualTo("id",id)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        collectionReference.document(id).update("index", finalI); }

                                });

            }

        }

    }


