package com.silvergruppen.photoblog.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.silvergruppen.photoblog.items.Achievement;
import com.silvergruppen.photoblog.other.DailyProgress;
import com.silvergruppen.photoblog.other.MonthlyProgress;
import com.silvergruppen.photoblog.other.User;
import com.silvergruppen.photoblog.other.WeekleyProgress;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProgressRepository {
    private static final String ACHIEVMENT1_KEY = "ach1";
    private static final String ACHIEVMENT2_KEY = "ach2";
    private static final String ACHIEVMENT3_KEY = "ach3";

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public MutableLiveData<DailyProgress> getDailyProgress(final String day,final String userId) {
        // This isn't an optimal implementation. We'll fix it later.
        final MutableLiveData<DailyProgress> data = new MutableLiveData<>();
        Log.d("day \n \n \n \n \n \n ", day+" "+userId);

        firebaseFirestore.collection("Users/"+userId+"/DailyAchievements").document(day).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    if(task.getResult().exists()){
                        Map<String, Object> tmpMap = task.getResult().getData();

                        String ach1 = tmpMap.get("Meditate").toString();
                        String ach2 = tmpMap.get("Selfie").toString();
                        String ach3 = tmpMap.get("Writing").toString();
                        String ach4 = tmpMap.get("Get emotional").toString();
                        String ach5 = tmpMap.get("Gratitude").toString();
                        String ach6 = tmpMap.get("Fresh air").toString();
                        String ach7 = tmpMap.get("No sugar").toString();
                        String ach8 = tmpMap.get("Exercise").toString();

                        ArrayList<Boolean> achDoneList = new ArrayList<>();
                        for(int i = 0; i<8; i++ )
                            achDoneList.add(false);


                        if(ach1.equals("1"))
                            achDoneList.set(0,true);
                        if(ach2.equals("1"))
                            achDoneList.set(1,true);
                        if(ach3.equals("1"))
                            achDoneList.set(2,true);
                        if(ach4.equals("1"))
                            achDoneList.set(3,true);
                        if(ach5.equals("1"))
                            achDoneList.set(4,true);
                        if(ach6.equals("1"))
                            achDoneList.set(5,true);
                        if(ach7.equals("1"))
                            achDoneList.set(6,true);
                        if(ach8.equals("1"))
                            achDoneList.set(7,true);


                        data.setValue(new DailyProgress(achDoneList));

                    }else{

                        Map<String, Object> tmpMap = new HashMap<>();

                        tmpMap.put("Meditate","0");
                        tmpMap.put("Selfie","0");
                        tmpMap.put("Writing","0");
                        tmpMap.put("Get emotional","0");
                        tmpMap.put("Gratitude","0");
                        tmpMap.put("Fresh air","0");
                        tmpMap.put("No sugar","0");
                        tmpMap.put("Exercise","0");

                        firebaseFirestore.collection("Users/"+userId+"/DailyAchievements").document(day).set(tmpMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                        ArrayList<Boolean> achDoneList = new ArrayList<>();
                        for(int i = 0; i<8; i++ )
                            achDoneList.add(false);

                        data.setValue(new DailyProgress(achDoneList));
                    }
                }else{



                }


            }
        });
        return data;
    }

    public MutableLiveData<WeekleyProgress> getWeekleyProgress(final String week, final String userId) {
        // This isn't an optimal implementation. We'll fix it later.
        final MutableLiveData<WeekleyProgress> data = new MutableLiveData<>();

        firebaseFirestore.collection("Users/"+userId+"/WeekleyAchievements").document(week).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    if(task.getResult().exists()){
                        Map<String, Object> tmpMap = task.getResult().getData();

                        String ach1 = tmpMap.get("Clean").toString();
                        String ach2 = tmpMap.get("Socialize").toString();
                        String ach3 = tmpMap.get("Be creative").toString();
                        String ach4 = tmpMap.get("Get emotional").toString();
                        String ach5 = tmpMap.get("Strength").toString();
                        String ach6 = tmpMap.get("Food").toString();
                        String ach7 = tmpMap.get("Reading").toString();
                        String ach8 = tmpMap.get("Think").toString();

                        ArrayList<Boolean> achDoneList = new ArrayList<>();
                        for(int i = 0; i<8; i++ )
                            achDoneList.add(false);


                        if(ach1.equals("1"))
                            achDoneList.set(0,true);
                        if(ach2.equals("1"))
                            achDoneList.set(1,true);
                        if(ach3.equals("1"))
                            achDoneList.set(2,true);
                        if(ach4.equals("1"))
                            achDoneList.set(3,true);
                        if(ach5.equals("1"))
                            achDoneList.set(4,true);
                        if(ach6.equals("1"))
                            achDoneList.set(5,true);
                        if(ach7.equals("1"))
                            achDoneList.set(6,true);
                        if(ach8.equals("1"))
                            achDoneList.set(7,true);


                        data.setValue(new WeekleyProgress(achDoneList));

                    }else{

                        Map<String, Object> tmpMap = new HashMap<>();

                        tmpMap.put("Clean","0");
                        tmpMap.put("Socialize","0");
                        tmpMap.put("Be creative","0");
                        tmpMap.put("Get emotional","0");
                        tmpMap.put("Strength","0");
                        tmpMap.put("Food","0");
                        tmpMap.put("Reading","0");
                        tmpMap.put("Think","0");

                        firebaseFirestore.collection("Users/"+userId+"/WeekleyAchievements").document(week).set(tmpMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                        ArrayList<Boolean> achDoneList = new ArrayList<>();
                        for(int i = 0; i<8; i++ )
                            achDoneList.add(false);

                        data.setValue(new WeekleyProgress(achDoneList));
                    }
                }else{



                }


            }
        });
        return data;
    }

    public MutableLiveData<MonthlyProgress> getMonthlyProgress(final String week, final String userId) {
        // This isn't an optimal implementation. We'll fix it later.
        final MutableLiveData<MonthlyProgress> data = new MutableLiveData<>();

        firebaseFirestore.collection("Users/"+userId+"/MonthlyAchievements").document(week).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    if(task.getResult().exists()){
                        Map<String, Object> tmpMap = task.getResult().getData();

                        String ach1 = tmpMap.get("Save").toString();


                        ArrayList<Boolean> achDoneList = new ArrayList<>();
                        for(int i = 0; i<1; i++ )
                            achDoneList.add(false);


                        if(ach1.equals("1"))
                            achDoneList.set(0,true);


                        data.setValue(new MonthlyProgress(achDoneList));

                    }else{

                        Map<String, Object> tmpMap = new HashMap<>();

                        tmpMap.put("Save","0");

                        firebaseFirestore.collection("Users/"+userId+"/MonthlyAchievements").document(week).set(tmpMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
                        ArrayList<Boolean> achDoneList = new ArrayList<>();
                        for(int i = 0; i<1; i++ )
                            achDoneList.add(false);

                        data.setValue(new MonthlyProgress(achDoneList));
                    }
                }else{



                }


            }
        });
        return data;
    }
    public void updateMonthlyProgress(String userId, final MutableLiveData<MonthlyProgress> monthlyProgress, int position, String done, String day){

        final HashMap<String, Object> tmpHashMap = new HashMap<>();
        tmpHashMap.put(monthlyProgress.getValue().getMonthlyAchievementsList().get(position).getName(), done);

        if(done.equals("1"))
            monthlyProgress.getValue().getMonthlyAchievementsList().get(position).setDone(true);
        else
            monthlyProgress.getValue().getMonthlyAchievementsList().get(position).setDone(false);

        firebaseFirestore.collection("Users/"+userId+"/MonthlyAchievements").document(day).update(tmpHashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        monthlyProgress.setValue(monthlyProgress.getValue());

                    }

                });


    }
    public void updateDailyProgress(String userId, final MutableLiveData<DailyProgress> dailyProgress, int position, String done, String day){

        final HashMap<String, Object> tmpHashMap = new HashMap<>();
        tmpHashMap.put(dailyProgress.getValue().getDailyAchievementsList().get(position).getName(), done);

        if(done.equals("1"))
            dailyProgress.getValue().getDailyAchievementsList().get(position).setDone(true);
        else
            dailyProgress.getValue().getDailyAchievementsList().get(position).setDone(false);

        firebaseFirestore.collection("Users/"+userId+"/DailyAchievements").document(day).update(tmpHashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dailyProgress.setValue(dailyProgress.getValue());

                    }

                });


    }


    public void updateWeekleyProgress(String userId, final MutableLiveData<WeekleyProgress> weekleyProgress, int position, String done, String week){

        final HashMap<String, Object> tmpHashMap = new HashMap<>();
        tmpHashMap.put(weekleyProgress.getValue().getWeekleyAchievementsList().get(position).getName(), done);

        if(done.equals("1"))
            weekleyProgress.getValue().getWeekleyAchievementsList().get(position).setDone(true);
        else
            weekleyProgress.getValue().getWeekleyAchievementsList().get(position).setDone(false);

        firebaseFirestore.collection("Users/"+userId+"/WeekleyAchievements").document(week).update(tmpHashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        weekleyProgress.setValue(weekleyProgress.getValue());

                    }

                });


    }
}
