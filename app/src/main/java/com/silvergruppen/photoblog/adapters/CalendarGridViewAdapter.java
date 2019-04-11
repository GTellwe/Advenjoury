package com.silvergruppen.photoblog.adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.silvergruppen.photoblog.R;
import com.silvergruppen.photoblog.activities.MainActivity;
import com.silvergruppen.photoblog.fragments.CalendarFragment;
import com.silvergruppen.photoblog.fragments.DailyProgressFragment;
import com.silvergruppen.photoblog.holders.CalendarGridViewHolder;
import com.silvergruppen.photoblog.items.CalendarItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;


public class CalendarGridViewAdapter extends BaseAdapter {

    private ArrayList<CalendarItem> calendarItems;
    private final static int dailyId=1, weekleyId = 2, monthlyId = 3;

    private CalendarFragment calendarFragment;
    private LayoutInflater mInflater;
    private Context context;

    public CalendarGridViewAdapter(Context context, ArrayList<CalendarItem> calendarItems, CalendarFragment calendarFragment){

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.calendarItems = calendarItems;
        this.context = context;
        this.calendarFragment = calendarFragment;

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
       boolean isCurrentDay = (Calendar.getInstance().get(Calendar.DAY_OF_MONTH)==calendarItem.getCalendar().get(Calendar.DAY_OF_MONTH));
       boolean isInCurrentMonth = (Calendar.getInstance().get(Calendar.MONTH)== calendarItem.getCalendar().get(Calendar.MONTH));

        if(convertView == null){

                convertView = mInflater.inflate(R.layout.calendar_grid_item, null);


                TextView dateNumberTextView = convertView.findViewById(R.id.calendar_item_date_number);

                TextView dailyProgressTextView = convertView.findViewById(R.id.calendar_item_daily_progress_text);

                ConstraintLayout wrap = convertView.findViewById(R.id.grid_item_wrap);

                holder = new CalendarGridViewHolder(dateNumberTextView, dailyProgressTextView, wrap);

        }else
            holder = (CalendarGridViewHolder) convertView.getTag();



            holder.getDateTextView().setText(Integer.toString(calendarItem.getCalendar().get(Calendar.DAY_OF_MONTH)));



            holder.getDailyProgressTextview().setText(calendarItem.getProgress() + "/8");
           // Log.d(calendarItem.getProgress()+" \n \n \n \n","adapter progress");

            if (calendarItem.getProgress() == 8) {

                holder.getDateTextView().setTextColor(Color.WHITE);
                holder.getDailyProgressTextview().setTextColor(Color.WHITE);
                holder.getWrap().setBackgroundResource(R.drawable.calendar_item_background_drawable);
            } else {

                // set the opacity of the text
                if(isInCurrentMonth) {
                    holder.getDailyProgressTextview().setTextColor(Color.parseColor("#767676"));
                    holder.getDateTextView().setTextColor(Color.parseColor("#767676"));
                }else{

                    holder.getDailyProgressTextview().setTextColor(Color.argb(60, 79, 79, 79));
                    holder.getDateTextView().setTextColor(Color.argb(60, 79, 79, 79));
                }

                holder.getWrap().setBackgroundResource(0);
            }

            if(isCurrentDay && isInCurrentMonth) {
                holder.getDailyProgressTextview().setTextColor(Color.parseColor("#FF0000"));
                holder.getDateTextView().setTextColor(Color.parseColor("#FF0000"));
            }

            convertView.setTag(holder);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                   /* // create and replace fragment
                    CalendarGridViewHolder holder = (CalendarGridViewHolder) view.getTag();
                    MainActivity mainActivity = (MainActivity) context;
                    mainActivity.startDailyProgressFragment(calendarItem.getCalendar(),dailyId);
                    */

                    calendarFragment.zoomImageFromThumb(view, R.drawable.icon);
                    // Retrieve and cache the system's default "short" animation time.


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
