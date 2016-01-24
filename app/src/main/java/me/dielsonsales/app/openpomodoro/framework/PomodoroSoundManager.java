package me.dielsonsales.app.openpomodoro.framework;

import android.content.Context;
import android.media.MediaPlayer;

import me.dielsonsales.app.openpomodoro.R;

public class PomodoroSoundManager implements ISoundPlayer {
    private Context mContext;
    private boolean mSoundAllowed;
    private MediaPlayer mPlayer;

    public PomodoroSoundManager(Context context) {
        mContext = context;
        mSoundAllowed = true;
    }

    // ISoundPlayer methods ----------------------------------------------------

    @Override
    public boolean isSoundAllowed() { return mSoundAllowed; }

    @Override
    public void setSoundAllowed(boolean soundAllowed) {
        mSoundAllowed = soundAllowed;
    }

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

    // Private methods ---------------------------------------------------------

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
