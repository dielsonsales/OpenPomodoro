package me.dielsonsales.app.openpomodoro.presentation;

import android.os.Bundle;
import android.util.Log;

import javax.inject.Inject;

import me.dielsonsales.app.openpomodoro.PomodoroApp;
import me.dielsonsales.app.openpomodoro.PomodoroService;
import me.dielsonsales.app.openpomodoro.controllers.PomodoroController;
import me.dielsonsales.app.openpomodoro.controllers.PomodoroListener;
import me.dielsonsales.app.openpomodoro.data.IPreferences;
import me.dielsonsales.app.openpomodoro.framework.INotification;
import me.dielsonsales.app.openpomodoro.framework.ISoundPlayer;
import me.dielsonsales.app.openpomodoro.framework.IVibrator;
import me.dielsonsales.app.openpomodoro.framework.IWakeLock;
import me.dielsonsales.app.openpomodoro.util.FormattingUtils;

public class ServicePresenter {
    private PomodoroService mService;
    @Inject PomodoroController mPomodoroController;
    @Inject ISoundPlayer mSoundPlayer;
    @Inject IVibrator mVibrator;
    @Inject INotification iNotification;
    @Inject IPreferences mPreferences;
    @Inject IWakeLock mWakeLock;

    public ServicePresenter(PomodoroService service) {
        mService = service;
        ((PomodoroApp) mService.getApplication()).getServiceComponent(mService).inject(this);
        mPomodoroController.setPomodoroListener(new PomodoroListener() {
            @Override
            public void onTimeUpdated(Bundle bundle) {
                mService.updateActivity(bundle);
            }
        });
    }

    public void startPomodoro() {
        updateControllerPreferences();
        try {
            mPomodoroController.start();
        } catch (Exception e) {
            Log.e("ServicePresenter", e.getMessage());
            mPomodoroController.stop();
        }
        mWakeLock.acquire();
    }

    public void skipPomodoro() {
        mPomodoroController.skip(true);
    }

    public void stopPomodoro() {
        mPomodoroController.stop();
        mWakeLock.release();
    }

    public boolean isRunning() {
        return mPomodoroController.isRunning();
    }

    // Private methods ---------------------------------------------------------

    void updateControllerPreferences() {
        mPreferences.retrievePreferences();
        // Set pomodoro time
        String pomodoroTimeString = mPreferences.getPomodoroTime();
        mPomodoroController.setPomodoroTime(FormattingUtils.timeToSeconds(pomodoroTimeString));

        // Set rest time
        String restTimeString = mPreferences.getRestTime();
        mPomodoroController.setRestTime(FormattingUtils.timeToSeconds(restTimeString));

        // Set long rest time
        String longRestTimeString = mPreferences.getLongRestTime();
        mPomodoroController.setLongRestTime(FormattingUtils.timeToSeconds(longRestTimeString));

        // Set extended time
        String extendedTimeString = mPreferences.getExtendedTime();
        mPomodoroController.setExtendedTime(FormattingUtils.timeToSeconds(extendedTimeString));

        mSoundPlayer.setSoundAllowed(mPreferences.getSoundAllowed());
        mVibrator.setVibrationAllowed(mPreferences.getVibrationAllowed());
        mPomodoroController.setExtendedAllowed(mPreferences.getExtendedAllowed());
    }
}
