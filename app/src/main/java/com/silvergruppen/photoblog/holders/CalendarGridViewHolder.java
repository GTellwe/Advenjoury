package com.silvergruppen.photoblog.holders;

import android.support.constraint.ConstraintLayout;
import android.widget.TextView;

public class CalendarGridViewHolder {

    TextView dateTextView, dailyProgressTextview;
    ConstraintLayout wrap;

    public CalendarGridViewHolder(TextView dateTextView, TextView dailyProgressTextview ,ConstraintLayout wrap) {
        this.dateTextView = dateTextView;
        this.dailyProgressTextview = dailyProgressTextview;
        this.wrap = wrap;
    }

    public ConstraintLayout getWrap() {
        return wrap;
    }

    public void setWrap(ConstraintLayout wrap) {
        this.wrap = wrap;
    }

    public TextView getDailyProgressTextview() {
        return dailyProgressTextview;
    }

    public void setDailyProgressTextview(TextView dailyProgressTextview) {
        this.dailyProgressTextview = dailyProgressTextview;
    }


    public TextView getDateTextView() {
        return dateTextView;
    }

    public void setDateTextView(TextView dateTextView) {
        this.dateTextView = dateTextView;
    }
}
