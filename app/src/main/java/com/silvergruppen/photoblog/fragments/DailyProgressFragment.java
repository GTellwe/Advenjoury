package com.silvergruppen.photoblog.fragments;

import android.content.Context;
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
    private ProgressBar progressBar;


    public DailyProgressFragment(){

        dailyAchievementsList = new ArrayList<>();
        dailyAchievementsList.add(new Achievement("meditate  for 10 minutes","Daily","10",false));
        dailyAchievementsList.add(new Achievement("Walk 10 000 steps","Daily","10",false));
        dailyAchievementsList.add(new Achievement("Walk 10 steps","Daily","10",false));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily_progress2, container, false);

        dateTextView = view.findViewById(R.id.daily_progrtess_day_text);
        progressTextView = view.findViewById(R.id.progres_text_view);
        progressBar = view.findViewById(R.id.progressbar_daily_progress);
        progressBar.setIndeterminate(false);

        if(calendar != null)
            dateTextView.setText(Integer.toString(calendar.get(Calendar.DAY_OF_MONTH))+"/"+Integer.toString(calendar.get(Calendar.MONTH)));

        MainActivity mainActivity = (MainActivity) getActivity();
        ArrayList<Integer> dailyProgress = mainActivity.getDailyProgressHashMap().get(calendar);
        if(dailyProgress == null) {
            progressTextView.setText("0/3");

        }else{

            Integer numberOfDoneTasks = Collections.frequency(dailyProgress,1);
            progressTextView.setText(Integer.toString(numberOfDoneTasks) + "/3");
            float progress = 100/3*numberOfDoneTasks ;
            Log.d("\n \n \n \n \n progress", Integer.toString(progressBar.getMax()));
            progressBar.setMax(50);
            progressBar.setProgress(20);
            progressBar.setProgress(0); // <--
            progressBar.setMax(100);
            progressBar.setProgress((int)progress);

        }
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
