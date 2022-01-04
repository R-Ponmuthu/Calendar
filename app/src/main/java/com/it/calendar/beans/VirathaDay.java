package com.it.calendar.beans;

import com.it.calendar.CalendarDB;
import com.it.core.db.annotations.FieldName;
import com.it.core.db.annotations.Table;


@Table(dataSet = CalendarDB.class, name = "VirathaDay", version = 1, autoIncrementID = true)
public class VirathaDay {

    @FieldName("date")
    private String date;
    @FieldName("viratham")
    private String viratham;
    @FieldName("time")
    private String time;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getViratham() {
        return viratham;
    }

    public void setViratham(String viratham) {
        this.viratham = viratham;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
