package me.dielsonsales.app.openpomodoro.controllers;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Controls the pomodoro clock
 * Created by dielson on 18/12/15.
 */
public class PomodoroController extends TimerTask {
    private static final int DEFAULT_POMODORO_TIME = 25;
    private static final int DEFAULT_REST_TIME = 5;
    private static final int DEFAULT_LONG_REST_TIME = 20;
    private static final int DEFAULT_EXTENDED_TIME = 3;
    private int mPomodoroTime;
    private int mRestTime;
    private int mLongRestTime;
    private int mExtendedTime;
    private Timer mTimer;
    private PomodoroListener mListener;

    public PomodoroController() {
        mPomodoroTime = DEFAULT_POMODORO_TIME;
        mRestTime = DEFAULT_REST_TIME;
        mLongRestTime = DEFAULT_LONG_REST_TIME;
        mExtendedTime = DEFAULT_EXTENDED_TIME;
        mTimer = new Timer();
    }

    // TimerTask methods -------------------------------------------------------

    /**
     * Testing.
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

    public void start() {
        mTimer.schedule(this, mPomodoroTime * 1000);
    }

    public void skip() {
        throw new UnsupportedOperationException();
    }

    public void stop() {
        throw new UnsupportedOperationException();
    }
}
