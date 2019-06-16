package com.it.calendar.model;

import io.realm.RealmObject;

public class Porutham extends RealmObject {
    private Double nid;
    private Long title;
    private Long mark;
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
