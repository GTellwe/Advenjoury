package com.silvergruppen.photoblog.listItems;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.silvergruppen.photoblog.ListViewHolders.AchievementListViewHolder;
import com.silvergruppen.photoblog.R;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class Achievement {
    private String name;
    private String points;
    private AchievementListViewHolder holder;
    private boolean open;
    private String topic;
    private int expandedHeight = 600, collapsedHeight = 250, currentHeight = 250;

    private ArrayList<AchievementPost> postList;

    private HashMap<Object,Date> postHashMap;

    private boolean done;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;


    public Achievement(){

    }

    public Achievement(String name,String topic, String points, boolean done) {
        this.name = name;
        this.points = points;
        this.collapsedHeight = collapsedHeight;
        this.expandedHeight = expandedHeight;
        this.currentHeight = currentHeight;
        this.topic = topic;
        postList = new ArrayList<>();
        postHashMap = new HashMap<>();
        this.done = done;

    }

    public Achievement(String name,String topic, String points, int collapsedHeight, int expandedHeight, int currentHeight, boolean done) {
        this.name = name;
        this.points = points;
        this.collapsedHeight = collapsedHeight;
        this.expandedHeight = expandedHeight;
        this.currentHeight = currentHeight;
        this.topic = topic;
        postList = new ArrayList<>();
        postHashMap = new HashMap<>();
        this.done = done;

    }
    public void addJournalItem(String text, String imagePath, Date timestamp){
        postList.add(new AchievementPost(text,imagePath,timestamp));
        expandedHeight = expandedHeight + 1200;
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

    public void setExpandedHeight(int expandedHeight) {
        this.expandedHeight = expandedHeight;
    }

    public int getCollapsedHeight() {
        return collapsedHeight;
    }

    public void setCollapsedHeight(int collapsedHeight) {
        this.collapsedHeight = collapsedHeight;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open, boolean editable) {
        this.open = open;


            if (open && editable) {
                holder.setMarkAsDoneBtnVisability(View.VISIBLE);
                holder.setPostBtnVisability(View.VISIBLE);
            } else {
                holder.setMarkAsDoneBtnVisability(View.INVISIBLE);
                holder.setPostBtnVisability(View.INVISIBLE);
            }

    }

    public void setHolder(AchievementListViewHolder holder) {
        this.holder = holder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoints() {

            return points;
    }

    public void setPoints(String members) {
        this.points = members;
    }

    public void setPostHashMap(Date timestamp, ImageView tmpImageView) {

        postHashMap.put(tmpImageView,timestamp);

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
