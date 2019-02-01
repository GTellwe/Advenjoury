package com.silvergruppen.photoblog.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.view.View;

import com.silvergruppen.photoblog.items.Achievement;
import com.silvergruppen.photoblog.other.DailyProgress;
import com.silvergruppen.photoblog.other.User;
import com.silvergruppen.photoblog.repositories.DailyProgressRepository;
import com.silvergruppen.photoblog.repositories.UserRepository;

import java.util.ArrayList;

public class DailyProgressViewModel extends ViewModel {

    private MutableLiveData<DailyProgress> dailyProgress;
    private DailyProgressRepository dailyRepo;
    private String day;
    private String userId;



    public DailyProgressViewModel() {
        dailyRepo = new DailyProgressRepository();
    }

    public void init(String day, String userId) {
        this.day = day;
        this.userId = userId;
        if (this.dailyProgress != null) {
            // ViewModel is created on a per-Fragment basis, so the userId
            // doesn't change.
            return;
        }
        dailyProgress = dailyRepo.getDailyProgress(day,userId);
    }
    public MutableLiveData<DailyProgress> getDailyProgress() {
        return this.dailyProgress;
    }

    public void updateDailyProgress(int position, int done){

        dailyRepo.updateDailyProgress(userId,dailyProgress, position,Integer.toString(done),day);

    }


}
