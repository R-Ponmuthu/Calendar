package com.it.calendar.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabLayout;
import com.it.calendar.R;
import com.it.calendar.ui.adapter.ViewPagerAdapter;
import com.it.calendar.ui.fragments.FestivalsFragment;
import com.it.calendar.ui.fragments.MuhurthamFragment;
import com.it.calendar.ui.fragments.PanchangamFragment;
import com.it.calendar.ui.fragments.RaaguFragment;
import com.it.calendar.ui.fragments.VirathaFragment;
import com.it.calendar.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuViewActivity extends AppCompatActivity {

    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.adView)
    AdView adView;
    @BindView(R.id.adLayout)
    LinearLayout adLayout;

    String queryFlag;
    int curYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viratham);
        ButterKnife.bind(this);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (getIntent().getExtras() != null) {
            queryFlag = getIntent().getExtras().getString("QueryFlag");
            curYear = getIntent().getExtras().getInt("curYear");
            getSupportActionBar().setTitle(getIntent().getExtras().getString("title"));
        }

        viewPager = findViewById(R.id.viewpager);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        if (queryFlag.equals("hindu_fes") || queryFlag.equals("muslim_fes") || queryFlag.equals("chirs_fes") || queryFlag.equals("gov_holiday")) {
            viewPagerAdapter.add(FestivalsFragment.newInstance(queryFlag, "01", curYear), "ஜனவரி");
            viewPagerAdapter.add(FestivalsFragment.newInstance(queryFlag, "02", curYear), "பிப்ரவரி");
            viewPagerAdapter.add(FestivalsFragment.newInstance(queryFlag, "03", curYear), "மார்ச்");
            viewPagerAdapter.add(FestivalsFragment.newInstance(queryFlag, "04", curYear), "ஏப்ரல்");
            viewPagerAdapter.add(FestivalsFragment.newInstance(queryFlag, "05", curYear), "மே");
            viewPagerAdapter.add(FestivalsFragment.newInstance(queryFlag, "06", curYear), "ஜூன்");
            viewPagerAdapter.add(FestivalsFragment.newInstance(queryFlag, "07", curYear), "ஜூலை");
            viewPagerAdapter.add(FestivalsFragment.newInstance(queryFlag, "08", curYear), "ஆகஸ்ட்");
            viewPagerAdapter.add(FestivalsFragment.newInstance(queryFlag, "09", curYear), "செப்டம்பர்");
            viewPagerAdapter.add(FestivalsFragment.newInstance(queryFlag, "10", curYear), "அக்டோபர்");
            viewPagerAdapter.add(FestivalsFragment.newInstance(queryFlag, "11", curYear), "நவம்பர்");
            viewPagerAdapter.add(FestivalsFragment.newInstance(queryFlag, "12", curYear), "டிசம்பர்");
        } else if (queryFlag.equals("muhurtha_days")) {
            viewPagerAdapter.add(MuhurthamFragment.newInstance(queryFlag, "01", curYear), "ஜனவரி");
            viewPagerAdapter.add(MuhurthamFragment.newInstance(queryFlag, "02", curYear), "பிப்ரவரி");
            viewPagerAdapter.add(MuhurthamFragment.newInstance(queryFlag, "03", curYear), "மார்ச்");
            viewPagerAdapter.add(MuhurthamFragment.newInstance(queryFlag, "04", curYear), "ஏப்ரல்");
            viewPagerAdapter.add(MuhurthamFragment.newInstance(queryFlag, "05", curYear), "மே");
            viewPagerAdapter.add(MuhurthamFragment.newInstance(queryFlag, "06", curYear), "ஜூன்");
            viewPagerAdapter.add(MuhurthamFragment.newInstance(queryFlag, "07", curYear), "ஜூலை");
            viewPagerAdapter.add(MuhurthamFragment.newInstance(queryFlag, "08", curYear), "ஆகஸ்ட்");
            viewPagerAdapter.add(MuhurthamFragment.newInstance(queryFlag, "09", curYear), "செப்டம்பர்");
            viewPagerAdapter.add(MuhurthamFragment.newInstance(queryFlag, "10", curYear), "அக்டோபர்");
            viewPagerAdapter.add(MuhurthamFragment.newInstance(queryFlag, "11", curYear), "நவம்பர்");
            viewPagerAdapter.add(MuhurthamFragment.newInstance(queryFlag, "12", curYear), "டிசம்பர்");
        } else if (queryFlag.equals("raagu")) {

            viewPagerAdapter.add(RaaguFragment.newInstance(queryFlag, "1", curYear), "ஞாயிறு");
            viewPagerAdapter.add(RaaguFragment.newInstance(queryFlag, "2", curYear), "திங்கள்");
            viewPagerAdapter.add(RaaguFragment.newInstance(queryFlag, "3", curYear), "செவ்வாய்");
            viewPagerAdapter.add(RaaguFragment.newInstance(queryFlag, "4", curYear), "புதன்");
            viewPagerAdapter.add(RaaguFragment.newInstance(queryFlag, "5", curYear), "வியாழன்");
            viewPagerAdapter.add(RaaguFragment.newInstance(queryFlag, "6", curYear), "வெள்ளி");
            viewPagerAdapter.add(RaaguFragment.newInstance(queryFlag, "7", curYear), "சனி");
        } else if (queryFlag.equals("gowri_panchanagam")) {
            viewPagerAdapter.add(PanchangamFragment.newInstance(queryFlag, "1", curYear), "");
//            viewPagerAdapter.add(PanchangamFragment.newInstance(queryFlag, "2", curYear), "திங்கள்");
//            viewPagerAdapter.add(PanchangamFragment.newInstance(queryFlag, "3", curYear), "செவ்வாய்");
//            viewPagerAdapter.add(PanchangamFragment.newInstance(queryFlag, "4", curYear), "புதன்");
//            viewPagerAdapter.add(PanchangamFragment.newInstance(queryFlag, "5", curYear), "வியாழன்");
//            viewPagerAdapter.add(PanchangamFragment.newInstance(queryFlag, "6", curYear), "வெள்ளி");
//            viewPagerAdapter.add(PanchangamFragment.newInstance(queryFlag, "7", curYear), "சனி");
        } else {
            viewPagerAdapter.add(VirathaFragment.newInstance("1", "20"), "அமாவாசை");
            viewPagerAdapter.add(VirathaFragment.newInstance("3"), "பௌர்ணமி");
            viewPagerAdapter.add(VirathaFragment.newInstance("4"), "கிருத்திகை");
            viewPagerAdapter.add(VirathaFragment.newInstance("17"), "திருவோணம்");
            viewPagerAdapter.add(VirathaFragment.newInstance("13"), "ஏகாதசி");
            viewPagerAdapter.add(VirathaFragment.newInstance("6"), "சஷ்டி");
//        viewPagerAdapter.add(VirathaFragment.newInstance("5"), "தேய்பிறை சஷ்டி");
            viewPagerAdapter.add(VirathaFragment.newInstance("7", "25"), "சங்கடஹர சதுர்த்தி");
            viewPagerAdapter.add(VirathaFragment.newInstance("11", "19"), "சிவராத்திரி");
//        viewPagerAdapter.add(VirathaFragment.newInstance("19"), "மகா சிவராத்திரி");
            viewPagerAdapter.add(VirathaFragment.newInstance("14"), "பிரதோஷம்");
            viewPagerAdapter.add(VirathaFragment.newInstance("15"), "சதுர்த்தி");
        }
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        if (Utils.isOnline(MenuViewActivity.this))
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
