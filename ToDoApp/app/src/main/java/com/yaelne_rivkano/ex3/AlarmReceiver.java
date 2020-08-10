package com.yaelne_rivkano.ex3;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver
{
    private static final String NOTIFICATION_CHANNEL_ID = "Alarm Notification Chanel";
    private static final int repeatingNotificationID = 9999;
    private static int notificationID = 0;

    private NotificationManager notificationManager;
    private NotificationChannel notificationChannel;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.d("debug","AlarmReceiver.onReceive()");

        // get intent extra info
        String title = intent.getStringExtra("title");
        String msg = intent.getStringExtra("msg");

        // create the Notification Channel
        createNotificationChannel(context);
        showOneTimeAlarmNotification(context, title, msg);
    }

    private void showOneTimeAlarmNotification(Context context, String title, String msg)
    {
        Log.d("debug","AlarmReceiver.showOneTimeAlarmNotification()");

        // Create the content intent for the notification, which launches MainActivity activity
        Intent tapIntent = new Intent(context, ToDoListActivity.class);
        PendingIntent tapPendingIntent = PendingIntent.getActivity(context, 0, tapIntent, 0);

        // create the notification
        Notification notification = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(msg)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(tapPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build();

        // Deliver the notification
        notificationManager.notify(notificationID, notification);
        notificationID++;
    }


    private void createNotificationChannel(Context context)
    {
        // Create a notification manager object.
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Notification channels are only available in OREO and higher.
        // So, add a check on SDK version.
        if (android.os.Build.VERSION.SDK_INT >=  android.os.Build.VERSION_CODES.O)
        {
            // Create the NotificationChannel with all the parameters.
            notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_ID, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription(NOTIFICATION_CHANNEL_ID);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}

