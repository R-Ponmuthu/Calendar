package com.it.calendar.util;

public enum EnumNeram {

    N0("0", ""),
    N1("1", "பகல்"),
    N2("2", "இரவு");

    private String neram;
    private String text;

    EnumNeram(String neram, String text) {
        this.neram = neram;
        this.text = text;
    }

    public static EnumNeram getNeram(String text) {
        for (EnumNeram e : values()) {
            if (e.text.equals(text))
                return e;
        }

        return N0;
    }

    public static EnumNeram getNeramStr(String neram) {
        for (EnumNeram e : values()) {
            if (e.neram.equals(neram))
                return e;
        }

        return N0;
    }

    public String getNeram() {
        return neram;
    }

    public void setNeram(String neram) {
        this.neram = neram;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
