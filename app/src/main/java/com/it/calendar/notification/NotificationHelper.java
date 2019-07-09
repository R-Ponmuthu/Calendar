package com.it.calendar.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.it.calendar.R;
import com.it.calendar.ui.ViewNotificationActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationHelper {

    private final Context context;

    public NotificationHelper(Context context) {
        this.context = context;
    }

    public void showBigPictureNotification(int nextId, String type, String title, String message, String imageUrl) {

        Bitmap image = getBitmapfromUrl(imageUrl);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(context, ViewNotificationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Id", nextId);
        intent.putExtra("Type", type);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        String channelId = context.getString(R.string.default_notification_channel_id);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channelId);
        notificationBuilder.setSmallIcon(R.drawable.ic_calendar_noti);
        notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(image)
                .setBigContentTitle(title)
                .setSummaryText(message));
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSound(defaultSoundUri);
        notificationBuilder.setWhen(System.currentTimeMillis());
        notificationBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});


        notificationBuilder.setContentIntent(pendingIntent);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setDescription(context.getString(R.string.default_notification_channel_id));
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0, notificationBuilder.build());
    }

    public void showBigTextNotification(int nextId, String type, String title, String message, String bigMessage) {

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(context, ViewNotificationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Id", nextId);
        intent.putExtra("Type", type);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        String channelId = context.getString(R.string.default_notification_channel_id);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channelId);
        notificationBuilder.setSmallIcon(R.drawable.ic_calendar_noti);
        notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle(title)
                .setSummaryText(message)
                .bigText(bigMessage));
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSound(defaultSoundUri);
        notificationBuilder.setWhen(System.currentTimeMillis());
        notificationBuilder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});


        notificationBuilder.setContentIntent(pendingIntent);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setDescription(context.getString(R.string.default_notification_channel_id));
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0, notificationBuilder.build());

    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }
}
