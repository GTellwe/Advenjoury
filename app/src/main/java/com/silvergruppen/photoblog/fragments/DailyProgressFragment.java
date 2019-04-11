package com.silvergruppen.photoblog.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.silvergruppen.photoblog.R;
import com.silvergruppen.photoblog.activities.MainActivity;
import com.silvergruppen.photoblog.adapters.DailyProgressRecyclerViewAdapter;
import com.silvergruppen.photoblog.other.DailyProgress;
import com.silvergruppen.photoblog.other.MonthlyProgress;
import com.silvergruppen.photoblog.other.WeekleyProgress;
import com.silvergruppen.photoblog.viewmodels.ProgressViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class DailyProgressFragment extends Fragment {

    private final static int dailyId=1, weekleyId = 2, monthlyId = 3;
    private static final String DAY_KEY = "day";
    private static final String UID_KEY = "uid";
    private static final String TYPE_KEY = "type";
    private ProgressViewModel viewModel;
    public RecyclerView dailyProgressRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private DailyProgressRecyclerViewAdapter recycleViewAdapter;
    private Calendar calendar;
    private TextView dateTextView, progressTextView, headline;
    //private ProgressBar progressBar;


    public DailyProgressFragment(){



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily_progress2, container, false);

        int day = calendar.get(Calendar.DAY_OF_YEAR);
        String userId = getArguments().getString(UID_KEY);
        int type = getArguments().getInt(TYPE_KEY);

        if(type == monthlyId)
            day = calendar.get(Calendar.MONTH);
        else if(type == weekleyId)
            day = calendar.get(Calendar.WEEK_OF_YEAR);

        viewModel = ViewModelProviders.of(this).get(ProgressViewModel.class);
        viewModel.init(Integer.toString(day),userId, type);
        final ProgressBar progressBar = view.findViewById(R.id.progressbar_daily_progress);

        // Create the observer which updates the UI.
        final Observer<DailyProgress> userObserver = new Observer<DailyProgress>() {
            @Override
            public void onChanged(@Nullable final DailyProgress newDailyProgress) {
                // Update the UI, in this case, a TextView.
                int numberOfDoneTasks = Integer.parseInt(newDailyProgress.getProgress());
                progressTextView.setText(newDailyProgress.getProgress()+"/8");
                recycleViewAdapter.setDailyProgress(newDailyProgress);
                //Log.d("\n \n \n \n \n progress", Integer.toString(numberOfDoneTasks));
                progressBar.setProgress((int)numberOfDoneTasks);
                progressBar.setMax(8);

            }
        };

        final Observer<WeekleyProgress> weekleyObserver = new Observer<WeekleyProgress>() {
            @Override
            public void onChanged(@Nullable final WeekleyProgress newWeekleyProgress) {
                // Update the UI, in this case, a TextView.
                int numberOfDoneTasks = Integer.parseInt(newWeekleyProgress.getProgress());
                progressTextView.setText(newWeekleyProgress.getProgress()+"/8");
                recycleViewAdapter.setWeekleyProgress(newWeekleyProgress);
                //Log.d("\n \n \n \n \n progress", Integer.toString(numberOfDoneTasks));
                progressBar.setProgress((int)numberOfDoneTasks);
                progressBar.setMax(8);

            }
        };

        final Observer<MonthlyProgress> monthlyObserver = new Observer<MonthlyProgress>() {
            @Override
            public void onChanged(@Nullable final MonthlyProgress newWeekleyProgress) {
                // Update the UI, in this case, a TextView.
                int numberOfDoneTasks = Integer.parseInt(newWeekleyProgress.getProgress());
                progressTextView.setText(newWeekleyProgress.getProgress()+"/1");
                recycleViewAdapter.setMonthlyProgress(newWeekleyProgress);
                //Log.d("\n \n \n \n \n progress", Integer.toString(numberOfDoneTasks));
                progressBar.setProgress((int)numberOfDoneTasks);
                progressBar.setMax(1);

            }
        };

        if(type == dailyId)
            viewModel.getDailyProgress().observe(this,userObserver);
        else if(type == weekleyId)
            viewModel.getWeekleyProgress().observe(this,weekleyObserver);
        else if(type == monthlyId)
            viewModel.getMonthlyProgress().observe(this, monthlyObserver);

        dateTextView = view.findViewById(R.id.daily_progrtess_day_text);
        progressTextView = view.findViewById(R.id.progres_text_view);


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        if(calendar != null) {
            switch (type){
                case dailyId:
                    dateTextView.setText(dateFormat.format(calendar.getTime()));
                    break;
                case weekleyId:
                    dateTextView.setText("v."+calendar.get(Calendar.WEEK_OF_YEAR));
                    break;

                    default:
                        break;
            }

        }

        MainActivity mainActivity = (MainActivity) getActivity();
        ArrayList<Integer> dailyProgress = mainActivity.getDailyProgressHashMap().get(calendar);

        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.circular_progress_bar);








        dailyProgressRecyclerView = (RecyclerView) view.findViewById(R.id.daily_progress_list_view);
        dailyProgressRecyclerView.setHasFixedSize(true);

        ((SimpleItemAnimator) dailyProgressRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        mLayoutManager = new LinearLayoutManager(getActivity());
        dailyProgressRecyclerView.setLayoutManager(mLayoutManager);
        recycleViewAdapter = new DailyProgressRecyclerViewAdapter(getActivity(), viewModel,type);
        recycleViewAdapter.setCalendar(calendar);
        dailyProgressRecyclerView.setAdapter(recycleViewAdapter);
        recycleViewAdapter.notifyDataSetChanged();


        headline = view.findViewById(R.id.daily_progress_headline);

        switch (type){

            case dailyId:
                headline.setText("DAILY PROGRESS");
                break;
            case weekleyId:
                headline.setText("WEEKLEY PROGRESS");
                break;
            case monthlyId:
                headline.setText("MONTHLY PROGRESS");
                break;
                default:
                    break;
        }


        return view;
    }

    public Calendar getCalendar() {
        return calendar;
    }


    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }
}
