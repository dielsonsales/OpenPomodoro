package me.dielsonsales.app.openpomodoro.controllers;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import me.dielsonsales.app.openpomodoro.R;

/**
 * The Notification manager. This class starts and stops the service from
 * running in foreground, depending if the pomodoro is running or not.
 * Created by dielson on 21/12/15.
 */
public class PomodoroNotificationManager {
    NotificationManager mNotificationManager;
    Service mService;

    public PomodoroNotificationManager(Context context) {
        mService = (Service) context;
        mNotificationManager = (NotificationManager) mService.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void showNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mService);
        builder.setContentTitle("OpenPomodoro");
        builder.setContentText("Pomodoro is running");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        mService.startForeground(1, builder.build());
    }

    public void hideNotification() {
        mService.stopForeground(true);
    }
}
