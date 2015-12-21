package me.dielsonsales.app.openpomodoro.controllers;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Controls the pomodoro clock
 * Created by dielson on 18/12/15.
 */
public class PomodoroController extends TimerTask {
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
    private IntervalType mCurrentIntervalType;

    public PomodoroController() {
        mPomodoroTime = DEFAULT_POMODORO_TIME;
        mRestTime = DEFAULT_REST_TIME;
        mLongRestTime = DEFAULT_LONG_REST_TIME;
        mExtendedTime = DEFAULT_EXTENDED_TIME;
        mLongRestFrequency = DEFAULT_LONG_REST_FREQUENCY;
        mTimer = new Timer();
        mCurrentIntervalType = IntervalType.POMODORO;
        mPomodoroCount = 0;
    }

    // TimerTask methods -------------------------------------------------------

    /**
     * Updates the pomodoro status
     */
    @Override
    public void run() {
        mListener.onTimeOver();
    }

    // Getters & setters -------------------------------------------------------

    public int getPomodoroTime() { return mPomodoroTime; }

    public int getRestTime() { return mRestTime; }

    public int getLongRestTime() { return mLongRestTime; }

    public int getExtendedTime() { return mExtendedTime; }

    public int getPomodoroCount() { return mPomodoroCount; }

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
        schedulePomodoro();
    }

    /**
     * Advances the clock to the next type: if it's a pomodoro, to a rest and if
     * it's a rest, to a pomodoro.
     */
    public void skip() {
        mTimer.cancel();
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
        mPomodoroCount = 0; // restart the count
        mTimer.cancel();
    }

    // Private methods ---------------------------------------------------------

    private void schedulePomodoro() {
        mTimer.schedule(this, mPomodoroTime * 1000);
    }

    private void scheduleRest() {
        if (mPomodoroCount == mLongRestFrequency) {
            // TODO schedule long pause
            mPomodoroCount = 0;
        }
    }

    // Enums -------------------------------------------------------------------

    private enum IntervalType {
        POMODORO,
        REST,
        LONG_REST
    }
}
