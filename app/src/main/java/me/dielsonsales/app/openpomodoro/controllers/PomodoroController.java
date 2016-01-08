package me.dielsonsales.app.openpomodoro.controllers;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import me.dielsonsales.app.openpomodoro.util.Duration;

/**
 * Controls the pomodoro clock
 * Created by dielson on 18/12/15.
 */
public class PomodoroController {
    private static final String TAG = "PomodoroController";
    // Enums -------------------------------------------------------------------
    public enum IntervalType {
        POMODORO,
        REST,
        LONG_REST,
        EXTENDED
    }

    // Static default values ---------------------------------------------------
    private static final int DEFAULT_LONG_REST_FREQUENCY = 4;

    // Configurable time values ------------------------------------------------
    private int mPomodoroTime;
    private int mRestTime;
    private int mLongRestTime;
    private int mExtendedTime;
    private int mLongRestFrequency;
    private int mPomodoroCount;

    // Member variables --------------------------------------------------------
    private boolean mAllowExtended;
    private Timer mTimer;
    private boolean mIsRunning;
    private long mCounter;
    private IntervalType mCurrentIntervalType;
    private Duration mDuration;
    private PomodoroListener mListener;
    private static ControllerHandler mHandler;
    private PomodoroSoundManager mSoundManager;

    // Constructors ------------------------------------------------------------
    /**
     * Creates a new PomodoroController instance.
     * @param soundManager a sound manager instance to play the sounds
     */
    public PomodoroController(PomodoroSoundManager soundManager) {
        mHandler = new ControllerHandler(this);
        mLongRestFrequency = DEFAULT_LONG_REST_FREQUENCY;
        mCurrentIntervalType = IntervalType.POMODORO;
        mPomodoroCount = 0; // counts the pomodoros until the long rest
        mSoundManager = soundManager;
        mAllowExtended = true;
    }

    public PomodoroController(PomodoroSoundManager soundManager, PomodoroListener listener) {
        this(soundManager);
        setPomodoroListener(listener);
    }

    // Getters & setters -------------------------------------------------------

    public int getPomodoroTime() { return mPomodoroTime; }

    public int getRestTime() { return mRestTime; }

    public int getLongRestTime() { return mLongRestTime; }

    public int getExtendedTime() { return mExtendedTime; }

    public int getPomodoroCount() { return mPomodoroCount; }

    public IntervalType getCurrentIntervalType() { return mCurrentIntervalType; }

    public boolean isRunning() { return mIsRunning; }

    public boolean isExtendedAllowed() { return mAllowExtended; }

    public void setPomodoroTime(int pomodoroTime) {
        checkParameterTime(pomodoroTime);
        mPomodoroTime = pomodoroTime;
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
        if (mPomodoroTime < 5 || mRestTime < 5 || mLongRestTime < 5 || mExtendedTime < 5) {
            throw new Exception("Pomodoro values were not set");
        }
        mCounter = mPomodoroTime;
        mIsRunning = true;
        startPomodoroTask();
        mDuration = createNewDuration(getCurrentPomodoroTime());
        mPomodoroCount += 1;
        mSoundManager.playTicTacSound();
    }

    /**
     * Advances the clock to the next duration if the user skipped it or to the
     * extended time if it was automatically skipped.
     *
     * @param userInduced If the function was explicitly called by the user
     */
    public void skip(boolean userInduced) {
        if (!mIsRunning) {
            return;
        }
        if (!userInduced && mAllowExtended) {
            mDuration = createNewDuration(mExtendedTime);
            mCounter = mExtendedTime;
        } else {
            mSoundManager.playTicTacSound();

            if (mCurrentIntervalType == IntervalType.POMODORO) {
                if (mPomodoroCount == mLongRestFrequency) {
                    mCurrentIntervalType = IntervalType.LONG_REST;
                    mPomodoroCount = 0;
                    mCounter = mLongRestTime;
                } else {
                    mCurrentIntervalType = IntervalType.REST;
                    mCounter = mRestTime;
                }
            } else {
                mCurrentIntervalType = IntervalType.POMODORO;
                mCounter = mPomodoroTime;
                mPomodoroCount += 1;
            }
            mDuration = createNewDuration(getCurrentPomodoroTime());
        }
    }

    /**
     * Stops any activity and cancels all operations.
     */
    public void stop() {
        if (mIsRunning) {
            mTimer.cancel();
            mTimer.purge();
        }
        mCurrentIntervalType = IntervalType.POMODORO;
        mPomodoroCount = 0; // restart the count
        mIsRunning = false;
        mDuration = null; // not necessary anymore
    }

    public void handleMessage() {
        if (mCounter > 0) {
            mCounter = mCounter - 1;
        }
        Bundle bundle = new Bundle();
        bundle.putLong("countdown", mCounter);
        bundle.putLong("startTime", mDuration.getStartTime().getTime().getTime());
        bundle.putLong("endTime", mDuration.getEndTime().getTime().getTime());
        mListener.onTimeUpdated(bundle);
        if (mCounter == 60) {
            mSoundManager.playBell();
        } else if (mCounter == 0) {
            mSoundManager.playAlarm();
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
        if (mCurrentIntervalType == IntervalType.POMODORO) {
            return mPomodoroTime;
        } else if (mCurrentIntervalType == IntervalType.REST ) {
            return mRestTime;
        } else if (mCurrentIntervalType == IntervalType.LONG_REST) {
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
