package com.it.calendar.util;

public enum EnumParikaram {

    P0("0", ""),
    P1("1", "வெல்லம்"),
    P2("2", "தயிர்"),
    P3("3", "பால்"),
    P4("4", "தைலம்");

    private String parikaram;
    private String text;

    EnumParikaram(String parikaram, String text) {
        this.parikaram = parikaram;
        this.text = text;
    }

    public static EnumParikaram getParikaram(String text) {
        for (EnumParikaram e : values()) {
            if (e.text.equals(text))
                return e;
        }

        return P0;
    }

    public static EnumParikaram getParikaramStr(String parikaram) {
        for (EnumParikaram e : values()) {
            if (e.parikaram.equals(parikaram))
                return e;
        }

        return P0;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
