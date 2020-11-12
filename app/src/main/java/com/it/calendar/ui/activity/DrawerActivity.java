package com.it.calendar.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.it.calendar.R;
import com.it.calendar.ui.fragments.CalendarFragment;
import com.it.calendar.ui.fragments.PoruthamFragment;
import com.it.calendar.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DrawerActivity extends AppCompatActivity {

    String queryFlag;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.adView)
    AdView adView;
    @BindView(R.id.adLayout)
    LinearLayout adLayout;
    private int curYear;
    private FirebaseAnalytics firebaseAnalytics;
    private Fragment fragment;

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
        } else {
            fragment = CalendarFragment.newInstance(queryFlag, curYear);
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();

        if (Utils.isOnline(DrawerActivity.this))
            loadAds();
        else
            adLayout.setVisibility(View.GONE);
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
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                adLayout.setVisibility(View.GONE);
                Log.e("Error", String.valueOf(errorCode));
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
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
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