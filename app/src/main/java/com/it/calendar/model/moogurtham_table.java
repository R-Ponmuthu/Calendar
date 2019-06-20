// Please note : @LinkingObjects and default values are not represented in the schema and thus will not be part of the generated models
package com.it.calendar.model;

import io.realm.RealmObject;

public class moogurtham_table extends RealmObject {
    private String date;
    private String thethi;
    private String star;
    private String yokam;
    private String time;
    private String lakknam;
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
