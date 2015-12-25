package me.dielsonsales.app.openpomodoro.controllers;

import android.content.Context;
import android.media.MediaPlayer;

import me.dielsonsales.app.openpomodoro.R;

public class PomodoroSoundManager {
    private static final String TAG = "PomodoroSoundManager";

    private MediaPlayer mPlayer;
    private Context mContext;
    private static PomodoroSoundManager mInstance;

    public static PomodoroSoundManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PomodoroSoundManager(context);
        }
        return mInstance;
    }

    private PomodoroSoundManager() {}

    private PomodoroSoundManager(Context context) {
        mContext = context;
        mPlayer = MediaPlayer.create(mContext, R.raw.alarm);
    }

    public void playAlarm() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            mPlayer.seekTo(0);
        }
        mPlayer.start();
    }
}
