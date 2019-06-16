package com.it.calendar.notification;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;

import androidx.core.app.NotificationCompat;

import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.it.calendar.R;
import com.it.calendar.SharedPreference;
import com.it.calendar.ui.MainActivity;
import com.it.calendar.model.MainTable;
import com.it.calendar.model.Virathaday;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationScheduler {

    public static final String TAG = "NotificationScheduler";
    private static final int DAILY_REMINDER_REQUEST_CODE = 100;
    private static final String CHANNEL_ID = "com.it.calendar.channelId";
    static RemoteViews remoteViews;
    static RemoteViews remoteViewsSmall;
    private static SharedPreference sharedPreference = new SharedPreference();

    public static void setReminder(Context context, Class<?> cls, int hour, int min) {
        Calendar calendar = Calendar.getInstance();

        Calendar setCalendar = Calendar.getInstance();
        setCalendar.set(Calendar.HOUR_OF_DAY, hour);
        setCalendar.set(Calendar.MINUTE, min);
        setCalendar.set(Calendar.SECOND, 0);

        //cancel already scheduled reminders
        //cancelReminder(context, cls);

        if (setCalendar.before(calendar))
            setCalendar.add(Calendar.DATE, 1);

        // Enable a receiver

        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

        Intent intent1 = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, DAILY_REMINDER_REQUEST_CODE, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        assert am != null;
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, setCalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void cancelReminder(Context context, Class<?> cls) {
        // Disable a receiver

        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        Intent intent1 = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, DAILY_REMINDER_REQUEST_CODE, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pendingIntent);
        pendingIntent.cancel();
    }

    static void showNotification(Context context) {

        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (defaultSharedPreferences.getBoolean("daily_notification", true)) {

            String dateFormat = "d/M/yyyy";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);
            Calendar calendar = Calendar.getInstance();

            Log.e("Date", simpleDateFormat.format(calendar.getTime()));

            Realm.init(context);
            Realm realm = Realm.getDefaultInstance();

            MainTable mainTable = realm.where(MainTable.class)
                    .equalTo("date", simpleDateFormat.format(calendar.getTime()))
                    .findFirst();

            RealmResults<Virathaday> virathaDays = realm.where(Virathaday.class)
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

            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Intent notificationIntent = new Intent(context, MainActivity.class);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(notificationIntent);

            Intent intent = new Intent(context, MainActivity.class);
            String str = sharedPreference.getString(context, "notifications_new_message_ringtone");
            boolean vibrate = sharedPreference.getBoolean(context, "notifications_new_message_vibrate");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
            remoteViewsSmall = new RemoteViews(context.getPackageName(), R.layout.small_notification_layout);

            remoteViewsTitle(remoteViews, remoteViewsSmall, mainTable, virathaDays);
            remoteViewsImage(remoteViews, stringBuilder, mainTable, virathaDays);

            String channelId = "default_notification_channel_id";
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channelId);
            notificationBuilder.setSmallIcon(R.drawable.ic_calendar_noti);
            notificationBuilder.setStyle(new NotificationCompat.DecoratedCustomViewStyle());
            notificationBuilder.setCustomContentView(remoteViewsSmall);
            notificationBuilder.setCustomBigContentView(remoteViews);
            notificationBuilder.setAutoCancel(true);
            notificationBuilder.setSound(alarmSound);
            notificationBuilder.setWhen(System.currentTimeMillis());

            if (vibrate) {
                notificationBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
            }

            notificationBuilder.setContentIntent(pendingIntent);


            // Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId, "MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);
                channel.enableLights(true);
                channel.setLightColor(Color.RED);
                channel.enableVibration(true);
                channel.setDescription("default_notification_channel_id");
                notificationManager.createNotificationChannel(channel);
            }
            notificationManager.notify(0, notificationBuilder.build());

        }
    }

    private static void remoteViewsImage(RemoteViews remoteViews, StringBuilder stringBuilder, MainTable mainTable, RealmResults<Virathaday> virathaDays) {

        remoteViews.setTextViewText(R.id.nallaNeram, "ந.நே.கா: " + mainTable.getNallanerem_m() + "     ந.நே.மா: " + mainTable.getNallanerem_e());
        remoteViews.setTextViewText(R.id.festivals, "" + stringBuilder.toString());
        if (virathaDays.get(0).getViratham().contains("சுபமுகூர்த்தம்"))
            remoteViews.setViewVisibility(R.id.muhurtham, View.VISIBLE);
        if (virathaDays.get(0).getViratham().contains("அமாவாசை"))
            remoteViews.setViewVisibility(R.id.amavasai, View.VISIBLE);
        if (virathaDays.get(0).getViratham().contains("பௌர்ணமி"))
            remoteViews.setViewVisibility(R.id.pournami, View.VISIBLE);

        StringBuilder stringBuilder1 = new StringBuilder();
        for (Virathaday virathaDay : virathaDays)
            stringBuilder1.append(virathaDay.getViratham()).append(",");

        if (stringBuilder1.length() > 0)
            remoteViews.setTextViewText(R.id.viradham, stringBuilder1.deleteCharAt(stringBuilder1.length() - 1).toString());
        else
            remoteViews.removeAllViews(R.id.viradham);
    }

    private static void remoteViewsTitle(RemoteViews remoteViews, RemoteViews remoteViewsSmall, MainTable mainTable, RealmResults<Virathaday> virathaDay) {


        remoteViews.setImageViewResource(R.id.logo, R.drawable.ic_logo);
        remoteViews.setTextViewText(R.id.monthYear, "" + mainTable.getMonth() + "  " + mainTable.getYear());
        remoteViews.setTextViewText(R.id.date, "" + mainTable.getDay());
        remoteViews.setTextViewText(R.id.day, "" + mainTable.getWeekday());
        remoteViews.setTextViewText(R.id.tamilDate, "" + mainTable.getTam_day());
        remoteViews.setTextViewText(R.id.tamilMonth, "" + mainTable.getTam_month());
        remoteViews.setTextViewText(R.id.tamilYear, "" + mainTable.getTam_year() + " வருடம்");

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
