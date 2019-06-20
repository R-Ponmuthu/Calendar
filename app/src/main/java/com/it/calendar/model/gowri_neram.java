package com.it.calendar.model;

import io.realm.RealmObject;

public class gowri_neram extends RealmObject {
    private String date;
    private String gowri_m;
    private String gowri_e;
    private Double sooriya_r;
    private String karanam;

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public String getGowri_m() { return gowri_m; }

    public void setGowri_m(String gowri_m) { this.gowri_m = gowri_m; }

    public String getGowri_e() { return gowri_e; }

    public void setGowri_e(String gowri_e) { this.gowri_e = gowri_e; }

    public Double getSooriya_r() { return sooriya_r; }

    public void setSooriya_r(Double sooriya_r) { this.sooriya_r = sooriya_r; }

    public String getKaranam() { return karanam; }

    public void setKaranam(String karanam) { this.karanam = karanam; }


}
