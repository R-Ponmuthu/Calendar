package com.it.calendar.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.it.calendar.CalendarApp;
import com.it.calendar.R;
import com.it.calendar.calendarview.CalendarView;
import com.it.calendar.beans.GowriNeram;
import com.it.calendar.beans.Kalangal;
import com.it.calendar.beans.MainTable;
import com.it.calendar.beans.VirathaDay;
import com.it.calendar.notification.DNotificationReceiver;
import com.it.calendar.notification.SNotificationReceiver;
import com.it.calendar.util.EnumKalangal;
import com.it.calendar.util.EnumMonth;
import com.it.calendar.util.EnumNatchathiram;
import com.it.calendar.util.EnumParikaram;
import com.it.calendar.util.EnumTamilMonth;
import com.it.calendar.util.EnumVirathaDay;
import com.it.calendar.util.EnumWeekDay;
import com.it.calendar.utils.AdManager;
import com.it.calendar.util.Constants;
import com.it.calendar.utils.SharedPreference;
import com.it.calendar.util.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.it.calendar.weekcalendar.WeekCalendarView;
import com.it.core.db.TableHelper;

public final class CalendarActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String DATE_YEAR = "yyyy";
    private static final String DATE_TEMPLATE = "dd-MM-yyyy";
    private static final String MONTH_TEMPLATE = "MMMM yyyy";
    private static final int DAILY_REMINDER_REQUEST_CODE = 100;

    @BindView(R.id.adView)
    AdView adView;
    //@BindView(R.id.adLayout)
    //LinearLayout adLayout;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
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
    @BindView(R.id.calendarView)
    CalendarView mCalendarView;
    @BindView(R.id.weekCalendarView)
    WeekCalendarView mWeekCalendarView;
    @BindView(R.id.btmTxtDate)
    TextView btmTxtDate;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.view2)
    View view2;
    @BindView(R.id.view3)
    View view3;


    private final SharedPreference sharedPreference = new SharedPreference();
    private Utils utils;
    private AdManager adManager;
    private String[] mShortMonths;
    private boolean doubleBackToExitPressedOnce = false;

    private static final int REQ_CODE_VERSION_UPDATE = 1001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_activity);
        ButterKnife.bind(this);

        utils = Utils.getInstance(this);
        utils.setFirebaseAnalytics(this, "CalendarActivity");

        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        mShortMonths = getResources().getStringArray(R.array.month_tamil);

        mCalendarView.setOnMonthChangedListener((month, year) -> {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(mShortMonths[month]);
                getSupportActionBar().setSubtitle(Integer.toString(year));
            }
        });

        mWeekCalendarView.setOnMonthChangedListener((month, year) -> {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(mShortMonths[month]);
                getSupportActionBar().setSubtitle(Integer.toString(year));
            }
        });

        mCalendarView.setOnItemClickedListener((calendarObjects, previousDate, selectedDate) -> {

            SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
            setCalendarData(DATE_FORMAT.format(selectedDate.getTime()));
        });

        if (getSupportActionBar() != null) {
            int month = mCalendarView.getCurrentDate().get(Calendar.MONTH);
            int year = mCalendarView.getCurrentDate().get(Calendar.YEAR);
            getSupportActionBar().setTitle(mShortMonths[month]);
            getSupportActionBar().setSubtitle(Integer.toString(year));
        }

        setCalendarData(formatDate(DATE_TEMPLATE, new Date(System.currentTimeMillis())));

        adManager = AdManager.getInstance();
        adManager.createAd(CalendarActivity.this);

        subscribeToMessagingService();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            if (adManager != null)
                if (adManager.getAd().isLoaded())
                    adManager.getAd().show();
                else {
                    this.doubleBackToExitPressedOnce = true;
                    Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
                }
        }
    }

    public static String singleQuote(String str) {
        return (str != null ? "'" + str + "'" : null);
    }

    @SuppressLint("SetTextI18n")
    private void setCalendarData(String dt) {

//        Long dt = DateTimeHelper.getMillisFromDate(date);

        TableHelper<MainTable> mainTh = CalendarApp.getTable(this, MainTable.class);
        MainTable mainTable = mainTh.getItem(mainTh.getReadableDatabase(), "date = ?", new String[]{dt}, null);

        if (mainTable != null) {

            dateTxt.setText("" + mainTable.getDay());
            day.setText("" + EnumWeekDay.getWeekDay(mainTable.getWeekday()).getText());
            monthYear.setText(EnumTamilMonth.getTamilMonth(mainTable.getTam_month()).getText() + "  " + mainTable.getYear());

            btmTxtDate.setText(mainTable.getDay() + "  " + EnumMonth.getMonthStr(mainTable.getMonth()).getText() + "  "
                    + mainTable.getYear());

            quote.setText(mainTable.getQuote());

            tamilDate.setText("" + mainTable.getTam_day());
            tamilMonth.setText(mainTable.getTam_month());
            tamilYear.setText(mainTable.getTam_year() + " ஆண்டு");

            TableHelper<Kalangal> kalangalTable = CalendarApp.getTable(this, Kalangal.class);
            Kalangal kalangal = kalangalTable.getItem(kalangalTable.getReadableDatabase(), "year=? and weekday=?", new String[]{String.valueOf(mainTable.getYear()), EnumWeekDay.getWeekDay(mainTable.getWeekday()).getDay()}, null);

            TableHelper<GowriNeram> gowriNeramTable = CalendarApp.getTable(this, GowriNeram.class);
            GowriNeram gowriNeram = gowriNeramTable.getItem(gowriNeramTable.getReadableDatabase(), "date=?", new String[]{String.valueOf(dt)}, null);


            TableHelper<VirathaDay> virathaDayTable = CalendarApp.getTable(this, VirathaDay.class);
            List<VirathaDay> virathaDays = virathaDayTable.getList("date=?", new String[]{String.valueOf(dt)}, null, null);

            nallaNeramK.setText(mainTable.getNallanerem_m());
            nallaNeramM.setText(mainTable.getNallanerem_e());

            gowriNeramK.setText(gowriNeram.getGowri_m());
            gowriNeramM.setText(gowriNeram.getGowri_e());

            raagu1.setText(kalangal.getRagu());
            kulikai1.setText(kalangal.getKuligai());
            ema1.setText(kalangal.getEmakandam());

            soolam.setText("சூலம்: " + EnumKalangal.getKalangalStr(kalangal.getSoolam()).getText());
            parikaram.setText("பரிகாரம்: " + EnumParikaram.getParikaramStr(kalangal.getParikaram()).getText());

            sUdayam.setText("" + gowriNeram.getSooriya_r());
            String[] strs = mainTable.getChanthran().split(",");
            if (strs.length > 1)
                chandhiram.setText(EnumNatchathiram.getNatchathiramStr(strs[0]).getText() + ", " + EnumNatchathiram.getNatchathiramStr(strs[1].trim()).getText());
            else
                chandhiram.setText(EnumNatchathiram.getNatchathiramStr(strs[0]).getText());
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
                    stringBuilder.append(EnumVirathaDay.getVirathaDay(virathaDay.getViratham()).getText()).append("\n");

            if (stringBuilder.length() > 0) {
                festivals.setVisibility(View.VISIBLE);
                view2.setVisibility(View.VISIBLE);
                festivals.setText("" + stringBuilder.deleteCharAt(stringBuilder.length() - 1));
            } else {
                festivals.setVisibility(View.GONE);
                view2.setVisibility(View.GONE);
            }

            if (mainTable.getImportantday() != null) {
                String[] impDaysArr = mainTable.getImportantday().split(",");
                StringBuilder stringBuilder1 = new StringBuilder();
                for (String impDay : impDaysArr) {
                    stringBuilder1.append(impDay + "\n");
                }
                impDays.setVisibility(View.VISIBLE);
                view1.setVisibility(View.VISIBLE);
                impDays.setText("" + stringBuilder1);
            } else {
                impDays.setVisibility(View.GONE);
                view1.setVisibility(View.GONE);
            }

            rasi1.setText(Constants.mesam + " - " + mainTable.getMesam());
            rasi2.setText(Constants.risabam + " - " + mainTable.getRisibam());
            rasi3.setText(Constants.mithunam + " - " + mainTable.getMithunam());
            rasi4.setText(Constants.kadakam + " - " + mainTable.getKadakam());
            rasi5.setText(Constants.simam + " - " + mainTable.getSimmam());
            rasi6.setText(Constants.kanni + " - " + mainTable.getKanni());
            rasi7.setText(Constants.thulam + " - " + mainTable.getThulam());
            rasi8.setText(Constants.viruchigam + " - " + mainTable.getViruchakam());
            rasi9.setText(Constants.dhanushu + " - " + mainTable.getDhanusu());
            rasi10.setText(Constants.magaram + " - " + mainTable.getMakaram());
            rasi11.setText(Constants.kumbum + " - " + mainTable.getKumbam());
            rasi12.setText(Constants.meenam + " - " + mainTable.getMeenam());

            if (mainTable.getDay_type().equals(Constants.melNookuNaal)) {
                daySymbol.setImageResource(R.drawable.ic_up_arrow);
            } else if (mainTable.getDay_type().equals(Constants.keelNookuNaal)) {
                daySymbol.setImageResource(R.drawable.ic_down_arrow);
            } else {
                daySymbol.setImageResource(R.drawable.ic_double_arrow);
            }
        }

        setNotification();

        if (Utils.isOnline(CalendarActivity.this))
            loadAds();
        //else
        //adLayout.setVisibility(View.GONE);
    }

    @SuppressLint("NewApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.calendar_menu, menu);

        int date = mCalendarView.getCurrentDate().get(Calendar.DAY_OF_MONTH);

        MenuItem menuItem = menu.findItem(R.id.curDate);
        TextView tvDate = new TextView(this);
        tvDate.setBackground(getDrawable(R.drawable.ic_calendar));
        tvDate.setText("" + date);
        tvDate.setGravity(Gravity.CENTER);
        tvDate.setTypeface(Typeface.DEFAULT_BOLD);
        tvDate.setTextSize(14);
        tvDate.setPadding(0, 12, 0, 0);
        tvDate.setTextColor(Color.BLACK);

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCalendarView.setSelectedDate(Calendar.getInstance());
                setCalendarData(formatDate(DATE_TEMPLATE, new Date(System.currentTimeMillis())));
            }
        });

        menuItem.setActionView(tvDate);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.curMonth) {
            int year = mCalendarView.getCurrentDate().get(Calendar.YEAR);
            startActivity(new Intent(CalendarActivity.this, CurrentMonthActivity.class)
                    .putExtra("Title", toolbar.getTitle() + "-" + year));
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        Menu menu = navigationView.getMenu();

        if (id == R.id.govt_leave) {
            startDrawerActivity("gov_holiday", String.valueOf(menu.findItem(R.id.govt_leave).getTitle()));
            utils.setFirebaseAnalytics(this, String.valueOf(menu.findItem(R.id.govt_leave).getTitle()));
        } else if (id == R.id.hindu_fes) {
            startDrawerActivity("hindu_fes", String.valueOf(menu.findItem(R.id.hindu_fes).getTitle()));
            utils.setFirebaseAnalytics(this, String.valueOf(menu.findItem(R.id.hindu_fes).getTitle()));
        } else if (id == R.id.muslim_fes) {
            startDrawerActivity("muslim_fes", String.valueOf(menu.findItem(R.id.muslim_fes).getTitle()));
            utils.setFirebaseAnalytics(this, String.valueOf(menu.findItem(R.id.muslim_fes).getTitle()));
        } else if (id == R.id.chris_fes) {
            startDrawerActivity("chirs_fes", String.valueOf(menu.findItem(R.id.chris_fes).getTitle()));
            utils.setFirebaseAnalytics(this, String.valueOf(menu.findItem(R.id.chris_fes).getTitle()));
        } else if (id == R.id.muhurtha_days) {
            startDrawerActivity("muhurtha_days", String.valueOf(menu.findItem(R.id.muhurtha_days).getTitle()));
            utils.setFirebaseAnalytics(this, String.valueOf(menu.findItem(R.id.muhurtha_days).getTitle()));
        } else if (id == R.id.viradha_days) {
            startDrawerActivity("viradha_days", String.valueOf(menu.findItem(R.id.viradha_days).getTitle()));
            utils.setFirebaseAnalytics(this, String.valueOf(menu.findItem(R.id.viradha_days).getTitle()));
        } else if (id == R.id.vasthu_days) {
            startDrawerActivity("vasthu_days", String.valueOf(menu.findItem(R.id.vasthu_days).getTitle()));
            utils.setFirebaseAnalytics(this, String.valueOf(menu.findItem(R.id.vasthu_days).getTitle()));
        } else if (id == R.id.raagu) {
            startDrawerActivity("raagu", String.valueOf(menu.findItem(R.id.raagu).getTitle()));
            utils.setFirebaseAnalytics(this, String.valueOf(menu.findItem(R.id.raagu).getTitle()));
        } else if (id == R.id.gowri_panjangam) {
            startDrawerActivity("gowri_panchanagam", String.valueOf(menu.findItem(R.id.gowri_panjangam).getTitle()));
            utils.setFirebaseAnalytics(this, String.valueOf(menu.findItem(R.id.gowri_panjangam).getTitle()));
        } else if (id == R.id.suba_horai) {
            startDrawerActivity("suba_horai", String.valueOf(menu.findItem(R.id.suba_horai).getTitle()));
            utils.setFirebaseAnalytics(this, String.valueOf(menu.findItem(R.id.suba_horai).getTitle()));
        } else if (id == R.id.astami) {
            startDrawerActivity(String.valueOf(menu.findItem(R.id.astami).getTitle()), String.valueOf(menu.findItem(R.id.astami).getTitle()));
            utils.setFirebaseAnalytics(this, String.valueOf(menu.findItem(R.id.astami).getTitle()));
        } else if (id == R.id.navami) {
            startDrawerActivity(String.valueOf(menu.findItem(R.id.navami).getTitle()), String.valueOf(menu.findItem(R.id.navami).getTitle()));
            utils.setFirebaseAnalytics(this, String.valueOf(menu.findItem(R.id.navami).getTitle()));
        } else if (id == R.id.kari_naal) {
            startDrawerActivity(String.valueOf(menu.findItem(R.id.kari_naal).getTitle()), String.valueOf(menu.findItem(R.id.kari_naal).getTitle()));
            utils.setFirebaseAnalytics(this, String.valueOf(menu.findItem(R.id.kari_naal).getTitle()));
        } else if (id == R.id.privacy_policy) {
            startActivity(new Intent(CalendarActivity.this, PrivacyPolicyActivity.class));
        } else if (id == R.id.settings) {
            startActivity(new Intent(CalendarActivity.this, SettingsActivity.class));
        } else if (id == R.id.porutham) {
            startDrawerActivity(String.valueOf(menu.findItem(R.id.porutham).getTitle()), String.valueOf(menu.findItem(R.id.porutham).getTitle()));
            utils.setFirebaseAnalytics(this, String.valueOf(menu.findItem(R.id.porutham).getTitle()));
        } else if (id == R.id.notifications) {
            startActivity(new Intent(CalendarActivity.this, NotificationActivity.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void startDrawerActivity(String flag, String title) {

        if (adManager != null)
            if (adManager.getAd().isLoaded())
                adManager.getAd().show();

        startActivity(new Intent(this, DrawerActivity.class)
                .putExtra("QueryFlag", flag)
                .putExtra("curYear", Integer.parseInt(formatDate(DATE_YEAR, new Date(System.currentTimeMillis()))))
                .putExtra("title", title));
    }

    private void setNotification() {

        if (!sharedPreference.getBoolean(this, "Pref_DailyNotification8")) {
            createAlarmManager(8, DNotificationReceiver.class);
            sharedPreference.putBoolean(this, "Pref_DailyNotification8", true);
        }

        if (!sharedPreference.getBoolean(this, "Pref_DailyNotification6")) {
            createAlarmManager(6, SNotificationReceiver.class);
            sharedPreference.putBoolean(this, "Pref_DailyNotification6", true);
        }
    }

    private void createAlarmManager(int timeHr, Class<?> cls) {

        Calendar calendar = Calendar.getInstance();

        Calendar setCalendar = Calendar.getInstance();
        setCalendar.set(Calendar.HOUR_OF_DAY, timeHr);
        setCalendar.set(Calendar.MINUTE, 0);
        setCalendar.set(Calendar.SECOND, 0);
        setCalendar.set(Calendar.MILLISECOND, 0);

        if (setCalendar.before(calendar))
            setCalendar.add(Calendar.DATE, 1);

        Intent notifyIntent = new Intent(this, cls);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(CalendarActivity.this, DAILY_REMINDER_REQUEST_CODE, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getBroadcast(CalendarActivity.this, DAILY_REMINDER_REQUEST_CODE, notifyIntent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getBroadcast(CalendarActivity.this, DAILY_REMINDER_REQUEST_CODE, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        assert alarmManager != null;
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, setCalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private String formatDate(@NonNull String dateTemplate, @NonNull Date date) {
        return new SimpleDateFormat(dateTemplate, Locale.getDefault()).format(date);
    }

    private void loadAds() {

        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice("5894BCF12F1B676D1385EEBA09EBA26F")
                .build();
        adView.loadAd(adRequest);

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                //adLayout.setVisibility(View.GONE);
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

    public void subscribeToMessagingService() {

        FirebaseMessaging.getInstance().subscribeToTopic("tamilCalendar");
    }
}
