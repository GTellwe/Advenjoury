package com.silvergruppen.photoblog.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.silvergruppen.photoblog.R;
import com.silvergruppen.photoblog.activities.MainActivity;
import com.silvergruppen.photoblog.adapters.DailyProgressRecyclerViewAdapter;
import com.silvergruppen.photoblog.adapters.RecycleViewAdapter;
import com.silvergruppen.photoblog.items.Achievement;
import com.silvergruppen.photoblog.other.DailyProgress;
import com.silvergruppen.photoblog.other.User;
import com.silvergruppen.photoblog.viewmodels.DailyProgressViewModel;
import com.silvergruppen.photoblog.viewmodels.UserProfileViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;


public class DailyProgressFragment extends Fragment {

    private static final String DAY_KEY = "day";
    private static final String UID_KEY = "uid";
    private DailyProgressViewModel viewModel;
    public RecyclerView dailyProgressRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private DailyProgressRecyclerViewAdapter recycleViewAdapter;
    private Calendar calendar;
    private TextView dateTextView, progressTextView;
    //private ProgressBar progressBar;


    public DailyProgressFragment(){



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily_progress2, container, false);

        int day = calendar.get(Calendar.DAY_OF_YEAR);
        String userId = getArguments().getString(UID_KEY);
        viewModel = ViewModelProviders.of(this).get(DailyProgressViewModel.class);
        viewModel.init(Integer.toString(day),userId);
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

        viewModel.getDailyProgress().observe(this,userObserver);

        dateTextView = view.findViewById(R.id.daily_progrtess_day_text);
        progressTextView = view.findViewById(R.id.progres_text_view);




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







        dailyProgressRecyclerView = (RecyclerView) view.findViewById(R.id.daily_progress_list_view);
        dailyProgressRecyclerView.setHasFixedSize(true);

        ((SimpleItemAnimator) dailyProgressRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        mLayoutManager = new LinearLayoutManager(getActivity());
        dailyProgressRecyclerView.setLayoutManager(mLayoutManager);
        recycleViewAdapter = new DailyProgressRecyclerViewAdapter(getActivity(), viewModel);
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
