// Please note : @LinkingObjects and default values are not represented in the schema and thus will not be part of the generated models
package com.it.calendar.beans;

import com.it.calendar.CalendarDB;
import com.it.core.db.annotations.FieldName;
import com.it.core.db.annotations.Table;


@Table(dataSet = CalendarDB.class, name = "MuhurthamTable", version = 1, autoIncrementID = true)

public class MuhurthamTable {

    @FieldName("date")
    private String date;
    @FieldName("thethi")
    private String thethi;
    @FieldName("star")
    private String star;
    @FieldName("yokam")
    private String yokam;
    @FieldName("time")
    private String time;
    @FieldName("lakknam")
    private String lakknam;
    @FieldName("valrpirai")
    private Long valrpirai;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getThethi() {
        return thethi;
    }

    public void setThethi(String thethi) {
        this.thethi = thethi;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getYokam() {
        return yokam;
    }

    public void setYokam(String yokam) {
        this.yokam = yokam;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLakknam() {
        return lakknam;
    }

    public void setLakknam(String lakknam) {
        this.lakknam = lakknam;
    }

    public Long getValrpirai() {
        return valrpirai;
    }

    public void setValrpirai(Long valrpirai) {
        this.valrpirai = valrpirai;
    }
}
