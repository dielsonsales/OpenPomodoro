package me.dielsonsales.app.openpomodoro.util;

import java.util.Calendar;

/**
 * Represents a time interval with a start and an end.
 */
public class Duration {
    private Calendar mStartTime;
    private Calendar mEndTime;
    public Duration(Calendar startTime, Calendar endTime) {
        mStartTime = startTime;
        mEndTime = endTime;
    }

    public Calendar getStartTime() { return mStartTime; }

    public Calendar getEndTime() { return mEndTime; }
}