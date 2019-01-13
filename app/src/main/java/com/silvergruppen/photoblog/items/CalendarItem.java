package com.silvergruppen.photoblog.items;

import java.util.Calendar;
import java.util.Date;

public class CalendarItem {
    private Calendar calendar;

    public CalendarItem(Calendar calendar) {
        this.calendar = calendar;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }
}
