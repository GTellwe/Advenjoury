package com.silvergruppen.photoblog.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.silvergruppen.photoblog.R;
import com.silvergruppen.photoblog.items.CalendarItem;
import com.silvergruppen.photoblog.adapters.CalendarGridViewAdapter;
import com.silvergruppen.photoblog.other.User;
import com.silvergruppen.photoblog.viewmodels.CalendarViewModel;
import com.silvergruppen.photoblog.viewmodels.UserProfileViewModel;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

public class CalendarFragment extends Fragment {


    private static final String UID_KEY = "uid";
    private CalendarViewModel calendarViewModel;
    private GridView calendarGridView;
    private CalendarGridViewAdapter calendarGridViewAdapter;
    private ArrayList<CalendarItem> calendarItems;
    private Calendar calendar;
    private TextView monthTextView;
    private String[] monthNames =  {"January", "February",
            "March", "April", "May", "June", "July",
            "August", "September", "October", "November",
            "December"};


    public CalendarFragment(){

        // get number of days in month
        calendar = Calendar.getInstance();
        int numberOfDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendarItems = new ArrayList<>();

        for(int i = 1; i <= numberOfDaysInMonth; i++)
            calendarItems.add(new CalendarItem(new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),i)));




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily_progress, container, false);

        String userId = getArguments().getString(UID_KEY);
        calendarViewModel = ViewModelProviders.of(this).get(CalendarViewModel.class);
        calendarViewModel.init(userId);

        final Observer<int[][]> userObserver = new Observer<int[][]>() {
            @Override
            public void onChanged(@Nullable final int[][] newDoneMatrix) {
                updateCalendarItems(newDoneMatrix);

            }
        };

        calendarViewModel.getDoneAchievementsMatrix().observe(this,userObserver);
        calendarGridView = view.findViewById(R.id.daily_progress_calendar_grid_view);

        calendarGridViewAdapter = new CalendarGridViewAdapter(getContext(), calendarItems);
        calendarGridView.setAdapter(calendarGridViewAdapter);

        // set the month name
        monthTextView = view.findViewById(R.id.daily_progress_month_text);
        calendar = Calendar.getInstance();
        String month  = monthNames[calendar.get(Calendar.MONTH)];
        monthTextView.setText(month);


        return view;
    }

    private void updateCalendarItems(int[][] newDoneMatrix){

        ArrayList<CalendarItem> newCalendarItems = calendarGridViewAdapter.getCalendarItems();
        for(int i =0; i<newCalendarItems.size();i++){

            CalendarItem item = newCalendarItems.get(i);
            int[] tmpArray  = newDoneMatrix[item.getCalendar().get(Calendar.DAY_OF_YEAR)-1];
            int progress = 0;
            for(int j =0; j<8; j++){
                if(tmpArray[j] == 1)
                    progress ++;
            }

            item.setProgress(progress);
           // Log.d("pogress \n \n \n \n"+i,Integer.toString(progress));

        }
        calendarGridViewAdapter.setCalendarItems(newCalendarItems);
        Log.d("notify \n \n \n \n",Integer.toString(newCalendarItems.get(5).getProgress()));
        calendarGridViewAdapter.notifyDataSetChanged();
    }
}
