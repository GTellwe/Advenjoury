package com.silvergruppen.photoblog.items;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.silvergruppen.photoblog.holders.AchievementListViewHolder;
import com.silvergruppen.photoblog.holders.MyRecyclerViewHolder;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Achievement extends RecycleListItem{
    private String points;
    private AchievementListViewHolder holder;
    private boolean open = true;
    private String topic;
    private final int expandedHeight = 800, collapsedHeight = 300;
    private int currentHeight = 300;

    private ArrayList<AchievementPost> postList;

    private HashMap<Object,Date> postHashMap;

    private MyRecyclerViewHolder recyclerHolder;

    private boolean[] dailyProgressDone = new boolean[365];

    private boolean done;

    private String catagorie;




    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;




    public Achievement(String name,String topic, String points, boolean done, String catagorie) {
        super(name);
        this.points = points;
        this.currentHeight = currentHeight;
        this.topic = topic;
        postList = new ArrayList<>();
        postHashMap = new HashMap<>();
        this.done = done;
        this.catagorie = catagorie;

    }
/*
    public Achievement(String name,String topic, String points, int collapsedHeight, int expandedHeight, int currentHeight, boolean done) {
        super(name);

        this.points = points;
        this.collapsedHeight = collapsedHeight;
        this.expandedHeight = expandedHeight;
        this.currentHeight = currentHeight;
        this.topic = topic;
        postList = new ArrayList<>();
        postHashMap = new HashMap<>();
        this.done = done;

    }
*/

    public String getCatagorie() {
        return catagorie;
    }

    public void setCatagorie(String catagorie) {
        this.catagorie = catagorie;
    }

    public boolean[] getDailyProgressDone() {
        return dailyProgressDone;
    }

    public void setDailyProgressDone(boolean[] dailyProgressDone) {
        this.dailyProgressDone = dailyProgressDone;
    }

    public MyRecyclerViewHolder getRecyclerHolder() {
        return recyclerHolder;
    }

    public void setRecyclerHolder(MyRecyclerViewHolder recyclerHolder) {
        this.recyclerHolder = recyclerHolder;
    }


    public void addJournalItem(String text, String imagePath, Date timestamp){
        postList.add(new AchievementPost(text,imagePath,timestamp));
        //expandedHeight = expandedHeight + 1200;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public ArrayList<AchievementPost> getBlogPostList() {
        return postList;
    }


    public AchievementListViewHolder getHolder() {
        return holder;
    }


    public int getCurrentHeight() {
        return currentHeight;
    }

    public void setCurrentHeight(int currentHeight) {
        this.currentHeight = currentHeight;
    }

    public int getExpandedHeight() {
        return expandedHeight;
    }


    public int getCollapsedHeight() {
        return collapsedHeight;
    }


    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open, boolean editable) {
        this.open = open;

/*
            if (open && editable) {
                holder.setMarkAsDoneBtnVisability(View.VISIBLE);
                holder.setPostBtnVisability(View.VISIBLE);
            } else {
                holder.setMarkAsDoneBtnVisability(View.INVISIBLE);
                holder.setPostBtnVisability(View.INVISIBLE);
            }
*/
    }
    public void setSwitchVisibility(int visibility){

        recyclerHolder.getaSwitch().setVisibility(visibility);
    }

    public void setUppArrowVisibility(int visibility){

        recyclerHolder.getUpArrowImageView().setVisibility(visibility);
    }

    public void setDownArrowVisibility(int visibility){

        recyclerHolder.getDownArrowImageView().setVisibility(visibility);

    }

    public void setDescriptionTextViewVisibility(int visibility){

        recyclerHolder.getDescriptionTextView().setVisibility(visibility);
    }
    public void setHolder(AchievementListViewHolder holder) {
        this.holder = holder;
    }



    public String getPoints() {

            return points;
    }

    public void setPoints(String members) {
        this.points = members;
    }

    public void setPostHashMap(Date timestamp, TextView tmpTextView) {

        postHashMap.put(tmpTextView,timestamp);

    }

    public HashMap<Object,Date> getBlogListViewHasMap() {

        return postHashMap;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;

    }

    public int getColor() {

        if(done)
            return Color.GREEN;
        else
            return  Color.WHITE;
    }
}
