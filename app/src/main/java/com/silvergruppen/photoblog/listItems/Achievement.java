package com.silvergruppen.photoblog.listItems;

import android.view.View;
import android.widget.ImageView;

import com.silvergruppen.photoblog.ListViewHolders.AchievementListViewHolder;

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
    private int expandedHeight, collapsedHeight, currentHeight;

    private ArrayList<AchievementPost> postList;

    private HashMap<Object,Date> postHashMap;


    public Achievement(){

    }

    public Achievement(String name,String topic, String points, int collapsedHeight, int expandedHeight, int currentHeight) {
        this.name = name;
        this.points = points;
        this.collapsedHeight = collapsedHeight;
        this.expandedHeight = expandedHeight;
        this.currentHeight = currentHeight;
        this.topic = topic;
        postList = new ArrayList<>();
        postHashMap = new HashMap<>();

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

    public void setOpen(boolean open) {
        this.open = open;
        if(open)
            holder.setPostBtnVisability(View.VISIBLE);
        else
            holder.setPostBtnVisability(View.INVISIBLE);
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
}
