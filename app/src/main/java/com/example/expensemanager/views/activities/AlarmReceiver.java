package com.example.expensemanager.views.activities;

import static android.content.Context.ACTIVITY_SERVICE;

import android.Manifest;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.expensemanager.R;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isRunning = false;
        String string = intent.getExtras().getString("extra");

        ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (Music.class.getName().equals(service.service.getClassName())) {
                isRunning = true;
            }
        }
        Intent mIntent = new Intent(context, Music.class);
        if (string.equals("on") && !isRunning) {
            context.startService(mIntent);
            ReminderActivity.activeAlarm = intent.getExtras().getString("active");
            showNotification(context, ReminderActivity.activeAlarm);
        } else if (string.equals("off")) {
            context.stopService(mIntent);
            ReminderActivity.activeAlarm = "";
        }
    }

    private void showNotification(Context context, String activeAlarm) {
        createNotificationChannel(context);

        Intent intent = new Intent(context, ReminderActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notify")
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentTitle("Reminder")
                .setContentText(activeAlarm)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(123, builder.build());
    }

    private void stopNotification(Context context) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.cancel(123);
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Reminder Channel";
            String channelDescription = "Channel for reminder notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel("notify", channelName, importance);
            channel.setDescription(channelDescription);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}

