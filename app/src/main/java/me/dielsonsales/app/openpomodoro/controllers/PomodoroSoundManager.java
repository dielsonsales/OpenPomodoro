package me.dielsonsales.app.openpomodoro.controllers;

import android.content.Context;
import android.media.MediaPlayer;

import me.dielsonsales.app.openpomodoro.R;

public class PomodoroSoundManager {
    private static final String TAG = "PomodoroSoundManager";
    private Context mContext;
    private boolean mSoundAllowed;
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

    protected PomodoroSoundManager() {}

    protected PomodoroSoundManager(Context context) {
        mContext = context;
        mSoundAllowed = true;
    }

    // Getters & setters -------------------------------------------------------

    public void setSoundAllowed(boolean soundAllowed) {
        mSoundAllowed = soundAllowed;
    }

    public boolean isSounsAllowed() {
        return mSoundAllowed;
    }

    // Public methods ----------------------------------------------------------

    public void playTicTacSound() {
        resetPlayer();
        if (mSoundAllowed) {
            mPlayer = MediaPlayer.create(mContext, R.raw.ticktack);
            mPlayer.start();
        }
    }

    public void playAlarm() {
        resetPlayer();
        if (mSoundAllowed) {
            mPlayer = MediaPlayer.create(mContext, R.raw.alarm);
            mPlayer.start();
        }
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
