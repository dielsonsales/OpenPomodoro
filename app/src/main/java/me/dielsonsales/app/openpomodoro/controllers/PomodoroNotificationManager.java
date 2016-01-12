package me.dielsonsales.app.openpomodoro.controllers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import me.dielsonsales.app.openpomodoro.MainActivity;
import me.dielsonsales.app.openpomodoro.R;

/**
 * The Notification manager. This class starts and stops the service from
 * running in foreground, depending if the pomodoro is running or not.
 * Created by dielson on 21/12/15.
 */
public class PomodoroNotificationManager {
    public enum NotificationType {
        WORK_NOTIFICATION,
        REST_NOTIFICATION
    }
    NotificationManager mNotificationManager;
    Service mService;

    public PomodoroNotificationManager(Context context) {
        mService = (Service) context;
        mNotificationManager = (NotificationManager) mService.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void showNotification(NotificationType notificationType) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mService);
        builder.setCategory(Notification.CATEGORY_SERVICE);
        builder.setContentTitle(mService.getResources().getString(R.string.app_name));
        builder.setContentText(mService.getResources().getString(R.string.notification_rest_message));
        if (notificationType == NotificationType.WORK_NOTIFICATION) {
            builder.setContentText(mService.getResources().getString(R.string.notification_work_message));
            builder.setSmallIcon(R.drawable.ic_stat_work);
        } else {
            builder.setContentText(mService.getResources().getString(R.string.notification_rest_message));
            builder.setSmallIcon(R.drawable.ic_stat_rest);
        }
        Intent resultIntent = new Intent(mService, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mService);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);

        Notification mNotification = builder.build();

        mService.startForeground(1, mNotification);
    }

    public void hideNotification() { mService.stopForeground(true); }
}
