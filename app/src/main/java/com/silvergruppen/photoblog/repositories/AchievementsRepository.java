package com.silvergruppen.photoblog.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.silvergruppen.photoblog.items.Achievement;
import com.silvergruppen.photoblog.items.RecycleListItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AchievementsRepository {

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public LiveData<ArrayList<RecycleListItem>> getAchievements(String userId) {
        // This isn't an optimal implementation. We'll fix it later.
        final MutableLiveData<ArrayList<RecycleListItem>> data = new MutableLiveData<>();


        firebaseFirestore.collection("Users/"+userId+"/Achievements")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<RecycleListItem> tmpList = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {

                                Map<String, Object> tmpMap = document.getData();


                                final String name = tmpMap.get("name").toString();
                                final String catagorie = tmpMap.get("topic").toString();
                                // check if user has done achievement

                                Achievement tmpAchievement = new Achievement(name, tmpMap.get("topic").toString(), tmpMap.get("points").toString(), false, catagorie);
                                tmpList.add(tmpAchievement);


                            }
                            data.setValue(tmpList);
                        }
                    }
                });
        return data;
    }

    public LiveData<Boolean> addAchievementToFirebase(String userId, String achievementName, String topic) {

        final MutableLiveData<Boolean> response = new MutableLiveData<>();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        Map<String, Object> achievementData = new HashMap<>();
        achievementData.put("name", achievementName);
        achievementData.put("points", "10");
        achievementData.put("topic", topic);

        firebaseFirestore.collection("Users/" + userId + "/Achievements").document(achievementName).set(achievementData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                    response.setValue(true);
                else
                    response.setValue(false);
            }
        });

        return response;
    }
}

