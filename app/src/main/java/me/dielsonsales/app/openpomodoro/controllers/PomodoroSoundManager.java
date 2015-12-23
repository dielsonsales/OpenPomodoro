package me.dielsonsales.app.openpomodoro.controllers;

import android.content.Context;
import android.media.MediaPlayer;

import me.dielsonsales.app.openpomodoro.R;

/**
 * Created by dielson on 22/12/15.
 */
public class PomodoroSoundManager {

    private MediaPlayer mPlayer;
    private Context mContext;

    public PomodoroSoundManager(Context context) {
        mPlayer = new MediaPlayer();
        mContext = context;
    }

    public void playAlarm() {
        mPlayer = MediaPlayer.create(mContext, R.raw.alarm);
        mPlayer.start();
    }
}
