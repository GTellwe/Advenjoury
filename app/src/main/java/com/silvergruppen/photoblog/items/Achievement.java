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

    // views
    View view;

    // strings
    private String points;
    private String topic;
    private String catagorie;
    private String desc;

    // booleans
    private boolean open;
    private boolean[] dailyProgressDone = new boolean[365];
    private boolean done;

    // constants
    private final int expandedHeight = 800, collapsedHeight = 300;
    private int currentHeight = 300;

    // lists
    private ArrayList<PostItem> postList;
    private HashMap<Object,Date> postHashMap;
    private ArrayList<Achievement> childAchievements;

    // other
    private AchievementListViewHolder holder;
    private MyRecyclerViewHolder recyclerHolder;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private Achievement parentAchievement;
    private int treeLevel;


    public Achievement(String name,String topic, String points, boolean done, String catagorie) {
        super(name);
        this.points = points;
        this.currentHeight = currentHeight;
        this.topic = topic;
        postList = new ArrayList<>();
        postHashMap = new HashMap<>();
        this.done = done;
        this.catagorie = catagorie;
        open = false;

    }
    public Achievement(String name,String topic, String points, boolean done, String catagorie,String desc) {
        super(name);
        this.points = points;
        this.currentHeight = currentHeight;
        this.topic = topic;
        postList = new ArrayList<>();
        postHashMap = new HashMap<>();
        this.done = done;
        this.catagorie = catagorie;
        this.desc = desc;
        open = false;

    }
    public Achievement(String name){
        super(name);

    }
    public Achievement(String name, ArrayList<Achievement> children, View view){
        super(name);
        childAchievements = children;
        this.view = view;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

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
        postList.add(new PostItem(text,imagePath,timestamp));
        //expandedHeight = expandedHeight + 1200;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public ArrayList<PostItem> getBlogPostList() {
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

    public ArrayList<Achievement> getChildAchievements() {
        return childAchievements;
    }

    public void setChildAchievements(ArrayList<Achievement> childAchievements) {
        this.childAchievements = childAchievements;
    }

    public Achievement getParentAchievement() {
        return parentAchievement;
    }

    public void setParentAchievement(Achievement parentAchievement) {
        this.parentAchievement = parentAchievement;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public int getTreeLevel() {
        return treeLevel;
    }

    public void setTreeLevel(int treeLevel) {
        this.treeLevel = treeLevel;
    }
}
