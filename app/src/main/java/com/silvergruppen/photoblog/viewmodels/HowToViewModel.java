package com.silvergruppen.photoblog.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.view.View;

import com.silvergruppen.photoblog.items.Achievement;
import com.silvergruppen.photoblog.other.User;
import com.silvergruppen.photoblog.repositories.CalendarRepository;
import com.silvergruppen.photoblog.repositories.HowToRepository;
import com.silvergruppen.photoblog.repositories.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;

public class HowToViewModel extends ViewModel{


    private LiveData<HashMap<String, ArrayList<Achievement>>> taskTree;
    private HowToRepository repo;



    public HowToViewModel() {
        repo = new HowToRepository();
    }

    public void init(String userId, Achievement achievement) {
        if (this.taskTree != null) {
            // ViewModel is created on a per-Fragment basis, so the userId
            // doesn't change.
            return;
        }
        taskTree = repo.getTaskTree(userId, achievement);
    }
   public LiveData<HashMap<String, ArrayList<Achievement>>> getTaskTree(){

        return taskTree;
   }
    public  LiveData<HashMap<String, ArrayList<Achievement>>> addAchievementToTaskTree(String userId,Achievement mainAchievement,Achievement parentAchievement, Achievement childAchievement){

        return repo.addAchievementToTaskTree(userId, mainAchievement,parentAchievement,childAchievement,taskTree.getValue());
    }
}
