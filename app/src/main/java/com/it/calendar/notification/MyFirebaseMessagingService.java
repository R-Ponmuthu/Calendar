package com.it.calendar.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.it.calendar.R;
import com.it.calendar.notification.beans.Notification;
import com.it.calendar.ui.activity.ViewNotificationActivity;
import com.it.calendar.util.DateTimeHelper;

import java.util.Random;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static String TAG = "MyFirebaseMessagingService";
    private RemoteViews remoteViews, remoteViewsSmall;
    private String GROUP_CALENDAR = "com.it.calendar";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        System.out.println("remoteMessage" + remoteMessage.getData());
        //Calling method to generate notification

        final int min = 1;
        final int max = 100;
        final int nextId = new Random().nextInt((max - min) + 1) + min;

        Notification notification = new Notification();
        notification.setId(nextId);
        notification.setTitle(remoteMessage.getData().get("title"));
        notification.setMessage(remoteMessage.getData().get("body"));
        notification.setBigMessage(remoteMessage.getData().get("bigMessage"));
        notification.setDate(remoteMessage.getData().get("date"));
        notification.setImageUrl(remoteMessage.getData().get("image"));
        notification.setRead("0");
        notification.setNotiType(remoteMessage.getData().get("type"));

        String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("body");
        String type = remoteMessage.getData().get("type");

        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Log.e("Notification", String.valueOf(defaultSharedPreferences.getBoolean("notifications_new_message", true)));
        if (defaultSharedPreferences.getBoolean("notifications_new_message", true)) {

            if (type.equals("BP")) {

                new NotificationHelper(this).showBigPictureNotification(nextId, type, title, message, remoteMessage.getData().get("image"));

            } else if (type.equals("BT")) {

                new NotificationHelper(this).showBigTextNotification(nextId, type, title, message, remoteMessage.getData().get("bigMessage"));
            } else {
                sendNotification(nextId, type, remoteMessage.getData().get("title"), remoteMessage.getData().get("body"));
            }
        }
    }

    private void sendNotification(int nextId, String type, String title, String messageBody) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        Intent intent = new Intent(this, ViewNotificationActivity.class);
        String str = defaultSharedPreferences.getString("notifications_new_message_ringtone", "DEFAULT_SOUND");
        boolean vibrate = defaultSharedPreferences.getBoolean("notifications_new_message_vibrate", false);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Id", nextId);
        intent.putExtra("Type", type);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        remoteViews = new RemoteViews(getPackageName(), R.layout.custom_notification);
        remoteViewsSmall = new RemoteViews(getPackageName(), R.layout.custom_notification_small);

        remoteViewsTitle(remoteViews, remoteViewsSmall, title);
        remoteViewsBody(remoteViews, remoteViewsSmall, messageBody);

        String channelId = getString(R.string.default_notification_channel_id);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_calendar_noti)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(remoteViewsSmall)
                .setCustomBigContentView(remoteViews)
                .setAutoCancel(true)
                .setSound(Uri.parse(str))
                .setWhen(System.currentTimeMillis())
                .setGroup(GROUP_CALENDAR)
                .setGroupSummary(true);

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
            channel.setDescription(getString(R.string.default_notification_channel_id));
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0, notificationBuilder.build());
    }

    private void remoteViewsBody(RemoteViews remoteViews, RemoteViews remoteViewsSmall, String body) {
        remoteViews.setTextViewText(R.id.noti_desc, body);
        remoteViewsSmall.setTextViewText(R.id.noti_desc, body);
    }

    private void remoteViewsTitle(RemoteViews remoteViews, RemoteViews remoteViewsSmall, String title) {
        remoteViews.setTextViewText(R.id.noti_title, title);
        remoteViewsSmall.setTextViewText(R.id.noti_title, title);
    }


    @Override
    public void onNewToken(String registrationToken) {

        super.onNewToken(registrationToken);
        String newToken = registrationToken;
        System.out.println("FCM Token = " + newToken);
    }
}