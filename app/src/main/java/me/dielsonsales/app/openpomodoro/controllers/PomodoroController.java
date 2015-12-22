package me.dielsonsales.app.openpomodoro.controllers;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Controls the pomodoro clock
 * Created by dielson on 18/12/15.
 */
public class PomodoroController {
    // Static default values ---------------------------------------------------
    private static final int DEFAULT_POMODORO_TIME = 25;
    private static final int DEFAULT_REST_TIME = 5;
    private static final int DEFAULT_LONG_REST_TIME = 20;
    private static final int DEFAULT_EXTENDED_TIME = 3;
    private static final int DEFAULT_LONG_REST_FREQUENCY = 4;
    private PomodoroListener mListener;
    // Configurable times ------------------------------------------------------
    private int mPomodoroTime;
    private int mRestTime;
    private int mLongRestTime;
    private int mExtendedTime;
    private int mLongRestFrequency;
    private int mPomodoroCount;

    private Timer mTimer;
    private boolean mIsRunning;
    private IntervalType mCurrentIntervalType;

    public PomodoroController() {
        mPomodoroTime = DEFAULT_POMODORO_TIME;
        mRestTime = DEFAULT_REST_TIME;
        mLongRestTime = DEFAULT_LONG_REST_TIME;
        mExtendedTime = DEFAULT_EXTENDED_TIME;
        mLongRestFrequency = DEFAULT_LONG_REST_FREQUENCY;
        mCurrentIntervalType = IntervalType.POMODORO;
        mPomodoroCount = 0;
    }

    // Getters & setters -------------------------------------------------------

    public int getPomodoroTime() { return mPomodoroTime; }

    public int getRestTime() { return mRestTime; }

    public int getLongRestTime() { return mLongRestTime; }

    public int getExtendedTime() { return mExtendedTime; }

    public int getPomodoroCount() { return mPomodoroCount; }

    public IntervalType getCurrentIntervalType() { return mCurrentIntervalType; }

    public boolean isRunning() { return mIsRunning; }

    public void setPomodoroTime(int pomodoroTime) {
        mPomodoroTime = pomodoroTime;
    }

    public void setRestTime(int restTime) {
        mRestTime = restTime;
    }

    public void setLongRestTime(int longRestTime) {
        mLongRestTime = longRestTime;
    }

    public void setExtendedTime(int extendedTime) {
        mExtendedTime = extendedTime;
    }

    public void setPomodoroListener(PomodoroListener listener) {
        mListener = listener;
    }

    // Behavior methods --------------------------------------------------------

    /**
     * Starts the pomodoro.
     */
    public void start() {
        mIsRunning = true;
        schedulePomodoro();
    }

    /**
     * Advances the clock to the next type: if it's a pomodoro, to a rest and if
     * it's a rest, to a pomodoro.
     */
    public void skip() {
        if (!mIsRunning) {
            return;
        }
        // skips to the next interval
        mTimer.cancel();
        mTimer.purge();
        if (mCurrentIntervalType == IntervalType.POMODORO) {
            scheduleRest();
        } else {
            schedulePomodoro();
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
    }

    // Private methods ---------------------------------------------------------

    private void schedulePomodoro() {
        mTimer = new Timer();
        mCurrentIntervalType = IntervalType.POMODORO;
        PomodoroTask pomodoroTask = new PomodoroTask(mListener, mPomodoroTime);
        mTimer.schedule(pomodoroTask, 1000, 1000);
        mPomodoroCount += 1;
    }

    private void scheduleRest() {
        mTimer = new Timer();
        if (mPomodoroCount == mLongRestFrequency) {
            PomodoroTask longRestTask = new PomodoroTask(mListener, mLongRestTime);
            mTimer.schedule(longRestTask, 1000, 1000);
            mCurrentIntervalType = IntervalType.LONG_REST;
            mPomodoroCount = 0; // start again
        } else {
            PomodoroTask restTask = new PomodoroTask(mListener, mRestTime);
            mTimer.schedule(restTask, 1000, 1000);
            mCurrentIntervalType = IntervalType.REST;
        }
    }

    // Enums -------------------------------------------------------------------

    public enum IntervalType {
        POMODORO,
        REST,
        LONG_REST
    }

    private class PomodoroTask extends TimerTask {
        private PomodoroListener mListener;
        private long mCountdown;
        public PomodoroTask(PomodoroListener listener, long countdown) {
            mListener = listener;
            mCountdown = countdown;
        }
        @Override
        public void run() {
            if (mCountdown > 0) {
                mCountdown = mCountdown - 1;
                mListener.onTimeUpdated(mCountdown);
            } else {
                mListener.onTimeFinished();
            }
        }
    }
}
