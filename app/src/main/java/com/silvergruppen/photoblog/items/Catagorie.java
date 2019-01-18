package com.silvergruppen.photoblog.items;

import com.silvergruppen.photoblog.holders.CatagoriesListViewHolder;

public class Catagorie extends RecycleListItem {

    CatagoriesListViewHolder holder;
    int numberOfAchievments, achievementsDoneByCurrentUser;

    public Catagorie(String name, int numberOfAchievments){
        super(name);
        this.numberOfAchievments = numberOfAchievments;


    }

    public CatagoriesListViewHolder getHolder() {
        return holder;
    }

    public void setHolder(CatagoriesListViewHolder holder) {
        this.holder = holder;
    }

    public int getNumberOfAchievments() {
        return numberOfAchievments;
    }

    public void setNumberOfAchievments(int numberOfAchievments) {
        this.numberOfAchievments = numberOfAchievments;
    }

    public int getAchievementsDoneByCurrentUser() {
        return achievementsDoneByCurrentUser;
    }

    public void setAchievementsDoneByCurrentUser(int achievementsDoneByCurrentUser) {
        this.achievementsDoneByCurrentUser = achievementsDoneByCurrentUser;
    }
}
