package com.it.calendar;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.google.android.gms.ads.MobileAds;
import com.it.calendar.realm.RealmHelper;


import io.realm.Realm;
import io.realm.RealmConfiguration;

public class CalendarApp extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        MobileAds.initialize(this, getString(R.string.admob_app_id));

        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name("Calendar.realm")
//                .deleteRealmIfMigrationNeeded()
                .modules(new CalendarModule())
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        RealmHelper.copyAssetFile(this, "Calendar.realm", realmConfiguration);
    }
}
