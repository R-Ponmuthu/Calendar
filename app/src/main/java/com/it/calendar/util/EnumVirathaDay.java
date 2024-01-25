package com.it.calendar.util;

public enum EnumVirathaDay {

    V0("0", ""),
    V1("1", "அமாவாசை"),
    V2("2", "கிரிவலம்"),
    V3("3", "பௌர்ணமி"),
    V4("4", "கிருத்திகை"),
    V5("5", "தேய்பிறை சஷ்டி"),
    V6("6", "ஷஷ்டி"),
    V7("7", "சங்கடஹர சதுர்த்தி"),
    V8("8", "அஷ்டமி"),
    V9("9", "நவமி"),
    V10("10", "தசமி"),
    V11("11", "சிவராத்திரி"),
    V12("12", "சந்திர தரிசனம்"),
    V13("13", "ஏகாதசி"),
    V14("14", "பிரதோஷம்"),
    V15("15", "சதுர்த்தி"),
    V16("16", "திருவாதிரை"),
    V17("17", "திருவோணம்"),
    V18("18", "சனிப்பிரதோஷம்"),
    V19("19", "மகா சிவராத்திரி"),
    V20("20", "ஆடி அமாவாசை"),
    V21("21", "சுபமுகூர்த்தம்"),
    V22("22", "கரிநாள்"),
    V23("23", "தேய்பிறை அஷ்டமி");

    private String day;
    private String text;

    EnumVirathaDay(String day, String text) {
        this.day = day;
        this.text = text;
    }

    public static EnumVirathaDay getVirathaDay(String day) {
        for (EnumVirathaDay e : values()) {
            if (e.day.equals(day))
                return e;
        }

        return V0;
    }

    public static EnumVirathaDay virathaDay(String text) {
        for (EnumVirathaDay e : values()) {
            if (e.text.equals(text))
                return e;
        }

        return V0;
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
