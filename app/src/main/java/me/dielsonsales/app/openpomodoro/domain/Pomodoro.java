package me.dielsonsales.app.openpomodoro.domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A pomodoro represents a single time interval, be it a work interval or a
 * rest/long-rest interval.
 */
public class Pomodoro {
    public enum PomodoroType {
        WORK,
        REST,
        LONG_REST,
        NONE // when pomodoro is stopped
    }
    private Calendar mStarted;
    private PomodoroType mType;
    private int mRemainingTime;

    public Pomodoro(Calendar started, PomodoroType type, int initialTime) {
        mStarted = started;
        mType = type;
        mRemainingTime = initialTime;
    }

    public Pomodoro(PomodoroType type, int initialTime) {
        this(Calendar.getInstance(), type, initialTime);
    }

    // Getters -----------------------------------------------------------------

    public Calendar getStartedTime() {
        return mStarted;
    }

    public PomodoroType getPomodoroType() {
        return mType;
    }

    public int getRemainingTime() {
        return mRemainingTime;
    }

    // Operations --------------------------------------------------------------

    public void decrement() {
        mRemainingTime = mRemainingTime - 1;
    }

    // Overridden --------------------------------------------------------------

    /**
     * Returns a brief description of this Pomodoro, in the following format:
     *
     * [Pomodoro: startedTime=10:35:24, mType=WORK, mRemainingTime=78s]
     */
    @Override
    public String toString() {
        DateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String pomodoroString = "[Pomodoro: ";
        pomodoroString += "startedTime=" + simpleDateFormat.format(mStarted.getTime()) + ", ";
        pomodoroString += "type=" + mType.toString() + ", ";
        pomodoroString += "remainingTime=" + String.valueOf(mRemainingTime) + "s]";
        return pomodoroString;
    }
}
