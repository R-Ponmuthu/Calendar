package com.it.calendar.util;

public enum EnumNatchathiram {

    N0("0", ""),
    N1("1", "அசுவினி"),
    N2("2", "பரணி"),
    N3("3", "கிருத்திை"),
    N4("4", "உரோகிணி"),
    N5("5", "மிருகசீரிடம்"),
    N6("6", "திருவாதிரை"),
    N7("7", "புனர்பூசம்"),
    N8("8", "பூசம்"),
    N9("9", "ஆயில்யம்"),
    N10("10", "மகம்"),
    N11("11", "பூரம்"),
    N12("12", "உத்திரம்"),
    N13("13", "அசுதம்"),
    N14("14", "சித்திரை"),
    N15("15", "சுவாதி"),
    N16("16", "விசாகம்"),
    N17("17", "அனுடம்"),
    N18("18", "கேட்டை"),
    N19("19", "மூலம்"),
    N20("20", "பூராடம்"),
    N21("21", "உத்திராடம்"),
    N22("22", "திருவோணம்"),
    N23("23", "அவிட்டம்"),
    N24("24", "சதயம்"),
    N25("25", "பூரட்டாதி"),
    N26("26", "உத்திரட்டாதி"),
    N27("27", "ரேவதி");

    private String natchathiram;
    private String text;

    EnumNatchathiram(String natchathiram, String text) {
        this.natchathiram = natchathiram;
        this.text = text;
    }

    public static EnumNatchathiram getNatchathiram(String text) {
        for (EnumNatchathiram e : values()) {
            if (e.text.equals(text))
                return e;
        }

        return N0;
    }

    public static EnumNatchathiram getNatchathiramStr(String natchathiram) {
        for (EnumNatchathiram e : values()) {
            if (e.natchathiram.equals(natchathiram))
                return e;
        }

        return N0;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
