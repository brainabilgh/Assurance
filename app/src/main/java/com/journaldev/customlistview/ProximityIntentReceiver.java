package com.journaldev.customlistview;

import android.content.BroadcastReceiver;

/**
 * Created by Aissa on 5/27/2017.
 */
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.provider.Settings;
import android.util.Log;

public class ProximityIntentReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 1000;

    @Override
    public void onReceive(Context context, Intent intent) {

        String key = LocationManager.KEY_PROXIMITY_ENTERING;

        Boolean entering = intent.getBooleanExtra(key, false);

        if (entering) {
            Log.d(getClass().getSimpleName(), "Entrer !");
        }
        else {
            Log.d(getClass().getSimpleName(), "Sortir !");
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context);
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);
        builder.setLights(Color.WHITE, 300, 1500);
        builder.setWhen(System.currentTimeMillis());
        builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        builder.setTicker("this is ticker text");
        builder.setContentTitle("Proximité d'un expert !");
        builder.setContentText("Vous approchez de: M." +intent.getBundleExtra(MainActivity.PROX_ALERT_INTENT).get("name"));
        builder.setSmallIcon(R.drawable.markerandroid);
        builder.setOngoing(true);
        builder.setSubText("Veuillez le visiter...");   //API level 16
        builder.build();
        //here-------------------------------------
        notificationManager.notify( intent.getBundleExtra(MainActivity.PROX_ALERT_INTENT).getInt("id"), builder.build());
        MainActivity.notice = true;

    }

}

