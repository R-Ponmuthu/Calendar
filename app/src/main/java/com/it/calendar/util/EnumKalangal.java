package com.it.calendar.util;

public enum EnumKalangal {

    K0("0", ""),
    K1("1", "கிழக்கு"),
    K2("2", "மேற்கு"),
    K3("3", "தெற்கு"),
    K4("4", "வடக்கு");

    private String neram;
    private String text;

    EnumKalangal(String neram, String text) {
        this.neram = neram;
        this.text = text;
    }

    public static EnumKalangal getKalangal(String text) {
        for (EnumKalangal e : values()) {
            if (e.text.equals(text))
                return e;
        }

        return K0;
    }

    public static EnumKalangal getKalangalStr(String neram) {
        for (EnumKalangal e : values()) {
            if (e.neram.equals(neram))
                return e;
        }

        return K0;
    }

    public String getKalangal() {
        return neram;
    }

    public void setKalangal(String neram) {
        this.neram = neram;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
