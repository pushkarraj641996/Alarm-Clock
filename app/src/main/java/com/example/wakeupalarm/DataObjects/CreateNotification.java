package com.example.wakeupalarm.DataObjects;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.wakeupalarm.R;

public class CreateNotification {
    private final static String EVENT_CHANNEL_ID = "Notification Channel";
    private final static String EVENT_CHANNEL_DESCRIPTION = "Notification for Missed alarms";

    private final Context context;
    private final int notificationID;

    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;

    public CreateNotification(Context context, int notificationID) {
        this.context = context;
        this.notificationID = notificationID;
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(EVENT_CHANNEL_ID, EVENT_CHANNEL_DESCRIPTION, NotificationManager.IMPORTANCE_MIN);
            notificationChannel.enableVibration(true);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setImportance(NotificationManager.IMPORTANCE_MIN);
            notificationChannel.setDescription(EVENT_CHANNEL_DESCRIPTION);

            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public void sendNotification(String title, String description, Intent intent) {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();
        builder = new NotificationCompat.Builder(context, EVENT_CHANNEL_ID);
        PendingIntent pendingIntent = PendingIntent.getActivity(context.getApplicationContext(), notificationID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentTitle(title)
                .setContentText(description)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        notificationManager.notify(notificationID, builder.build());
    }
}
