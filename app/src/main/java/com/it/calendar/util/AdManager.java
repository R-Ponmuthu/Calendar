package com.it.calendar.utils;

import android.content.Context;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class AdManager {

    private static AdManager singleton;
    private InterstitialAd interstitialAd;

    public AdManager() {
    }

    /***
     * returns an instance of this class. if singleton is null create an instance
     * else return  the current instance
     * @return
     */
    public static AdManager getInstance() {
        if (singleton == null) {
            singleton = new AdManager();
        }

        return singleton;
    }

    /***
     * Create an interstitial ad
     * @param context
     */
    public void createAd(Context context) {
        interstitialAd = new InterstitialAd(context);
        interstitialAd.setAdUnitId("ca-app-pub-2174081597275508/6114222649");
        interstitialAd.loadAd(new AdRequest.Builder().addTestDevice("5894BCF12F1B676D1385EEBA09EBA26F").build());
    }

    /***
     * get an interstitial Ad
     * @return
     */
    public InterstitialAd getAd() {
        return interstitialAd;
    }
}
