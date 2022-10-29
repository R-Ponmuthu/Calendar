package com.it.calendar.util;

import com.it.calendar.R;

public enum EnumPanchangam {

    K0("0", "",R.color.g_green),
    K1("1", "சோரம்", R.color.g_red),
    K2("2", "உத்தி", R.color.g_green),
    K3("3", "விஷம்", R.color.g_red),
    K4("4", "அமிர்த", R.color.g_green),
    K5("5", "ரோகம்", R.color.g_red),
    K6("6", "லாபம்", R.color.g_green),
    K7("7", "சுகம்", R.color.g_green),
    K8("8", "தனம்", R.color.g_green);

    private String neram;
    private String text;
    private int color;

    EnumPanchangam(String neram, String text, int color) {
        this.neram = neram;
        this.text = text;
        this.color = color;
    }

    public static EnumPanchangam getPanchangam(String text) {
        for (EnumPanchangam e : values()) {
            if (e.text.equals(text))
                return e;
        }

        return K0;
    }

    public static EnumPanchangam getPanchangamStr(String neram) {
        for (EnumPanchangam e : values()) {
            if (e.neram.equals(neram))
                return e;
        }

        return K0;
    }

    public String getPanchangam() {
        return neram;
    }

    public void setPanchangam(String neram) {
        this.neram = neram;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
