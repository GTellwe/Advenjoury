package com.silvergruppen.photoblog.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.silvergruppen.photoblog.R;


public class DailyProgressAchievementsFragment extends Fragment {



    public DailyProgressAchievementsFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily_progress2, container, false);



        return view;
    }
}
