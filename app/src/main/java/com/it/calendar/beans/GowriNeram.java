package com.it.calendar.beans;

import com.it.calendar.CalendarDB;
import com.it.core.db.annotations.FieldName;
import com.it.core.db.annotations.Table;


@Table(dataSet = CalendarDB.class, name = "GowriNeram", version = 1, autoIncrementID = true)

public class GowriNeram {

    @FieldName("date")
    private Long date;

    @FieldName("gowri_m")
    private String gowri_m;

    @FieldName("gowri_e")
    private String gowri_e;

    @FieldName("sooriya_r")
    private Double sooriya_r;

    @FieldName("karanam")
    private String karanam;

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getGowri_m() {
        return gowri_m;
    }

    public void setGowri_m(String gowri_m) {
        this.gowri_m = gowri_m;
    }

    public String getGowri_e() {
        return gowri_e;
    }

    public void setGowri_e(String gowri_e) {
        this.gowri_e = gowri_e;
    }

    public Double getSooriya_r() {
        return sooriya_r;
    }

    public void setSooriya_r(Double sooriya_r) {
        this.sooriya_r = sooriya_r;
    }

    public String getKaranam() {
        return karanam;
    }

    public void setKaranam(String karanam) {
        this.karanam = karanam;
    }
}
