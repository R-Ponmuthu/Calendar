// Please note : @LinkingObjects and default values are not represented in the schema and thus will not be part of the generated models
package com.it.calendar.model;

import io.realm.RealmObject;

public class Kalangal extends RealmObject {
    private Long year;
    private String weekday;
    private String ragu;
    private String kuligai;
    private String emakandam;
    private String soolam;
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
