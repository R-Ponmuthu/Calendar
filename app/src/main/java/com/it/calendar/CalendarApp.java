package com.it.calendar;

import androidx.multidex.MultiDexApplication;

import com.google.android.gms.ads.MobileAds;

import com.google.android.gms.ads.MobileAds;

public class CalendarApp extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(this, getString(R.string.admob_app_id));

        //Thread.setDefaultUncaughtExceptionHandler((thread, ex) -> FirebaseCrash.report(ex));
    }
}
