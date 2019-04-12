package com.silvergruppen.photoblog.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.silvergruppen.photoblog.R;
import com.silvergruppen.photoblog.activities.MainActivity;
import com.silvergruppen.photoblog.items.Achievement;
import com.silvergruppen.photoblog.repositories.AchievementSettingsRepository;

import java.util.Calendar;

public class AchievementSettingsFragment extends Fragment {

    // Constants
    private final String DAILY_KEY = "DailyAchievements";
    private final String WEEKLY_KEY = "WeekleyAchievements";
    private final String MONTHLY_KEY = "MonthlyAchievements";


    // Strings
    private String userId;

    // Other
    private Switch achievementDoneSwitch, addToDailySwitch, addToWeeklySwitch,addToMonthlySwitch;
    private MainActivity mainActivity;
    private Achievement achievement;
    private AchievementSettingsRepository repo;



    public AchievementSettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_achievement_settings, container, false);
        repo = new AchievementSettingsRepository();

        /*achievementDoneSwitch = view.findViewById(R.id.achievement_settings_switch);

        mainActivity = (MainActivity) getActivity();
        final Achievement tmpAchievement = achievement;

        achievementDoneSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // send information to firebase
                if(achievementDoneSwitch.isChecked() && achievement !=null)
                mainActivity.setAchievementDoneInfireBase(true,tmpAchievement);
                else if(achievement != null)
                    mainActivity.setAchievementDoneInfireBase(false,tmpAchievement);

            }
        });
        */

        // Handle the switches: when a switch is pushed send update firebase
        addToDailySwitch = view.findViewById(R.id.add_to_daily_switch);
        addToWeeklySwitch = view.findViewById(R.id.add_to_weekly_switch);
        addToMonthlySwitch = view.findViewById(R.id.add_to_monthly_switch);

        addToDailySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                repo.updateCalendarAchievement(userId, achievement, DAILY_KEY, Integer.toString(Calendar.getInstance().get(Calendar.DAY_OF_YEAR)), true);

            }
        } );
        addToWeeklySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                repo.updateCalendarAchievement(userId,achievement,WEEKLY_KEY, Integer.toString(Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)),b);
            }
        });

        addToMonthlySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                repo.updateCalendarAchievement(userId,achievement,MONTHLY_KEY, Integer.toString(Calendar.getInstance().get(Calendar.MONTH)),b);
            }
        });
        return view;


    }

    public Achievement getAchievement() {
        return achievement;
    }

    public void setAchievement(Achievement achievement) {
        this.achievement = achievement;
    }

    public void setUserId(String userId){

        this.userId = userId;
    }
}
