package com.silvergruppen.photoblog.repositories;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.silvergruppen.photoblog.items.Achievement;

import java.util.HashMap;
import java.util.Map;

public class AchievementSettingsRepository {


    public void updateCalendarAchievement(String userId, Achievement achievement, String achievementType, String date, boolean add){

        if(userId == null || achievement == null || achievementType == null || date == null)
            return;

        if(add) {
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            Map<String, Object> data = new HashMap<>();
            data.put(achievement.getName(), "0");
            firebaseFirestore.collection("Users/" + userId + "/" + achievementType).document(date).set(data, SetOptions.merge());
        }else{

            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            Map<String,Object> updates = new HashMap<>();
            updates.put(achievement.getName(), FieldValue.delete());
            firebaseFirestore.collection("Users/"+userId+"/"+achievementType).document(date).update(updates);

        }
    }


}
