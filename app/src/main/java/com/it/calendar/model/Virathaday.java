package com.it.calendar.model;

import io.realm.RealmObject;

public class Virathaday extends RealmObject {
    private String date;
    private String viratham;
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
