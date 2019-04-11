package com.silvergruppen.photoblog.items;

import java.util.Calendar;
import java.util.Date;

public class CalendarItem {
    private Calendar calendar;
    private int progress;


    public CalendarItem(Calendar calendar) {

        this.calendar = calendar;

        progress = 0;
    }



    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
