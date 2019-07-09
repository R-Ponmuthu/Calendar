package com.it.calendar.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.it.calendar.R;
import com.it.calendar.model.MainTable;
import com.it.calendar.model.VirathaDay;
import com.it.calendar.ui.CalendarActivity1;
import com.it.calendar.ui.CalendarActivity2;
import com.it.calendar.util.SharedPreference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;

public class MyIntentService extends JobIntentService {

    private static final int NOTIFICATION_ID = 3;
    private SharedPreference sharedPreference = new SharedPreference();

    public MyIntentService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground(getApplicationContext());
        else
            startForeground(1, new Notification());
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        String dateFormat = "d/M/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);
        Calendar calendar = Calendar.getInstance();

        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("தமிழ் நாட்காட்டி");
        builder.setContentText(simpleDateFormat.format(calendar.getTime()) + " இன்றைய நாளுக்கான விவரங்கள் அறிய");
        builder.setSmallIcon(R.drawable.ic_calendar_noti);
        Intent notifyIntent;
        if (sharedPreference.getInt(getApplicationContext(), "Theme") == 0)
            notifyIntent = new Intent(this, CalendarActivity1.class);
        else
            notifyIntent = new Intent(this, CalendarActivity2.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        Notification notificationCompat = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(NOTIFICATION_ID, notificationCompat);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground(Context context) {

        String dateFormat = "d/M/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);
        Calendar calendar = Calendar.getInstance();

        Log.e("Date", simpleDateFormat.format(calendar.getTime()));

        Realm.init(context);
        Realm realm = Realm.getDefaultInstance();

        MainTable mainTable = realm.where(MainTable.class)
                .equalTo("date", simpleDateFormat.format(calendar.getTime()))
                .findFirst();

        RealmResults<VirathaDay> virathaDays = realm.where(VirathaDay.class)
                .equalTo("date", simpleDateFormat.format(calendar.getTime()))
                .findAll();

        StringBuilder stringBuilder = new StringBuilder();
        if (!mainTable.getGov_holiday().equals("-"))
            stringBuilder.append(mainTable.getGov_holiday()).append(",");
        if (!mainTable.getHindu_fes().equals("-"))
            stringBuilder.append(mainTable.getHindu_fes()).append(',');
        if (!mainTable.getChirs_fes().equals("-"))
            stringBuilder.append(mainTable.getChirs_fes()).append(",");
        if (!mainTable.getMuslim_fes().equals("-"))
            stringBuilder.append(mainTable.getMuslim_fes()).append(",");

        String NOTIFICATION_CHANNEL_ID = "com.it.calendar";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        Intent notifyIntent;
        if (sharedPreference.getInt(getApplicationContext(), "Theme") == 0)
            notifyIntent = new Intent(this, CalendarActivity1.class);
        else
            notifyIntent = new Intent(this, CalendarActivity2.class);
        notifyIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
        //RemoteViews remoteViewsSmall = new RemoteViews(context.getPackageName(), R.layout.small_notification_layout);

        //remoteViewsTitle(remoteViewsSmall, mainTable, virathaDays);
        //remoteViewsImage(remoteViews, stringBuilder, mainTable, virathaDays);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder
                .setSmallIcon(R.drawable.ic_calendar_noti)
                .setSound(alarmSound)
                .setContentTitle(simpleDateFormat.format(calendar.getTime()) + " இன்றைய நாளுக்கான விவரங்கள் அறிய")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(2, notification);
    }

    private static void remoteViewsImage(RemoteViews remoteViews, StringBuilder stringBuilder, MainTable mainTable, RealmResults<VirathaDay> virathaDays) {

        remoteViews.setTextViewText(R.id.nallaNeram, "ந.நே.கா: " + mainTable.getNallanerem_m() + "     ந.நே.மா: " + mainTable.getNallanerem_e());
        remoteViews.setTextViewText(R.id.festivals, "" + stringBuilder.toString());
        if (virathaDays.get(0).getViratham().contains("சுபமுகூர்த்தம்"))
            remoteViews.setViewVisibility(R.id.muhurtham, View.VISIBLE);
        if (virathaDays.get(0).getViratham().contains("அமாவாசை"))
            remoteViews.setViewVisibility(R.id.amavasai, View.VISIBLE);
        if (virathaDays.get(0).getViratham().contains("பௌர்ணமி"))
            remoteViews.setViewVisibility(R.id.pournami, View.VISIBLE);

        StringBuilder stringBuilder1 = new StringBuilder();
        for (VirathaDay virathaDay : virathaDays)
            stringBuilder1.append(virathaDay.getViratham()).append(",");

        if (stringBuilder1.length() > 0)
            remoteViews.setTextViewText(R.id.viradham, stringBuilder1.deleteCharAt(stringBuilder1.length() - 1).toString());
        else
            remoteViews.removeAllViews(R.id.viradham);
    }

    private static void remoteViewsTitle(RemoteViews remoteViewsSmall, MainTable mainTable, RealmResults<VirathaDay> virathaDay) {

//        remoteViews.setImageViewResource(R.id.logo, R.drawable.ic_logo);
//        remoteViews.setTextViewText(R.id.monthYear, "" + mainTable.getMonth() + "  " + mainTable.getYear());
//        remoteViews.setTextViewText(R.id.date, "" + mainTable.getDay());
//        remoteViews.setTextViewText(R.id.day, "" + mainTable.getWeekday());
//        remoteViews.setTextViewText(R.id.tamilDate, "" + mainTable.getTam_day());
//        remoteViews.setTextViewText(R.id.tamilMonth, "" + mainTable.getTam_month());
//        remoteViews.setTextViewText(R.id.tamilYear, "" + mainTable.getTam_year() + " வருடம்");

        remoteViewsSmall.setImageViewResource(R.id.logo, R.drawable.ic_logo);
        remoteViewsSmall.setTextViewText(R.id.monthYear, "" + mainTable.getMonth() + "  " + mainTable.getYear());
        remoteViewsSmall.setTextViewText(R.id.date, "" + mainTable.getDay());
        remoteViewsSmall.setTextViewText(R.id.day, "" + mainTable.getWeekday());
        remoteViewsSmall.setTextViewText(R.id.tamilDate, "" + mainTable.getTam_day());
        remoteViewsSmall.setTextViewText(R.id.tamilMonth, "" + mainTable.getTam_month());
        remoteViewsSmall.setTextViewText(R.id.tamilYear, "" + mainTable.getTam_year() + " வருடம்");

        /*remoteViewsSmall.setImageViewResource(R.id.logo, R.drawable.ic_logo);
        remoteViewsSmall.setTextViewText(R.id.current_date, "" + mainTable.getDay() + " - " + mainTable.getMonth() + " - " + mainTable.getYear() + " - " + mainTable.getWeekday());
        remoteViewsSmall.setTextViewText(R.id.tamil_date, "" + mainTable.getTam_day() + " - " + mainTable.getTam_month() + " - " + mainTable.getTam_year());*/
    }
}