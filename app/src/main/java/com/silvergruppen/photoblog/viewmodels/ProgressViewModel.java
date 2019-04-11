package com.silvergruppen.photoblog.viewmodels;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.silvergruppen.photoblog.other.DailyProgress;
import com.silvergruppen.photoblog.other.MonthlyProgress;
import com.silvergruppen.photoblog.other.WeekleyProgress;
import com.silvergruppen.photoblog.repositories.ProgressRepository;

public class ProgressViewModel extends ViewModel {

    private final static int dailyId=1, weekleyId = 2, monthlyId = 3;
    private MutableLiveData<DailyProgress> dailyProgress;
    private MutableLiveData<WeekleyProgress> weekleyProgress;
    private MutableLiveData<MonthlyProgress> monthlyProgress;
    private ProgressRepository repo;
    private String dayweek;
    private String userId;
    private int type;

    public ProgressViewModel() {
        repo = new ProgressRepository();
    }

    public void init(String dayweek, String userId, int type) {
        this.dayweek = dayweek;
        this.userId = userId;
        this.type = type;
        if (this.dailyProgress != null && type == dailyId) {
            // ViewModel is created on a per-Fragment basis, so the userId
            // doesn't change.
            return;
        }else if(this.weekleyProgress != null && type == weekleyId)
            return;

        else if(this.monthlyProgress !=null && type == monthlyId)
            return;

        if(type == dailyId)
            dailyProgress = repo.getDailyProgress(dayweek,userId);
        else if(type == weekleyId)
            weekleyProgress = repo.getWeekleyProgress(dayweek, userId);
        else if(type == monthlyId)
            monthlyProgress = repo.getMonthlyProgress(dayweek, userId);
    }
    public MutableLiveData<DailyProgress> getDailyProgress() {
        return this.dailyProgress;
    }
    public MutableLiveData<WeekleyProgress> getWeekleyProgress() {
        return this.weekleyProgress;
    }
    public MutableLiveData<MonthlyProgress> getMonthlyProgress() {
        return this.monthlyProgress;
    }

    public void updateProgress(int position, int done){

        if(type == dailyId)
            repo.updateDailyProgress(userId,dailyProgress, position,Integer.toString(done),dayweek);
        else if(type == weekleyId)
            repo.updateWeekleyProgress(userId, weekleyProgress, position, Integer.toString(done), dayweek);
        else if(type == monthlyId)
            repo.updateMonthlyProgress(userId, monthlyProgress, position, Integer.toString(done), dayweek);

    }


}
