package com.silvergruppen.photoblog.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.silvergruppen.photoblog.R;
import com.silvergruppen.photoblog.items.CalendarItem;
import com.silvergruppen.photoblog.adapters.CalendarGridViewAdapter;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CalendarFragment extends Fragment {

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
            calendarItems.add(new CalendarItem(new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.DAY_OF_MONTH),i)));




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daily_progress, container, false);
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
}
