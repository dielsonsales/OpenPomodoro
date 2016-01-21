package me.dielsonsales.app.openpomodoro.android;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Vibrator;

import me.dielsonsales.app.openpomodoro.R;

public class PomodoroSoundManager implements ISoundPlayer, IVibrator {
    private Context mContext;
    private boolean mSoundAllowed;
    private boolean mVibrationAllowed;
    private MediaPlayer mPlayer;
    private Vibrator mVibrator;

    // Constructors ------------------------------------------------------------

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

    public boolean isSoundAllowed() { return mSoundAllowed; }

    // ISoundPlayer methods ----------------------------------------------------

    @Override
    public void playTicTacSound() {
        resetPlayer();
        if (mSoundAllowed) {
            mPlayer = MediaPlayer.create(mContext, R.raw.ticktack);
            mPlayer.start();
        }
    }

    @Override
    public void playAlarm() {
        resetPlayer();
        if (mSoundAllowed) {
            mPlayer = MediaPlayer.create(mContext, R.raw.alarm);
            mPlayer.start();
        }
    }

    @Override
    public void playBell() {
        resetPlayer();
        if (mSoundAllowed) {
            mPlayer = MediaPlayer.create(mContext, R.raw.bell);
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

    // IVibrator methods -------------------------------------------------------

    @Override
    public void setVibrationAllowed(boolean vibrationAllowed) {
        mVibrationAllowed = vibrationAllowed;
    }

    @Override
    public boolean isVibrationAllowed() {
        return mVibrationAllowed;
    }

    @Override
    public void vibrate() {
        if (mVibrator.hasVibrator() && mVibrationAllowed) {
            mVibrator.vibrate(1000);
        }
    }
}
