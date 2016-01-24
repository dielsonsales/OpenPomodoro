package me.dielsonsales.app.openpomodoro.controllers;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import me.dielsonsales.app.openpomodoro.android.INotification;
import me.dielsonsales.app.openpomodoro.android.ISoundPlayer;
import me.dielsonsales.app.openpomodoro.android.IVibrator;
import me.dielsonsales.app.openpomodoro.android.PomodoroNotificationManager;
import me.dielsonsales.app.openpomodoro.domain.Duration;
import me.dielsonsales.app.openpomodoro.domain.Pomodoro;

/**
 * Controls the pomodoro clock
 * Created by dielson on 18/12/15.
 */
public class PomodoroController {
    private static final String TAG = "PomodoroController";

    // Static default values ---------------------------------------------------
    private static final int DEFAULT_LONG_REST_FREQUENCY = 4;

    // Configurable time values ------------------------------------------------
    private int mWorkTime;
    private int mRestTime;
    private int mLongRestTime;
    private int mExtendedTime;
    private int mLongRestFrequency;
    private int mPomodoroCount; // the number of pomodoros since the last long rest

    // Member variables --------------------------------------------------------
    private Pomodoro mCurrentPomodoro;
    private boolean mAllowExtended;
    private Timer mTimer;
    private Duration mDuration;
    private PomodoroListener mListener;
    private static ControllerHandler mHandler;
    private ISoundPlayer mSoundPlayer;
    private IVibrator mVibrator;
    private INotification mNotificationManager;

    private static final SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("HH:mm:ss");

    // Constructors ------------------------------------------------------------
    /**
     * Creates a new PomodoroController instance.
     */
    public PomodoroController(ISoundPlayer soundPlayer, IVibrator vibrator, INotification notificationManager) {
        mHandler = new ControllerHandler(this);
        mLongRestFrequency = DEFAULT_LONG_REST_FREQUENCY;
        mSoundPlayer = soundPlayer;
        mVibrator = vibrator;
        mNotificationManager = notificationManager;
        mAllowExtended = true;
    }


    // Getters & setters -------------------------------------------------------

    public int getWorkTime() { return mWorkTime; }

    public int getRestTime() { return mRestTime; }

    public int getLongRestTime() { return mLongRestTime; }

    public int getExtendedTime() { return mExtendedTime; }

    /**
     * Returns the number of pomodoros since the last long rest.
     */
    public int getPomodoroCount() { return mPomodoroCount; }

    public Pomodoro.PomodoroType getCurrentPomodoroType() {
        if (isRunning()) {
            return mCurrentPomodoro.getPomodoroType();
        }
        return Pomodoro.PomodoroType.NONE;
    }

    public boolean isRunning() { return mCurrentPomodoro != null; }

    public boolean isExtendedAllowed() { return mAllowExtended; }

    public void setPomodoroTime(int pomodoroTime) {
        checkParameterTime(pomodoroTime);
        mWorkTime = pomodoroTime;
    }

    public void setRestTime(int restTime) {
        checkParameterTime(restTime);
        mRestTime = restTime;
    }

    public void setLongRestTime(int longRestTime) {
        checkParameterTime(longRestTime);
        mLongRestTime = longRestTime;
    }

    public void setExtendedTime(int extendedTime) {
        checkParameterTime(extendedTime);
        mExtendedTime = extendedTime;
    }

    /**
     * Sets if the clock is allowed to go to extended time.
     */
    public void setExtendedAllowed(boolean allowExtended) {
        mAllowExtended = allowExtended;
    }

    public void setPomodoroListener(PomodoroListener listener) { mListener = listener; }

    // Behavior methods --------------------------------------------------------

    /**
     * Starts the pomodoro.
     */
    public void start() throws Exception {
        if (mWorkTime < 5 || mRestTime < 5 || mLongRestTime < 5 || mExtendedTime < 5) {
            throw new Exception("Pomodoro values are invalid");
        }
        mCurrentPomodoro = new Pomodoro(Pomodoro.PomodoroType.WORK, mWorkTime);
        mDuration = createNewDuration(getCurrentPomodoroTime());
        startPomodoroTask();
        mPomodoroCount += 1;
        mSoundPlayer.playTicTacSound();
        mNotificationManager.showNotification(PomodoroNotificationManager.NotificationType.WORK_NOTIFICATION);
    }

