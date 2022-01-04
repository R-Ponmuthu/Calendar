package com.it.calendar.beans;

import com.it.calendar.CalendarDB;
import com.it.core.db.annotations.FieldName;
import com.it.core.db.annotations.Table;


@Table(dataSet = CalendarDB.class, name = "MainTable", version = 1, autoIncrementID = true)
public class MainTable {

    @FieldName("date")
    private String date;

    @FieldName("day")
    private Long day;

    @FieldName("month")
    private String month;

    @FieldName("year")
    private Long year;

    @FieldName("weekday")
    private String weekday;

    @FieldName("tam_month")
    private String tam_month;

    @FieldName("tam_day")
    private Long tam_day;

    @FieldName("tam_year")
    private String tam_year;

    @FieldName("nallanerem_m")
    private String nallanerem_m;

    @FieldName("nallanerem_e")
    private String nallanerem_e;

    @FieldName("day_type")
    private String day_type;

    @FieldName("quote")
    private String quote;

    @FieldName("thiti")
    private String thiti;

    @FieldName("star")
    private String star;

    @FieldName("yokam")
    private String yokam;

    @FieldName("chanthran")
    private String chanthran;

    @FieldName("importantday")
    private String importantday;

    @FieldName("hindu_fes")
    private String hindu_fes;

    @FieldName("muslim_fes")
    private String muslim_fes;

    @FieldName("chirs_fes")
    private String chirs_fes;

    @FieldName("gov_holiday")
    private String gov_holiday;

    @FieldName("leave_flag")
    private Long leave_flag;

    @FieldName("mesam")
    private String mesam;

    @FieldName("risibam")
    private String risibam;

    @FieldName("mithunam")
    private String mithunam;

    @FieldName("kadakam")
    private String kadakam;

    @FieldName("simmam")
    private String simmam;

    @FieldName("kanni")
    private String kanni;

    @FieldName("thulam")
    private String thulam;

    @FieldName("viruchakam")
    private String viruchakam;

    @FieldName("dhanusu")
    private String dhanusu;

    @FieldName("makaram")
    private String makaram;

    @FieldName("kumbam")
    private String kumbam;

    @FieldName("meenam")
    private String meenam;

    @FieldName("laknam")
    private Long laknam;

    @FieldName("laknam_time")
    private String laknam_time;

    @FieldName("pirai")
    private Long pirai;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getDay() {
        return day;
    }

    public void setDay(Long day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

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

    public String getTam_month() {
        return tam_month;
    }

    public void setTam_month(String tam_month) {
        this.tam_month = tam_month;
    }

    public Long getTam_day() {
        return tam_day;
    }

    public void setTam_day(Long tam_day) {
        this.tam_day = tam_day;
    }

    public String getTam_year() {
        return tam_year;
    }

    public void setTam_year(String tam_year) {
        this.tam_year = tam_year;
    }

    public String getNallanerem_m() {
        return nallanerem_m;
    }

    public void setNallanerem_m(String nallanerem_m) {
        this.nallanerem_m = nallanerem_m;
    }

    public String getNallanerem_e() {
        return nallanerem_e;
    }

    public void setNallanerem_e(String nallanerem_e) {
        this.nallanerem_e = nallanerem_e;
    }

    public String getDay_type() {
        return day_type;
    }

    public void setDay_type(String day_type) {
        this.day_type = day_type;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getThiti() {
        return thiti;
    }

    public void setThiti(String thiti) {
        this.thiti = thiti;
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

    public String getChanthran() {
        return chanthran;
    }

    public void setChanthran(String chanthran) {
        this.chanthran = chanthran;
    }

    public String getImportantday() {
        return importantday;
    }

    public void setImportantday(String importantday) {
        this.importantday = importantday;
    }

    public String getHindu_fes() {
        return hindu_fes;
    }

    public void setHindu_fes(String hindu_fes) {
        this.hindu_fes = hindu_fes;
    }

    public String getMuslim_fes() {
        return muslim_fes;
    }

    public void setMuslim_fes(String muslim_fes) {
        this.muslim_fes = muslim_fes;
    }

    public String getChirs_fes() {
        return chirs_fes;
    }

    public void setChirs_fes(String chirs_fes) {
        this.chirs_fes = chirs_fes;
    }

    public String getGov_holiday() {
        return gov_holiday;
    }

    public void setGov_holiday(String gov_holiday) {
        this.gov_holiday = gov_holiday;
    }

    public Long getLeave_flag() {
        return leave_flag;
    }

    public void setLeave_flag(Long leave_flag) {
        this.leave_flag = leave_flag;
    }

    public String getMesam() {
        return mesam;
    }

    public void setMesam(String mesam) {
        this.mesam = mesam;
    }

    public String getRisibam() {
        return risibam;
    }

    public void setRisibam(String risibam) {
        this.risibam = risibam;
    }

    public String getMithunam() {
        return mithunam;
    }

    public void setMithunam(String mithunam) {
        this.mithunam = mithunam;
    }

    public String getKadakam() {
        return kadakam;
    }

    public void setKadakam(String kadakam) {
        this.kadakam = kadakam;
    }

    public String getSimmam() {
        return simmam;
    }

    public void setSimmam(String simmam) {
        this.simmam = simmam;
    }

    public String getKanni() {
        return kanni;
    }

    public void setKanni(String kanni) {
        this.kanni = kanni;
    }

    public String getThulam() {
        return thulam;
    }

    public void setThulam(String thulam) {
        this.thulam = thulam;
    }

    public String getViruchakam() {
        return viruchakam;
    }

    public void setViruchakam(String viruchakam) {
        this.viruchakam = viruchakam;
    }

    public String getDhanusu() {
        return dhanusu;
    }

    public void setDhanusu(String dhanusu) {
        this.dhanusu = dhanusu;
    }

    public String getMakaram() {
        return makaram;
    }

    public void setMakaram(String makaram) {
        this.makaram = makaram;
    }

    public String getKumbam() {
        return kumbam;
    }

    public void setKumbam(String kumbam) {
        this.kumbam = kumbam;
    }

    public String getMeenam() {
        return meenam;
    }

    public void setMeenam(String meenam) {
        this.meenam = meenam;
    }

    public Long getLaknam() {
        return laknam;
    }

    public void setLaknam(Long laknam) {
        this.laknam = laknam;
    }

    public String getLaknam_time() {
        return laknam_time;
    }

    public void setLaknam_time(String laknam_time) {
        this.laknam_time = laknam_time;
    }

    public Long getPirai() {
        return pirai;
    }

    public void setPirai(Long pirai) {
        this.pirai = pirai;
    }
}
