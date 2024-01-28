package com.it.calendar.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.it.calendar.R;
import com.it.calendar.ui.fragments.CalendarFragment;
import com.it.calendar.ui.fragments.FestivalsFragment;
import com.it.calendar.ui.fragments.PoruthamFragment;
import com.it.calendar.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DrawerActivity extends AppCompatActivity {

    String queryFlag;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.adView)
    AdView adView;
    private int curYear;
    private FirebaseAnalytics firebaseAnalytics;
    private Fragment fragment;
    private InterstitialAd mInterstitialAd;
    private boolean adIsLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        ButterKnife.bind(this);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        if (getIntent().getExtras() != null) {
            queryFlag = getIntent().getExtras().getString("QueryFlag");
            curYear = getIntent().getExtras().getInt("curYear");
            getSupportActionBar().setTitle(getIntent().getExtras().getString("title"));

            firebaseAnalytics = FirebaseAnalytics.getInstance(this);

            Bundle bundle = new Bundle();
            bundle.putString("Screen", "DrawerActivity-" + getIntent().getExtras().getString("title"));
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            firebaseAnalytics.setUserProperty("Screen", "DrawerActivity-" + getIntent().getExtras().getString("title"));
        }

        if (queryFlag.equals("திருமண பொருத்தம்")) {
            fragment = PoruthamFragment.newInstance(queryFlag, curYear);
        }  else {
            fragment = CalendarFragment.newInstance(queryFlag, curYear);
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();

        createAd(this);

//        if (Utils.isOnline(DrawerActivity.this))
//            if (mInterstitialAd != null)
//                mInterstitialAd.show(this);
    }

    private void loadAds() {

        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice("5894BCF12F1B676D1385EEBA09EBA26F")
                .build();
        adView.loadAd(adRequest);

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {

            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
    }

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
                        mInterstitialAd.show(DrawerActivity.this);
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        mInterstitialAd = null;
                                        Log.d("TAG", "The ad was dismissed.");
                                        loadAds();
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
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}