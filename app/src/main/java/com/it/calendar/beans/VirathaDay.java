package com.it.calendar.beans;

import io.realm.RealmObject;

public class VirathaDay extends RealmObject {

    private Long date;
    private String viratham;
    private String time;

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
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
