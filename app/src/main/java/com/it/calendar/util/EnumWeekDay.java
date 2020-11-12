package com.it.calendar.util;

public enum EnumWeekDay {

    Sunday(Long.valueOf("1"), "ஞாயிறு"),
    Monday(Long.valueOf("2"), "திங்கள்"),
    Tuesday(Long.valueOf("3"), "செவ்வாய்"),
    Wednesday(Long.valueOf("4"), "புதன்"),
    Thursday(Long.valueOf("5"), "வியாழன்"),
    Friday(Long.valueOf("6"), "வெள்ளி"),
    Saturday(Long.valueOf("7"), "சனி");

    private Long day;
    private String text;

    EnumWeekDay(Long day, String text) {
        this.day = day;
        this.text = text;
    }

    public static EnumWeekDay getWeekDay(Long day) {
        for (EnumWeekDay e : values()) {
            if (e.day.equals(day))
                return e;
        }

        return Sunday;
    }

    public Long getDay() {
        return day;
    }

    public void setDay(Long day) {
        this.day = day;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
