package com.it.calendar.beans;

import com.it.calendar.CalendarDB;

import com.it.core.db.annotations.FieldName;
import com.it.core.db.annotations.Table;

@Table(dataSet = CalendarDB.class, name = "Kalangal", version = 1, autoIncrementID = true)

public class Kalangal  {

    @FieldName("year")
    private Long year;

    @FieldName("weekday")
    private String weekday;

    @FieldName("ragu")
    private String ragu;

    @FieldName("kuligai")
    private String kuligai;

    @FieldName("emakandam")
    private String emakandam;

    @FieldName("soolam")
    private String soolam;

    @FieldName("parikaram")
    private String parikaram;

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

    public String getRagu() {
        return ragu;
    }

    public void setRagu(String ragu) {
        this.ragu = ragu;
    }

    public String getKuligai() {
        return kuligai;
    }

    public void setKuligai(String kuligai) {
        this.kuligai = kuligai;
    }

    public String getEmakandam() {
        return emakandam;
    }

    public void setEmakandam(String emakandam) {
        this.emakandam = emakandam;
    }

    public String getSoolam() {
        return soolam;
    }

    public void setSoolam(String soolam) {
        this.soolam = soolam;
    }

    public String getParikaram() {
        return parikaram;
    }

    public void setParikaram(String parikaram) {
        this.parikaram = parikaram;
    }
}
