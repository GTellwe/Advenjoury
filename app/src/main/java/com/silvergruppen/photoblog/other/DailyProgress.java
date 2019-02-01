package com.silvergruppen.photoblog.other;

import com.silvergruppen.photoblog.items.Achievement;

import java.util.ArrayList;
import java.util.HashMap;

public class DailyProgress {

    private HashMap<Integer, Integer> hashMap;
    private ArrayList<Achievement> dailyAchievementsList;

    public DailyProgress(ArrayList<Boolean> dailiesDone) {

        if(dailyAchievementsList != null){

            for(int i = 0; i<dailyAchievementsList.size(); i++){
                dailyAchievementsList.get(i).setDone(dailiesDone.get(i));
            }


        }else{

            dailyAchievementsList = new ArrayList<>();
            dailyAchievementsList.add(new Achievement("Meditate","Daily","10",dailiesDone.get(0),"daily",
                    "Go to a quite and comfortable place and breathe in a rytmic pattern for 10 minutes"));
            dailyAchievementsList.add(new Achievement("Selfie","Daily","10",dailiesDone.get(1),"daily",
                    "Take a selfie and save it in an album"));
            dailyAchievementsList.add(new Achievement("Writing","Daily","10",dailiesDone.get(2),"daily",
                    "Write a small text and reflect on your day"));
            dailyAchievementsList.add(new Achievement("Get emotional","Daily","10",dailiesDone.get(3),"daily",
                    "Take a look at a cute animal"));
            dailyAchievementsList.add(new Achievement("Gratitude","Daily","10",dailiesDone.get(4),"daily",
                    "Look your self in the mirror and be thankful for all the opportunities and people you have around you"));
            dailyAchievementsList.add(new Achievement("Fresh air","Daily","10",dailiesDone.get(5),"daily",
                    " Go outside for at least 10 minutes to get som fresh air"));
            dailyAchievementsList.add(new Achievement("No sugar","Daily","10",dailiesDone.get(6),"daily",
                    "Dont eat any refined sugars"));
            dailyAchievementsList.add(new Achievement("Exercise","Daily","10",dailiesDone.get(7),"daily",
                    "Do at least 15 minutes of cardio"));

        }




    }


    public ArrayList<Achievement> getDailyAchievementsList() {
        return dailyAchievementsList;
    }


    public String getProgress(){

        int progress =0;

        for(int i =0; i < dailyAchievementsList.size();i++)
            if(dailyAchievementsList.get(i).isDone())
                progress++;

        return Integer.toString(progress);
    }
}
