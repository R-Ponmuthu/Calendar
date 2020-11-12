// Please note : @LinkingObjects and default values are not represented in the schema and thus will not be part of the generated models
package com.it.calendar.beans;

import io.realm.RealmObject;

public class Krakakalam extends RealmObject {
    private Long year;
    private String weekday;
    private String neram;
    private String time;
    private String yokam;

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public String getWeekday() {
        return weekday;
    }

    public void setWeekday(String weekday) {
        this.weekday = weekday;
    }

    public String getNeram() {
        return neram;
    }

    public void setNeram(String neram) {
        this.neram = neram;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getYokam() {
        return yokam;
    }

    public void setYokam(String yokam) {
        this.yokam = yokam;
    }
}
