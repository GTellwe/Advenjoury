package com.silvergruppen.photoblog.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.view.View;

import com.silvergruppen.photoblog.other.User;
import com.silvergruppen.photoblog.repositories.CalendarRepository;
import com.silvergruppen.photoblog.repositories.UserRepository;

public class CalendarViewModel extends ViewModel{


    private LiveData<int[][]> doneAchievementsMatrix;
    private LiveData<int[][]> doneMonthlyAchievementsMatrix;
    private LiveData<int[][]> doneWeekleyAchievementsMatrix;
    private CalendarRepository calendarRepo;


    public CalendarViewModel() {
        calendarRepo = new CalendarRepository();
    }

    public void init(String userId) {
        if ((this.doneAchievementsMatrix != null) || (this.doneMonthlyAchievementsMatrix != null) || (this.doneWeekleyAchievementsMatrix != null)) {
            // ViewModel is created on a per-Fragment basis, so the userId
            // doesn't change.
            return;
        }
        doneAchievementsMatrix = calendarRepo.getDoneAchievements(userId);
        doneMonthlyAchievementsMatrix = calendarRepo.getMonthlyDoneAchievements(userId);
        doneWeekleyAchievementsMatrix = calendarRepo.getWeekleyDoneAchievements(userId);
    }
    public LiveData<int[][]> getDoneAchievementsMatrix() {
        return this.doneAchievementsMatrix;
    }

    public LiveData<int[][]> getDoneMonthlyAchievementsMatrix() {
        return this.doneMonthlyAchievementsMatrix;
    }

    public LiveData<int[][]> getDoneWeekleyAchievementsMatrix() {
        return doneWeekleyAchievementsMatrix;
    }
}