    /**
     * Advances the clock to the next duration if the user skipped it or to the
     * extended time if it was automatically skipped.
     *
     * @param userInduced If the function was explicitly called by the user
     */
    public void skip(boolean userInduced) {
        if (!isRunning()) {
            return;
        }
        if (userInduced) {
            mSoundPlayer.playTicTacSound();
        }
        if (!userInduced && mAllowExtended) {
            mDuration = createNewDuration(mExtendedTime);
            mCurrentPomodoro = new Pomodoro(Calendar.getInstance(), mCurrentPomodoro.getPomodoroType(), mExtendedTime);
        } else {
            if (mCurrentPomodoro.getPomodoroType() == Pomodoro.PomodoroType.WORK) {
                if (mPomodoroCount == mLongRestFrequency) {
                    mPomodoroCount = 0;
                    mCurrentPomodoro = new Pomodoro(Pomodoro.PomodoroType.LONG_REST, mLongRestTime);
                } else {
                    mCurrentPomodoro = new Pomodoro(Pomodoro.PomodoroType.REST, mRestTime);
                }
                mNotificationManager.showNotification(PomodoroNotificationManager.NotificationType.REST_NOTIFICATION);
            } else {
                mCurrentPomodoro = new Pomodoro(Pomodoro.PomodoroType.WORK, mWorkTime);
                mPomodoroCount += 1;
                mNotificationManager.showNotification(PomodoroNotificationManager.NotificationType.WORK_NOTIFICATION);
            }
            mDuration = createNewDuration(getCurrentPomodoroTime());
        }
    }

    /**
     * Stops any activity and cancels all operations.
     */
    public void stop() {
        if (isRunning()) {
            mTimer.cancel();
            mTimer.purge();
        }
        mCurrentPomodoro = null;
        mDuration = null; // not necessary anymore
        mPomodoroCount = 0; // restart the count
        mNotificationManager.hideNotification();
    }

    public void handleMessage() {
        if (mCurrentPomodoro.getRemainingTime() > 0) {
            mCurrentPomodoro.decrement();
        }
        Bundle bundle = new Bundle();
        bundle.putLong("countdown", mCurrentPomodoro.getRemainingTime());
        bundle.putLong("startTime", mDuration.getStartTime().getTime().getTime());
        Log.d(TAG, "startTime: " + mSimpleDateFormat.format(mDuration.getStartTime().getTime()));
        bundle.putLong("endTime", mDuration.getEndTime().getTime().getTime());
        Log.d(TAG, "endTime: " + mSimpleDateFormat.format(mDuration.getEndTime().getTime()));
        bundle.putBoolean("isRest", mCurrentPomodoro.getPomodoroType() == Pomodoro.PomodoroType.REST ||
                mCurrentPomodoro.getPomodoroType() == Pomodoro.PomodoroType.LONG_REST);
        mListener.onTimeUpdated(bundle);
        if (mCurrentPomodoro.getRemainingTime() == 60) {
            mSoundPlayer.playBell();
        } else if (mCurrentPomodoro.getRemainingTime() == 0) {
            mSoundPlayer.playAlarm();
            mVibrator.vibrate();
            skip(false);
        }
    }

    // Private methods ---------------------------------------------------------

    private void startPomodoroTask() {
        mTimer = new Timer();
        PomodoroTask pomodoroTask = new PomodoroTask();
        mTimer.schedule(pomodoroTask, 0, 1000);
    }

    private int getCurrentPomodoroTime() {
        if (mCurrentPomodoro.getPomodoroType() == Pomodoro.PomodoroType.WORK) {
            return mWorkTime;
        } else if (mCurrentPomodoro.getPomodoroType() == Pomodoro.PomodoroType.REST) {
            return mRestTime;
        } else if (mCurrentPomodoro.getPomodoroType() == Pomodoro.PomodoroType.LONG_REST) {
            return mLongRestTime;
        } else {
            return mExtendedTime;
        }
    }

    private static Duration createNewDuration(int currentDurationSeconds) {
        Calendar startTime = Calendar.getInstance();
        Calendar endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.SECOND, currentDurationSeconds);
        return new Duration(startTime, endTime);
    }

    private static void checkParameterTime(int seconds) {
        if (seconds < 5) {
            throw new IllegalArgumentException("Time should not be less than 5 seconds");
        }
    }

    // Handler -----------------------------------------------------------------

    /**
     * Receives messages from the PomodoroTask class in the worker thread and
     * redirects them to the PomodoroController class, in the UI thread.
     */
    static class ControllerHandler extends Handler {
        private WeakReference<PomodoroController> mController;
        public ControllerHandler(PomodoroController controller) {
            setController(controller);
        }
        public ControllerHandler() {
            // does nothing
        }
        void setController(PomodoroController controller) {
            mController = new WeakReference<>(controller);
        }
        // Overridden methods --------------------------------------------------
        @Override
        public void handleMessage(Message msg) {
            if (mController != null) {
                PomodoroController pomodoroController = mController.get();
                if (pomodoroController != null) {
                    pomodoroController.handleMessage();
                }
            }
        }
    }

    // Pomodoro task -----------------------------------------------------------

    private class PomodoroTask extends TimerTask {
        @Override
        public void run() { mHandler.sendEmptyMessage(0); }
    }
}
