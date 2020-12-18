package com.it.calendar.util;

public enum EnumKrakakalam {

    K0("0", ""),
    K1("1", "சூரியன்"),
    K2("2", "சுக்கிரன்"),
    K3("3", "புதன்"),
    K4("4", "சந்திரன்"),
    K5("5", "சனி"),
    K6("6", "குரு"),
    K7("7", "அங்காரகன்");

    private String month;
    private String text;

    EnumKrakakalam(String month, String text) {
        this.month = month;
        this.text = text;
    }

    public static EnumKrakakalam getKrakakalam(String text) {
        for (EnumKrakakalam e : values()) {
            if (e.text.equals(text))
                return e;
        }

        return K0;
    }

    public static EnumKrakakalam getKrakakalamStr(String month) {
        for (EnumKrakakalam e : values()) {
            if (e.month.equals(month))
                return e;
        }

        return K0;
    }

    public String getDay() {
        return month;
    }

    public void setDay(String day) {
        this.month = day;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
