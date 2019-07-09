package com.it.calendar.detailed_calendarview.view;

import android.content.Context;
import android.util.AttributeSet;

import com.it.calendar.detailed_calendarview.internal.data.Day;


public final class DayView extends androidx.appcompat.widget.AppCompatTextView {

    private Day day;

    public DayView(Context context) {
        this(context, null, 0);
    }

    public DayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setDay(Day day) {
        setText(String.valueOf(day.getDay()));
        this.day = day;
        invalidate();
    }

    public Day getDay() {
        return day;
    }
}
