package com.silvergruppen.photoblog.other;

import com.silvergruppen.photoblog.items.Achievement;

import java.util.ArrayList;

public class MonthlyProgress {


    private ArrayList<Achievement> monthlyAchievementsList;

    public MonthlyProgress(ArrayList<Boolean> montliesDone) {

        if(monthlyAchievementsList != null){

            for(int i = 0; i< monthlyAchievementsList.size(); i++){
                monthlyAchievementsList.get(i).setDone(montliesDone.get(i));
            }


        }else{

            monthlyAchievementsList = new ArrayList<>();
            monthlyAchievementsList.add(new Achievement("Save","Monthly","10",montliesDone.get(0),"Monthly",
                    "Save 1000 SEK"));

        }


    }


    public ArrayList<Achievement> getMonthlyAchievementsList() {
        return monthlyAchievementsList;
    }


    public String getProgress(){

        int progress =0;

        for(int i = 0; i < monthlyAchievementsList.size(); i++)
            if(monthlyAchievementsList.get(i).isDone())
                progress++;

        return Integer.toString(progress);
    }
}
