package com.CMPUT301F21T19.habitappt;

import android.media.Image;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.GregorianCalendar;

public class Habit {
    private String title;
    private String reason;
    private long dateToStart;
    private ArrayList<Boolean> datesToDo;
    String id;

    private ArrayList<HabitEvent> habitEvents;
    private long score;

    public Habit(String title,String reason,long dateToStart,ArrayList<Boolean> datesToDo,String id){
        this.title = title;
        this.reason = reason;
        this.dateToStart = dateToStart;
        this.datesToDo = datesToDo;
        this.id = id;

    }

    public Habit(){
        this.title = "New Habit";
        this.reason = "Habit Reason";
        this.dateToStart = GregorianCalendar.getInstance().getTimeInMillis();
        this.datesToDo = new ArrayList<>();
        for(int i=0;i<7;i++){
            datesToDo.add(false);
        }
        id = "-1";
    }

    public String getId(){
        return id;
    }

    public ArrayList<Boolean> getWeekly(){
        return datesToDo;
    }

    public Boolean getDateSelected(int i){
        if(i > datesToDo.size()){
            return false;
        }

        return datesToDo.get(i);
    }

    public Boolean setDateSelected(int i,Boolean b){
        if(i >= datesToDo.size() && i < 0){
            return false;
        }

        datesToDo.set(i,b);

        return true;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public long getDateToStart() {
        return dateToStart;
    }

    public void setDateToStart(long dateToStart) {
        this.dateToStart = dateToStart;
    }

    public void calculateScore() {
        long dateBeginning = dateToStart;









            for (int i = 0; i < 7; i++) {
                datesToDo.add(false);

            }




        habitEvents.size();
        datesToDo.size();
        //look through each a habits habit events,
        // find how many days a habit event should have been done,
        // how many has been done
        // score returns percent 0-100
    }

    public long getScore() { return 59; }

    public void setScore(int newScore) { this.score = newScore; }

    public ImageView getEmoji(){

        ImageView emoji = null;

        if (getScore() >=0 && getScore() <=20){
            emoji.setImageResource(R.drawable.ic_dissapointed_emoji);}
        else if (getScore() >20 && getScore() <=40){
            emoji.setImageResource(R.drawable.ic_yellow_emoji);}
        else if (getScore() >40 && getScore() <=60){
            emoji.setImageResource(R.drawable.ic_orange_emoji);}
        else if (getScore() >60 && getScore() <=80){
            emoji.setImageResource(R.drawable.ic_light_green_emoji);}
        else if (getScore() >80 && getScore() <=100){
            emoji.setImageResource(R.drawable.ic_green_emoji);}

        return emoji;


        }
    }




