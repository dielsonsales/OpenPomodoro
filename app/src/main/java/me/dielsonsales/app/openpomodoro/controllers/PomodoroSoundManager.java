package me.dielsonsales.app.openpomodoro.controllers;

import android.content.Context;
import android.media.MediaPlayer;

import me.dielsonsales.app.openpomodoro.R;

public class PomodoroSoundManager {
    private static final String TAG = "PomodoroSoundManager";
    private Context mContext;
    private boolean mSoundAllowed;
    private MediaPlayer mPlayer;

    // Constructors ------------------------------------------------------------

    public PomodoroSoundManager() {}

    public PomodoroSoundManager(Context context) {
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

    public void playBell() {
        resetPlayer();
        if (mSoundAllowed) {
            mPlayer = MediaPlayer.create(mContext, R.raw.bell);
            mPlayer.start();
        }
    }

    public void resetPlayer() {
        if (mPlayer != null) {
            if (mPlayer.isPlaying()) {
                mPlayer.stop();
            }
            mPlayer.release();
            mPlayer = null;
        }
    }
}
