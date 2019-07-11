package com.it.calendar.ui;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.it.calendar.R;
import com.it.calendar.util.Utils;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoreCalendarMenu extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.adView)
    AdView adView;
    @BindView(R.id.adLayout)
    LinearLayout adLayout;

    private int year;

    private String[] nameList = {"அரசினர் விடுமுறை நாட்கள்", "இந்துக்கள் பண்டிகைகள்", "முஸ்லீம் பண்டிகைகள்", "கிறிஸ்துவ பண்டிகைகள்", "சுப முகூர்த்த தினங்கள்",
            "திருமண பொருத்தம்", "முக்கிய விரத தினங்கள்", "வாஸ்து செய்யும் நாட்கள் நேரங்கள்", "ராகு, குளிகை, எமகண்டம்", "வருட கௌரி பஞ்சாங்கம்", "சுப ஹோரைகள்",
            "அஷ்டமி", "நவமி", "கரிநாள்"};

    private Integer[] iconList = {R.drawable.ic_govt, R.drawable.ic_hindu_temple, R.drawable.ic_islam, R.drawable.ic_church, R.drawable.subamuhurtham, R.drawable.ic_marriage,
            R.drawable.fasting, R.drawable.vasthu, R.drawable.raagu, R.drawable.panchangam, R.drawable.suba_horai, R.drawable.ashtami, R.drawable.navami, R.drawable.karinal};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_calendar_menu);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("மேலும்");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        if (getIntent().getExtras() != null)
            year = getIntent().getExtras().getInt("Year");

        recyclerView.setLayoutManager(new GridLayoutManager(MoreCalendarMenu.this, 2));
        recyclerView.setAdapter(new CalendarAdapter3(MoreCalendarMenu.this, year, Arrays.asList(nameList), iconList));

        if (Utils.isOnline(MoreCalendarMenu.this))
            loadAds();
        else
            adLayout.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
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
}
