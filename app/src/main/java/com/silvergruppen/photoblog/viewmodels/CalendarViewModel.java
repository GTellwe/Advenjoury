package com.silvergruppen.photoblog.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.view.View;

import com.silvergruppen.photoblog.items.Achievement;
import com.silvergruppen.photoblog.other.User;
import com.silvergruppen.photoblog.repositories.CalendarRepository;
import com.silvergruppen.photoblog.repositories.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;

public class CalendarViewModel extends ViewModel{

    // Constants
    private final String DAILY_KEY = "DailyAchievements";
    private final String WEEKLY_KEY = "WeekleyAchievements";
    private final String MONTHLY_KEY = "MonthlyAchievements";


    private LiveData<HashMap<String, ArrayList<Achievement>>> doneAchievementsMatrix;
    private LiveData<HashMap<String, ArrayList<Achievement>>> doneMonthlyAchievementsMatrix;
    private LiveData<HashMap<String, ArrayList<Achievement>>> doneWeekleyAchievementsMatrix;
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
        doneAchievementsMatrix = calendarRepo.getCalendarAchievements(userId, DAILY_KEY);
        doneMonthlyAchievementsMatrix = calendarRepo.getCalendarAchievements(userId,MONTHLY_KEY);
        doneWeekleyAchievementsMatrix = calendarRepo.getCalendarAchievements(userId, WEEKLY_KEY);
    }
    public LiveData<HashMap<String, ArrayList<Achievement>>>getDoneAchievementsMatrix() {
        return this.doneAchievementsMatrix;
    }

    public LiveData<HashMap<String, ArrayList<Achievement>>>getDoneMonthlyAchievementsMatrix() {
        return this.doneMonthlyAchievementsMatrix;
    }

    public LiveData<HashMap<String, ArrayList<Achievement>>> getDoneWeekleyAchievementsMatrix() {
        return doneWeekleyAchievementsMatrix;
    }

    public void uppdateCurrentAchievementList(String userId,ArrayList<Achievement> achievementsList, String achievementKey){

        calendarRepo.uppdateCurrentAchievementList(userId,achievementsList,achievementKey);
    }
}
