package com.silvergruppen.photoblog.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.silvergruppen.photoblog.R;
import com.silvergruppen.photoblog.activities.MainActivity;
import com.silvergruppen.photoblog.holders.CalendarGridViewHolder;
import com.silvergruppen.photoblog.items.CalendarItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;


public class CalendarGridViewAdapter extends BaseAdapter {

    private ArrayList<CalendarItem> calendarItems;

    private LayoutInflater mInflater;
    private Context context;

    public CalendarGridViewAdapter(Context context, ArrayList<CalendarItem> calendarItems){

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.calendarItems = calendarItems;
        this.context = context;

    }

    @Override
    public int getCount() {
        return calendarItems.size();
    }

    @Override
    public Object getItem(int position) {
        return calendarItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

       CalendarGridViewHolder holder;
       final CalendarItem calendarItem = calendarItems.get(position);

        if(convertView == null){

            convertView = mInflater.inflate(R.layout.calendar_grid_item, null);

            TextView dateNumberTextView = convertView.findViewById(R.id.calendar_item_date_number);

            TextView dailyProgressTextView = convertView.findViewById(R.id.calendar_item_daily_progress_text);

            ConstraintLayout wrap = convertView.findViewById(R.id.grid_item_wrap);

            holder = new CalendarGridViewHolder(dateNumberTextView, dailyProgressTextView, wrap);


        }else
            holder = (CalendarGridViewHolder) convertView.getTag();

        holder.getDateTextView().setText(Integer.toString(calendarItem.getCalendar().get(Calendar.DAY_OF_MONTH)));

        Log.d("\n \n \n "+calendarItem.getProgress(),"erervv");
        holder.getDailyProgressTextview().setText(calendarItem.getProgress()+"/8");
        if(calendarItem.getProgress() == 8){

            holder.getDateTextView().setTextColor(Color.WHITE);
            holder.getDailyProgressTextview().setTextColor(Color.WHITE);
            holder.getWrap().setBackgroundResource(R.drawable.calendar_item_background_drawable);
        }else {

            holder.getDateTextView().setTextColor(Color.parseColor("#4F4F4F"));
            holder.getDailyProgressTextview().setTextColor(Color.parseColor("#4F4F4F"));
            holder.getWrap().setBackgroundResource(0);
        }

        convertView.setTag(holder);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // create and replace fragment
                CalendarGridViewHolder holder = (CalendarGridViewHolder) view.getTag();
                MainActivity mainActivity = (MainActivity) context;
                mainActivity.startDailyProgressFragment(calendarItem.getCalendar());


            }
        });

        return convertView;
    }

    public ArrayList<CalendarItem> getCalendarItems() {
        return calendarItems;
    }

    public void setCalendarItems(ArrayList<CalendarItem> calendarItems) {
        this.calendarItems = calendarItems;
    }
}
