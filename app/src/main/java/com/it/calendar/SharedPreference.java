package com.it.calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.nio.charset.StandardCharsets;

public class SharedPreference {
    public static final String PREFS_NAME = "pref";
    SharedPreferences prefrence;
    Editor editor;

    public SharedPreference() {
        super();
    }

    public void putString(Context context, String text, String text1) {
        prefrence = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = prefrence.edit();

        editor.putString(text, text1);
        editor.commit();

    }

    public String getString(Context context, String PREFS_KEY) {
        String text;
        prefrence = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        text = prefrence.getString(PREFS_KEY, "");
        return text;
    }

    public void removeString(Context context, String PREFS_KEY) {

        prefrence = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = prefrence.edit();

        editor.remove(PREFS_KEY);
        editor.commit();
    }

    public void putInt(Context context, String text, int text1) {
        prefrence = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = prefrence.edit();

        editor.putInt(text, text1);
        editor.commit();
    }

    public int getInt(Context context, String PREFS_KEY) {
        int text;
        prefrence = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        text = prefrence.getInt(PREFS_KEY, 0);
        return text;
    }

    public void removeInt(Context context, String PREFS_KEY) {

        prefrence = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = prefrence.edit();

        editor.remove(PREFS_KEY);
        editor.commit();
    }

    public void putBoolean(Context context, String text, Boolean text1) {
        prefrence = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = prefrence.edit();

        editor.putBoolean(text, text1);
        editor.commit();
    }

    public Boolean getBoolean(Context context, String PREFS_KEY) {
        boolean text;
        prefrence = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        text = prefrence.getBoolean(PREFS_KEY, false);
        return text;
    }

    public void removeBoolean(Context context, String PREFS_KEY) {

        prefrence = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = prefrence.edit();

        editor.remove(PREFS_KEY);
        editor.commit();
    }

    public void clearSharedPreference(Context context) {
        prefrence = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = prefrence.edit();

        editor.clear();
        editor.commit();
    }

    public byte[] getBytes(Context context, String PREFS_KEY) {
        String bytes;
        prefrence = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        bytes = prefrence.getString(PREFS_KEY, null);
        if (bytes != null) {
            return bytes.getBytes(StandardCharsets.UTF_8);
        }
        return null;
    }

    public void putBytes(Context context, String PREFS_KEY, byte[] bytes) {
        prefrence = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = prefrence.edit();
        editor.putString(PREFS_KEY, new String(bytes, StandardCharsets.UTF_8));
        editor.commit();
    }
}
