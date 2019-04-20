package com.silvergruppen.photoblog.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.silvergruppen.photoblog.items.Achievement;
import com.silvergruppen.photoblog.other.User;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class CalendarRepository {

    private final String DAILY_KEY = "DailyAchievements";
    private final String WEEKLY_KEY = "WeekleyAchievements";
    private final String MONTHLY_KEY = "MonthlyAchievements";


    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public LiveData<HashMap<String, ArrayList<Achievement>>> getCalendarAchievements(String userId, final String achievementType) {

        final MutableLiveData<HashMap<String, ArrayList<Achievement>>> data = new MutableLiveData<>();
        firebaseFirestore.collection("Users/"+userId+"/"+achievementType).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    HashMap<String, ArrayList<Achievement>> achievementList = new HashMap<>();
                    for (DocumentSnapshot document : task.getResult()) {

                        ArrayList<Achievement> tmpList = new ArrayList<>();
                        Map<String,Object> tmpMap = document.getData();
                        for(HashMap.Entry<String, Object> entry : tmpMap.entrySet()){

                            boolean done = false;
                            if(entry.getValue() == "1")
                                done = true;
                            tmpList.add(new Achievement(entry.getKey(),null, null,done, null));
                        }
                        achievementList.put(document.getId(),tmpList);

                    }
                    // if the current day dosent exist: take the latest day there was a list or leave it to be null
                    int achievementKey;
                    switch (achievementType){

                        case DAILY_KEY: achievementKey = Calendar.DAY_OF_YEAR;
                            break;
                        case WEEKLY_KEY: achievementKey = Calendar.WEEK_OF_YEAR;
                            break;
                        default: achievementKey =0;
                            break;
                    }
                    if (achievementList.get(Integer.toString(Calendar.getInstance().get(achievementKey))) == null) {
                        // check if some previous day has achievement
                        int currentDay = Calendar.getInstance().get(achievementKey);
                        while (currentDay >0){
                            if(achievementList.get(Integer.toString(currentDay)) != null){
                               achievementList.put(Integer.toString(Calendar.getInstance().get(achievementKey)),achievementList.get(Integer.toString(currentDay)));
                                break;
                            }else
                                currentDay --;
                        }

                    }
                    data.setValue(achievementList);
                }


            }
        });
        return data;
    }

    public LiveData<int[][]> getWeekleyDoneAchievements(String userId) {
        // This isn't an optimal implementation. We'll fix it later.
        final MutableLiveData<int[][]> data = new MutableLiveData<>();



        firebaseFirestore.collection("Users/"+userId+"/WeekleyAchievements").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    int[][] tmpMatrix = new int[52][8];
                    for (DocumentSnapshot document : task.getResult()) {

                        Map<String,Object> tmpMap = document.getData();

                        String ach1 = tmpMap.get("Clean").toString();
                        String ach2 = tmpMap.get("Socialize").toString();
                        String ach3 = tmpMap.get("Be creative").toString();
                        String ach4 = tmpMap.get("Get emotional").toString();
                        String ach5 = tmpMap.get("Strength").toString();
                        String ach6 = tmpMap.get("Food").toString();
                        String ach7 = tmpMap.get("Reading").toString();
                        String ach8 = tmpMap.get("Think").toString();


                        tmpMatrix[Integer.parseInt(document.getId())-1][0] = Integer.parseInt(ach1);
                        tmpMatrix[Integer.parseInt(document.getId())-1][1] = Integer.parseInt(ach2);
                        tmpMatrix[Integer.parseInt(document.getId())-1][2] = Integer.parseInt(ach3);
                        tmpMatrix[Integer.parseInt(document.getId())-1][3] = Integer.parseInt(ach4);
                        tmpMatrix[Integer.parseInt(document.getId())-1][4] = Integer.parseInt(ach5);
                        tmpMatrix[Integer.parseInt(document.getId())-1][5] = Integer.parseInt(ach6);
                        tmpMatrix[Integer.parseInt(document.getId())-1][6] = Integer.parseInt(ach7);
                        tmpMatrix[Integer.parseInt(document.getId())-1][7] = Integer.parseInt(ach8);




                    }
                    data.setValue(tmpMatrix);
                }


            }
        });
        return data;
    }

    public LiveData<int[][]> getMonthlyDoneAchievements(String userId) {
        // This isn't an optimal implementation. We'll fix it later.
        final MutableLiveData<int[][]> data = new MutableLiveData<>();



        firebaseFirestore.collection("Users/"+userId+"/MonthlyAchievements").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    int[][] tmpMatrix = new int[12][1];
                    for (DocumentSnapshot document : task.getResult()) {

                        Map<String,Object> tmpMap = document.getData();

                        String ach1 = tmpMap.get("Save").toString();
                        tmpMatrix[Integer.parseInt(document.getId())-1][0] = Integer.parseInt(ach1);

                    }
                    data.setValue(tmpMatrix);
                }


            }
        });
        return data;
    }

    public void uppdateCurrentAchievementList(String userId,ArrayList<Achievement> achievementsList,String achievementType){

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();

        for(Achievement achievement : achievementsList)
            data.put(achievement.getName(), "0");
        int achievementKey;
        switch (achievementType){

            case DAILY_KEY: achievementKey = Calendar.DAY_OF_YEAR;
                break;
            case WEEKLY_KEY: achievementKey = Calendar.WEEK_OF_YEAR;
                break;
            default: achievementKey =0;
            break;
        }
        firebaseFirestore.collection("Users/" + userId + "/" + achievementType).document(Integer.toString(Calendar.getInstance().get(achievementKey))).set(data);

    }

    public void removeAchievement(String userId, String achievementName, String achievementType,ArrayList<Achievement> currentAchievementList){

        // copy all items except the one to be removed to the data map
        Log.d("\n \n \n jsjs"," "+ currentAchievementList.size());
        Map<String,Object> updates = new HashMap<>();
        for(Achievement achievement : currentAchievementList){
            if(!achievement.getName().equals(achievementName)){
                String done = "0";
                if(achievement.isDone())
                    done= "1";

                updates.put(achievement.getName(), done);
            }

        }
        // uppdate the document
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        int achievementKey;
        switch (achievementType){

            case DAILY_KEY: achievementKey = Calendar.DAY_OF_YEAR;
                break;
            case WEEKLY_KEY: achievementKey = Calendar.WEEK_OF_YEAR;
                break;
            default: achievementKey =0;
                break;
        }
        firebaseFirestore.collection("Users/"+userId+"/"+achievementType).document(Integer.toString(Calendar.getInstance().get(achievementKey))).set(updates);

    }

}
