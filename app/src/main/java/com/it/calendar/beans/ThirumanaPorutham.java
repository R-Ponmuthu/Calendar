package com.it.calendar.beans;

import com.it.calendar.CalendarDB;
import com.it.core.db.annotations.FieldName;
import com.it.core.db.annotations.Table;


@Table(dataSet = CalendarDB.class, name = "ThirumanaPorutham", version = 1, autoIncrementID = true)

public class ThirumanaPorutham  {

    @FieldName("nid")
    private Double nid;
    @FieldName("title")
    private Long title;
    @FieldName("mark")
    private Long mark;
    @FieldName("value")
    private Long value;

    public Double getNid() {
        return nid;
    }

    public void setNid(Double nid) {
        this.nid = nid;
    }

    public Long getTitle() {
        return title;
    }

    public void setTitle(Long title) {
        this.title = title;
    }

    public Long getMark() {
        return mark;
    }

    public void setMark(Long mark) {
        this.mark = mark;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }


}
