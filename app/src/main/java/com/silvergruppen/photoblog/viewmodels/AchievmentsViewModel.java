package com.silvergruppen.photoblog.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.silvergruppen.photoblog.items.Achievement;
import com.silvergruppen.photoblog.items.RecycleListItem;
import com.silvergruppen.photoblog.repositories.AchievementsRepository;
import com.silvergruppen.photoblog.repositories.CalendarRepository;

import java.util.ArrayList;

public class AchievmentsViewModel extends ViewModel {


    private LiveData<ArrayList<RecycleListItem>> achievementsList;
    private AchievementsRepository achievementrRepo;


    public AchievmentsViewModel() {
        achievementrRepo = new AchievementsRepository();
    }

    public void init(String userId) {
        if (this.achievementsList != null){
            // ViewModel is created on a per-Fragment basis, so the userId
            // doesn't change.
            return;
        }
        achievementsList = achievementrRepo.getAchievements(userId);
    }
    public LiveData<ArrayList<RecycleListItem>> getAchievementsList() {
        return this.achievementsList;
    }

    public LiveData<Boolean>  addAchievementToList(String userId,String achievementName, String topic){

        return achievementrRepo.addAchievementToFirebase(userId, achievementName, topic);
    }

}
