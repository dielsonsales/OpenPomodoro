package me.dielsonsales.app.openpomodoro.controllers;

import android.content.Context;
import android.media.MediaPlayer;

import me.dielsonsales.app.openpomodoro.R;

public class PomodoroSoundManager {
    private static final String TAG = "PomodoroSoundManager";

    private MediaPlayer mPlayer;
    private static PomodoroSoundManager mInstance;

    // Static methods ----------------------------------------------------------

    public static PomodoroSoundManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PomodoroSoundManager(context);
        }
        return mInstance;
    }

    public static PomodoroSoundManager getInstance(MediaPlayer mediaPlayer) {
        if (mInstance == null) {
            mInstance = new PomodoroSoundManager(mediaPlayer);
        }
        return mInstance;
    }

    // Constructors ------------------------------------------------------------

    private PomodoroSoundManager() {}

    private PomodoroSoundManager(Context context) {
        mPlayer = MediaPlayer.create(context, R.raw.alarm);
    }

    // Used mainly for mocking the media player
    private PomodoroSoundManager(MediaPlayer mediaPlayer) {
        mPlayer = mediaPlayer;
    }

    // Public methods ----------------------------------------------------------

    public void playAlarm() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            mPlayer.seekTo(0);
        }
        mPlayer.start();
    }
}
