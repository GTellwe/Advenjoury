package com.silvergruppen.photoblog.other;

import com.silvergruppen.photoblog.items.Achievement;

import java.util.ArrayList;
import java.util.HashMap;

public class WeekleyProgress {


    private ArrayList<Achievement> weekleyAchievementsList;

    public WeekleyProgress(ArrayList<Boolean> weekliesDone) {

        if(weekleyAchievementsList != null){

            for(int i = 0; i<weekleyAchievementsList.size(); i++){
                weekleyAchievementsList.get(i).setDone(weekliesDone.get(i));
            }


        }else{

            weekleyAchievementsList = new ArrayList<>();
            weekleyAchievementsList.add(new Achievement("Clean","Weekley","10",weekliesDone.get(0),"Weekley",
                    "Clean your home"));
            weekleyAchievementsList.add(new Achievement("Socialize","Daily","10",weekliesDone.get(1),"Weekley",
                    "Spend some time with a friend"));
            weekleyAchievementsList.add(new Achievement("Be creative","Weekley","10",weekliesDone.get(2),"Weekley",
                    "Solve a problem. Write a short storie. Learn a new song. Let your creative side be in charge for a moment."));
            weekleyAchievementsList.add(new Achievement("Get emotional","Weekley","10",weekliesDone.get(3),"Weekley",
                    "Take a look at a cute animal"));
            weekleyAchievementsList.add(new Achievement("Strength","Weekley","10",weekliesDone.get(4),"Weekley",
                    "Do at least two sessions of strength excersises"));
            weekleyAchievementsList.add(new Achievement("Food","Weekley","10",weekliesDone.get(5),"Weekley",
                    " Eat vegetarian food for one day"));
            weekleyAchievementsList.add(new Achievement("Reading","Weekley","10",weekliesDone.get(6),"Weekley",
                    "Read for one hour"));
            weekleyAchievementsList.add(new Achievement("Think","Weekley","10",weekliesDone.get(7),"Weekley",
                    "Have 4 hour of completely unplanned time and see where your mind takes you"));

        }


    }


    public ArrayList<Achievement> getWeekleyAchievementsList() {
        return weekleyAchievementsList;
    }


    public String getProgress(){

        int progress =0;

        for(int i =0; i < weekleyAchievementsList.size();i++)
            if(weekleyAchievementsList.get(i).isDone())
                progress++;

        return Integer.toString(progress);
    }
}
