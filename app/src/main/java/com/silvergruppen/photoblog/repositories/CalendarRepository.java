package com.silvergruppen.photoblog.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.silvergruppen.photoblog.other.User;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

public class CalendarRepository {

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public LiveData<int[][]> getDoneAchievements(String userId) {
        // This isn't an optimal implementation. We'll fix it later.
        final MutableLiveData<int[][]> data = new MutableLiveData<>();



        firebaseFirestore.collection("Users/"+userId+"/DailyAchievements").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    int[][] tmpMatrix = new int[365][8];
                    for (DocumentSnapshot document : task.getResult()) {

                        Map<String,Object> tmpMap = document.getData();

                        String ach1 = tmpMap.get("Meditate").toString();
                        String ach2 = tmpMap.get("Selfie").toString();
                        String ach3 = tmpMap.get("Writing").toString();
                        String ach4 = tmpMap.get("Get emotional").toString();
                        String ach5 = tmpMap.get("Gratitude").toString();
                        String ach6 = tmpMap.get("Fresh air").toString();
                        String ach7 = tmpMap.get("No sugar").toString();
                        String ach8 = tmpMap.get("Exercise").toString();


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

}
