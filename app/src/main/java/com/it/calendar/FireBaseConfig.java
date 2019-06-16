package com.it.calendar;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class FireBaseConfig {

    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private long cacheExpiration = 3600;
    private boolean displayAds;

    public FireBaseConfig() {

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(true)
                .build();

        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);

        //fetchData(context);
    }

    public boolean fetchData(Context context) {

        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener((Activity) context, task -> {
// If is successful, activated fetched
                    if (task.isSuccessful()) {
                        //mFirebaseRemoteConfig.activateFetched();
                        Log.e("Task Result", String.valueOf(task.getResult()));
                    } else {
                        Log.e("Failed", "Fail");
                    }

                    displayAds = mFirebaseRemoteConfig.getBoolean("displayAds");
                });

        Log.e("Ads", String.valueOf(displayAds));

        return displayAds;
    }

    public long getCacheExpiration() {
// If is developer mode, cache expiration set to 0, in order to test
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }
        return cacheExpiration;
    }
}
