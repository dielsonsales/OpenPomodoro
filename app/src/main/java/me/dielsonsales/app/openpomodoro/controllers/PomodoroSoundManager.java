package me.dielsonsales.app.openpomodoro.controllers;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Vibrator;

import me.dielsonsales.app.openpomodoro.R;

public class PomodoroSoundManager {
    private static final String TAG = "PomodoroSoundManager";
    private Context mContext;
    private boolean mSoundAllowed;
    private boolean mVibrationAllowed;
    private MediaPlayer mPlayer;
    private Vibrator mVibrator;

    // Constructors ------------------------------------------------------------

    public PomodoroSoundManager() {}

    public PomodoroSoundManager(Context context) {
        mContext = context;
        mSoundAllowed = true;
        mVibrationAllowed = false;
        mVibrator = (Vibrator) mContext.getSystemService(mContext.VIBRATOR_SERVICE);
    }

    // Getters & setters -------------------------------------------------------

    public void setSoundAllowed(boolean soundAllowed) {
        mSoundAllowed = soundAllowed;
    }

    public void setVibrationAllowed(boolean vibrationAllowed) {
        mVibrationAllowed = vibrationAllowed;
    }

    public boolean isSounsAllowed() { return mSoundAllowed; }

    // Sound methods -----------------------------------------------------------

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

    // Vibration methods -------------------------------------------------------

    public void vibrate() {
        if (mVibrator.hasVibrator() && mVibrationAllowed) {
            mVibrator.vibrate(1000);
        }
    }
}
