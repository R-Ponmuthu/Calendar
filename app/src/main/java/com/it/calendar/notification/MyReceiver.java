package com.it.calendar.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class MyReceiver extends BroadcastReceiver {

    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(new Intent(context, MyIntentService.class));
        } else {
            context.startService(new Intent(context, MyIntentService.class));
        }

        //Intent intent1 = new Intent(context, MyIntentService.class);
        //context.startService(intent1);
    }
}
