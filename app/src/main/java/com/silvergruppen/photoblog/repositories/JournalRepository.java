package com.silvergruppen.photoblog.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.silvergruppen.photoblog.items.Achievement;
import com.silvergruppen.photoblog.items.PostItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class JournalRepository {

    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public LiveData<ArrayList<PostItem>> getJournalItems(String userId, final Achievement achievement) {

        final MutableLiveData<ArrayList<PostItem>> data = new MutableLiveData<>();
        firebaseFirestore.collection("Users/"+userId+"/Posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<PostItem> tmpList = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {

                                Map<String, Object> tmpMap = document.getData();
                                String tmpTopic = tmpMap.get("topic").toString();
                                String achievementName = tmpMap.get("achievement_name").toString();

                                if (tmpTopic.equals(achievement.getTopic()) && achievementName.equals(achievement.getName())) {
                                    String postText = tmpMap.get("desc").toString();
                                    String postImageUrl = null;
                                    if (tmpMap.get("image_url") != null)
                                        postImageUrl = tmpMap.get("image_url").toString();
                                    Date timestamp = (Date) tmpMap.get("timestamp");
                                    tmpList.add(new PostItem(postText, postImageUrl, timestamp));

                                }
                            }
                            data.setValue(tmpList);
                        }


                    }
                });
        return data;
    }



}