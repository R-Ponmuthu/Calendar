package com.it.calendar.ui;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.internal.NavigationMenuView;
import com.google.android.material.navigation.NavigationView;
import com.it.calendar.R;
import com.it.calendar.detailed_calendarview.logic.presenter.MainPresenter;
import com.it.calendar.detailed_calendarview.logic.presenter_view.MainView;
import com.it.calendar.detailed_calendarview.view.CalendarView;
import com.it.calendar.model.CalendarModule;
import com.it.calendar.model.MainTable;
import com.it.calendar.model.VirathaDay;
import com.it.calendar.model.gowri_neram;
import com.it.calendar.model.kalangal;
import com.it.calendar.notification.MyReceiver;
import com.it.calendar.util.SharedPreference;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.exceptions.RealmMigrationNeededException;


public final class CalendarActivity2 extends AppCompatActivity implements MainView, NavigationView.OnNavigationItemSelectedListener {

    private static final String DATE_TEMPLATE = "d/M/yyyy";
    private static final String MONTH_TEMPLATE = "MMMM yyyy";

    private final MainPresenter presenter = new MainPresenter(this);

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.calendar_view)
    CalendarView calendarView;
    @BindView(R.id.monthYear)
    TextView monthYear;
    @BindView(R.id.date)
    TextView dateTxt;
    @BindView(R.id.day)
    TextView day;
    @BindView(R.id.tamilYear)
    TextView tamilYear;
    @BindView(R.id.tamilMonth)
    TextView tamilMonth;
    @BindView(R.id.tamilDate)
    TextView tamilDate;
    @BindView(R.id.day_symbol)
    ImageView daySymbol;
    @BindView(R.id.quote)
    TextView quote;
    @BindView(R.id.festivals)
    TextView festivals;
    @BindView(R.id.rasi1)
    TextView rasi1;
    @BindView(R.id.rasi2)
    TextView rasi2;
    @BindView(R.id.rasi3)
    TextView rasi3;
    @BindView(R.id.rasi4)
    TextView rasi4;
    @BindView(R.id.rasi5)
    TextView rasi5;
    @BindView(R.id.rasi6)
    TextView rasi6;
    @BindView(R.id.rasi7)
    TextView rasi7;
    @BindView(R.id.rasi8)
    TextView rasi8;
    @BindView(R.id.rasi9)
    TextView rasi9;
    @BindView(R.id.rasi10)
    TextView rasi10;
    @BindView(R.id.rasi11)
    TextView rasi11;
    @BindView(R.id.rasi12)
    TextView rasi12;
    @BindView(R.id.impDays)
    TextView impDays;
    @BindView(R.id.nallaNeram_K)
    TextView nallaNeramK;
    @BindView(R.id.nallaNeram_M)
    TextView nallaNeramM;
    @BindView(R.id.gowriNeram_K)
    TextView gowriNeramK;
    @BindView(R.id.gowriNeram_M)
    TextView gowriNeramM;
    @BindView(R.id.raagu1)
    TextView raagu1;
    @BindView(R.id.kulikai1)
    TextView kulikai1;
    @BindView(R.id.ema1)
    TextView ema1;
    @BindView(R.id.soolam)
    TextView soolam;
    @BindView(R.id.parikaram)
    TextView parikaram;
    @BindView(R.id.chandhiram)
    TextView chandhiram;
    @BindView(R.id.s_udayam)
    TextView sUdayam;
    @BindView(R.id.yokam)
    TextView yokam;
    @BindView(R.id.thithi)
    TextView thithi;
    @BindView(R.id.natchatiram)
    TextView natchatiram;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private Realm realm;
    private String[] nameList = {"அரசினர் விடுமுறை நாட்கள்", "இந்துக்கள் பண்டிகைகள்", "முஸ்லீம் பண்டிகைகள்", "கிறிஸ்துவ பண்டிகைகள்", "சுப முகூர்த்த தினங்கள்",
            "மேலும்"};
    private Integer[] iconList = {R.drawable.ic_govt, R.drawable.ic_hindu_temple, R.drawable.ic_islam, R.drawable.ic_church, R.drawable.subamuhurtham, R.drawable.ic_more};
    private SharedPreference sharedPreference = new SharedPreference();
    private static final int DAILY_REMINDER_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar2);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        presenter.addNavigationDrawer();
        presenter.addCalendarView();
        presenter.addTextView();
        presenter.animate();
    }

    private void loadData() {

        Realm.init(CalendarActivity2.this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .assetFile("data/calendar.realm")
                .name("default.realm")
                .encryptionKey(hexStringToByteArray(getResources().getString(R.string.ENCRYPTION_KEY)))
                .schemaVersion(9)
                .modules(new CalendarModule())
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
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.nav_notification:
                startActivity(new Intent(CalendarActivity2.this, NotificationActivity.class));
                break;
            case R.id.nav_settings:
                startActivity(new Intent(CalendarActivity2.this, SettingsActivity.class));
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void prepareTextView() {

        loadData();

        MainTable mainTable = realm.where(MainTable.class)
                .equalTo("date", formatDate(DATE_TEMPLATE, new Date(System.currentTimeMillis())))
                .findFirst();

        if (mainTable != null) {
            final ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle(mainTable.getTam_month() + " " + mainTable.getYear());
        }

        setCalendarData(formatDate(DATE_TEMPLATE, new Date(System.currentTimeMillis())));

        if (mainTable != null)
            setRecyclerViewData(mainTable.getYear().intValue());
    }

    private void setRecyclerViewData(int year) {

        recyclerView.setLayoutManager(new GridLayoutManager(CalendarActivity2.this, 3));
        recyclerView.setAdapter(new CalendarAdapter2(CalendarActivity2.this, year, Arrays.asList(nameList), iconList));
    }

    @SuppressLint("SetTextI18n")
    private void setCalendarData(String date) {

        MainTable mainTable = realm.where(MainTable.class)
                .equalTo("date", date)
                .findFirst();

        if (mainTable != null) {

            dateTxt.setText("" + mainTable.getDay());
            day.setText("" + mainTable.getWeekday());
            monthYear.setText(mainTable.getMonth() + "  " + mainTable.getYear());

            tamilDate.setText("" + mainTable.getDay());
            tamilMonth.setText(mainTable.getTam_month());
            tamilYear.setText(mainTable.getTam_year() + " வருடம்");

            kalangal kalangal = realm.where(kalangal.class)
                    .equalTo("year", mainTable.getYear())
                    .and()
                    .equalTo("weekday", mainTable.getWeekday())
                    .findFirst();

            gowri_neram gowriNeram = realm.where(gowri_neram.class)
                    .equalTo("date", mainTable.getDate())
                    .findFirst();

            RealmResults<VirathaDay> virathaDays = realm.where(VirathaDay.class)
                    .equalTo("date", mainTable.getDate())
                    .findAll();

            nallaNeramK.setText(mainTable.getNallanerem_m());
            nallaNeramM.setText(mainTable.getNallanerem_e());

            gowriNeramK.setText(gowriNeram.getGowri_m());
            gowriNeramM.setText(gowriNeram.getGowri_e());

            raagu1.setText(kalangal.getRagu());
            kulikai1.setText(kalangal.getKuligai());
            ema1.setText(kalangal.getEmakandam());

            soolam.setText("சூலம்: " + kalangal.getSoolam());
            parikaram.setText("பரிகாரம்: " + kalangal.getParikaram());

            sUdayam.setText("" + gowriNeram.getSooriya_r());
            chandhiram.setText(mainTable.getChanthran());
            yokam.setText(mainTable.getYokam());

            thithi.setText(mainTable.getThiti());
            natchatiram.setText(mainTable.getStar());

            StringBuilder stringBuilder = new StringBuilder();
            if (!mainTable.getGov_holiday().equals("-"))
                stringBuilder.append(mainTable.getGov_holiday()).append("\n");
            if (!mainTable.getHindu_fes().equals("-"))
                stringBuilder.append(mainTable.getHindu_fes()).append("\n");
            if (!mainTable.getChirs_fes().equals("-"))
                stringBuilder.append(mainTable.getChirs_fes()).append("\n");
            if (!mainTable.getMuslim_fes().equals("-"))
                stringBuilder.append(mainTable.getMuslim_fes()).append("\n");
            if (virathaDays.size() > 0)
                for (VirathaDay virathaDay : virathaDays)
                    stringBuilder.append(virathaDay.getViratham()).append("\n");

            if (stringBuilder.length() > 0)
                festivals.setText("" + stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString());
            else
                festivals.setVisibility(View.GONE);

            String[] impDaysArr = mainTable.getImportantday().split(",");
            StringBuilder stringBuilder1 = new StringBuilder();
            for (String impDay : impDaysArr) {
                stringBuilder1.append(impDay + "\n");
            }
            impDays.setText("" + stringBuilder1.toString());

            rasi1.setText("மேஷம் - " + mainTable.getMesam());
            rasi2.setText("ரிஷபம் - " + mainTable.getRisibam());
            rasi3.setText("மிதுனம் - " + mainTable.getMithunam());
            rasi4.setText("கடகம் - " + mainTable.getKadakam());
            rasi5.setText("சிம்மம் - " + mainTable.getSimmam());
            rasi6.setText("கன்னி - " + mainTable.getKanni());
            rasi7.setText("துலாம் - " + mainTable.getThulam());
            rasi8.setText("விருச்சிகம் - " + mainTable.getViruchakam());
            rasi9.setText("தனுசு - " + mainTable.getDhanusu());
            rasi10.setText("மகரம் - " + mainTable.getMakaram());
            rasi11.setText("கும்பம் - " + mainTable.getKumbam());
            rasi12.setText("மீனம் - " + mainTable.getMeenam());

            if (mainTable.getDay_type().equals("மேல் நோக்கு நாள்")) {
                daySymbol.setImageResource(R.drawable.ic_up_arrow);
            } else if (mainTable.getDay_type().equals("கீழ் நோக்கு நாள்")) {
                daySymbol.setImageResource(R.drawable.ic_down_arrow);
            } else {
                daySymbol.setImageResource(R.drawable.ic_double_arrow);
            }
        }

        setNotification();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.calendar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.theme) {
            sharedPreference.putInt(CalendarActivity2.this, "Theme", 0);
            startActivity(new Intent(CalendarActivity2.this, CalendarActivity1.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void prepareCalendarView() {
        Calendar disabledCal = Calendar.getInstance();
        disabledCal.set(Calendar.DATE, disabledCal.get(Calendar.DATE) - 1);

        calendarView.setFirstDayOfWeek(Calendar.SUNDAY)
                .setOnDateClickListener(this::onDateClick)
                .setOnMonthChangeListener(this::onMonthChange)
                .setOnDateLongClickListener(this::onDateLongClick)
                .setOnMonthTitleClickListener(this::onMonthTitleClick);

        if (calendarView.isMultiSelectDayEnabled()) {
            calendarView.setOnMultipleDaySelectedListener((month, dates) -> {
                //Do something with your current selection
            });
        }

        calendarView.update(Calendar.getInstance(Locale.getDefault()));
    }

    @Override
    public void prepareNavigationDrawer() {
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setItemIconTintList(null);
        NavigationMenuView navMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        navMenuView.addItemDecoration(new DividerItemDecoration(CalendarActivity2.this, DividerItemDecoration.VERTICAL));
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
    }

    @Override
    public void animateViews() {
        calendarView.shouldAnimateOnEnter(true);
    }

    private void onDateLongClick(@NonNull final Date date) {

    }

    private void setNotification() {

        if (!sharedPreference.getBoolean(CalendarActivity2.this, "DailyNotificationPref")) {

            java.util.Calendar calendar = java.util.Calendar.getInstance();

            java.util.Calendar setCalendar = java.util.Calendar.getInstance();
            setCalendar.set(java.util.Calendar.HOUR_OF_DAY, 8);
            setCalendar.set(java.util.Calendar.MINUTE, 0);
            setCalendar.set(java.util.Calendar.SECOND, 0);
            setCalendar.set(java.util.Calendar.MILLISECOND, 0);

            if (setCalendar.before(calendar))
                setCalendar.add(java.util.Calendar.DATE, 1);

            Intent notifyIntent = new Intent(this, MyReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(CalendarActivity2.this, DAILY_REMINDER_REQUEST_CODE, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            assert alarmManager != null;
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, setCalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

            sharedPreference.putBoolean(CalendarActivity2.this, "DailyNotificationPref", true);
        }
    }

    private void onDateClick(@NonNull final Date date) {
        //textView.setText(formatDate(DATE_TEMPLATE, date));

        setCalendarData(formatDate(DATE_TEMPLATE, date));
    }

    private void onMonthTitleClick(@NonNull final Date date) {
        //Do something after month selection
    }

    private void onMonthChange(@NonNull final Date date) {

        MainTable mainTable = realm.where(MainTable.class)
                .equalTo("date", formatDate(DATE_TEMPLATE, date))
                .findFirst();

        if (mainTable != null) {
            final ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle(mainTable.getTam_month() + " " + mainTable.getYear());
        }
    }

    private String formatDate(@NonNull String dateTemplate, @NonNull Date date) {
        return new SimpleDateFormat(dateTemplate, Locale.getDefault()).format(date);
    }
}
