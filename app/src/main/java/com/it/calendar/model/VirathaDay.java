package com.it.calendar.model;

import io.realm.RealmObject;

public class VirathaDay extends RealmObject {
    private String date;
    private String viratham;
    private String time;
    private String field4;

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

    public String getField4() {
        return field4;
    }

    public void setField4(String field4) {
        this.field4 = field4;
    }
}
