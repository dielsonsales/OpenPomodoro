package me.dielsonsales.app.openpomodoro.controllers;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

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
        LONG_REST
    }

    // Static default values ---------------------------------------------------
    private static final int DEFAULT_POMODORO_TIME = 25;
    private static final int DEFAULT_REST_TIME = 5;
    private static final int DEFAULT_LONG_REST_TIME = 20;
    private static final int DEFAULT_EXTENDED_TIME = 3;
    private static final int DEFAULT_LONG_REST_FREQUENCY = 4;

    // Configurable time values ------------------------------------------------
    private int mPomodoroTime;
    private int mRestTime;
    private int mLongRestTime;
    private int mExtendedTime;
    private int mLongRestFrequency;
    private int mPomodoroCount;

    private Context mContext;
    private Timer mTimer;
    private boolean mIsRunning;
    private long mCounter;
    private IntervalType mCurrentIntervalType;
    private PomodoroListener mListener;
    private static ControllerHandler mHandler;
    private PomodoroSoundManager mSoundManager;

    /**
     * Creates a new PomodoroController instance.
     * @param context the service context
     */
    public PomodoroController(Context context) {
        mContext = context;
        mHandler = new ControllerHandler(this);
        mPomodoroTime = DEFAULT_POMODORO_TIME;
        mRestTime = DEFAULT_REST_TIME;
        mLongRestTime = DEFAULT_LONG_REST_TIME;
        mExtendedTime = DEFAULT_EXTENDED_TIME;
        mLongRestFrequency = DEFAULT_LONG_REST_FREQUENCY;
        mCurrentIntervalType = IntervalType.POMODORO;
        mPomodoroCount = 0;
        mSoundManager = PomodoroSoundManager.getInstance(mContext);
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
        mCounter = mPomodoroTime;
        mIsRunning = true;
        startPomodoroTask();
    }

    /**
     * Advances the clock to the next type: if it's a pomodoro, to a rest and if
     * it's a rest, to a pomodoro.
     */
    public void skip() {
        if (!mIsRunning) {
            return;
        }
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

    public void playAlarm() {
        mSoundManager.playAlarm();
    }

    public void handleMessage() {
        if (mCounter > 0) {
            mCounter = mCounter - 1;
        }
        Bundle bundle = new Bundle();
        bundle.putLong("countdown", mCounter);
        mListener.onTimeUpdated(bundle);
        if (mCounter == 0) {
            playAlarm();
            skip();
        }
    }

    // Private methods ---------------------------------------------------------

    private void startPomodoroTask() {
        mTimer = new Timer();
        PomodoroTask pomodoroTask = new PomodoroTask();
        mTimer.schedule(pomodoroTask, 0, 1000);
    }

    // Handler -----------------------------------------------------------------

    /**
     * Receives messages from the PomodoroTask class in the worker thread and
     * redirects them to the PomodoroController class, in the UI thread.
     */
    static class ControllerHandler extends Handler {
        private WeakReference<PomodoroController> mController;
        public ControllerHandler(PomodoroController controller) {
            mController = new WeakReference<>(controller);
        }

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

    /**
     * Executes in a
     */
    private class PomodoroTask extends TimerTask {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(0);
        }
    }
}
