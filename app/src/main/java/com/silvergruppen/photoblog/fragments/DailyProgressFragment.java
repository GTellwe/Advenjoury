package com.silvergruppen.photoblog.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.silvergruppen.photoblog.R;
import com.silvergruppen.photoblog.activities.MainActivity;
import com.silvergruppen.photoblog.adapters.DailyProgressRecyclerViewAdapter;
import com.silvergruppen.photoblog.adapters.RecycleViewAdapter;
import com.silvergruppen.photoblog.items.Achievement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;


public class DailyProgressFragment extends Fragment {

    private ArrayList<Achievement> dailyAchievementsList;
    public RecyclerView dailyProgressRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private DailyProgressRecyclerViewAdapter recycleViewAdapter;
    private Calendar calendar;
    private TextView dateTextView, progressTextView;
    //private ProgressBar progressBar;


    public DailyProgressFragment(){

        dailyAchievementsList = new ArrayList<>();
        dailyAchievementsList.add(new Achievement("meditate  for 10 minutes","Daily","10",false,"daily"));
        dailyAchievementsList.add(new Achievement("Walk 10 000 steps","Daily","10",false,"daily"));
        dailyAchievementsList.add(new Achievement("Walk 10 steps","Daily","10",false,"daily"));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily_progress2, container, false);

        dateTextView = view.findViewById(R.id.daily_progrtess_day_text);
        progressTextView = view.findViewById(R.id.progres_text_view);
        final ProgressBar progressBar = view.findViewById(R.id.progressbar_daily_progress);



        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        if(calendar != null)
            dateTextView.setText(dateFormat.format(calendar.getTime()));

        MainActivity mainActivity = (MainActivity) getActivity();
        ArrayList<Integer> dailyProgress = mainActivity.getDailyProgressHashMap().get(calendar);

        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.circular_progress_bar);
        Integer numberOfDoneTasks =0;
        if(dailyProgress != null) {
            numberOfDoneTasks = Collections.frequency(dailyProgress,1);

        }

        progressTextView.setText(Integer.toString(numberOfDoneTasks) + "/3");
        float progress = 100/3*numberOfDoneTasks ;
        Log.d("\n \n \n \n \n progress", Integer.toString(numberOfDoneTasks));
        progressBar.setProgress((int)numberOfDoneTasks);
        progressBar.setSecondaryProgress(100);
        progressBar.setMax(100);
        progressBar.setProgressDrawable(drawable);





        dailyProgressRecyclerView = (RecyclerView) view.findViewById(R.id.daily_progress_list_view);
        dailyProgressRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        dailyProgressRecyclerView.setLayoutManager(mLayoutManager);
        recycleViewAdapter = new DailyProgressRecyclerViewAdapter(getActivity(), dailyAchievementsList);
        recycleViewAdapter.setCalendar(calendar);
        dailyProgressRecyclerView.setAdapter(recycleViewAdapter);
        recycleViewAdapter.notifyDataSetChanged();

        return view;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }
}
