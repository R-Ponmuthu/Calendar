package com.it.calendar.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.it.calendar.BuildConfig;
import com.it.calendar.R;
import com.it.calendar.util.SharedPreference;
import com.it.calendar.group.GroupItemDecoration;
import com.it.calendar.meizu_calendarview.EnglishWeekBar;
import com.it.calendar.meizu_calendarview.MeiZuMonthView;
import com.it.calendar.meizu_calendarview.MeizuWeekView;
import com.it.calendar.model.Article;
import com.it.calendar.model.CalendarModule;
import com.it.calendar.model.MainTable;
import com.it.calendar.model.NotificationModule;
import com.it.calendar.model.Virathaday;
import com.it.calendar.notification.AlarmReceiver;
import com.it.calendar.notification.LocalData;
import com.it.calendar.notification.NotificationScheduler;
import com.it.calendar.ui.view.BaseActivity;
import com.it.calendar.util.Utils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.exceptions.RealmMigrationNeededException;

import static android.content.ContentValues.TAG;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, CalendarView.OnCalendarSelectListener,
        CalendarView.OnYearChangeListener, View.OnClickListener {

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.adView)
    AdView adView;
    @BindView(R.id.adLayout)
    LinearLayout adLayout;
    private TextView mTextCurDay;
    private TextView mTextCurMonth;
    private TextView mTextCurYear;
    private TextView mTextCurDate;
    private CalendarView mCalendarView;
    private CalendarLayout mCalendarLayout;
    private RecyclerView mRecyclerView;
    private int mYear;
    private Realm realm;
    private SharedPreference sharedPreference = new SharedPreference();
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private boolean displayAds;
    private String realmKey;
    private int year = 0;
    private FirebaseAnalytics firebaseAnalytics;

    //Original source: https://stackoverflow.com/a/9855338/1389357
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        ButterKnife.bind(this);
        setStatusBarDarkMode();

        firebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        bundle.putString("Screen", "MainActivity");

        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        firebaseAnalytics.setUserProperty("Screen", "MainActivity");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        mTextCurDay = findViewById(R.id.tv_cur_day);
        mTextCurMonth = findViewById(R.id.tv_cur_month);
        mTextCurYear = findViewById(R.id.tv_cur_year);
        mTextCurDate = findViewById(R.id.tv_current_date);
        mCalendarView = findViewById(R.id.calendarView);
        mRecyclerView = findViewById(R.id.recyclerView);

        if (sharedPreference.getInt(MainActivity.this, "year") != 0) {
            int year = sharedPreference.getInt(MainActivity.this, "year");
            if (year != 0)
                mCalendarView.setRange(2018, 1, 1, year, 12, 31);
        }

        mCalendarView.setWeekStarWithSun();
        mCalendarView.setWeekView(MeizuWeekView.class);
        mCalendarView.setMonthView(MeiZuMonthView.class);
        mCalendarView.setWeekBar(EnglishWeekBar.class);
        mCalendarView.setWeeColor(Color.WHITE, getResources().getColor(R.color.grey_d5));

        mTextCurMonth.setOnClickListener(v -> {
            if (!mCalendarLayout.isExpand()) {
                mCalendarLayout.expand();
                return;
            }
            mCalendarView.showYearSelectLayout(mYear);
        });

        findViewById(R.id.fl_current).setOnClickListener(v -> mCalendarView.scrollToCurrent());
        mCalendarLayout = findViewById(R.id.calendarLayout);
        mCalendarView.setOnCalendarSelectListener(this);
        mCalendarView.setOnYearChangeListener(this);
        mYear = mCalendarView.getCurYear();

        int year = mCalendarView.getCurYear();
        int month = mCalendarView.getCurMonth();
        int day = mCalendarView.getCurDay();

        //mTextCurDate.setText("" + day + "." + month + "." + year);
        mTextCurDate.setText(String.valueOf(mCalendarView.getCurDay()));
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initData() {

        Realm.init(getApplicationContext());
        loadData();

        if (Utils.isOnline(MainActivity.this))
            loadAds();
        else
            adLayout.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //mCalendarView.scrollToCurrent();
        fireBaseConfig();

        sendFcmRegistrationToken();
    }

    public void fireBaseConfig() {

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .setMinimumFetchIntervalInSeconds(3600)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);

        //fetchData();
    }


    private void fetchData() {

        // [START fetch_config_with_callback]
        mFirebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        boolean updated = task.getResult();
                        Log.d(TAG, "Config params updated: " + updated);

                        displayAds = mFirebaseRemoteConfig.getBoolean("displayAds");
                        realmKey = mFirebaseRemoteConfig.getString("realmKey");
                        year = Integer.parseInt(mFirebaseRemoteConfig.getString("year"));

                        sharedPreference.putBoolean(MainActivity.this, "displayAds", displayAds);
                        sharedPreference.putString(MainActivity.this, "realmKey", realmKey);
                        sharedPreference.putInt(MainActivity.this, "year", year);

                    } else {
                        Log.e("Fetch Fail", "Failed");
                    }

                    loadData();

                    if (sharedPreference.getBoolean(MainActivity.this, "displayAds"))
                        if (Utils.isOnline(MainActivity.this))
                            loadAds();
                        else
                            adLayout.setVisibility(View.GONE);
                    else
                        adLayout.setVisibility(View.GONE);

                    if (year != 0)
                        mCalendarView.setRange(2018, 1, 1, year, 12, 31);
                });
    }


    private void loadData() {

        Realm.init(MainActivity.this);
        RealmConfiguration myConfig = new RealmConfiguration.Builder()
                .name("notification.realm")
                .modules(new NotificationModule())
                .build();

        Realm notiRealm = Realm.getInstance(myConfig);

        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .assetFile("data/calendar.realm")
                .name("calendar.realm")
                .modules(new CalendarModule())
                .schemaVersion(9)
                .build();
        try {
            Realm.setDefaultConfiguration(realmConfig);
            realm = Realm.getDefaultInstance();
        } catch (Exception e) {
            try {
                realm = Realm.getInstance(realmConfig);
            } catch (RealmMigrationNeededException r) {
                Realm.deleteRealm(realmConfig);
                realm = Realm.getInstance(realmConfig);
            }
        }

        /*Realm.init(MainActivity.this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .assetFile("data/default.realm")
                .name("calendar.realm")
                .schemaVersion(9)
                .build();

        Realm.setDefaultConfiguration(realmConfig);
        realm = Realm.getDefaultInstance();*/

        getData(mCalendarView.getCurDay(), mCalendarView.getCurMonth(), mCalendarView.getCurYear());

        RealmResults<MainTable> mainTables = realm.where(MainTable.class)
                .findAll();

        Map<String, Calendar> map = new HashMap<>();
        for (MainTable mainTable : mainTables) {
            String date = mainTable.getDate();
            Virathaday virathaDay = realm.where(Virathaday.class)
                    .equalTo("date", date)
                    .and()
                    .in("viratham", new String[]{"பௌர்ணமி", "அமாவாசை", "சுபமுகூர்த்தம்"})
                    .findFirst();

            if (virathaDay != null) {

                int color;
                String text;
                if (virathaDay.getViratham().equals("பௌர்ணமி")) {
                    color = Color.RED;
                    text = "பௌ";
                } else if (virathaDay.getViratham().equals("அமாவாசை")) {
                    color = Color.BLACK;
                    text = "அ";
                } else {
                    color = Color.MAGENTA;
                    text = "சுமு";
                }

                String[] dates = virathaDay.getDate().split("/");
                map.put(getSchemeCalendar(Integer.parseInt(dates[2]), Integer.parseInt(dates[1]), Integer.parseInt(dates[0]), color, text).toString(),
                        getSchemeCalendar(Integer.parseInt(dates[2]), Integer.parseInt(dates[1]), Integer.parseInt(dates[0]), color, text));
            }
        }
        //This method does not affect traversal performance on a huge amount of data. It is recommended to use
        mCalendarView.setSchemeDate(map);

        setNotification();
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

    private void setNotification() {

        if (!sharedPreference.getBoolean(MainActivity.this, "NotificationPref")) {

            LocalData localData = new LocalData(this);
            NotificationScheduler.setReminder(MainActivity.this, AlarmReceiver.class, localData.get_hour(), localData.get_min());

            sharedPreference.putBoolean(MainActivity.this, "NotificationPref", true);
        }
    }

    /*public Realm getInstance() {
        RealmConfiguration newConfig = new RealmConfiguration.Builder()
                .name("encrypted.realm")
                .encryptionKey(hexStringToByteArray("A352D1F5C8AA6D870EB32E690D574DD788EB4BB27F36E8A3902E71F9A92DEAD810C06B3D78EEE247066AE050D43CEA2F0A4D1C6BC671D778A966E412CDA9269B"))
                .build();

        // If new file exist, assume it has already been migrated
        File newRealmFile = new File(newConfig.getPath());
        if (newRealmFile.exists()) {
            return Realm.getInstance(newConfig);
        } else {
            // Migrate old Realm and delete old
            RealmConfiguration old = new RealmConfiguration.Builder().build();
            Realm realm = Realm.getInstance(old);
            realm.writeEncryptedCopyTo(newRealmFile, hexStringToByteArray("A352D1F5C8AA6D870EB32E690D574DD788EB4BB27F36E8A3902E71F9A92DEAD810C06B3D78EEE247066AE050D43CEA2F0A4D1C6BC671D778A966E412CDA9269B"));
            realm.close();
            Realm.deleteRealm(old);
            return Realm.getInstance(newConfig);
        }
    }*/

    @Override
    public void onClick(View v) {

    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//If you mark the color separately, you will use this color.
        calendar.setScheme(text);
        calendar.addScheme(new Calendar.Scheme());
        calendar.addScheme(0xFF000000, "day");
        calendar.addScheme(0xFF008800, "false");
        return calendar;
    }


    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        //mTextCurDate.setText("" + calendar.getDay() + "." + calendar.getMonth() + "." + calendar.getYear());
        mYear = calendar.getYear();
        getData(calendar.getDay(), calendar.getMonth(), calendar.getYear());
    }

    @Override
    public void onYearChange(int year) {
        //mTextCurDate.setText(String.valueOf(year));
    }

    @SuppressLint("SetTextI18n")
    public void getData(int day, int month, int year) {

        MainTable mainTbl = realm.where(MainTable.class)
                .equalTo("date", day + "/" + month + "/" + year)
                .findFirst();

        if (mainTbl != null) {
            mTextCurDay.setText("" + mainTbl.getWeekday());
            mTextCurMonth.setText("" + mainTbl.getMonth());
            mTextCurYear.setText("" + mainTbl.getYear());
            mTextCurDate.setText("" + mainTbl.getDay());
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new GroupItemDecoration<String, Article>());
        mRecyclerView.setAdapter(new CalendarAdapter(this, mainTbl));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Menu menu = navigationView.getMenu();

        if (id == R.id.govt_leave) {
            startDrawerActivity("gov_holiday", String.valueOf(menu.findItem(R.id.govt_leave).getTitle()));
            sendFirebaseAnalytics(String.valueOf(menu.findItem(R.id.govt_leave).getTitle()));
        } else if (id == R.id.hindu_fes) {
            startDrawerActivity("hindu_fes", String.valueOf(menu.findItem(R.id.hindu_fes).getTitle()));
            sendFirebaseAnalytics(String.valueOf(menu.findItem(R.id.hindu_fes).getTitle()));
        } else if (id == R.id.muslim_fes) {
            startDrawerActivity("muslim_fes", String.valueOf(menu.findItem(R.id.muslim_fes).getTitle()));
            sendFirebaseAnalytics(String.valueOf(menu.findItem(R.id.muslim_fes).getTitle()));
        } else if (id == R.id.chris_fes) {
            startDrawerActivity("chirs_fes", String.valueOf(menu.findItem(R.id.chris_fes).getTitle()));
            sendFirebaseAnalytics(String.valueOf(menu.findItem(R.id.chris_fes).getTitle()));
        } else if (id == R.id.muhurtha_days) {
            startDrawerActivity("muhurtha_days", String.valueOf(menu.findItem(R.id.muhurtha_days).getTitle()));
            sendFirebaseAnalytics(String.valueOf(menu.findItem(R.id.muhurtha_days).getTitle()));
        } else if (id == R.id.viradha_days) {
            startDrawerActivity("viradha_days", String.valueOf(menu.findItem(R.id.viradha_days).getTitle()));
            sendFirebaseAnalytics(String.valueOf(menu.findItem(R.id.viradha_days).getTitle()));
        } else if (id == R.id.vasthu_days) {
            startDrawerActivity("vasthu_days", String.valueOf(menu.findItem(R.id.vasthu_days).getTitle()));
            sendFirebaseAnalytics(String.valueOf(menu.findItem(R.id.viradha_days).getTitle()));
        } else if (id == R.id.raagu) {
            startDrawerActivity("raagu", String.valueOf(menu.findItem(R.id.raagu).getTitle()));
            sendFirebaseAnalytics(String.valueOf(menu.findItem(R.id.raagu).getTitle()));
        } else if (id == R.id.gowri_panjangam) {
            startDrawerActivity("gowri_panchanagam", String.valueOf(menu.findItem(R.id.gowri_panjangam).getTitle()));
            sendFirebaseAnalytics(String.valueOf(menu.findItem(R.id.gowri_panjangam).getTitle()));
        } else if (id == R.id.suba_horai) {
            startDrawerActivity("suba_horai", String.valueOf(menu.findItem(R.id.suba_horai).getTitle()));
            sendFirebaseAnalytics(String.valueOf(menu.findItem(R.id.suba_horai).getTitle()));
        } else if (id == R.id.astami) {
            startDrawerActivity(String.valueOf(menu.findItem(R.id.astami).getTitle()), String.valueOf(menu.findItem(R.id.astami).getTitle()));
            sendFirebaseAnalytics(String.valueOf(menu.findItem(R.id.astami).getTitle()));
        } else if (id == R.id.navami) {
            startDrawerActivity(String.valueOf(menu.findItem(R.id.navami).getTitle()), String.valueOf(menu.findItem(R.id.navami).getTitle()));
            sendFirebaseAnalytics(String.valueOf(menu.findItem(R.id.navami).getTitle()));
        } else if (id == R.id.kari_naal) {
            startDrawerActivity(String.valueOf(menu.findItem(R.id.kari_naal).getTitle()), String.valueOf(menu.findItem(R.id.kari_naal).getTitle()));
            sendFirebaseAnalytics(String.valueOf(menu.findItem(R.id.kari_naal).getTitle()));
        } else if (id == R.id.privacy_policy) {
            startActivity(new Intent(MainActivity.this, PrivacyPolicyActivity.class));
        } else if (id == R.id.settings) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        } else if (id == R.id.porutham) {
            startDrawerActivity(String.valueOf(menu.findItem(R.id.porutham).getTitle()), String.valueOf(menu.findItem(R.id.porutham).getTitle()));
            sendFirebaseAnalytics(String.valueOf(menu.findItem(R.id.porutham).getTitle()));
        } else if (id == R.id.notifications) {
            startActivity(new Intent(MainActivity.this, NotificationActivity.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void startDrawerActivity(String flag, String title) {
        startActivity(new Intent(this, DrawerActivity.class)
                .putExtra("QueryFlag", flag)
                .putExtra("curYear", mCalendarView.getCurYear())
                .putExtra("title", title));
    }


    private void sendFcmRegistrationToken() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.w("getInstanceId failed", task.getException());
                return;
            }
            if (task.getResult() != null) {
                String token = task.getResult().getToken();
                System.out.println("FCM Regestration Token = " + token);
            }
        });

        subscribeToMessagingService();
    }

    public void subscribeToMessagingService() {

        FirebaseMessaging.getInstance().subscribeToTopic("tamilCalendar");
    }


    public void sendFirebaseAnalytics(String str) {

        Bundle bundle = new Bundle();
        bundle.putString("Screen", str);

        firebaseAnalytics.logEvent(str, bundle);
        firebaseAnalytics.setUserProperty("Screen", str);
        firebaseAnalytics.setCurrentScreen(MainActivity.this, str, null);
    }
}
