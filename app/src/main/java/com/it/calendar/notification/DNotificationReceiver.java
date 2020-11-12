package com.it.calendar.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.it.calendar.R;
import com.it.calendar.ui.activity.CalendarActivity;
import com.it.calendar.utils.SharedPreference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DNotificationReceiver extends BroadcastReceiver {

    private NotificationManager manager;
    private SharedPreference sharedPreference = new SharedPreference();

    public DNotificationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction() != null)
            if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()) ||
                    intent.getAction().equalsIgnoreCase("android.intent.action.QUICKBOOT_POWERON")) {

            }

        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Log.e("Notification", String.valueOf(defaultSharedPreferences.getBoolean("daily_notification", true)));
        if (defaultSharedPreferences.getBoolean("daily_notification", true))
            createNotification(context);
    }

    public void createNotification(Context context) {
        final int NOTIFY_ID = 1002;

        String dateFormat = "d/M/yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);
        Calendar calendar = Calendar.getInstance();

        // There are hardcoding only for show it's just strings
        String name = "com.it.calendar";
        String id = "my_package_channel_1"; // The user-visible name of the channel.
        String description = "my_package_first_channel"; // The user-visible description of the channel.

        Intent intent;
        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;

        if (manager == null) {
            manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = manager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, name, importance);
                mChannel.setDescription(description);
                mChannel.enableVibration(true);
                mChannel.setLightColor(Color.GREEN);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                manager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(context, id);

            intent = new Intent(context, CalendarActivity.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentTitle(simpleDateFormat.format(calendar.getTime()))  // required
                    .setSmallIcon(R.drawable.ic_calendar_noti) // required
                    .setContentText("இன்றைய நாளுக்கான விவரங்கள் அறிய")  // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(simpleDateFormat.format(calendar.getTime()))
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        } else {

            builder = new NotificationCompat.Builder(context);

            intent = new Intent(context, CalendarActivity.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentTitle(simpleDateFormat.format(calendar.getTime()))                           // required
                    .setSmallIcon(R.drawable.ic_calendar_noti) // required
                    .setContentText("இன்றைய நாளுக்கான விவரங்கள் அறிய")  // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(simpleDateFormat.format(calendar.getTime()))
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setPriority(Notification.PRIORITY_HIGH);
        }

        Notification notification = builder.build();
        manager.notify(NOTIFY_ID, notification);
    }
}
