package me.dielsonsales.app.openpomodoro.controllers;

import android.content.Context;
import android.media.MediaPlayer;

import me.dielsonsales.app.openpomodoro.R;

public class PomodoroSoundManager {
    private static final String TAG = "PomodoroSoundManager";
    private Context mContext;

    private MediaPlayer mPlayer;
    private static PomodoroSoundManager mInstance;

    // Static methods ----------------------------------------------------------

    public static PomodoroSoundManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PomodoroSoundManager(context);
        }
        return mInstance;
    }

    // Constructors ------------------------------------------------------------

    private PomodoroSoundManager() {}

    private PomodoroSoundManager(Context context) {
        mContext = context;
        mPlayer = MediaPlayer.create(context, R.raw.alarm);
    }

    // Public methods ----------------------------------------------------------

    public void playTicTacSound() {
        resetPlayer();
        mPlayer = MediaPlayer.create(mContext, R.raw.ticktack);
        mPlayer.start();
    }

    public void playAlarm() {
        resetPlayer();
        mPlayer = MediaPlayer.create(mContext, R.raw.alarm);
        mPlayer.start();
    }

    private void resetPlayer() {
        if (mPlayer != null) {
            if (mPlayer.isPlaying()) {
                mPlayer.stop();
            }
            mPlayer.release();
            mPlayer = null;
        }
    }
}
