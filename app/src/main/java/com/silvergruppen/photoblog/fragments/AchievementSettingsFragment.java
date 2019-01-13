package com.silvergruppen.photoblog.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.silvergruppen.photoblog.R;
import com.silvergruppen.photoblog.activities.MainActivity;
import com.silvergruppen.photoblog.items.Achievement;

public class AchievementSettingsFragment extends Fragment {

    private Switch achievementDoneSwitch;
    private MainActivity mainActivity;
    private Achievement achievement;


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

        achievementDoneSwitch = view.findViewById(R.id.achievement_settings_switch);

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
        return view;


    }

    public Achievement getAchievement() {
        return achievement;
    }

    public void setAchievement(Achievement achievement) {
        this.achievement = achievement;
    }
}
