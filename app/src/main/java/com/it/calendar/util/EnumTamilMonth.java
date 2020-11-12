package com.it.calendar.util;

public enum EnumTamilMonth {

    one("1", "சித்திரை"),
    two("2", "வைகாசி"),
    three("3", "ஆனி"),
    four("4", "ஆடி"),
    five("5", "ஆவணி"),
    six("6", "புரட்டாசி"),
    seven("7", "ஐப்பசி"),
    eight("8", "கார்த்திகை"),
    nine("9", "மார்கழி"),
    ten("10", "தை"),
    eleven("11", "மாசி"),
    tweleve("12", "பங்குனி");

    private String month;
    private String text;

    EnumTamilMonth(String month, String text) {
        this.month = month;
        this.text = text;
    }

    public static EnumTamilMonth getTamilMonth(String day) {
        for (EnumTamilMonth e : values()) {
            if (e.month == day)
                return e;
        }

        return one;
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
