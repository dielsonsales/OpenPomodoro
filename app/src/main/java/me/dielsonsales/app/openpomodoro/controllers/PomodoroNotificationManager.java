package me.dielsonsales.app.openpomodoro.controllers;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import me.dielsonsales.app.openpomodoro.R;

/**
 * Created by dielson on 21/12/15.
 */
public class PomodoroNotificationManager {
    NotificationManager mNotificationManager;
    Context mContext;

    public PomodoroNotificationManager(Context context) {
        mContext = context;
        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void showNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
        builder.setContentTitle("OpenPomodoro");
        builder.setContentText("Pomodoro is running");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        mNotificationManager.notify(1, builder.build());
    }

    public void hideNotification() {
        mNotificationManager.cancel(1);
    }
}
