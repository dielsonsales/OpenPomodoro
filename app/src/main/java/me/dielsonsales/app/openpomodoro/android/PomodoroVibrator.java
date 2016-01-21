package me.dielsonsales.app.openpomodoro.android;

import android.content.Context;
import android.os.Vibrator;

/**
 * Created by dielson on 21/01/16.
 */
public class PomodoroVibrator implements IVibrator {
    private boolean mVibrationAllowed;
    private Vibrator mVibrator;

    public PomodoroVibrator(Context context) {
        mVibrationAllowed = false;
        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
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
            mVibrator.vibrate(1000); // 1 second
        }
    }
}
