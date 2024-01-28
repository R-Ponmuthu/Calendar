package com.it.calendar.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class AdManager {

    private static AdManager singleton;
    private InterstitialAd mInterstitialAd;
    private boolean adIsLoading;

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
//    public void createAd(Context context) {
//        interstitialAd = new InterstitialAd(context);
//        interstitialAd.setAdUnitId("ca-app-pub-2174081597275508/6114222649");
//        interstitialAd.loadAd(new AdRequest.Builder().addTestDevice("5894BCF12F1B676D1385EEBA09EBA26F").build());
//    }
    public void createAd(Context context) {
        // Request a new ad if one isn't already loaded.
        if (adIsLoading || mInterstitialAd != null) {
            return;
        }
        adIsLoading = true;
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(
                context,
                "ca-app-pub-2174081597275508/6114222649",
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                        adIsLoading = false;
                        Log.i("AdManager", "onAdLoaded");
                        Toast.makeText(context, "onAdLoaded()", Toast.LENGTH_SHORT).show();
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        mInterstitialAd = null;
                                        Log.d("TAG", "The ad was dismissed.");
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        mInterstitialAd = null;
                                        Log.d("TAG", "The ad failed to show.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        Log.d("TAG", "The ad was shown.");
                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.i("AdManager", loadAdError.getMessage());
                        mInterstitialAd = null;
                        adIsLoading = false;

                        String error =
                                String.format(
                                        java.util.Locale.US,
                                        "domain: %s, code: %d, message: %s",
                                        loadAdError.getDomain(),
                                        loadAdError.getCode(),
                                        loadAdError.getMessage());
                        Toast.makeText(context, "onAdFailedToLoad() with error: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /***
     * get an interstitial Ad
     * @return
     */
    public InterstitialAd getAd() {
        return mInterstitialAd;
    }
}
