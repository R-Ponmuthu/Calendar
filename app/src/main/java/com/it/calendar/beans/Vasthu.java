// Please note : @LinkingObjects and default values are not represented in the schema and thus will not be part of the generated models
package com.it.calendar.beans;

import com.it.calendar.CalendarDB;
import com.it.core.db.annotations.FieldName;
import com.it.core.db.annotations.Table;


@Table(dataSet = CalendarDB.class, name = "Vasthu", version = 1, autoIncrementID = true)

public class Vasthu {
    @FieldName("year")
    private Long year;
    @FieldName("day")
    private String day;
    @FieldName("neram")
    private String neram;
    @FieldName("time")
    private String time;

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
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


}
