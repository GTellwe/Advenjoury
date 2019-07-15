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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.silvergruppen.photoblog.activities.MainActivity;
import com.silvergruppen.photoblog.items.Achievement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class HowToRepository {


    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public LiveData<HashMap<String, ArrayList<Achievement>>> getTaskTree(final String userId, final Achievement achievement) {

        final MutableLiveData<HashMap<String, ArrayList<Achievement>>> data = new MutableLiveData<>();
        firebaseFirestore.collection("Users/"+userId+"/Achievements/"+achievement.getName()+"/subtasks").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    final HashMap<String, ArrayList<Achievement>> taskMap = new HashMap<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        ArrayList<Achievement> tmpList = new ArrayList<>();
                        Map<String,Object> tmpMap = document.getData();
                        for(int i =1; i<= Integer.parseInt(tmpMap.get("numberOfChildren").toString()); i++){

                            if(tmpMap.get("child"+Integer.toString(i))!= null) {
                                tmpList.add(new Achievement(tmpMap.get("child" + Integer.toString(i)).toString()));

                            }


                        }
                        taskMap.put(document.getId(),tmpList);
                    }
                    data.setValue(taskMap);
                }
            }
        });
        return data;
    }

    public LiveData<HashMap<String, ArrayList<Achievement>>> addAchievementToTaskTree(final String userId, final Achievement mainAchievement, final Achievement parentAchievement, final Achievement childAchievement, final HashMap<String, ArrayList<Achievement>> currentTaskTree){


        // get the number of childern in parent
        final MutableLiveData<HashMap<String, ArrayList<Achievement>>> data = new MutableLiveData<>();
        firebaseFirestore.collection("Users/"+userId+"/Achievements/"+mainAchievement.getName()+"/subtasks").document(parentAchievement.getName()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){

                        Map<String,Object> tmpMap = task.getResult().getData();
                        String numnberOfChildren = tmpMap.get("numberOfChildren").toString();
                        numnberOfChildren = Integer.toString(Integer.parseInt(numnberOfChildren)+1);
                        Map<String, Object> tmpData = new HashMap<>();
                        tmpData.put("child"+numnberOfChildren, childAchievement.getName());
                        tmpData.put("numberOfChildren", numnberOfChildren);
                        firebaseFirestore.collection("Users/"+userId+"/Achievements/"+mainAchievement.getName()+"/subtasks/").document(parentAchievement.getName()).set(tmpData, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                currentTaskTree.get(parentAchievement.getName()).add(new Achievement(childAchievement.getName()));
                                data.setValue(currentTaskTree);
                            }
                        });

                        Map<String, Object> tmpData2 = new HashMap<>();
                        tmpData2.put("numberOfChildren", "0");
                        firebaseFirestore.collection("Users/"+userId+"/Achievements/"+mainAchievement.getName()+"/subtasks/").document(childAchievement.getName()).set(tmpData2);
                    }

                }

        });
        return data;

    }
}
