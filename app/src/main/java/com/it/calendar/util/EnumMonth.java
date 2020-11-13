package com.it.calendar.util;

public enum EnumMonth {

    nil("0", ""),
    jan("1", "ஜனவரி"),
    feb("2", "பிப்ரவரி"),
    mar("3", "மார்ச்"),
    apr("4", "ஏப்ரல்"),
    may("5", "மே"),
    jun("6", "ஜூன்"),
    jul("7", "ஜூலை"),
    aug("8", "ஆகஸ்ட்"),
    sep("9", "செப்டம்பர்"),
    oct("10", "அக்டோபர்"),
    nov("11", "நவம்பர்"),
    dec("12", "டிசம்பர்");

    private String month;
    private String text;

    EnumMonth(String month, String text) {
        this.month = month;
        this.text = text;
    }

    public static EnumMonth getMonth(String text) {
        for (EnumMonth e : values()) {
            if (e.text.equals(text))
                return e;
        }

        return nil;
    }

    public static EnumMonth getMonthStr(String month) {
        for (EnumMonth e : values()) {
            if (e.month.equals(month))
                return e;
        }

        return nil;
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
