package com.it.calendar.util;

public enum EnumWeekDay {

    Sunday("1", "ஞாயிறு"),
    Monday("2", "திங்கள்"),
    Tuesday("3", "செவ்வாய்"),
    Wednesday("4", "புதன்"),
    Thursday("5", "வியாழன்"),
    Friday("6", "வெள்ளி"),
    Saturday("7", "சனி");

    private String day;
    private String text;

    EnumWeekDay(String day, String text) {
        this.day = day;
        this.text = text;
    }

    public static EnumWeekDay getWeekDay(String day) {
        for (EnumWeekDay e : values()) {
            if (e.day.equals(day))
                return e;
        }

        return Sunday;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
