package me.dielsonsales.app.openpomodoro.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import me.dielsonsales.app.openpomodoro.R;
import me.dielsonsales.app.openpomodoro.data.Duration;

/**
 * This class is supposed to draw a clock circle
 * Created by dielson on 02/12/15.
 */
public class ClockCanvas extends View {

    // Abstract members
    static final private String TAG = "ClockCanvas";
    static final private double MINUTE_POINTER_MODIFIER = 0.8;
    static final private double HOUR_POINTER_MODIFIER = 0.5;
    static final private int POMODORO_THICKNESS = 20;
    static final private int CLOCK_BORDER_THICKNESS = 15;
    static public int CLOCK_COLOR;
    static public int POINTERS_COLOR;
    static public int INTERVALS_COLOR;
    static public int POMODORO_COLOR;
    private static Paint mPaint;
    private int mViewWidth;
    private int mViewHeight;
    private int mClockRadius;

    // Elements of the clock
    private int mHours;
    private int mMinutes;
    private List<Duration> mIntervals;
    private Duration mDuration;

    // constructor -------------------------------------------------------------

    public ClockCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mIntervals =  new ArrayList<>();

        CLOCK_COLOR = ContextCompat.getColor(context, R.color.colorKoi);
        POINTERS_COLOR = ContextCompat.getColor(context, R.color.colorFabulan);
        INTERVALS_COLOR = ContextCompat.getColor(context, R.color.colorAquent);
        POMODORO_COLOR = ContextCompat.getColor(context, R.color.colorCopper);

//        List<Duration> durations = new ArrayList<>();
//        Calendar startInterval = new GregorianCalendar();
//        startInterval.set(Calendar.HOUR, 11);
//        startInterval.set(Calendar.MINUTE, 58);
//        Calendar endInterval = new GregorianCalendar();
//        endInterval.set(Calendar.HOUR, 0);
//        endInterval.set(Calendar.MINUTE, 22);
//        durations.add(new Duration(startInterval, endInterval));
//        this.addIntervals(durations);
    }

    // getters & setters -------------------------------------------------------

    /**
     * Sets the clock time.
     * @param time the time that the clock will show
     */
    public void setTime(Calendar time) {
        mHours = time.get(Calendar.HOUR);
        mMinutes = time.get(Calendar.MINUTE);
    }

    /**
     * Returns the clock current time.
     * @return the time shown in the clock
     */
    public Calendar getTime() {
        Calendar time = Calendar.getInstance();
        time.set(Calendar.HOUR, mHours);
        time.set(Calendar.MINUTE, mMinutes);
        return time;
    }

    public void addIntervals(List<Duration> intervals) {
        mIntervals.addAll(intervals);
    }

    public void clearIntervals() {
        mIntervals.clear();
    }

    /**
     * Sets the current duration arc to be shown in the clock.
     * @param duration
     */
    public void addDuration(Duration duration) {
        mDuration = duration;
    }

    public Duration getDuration() {
        return mDuration;
    }

    /**
     * Removes the current duration arc shwon in the clock.
     */
    public void clearDuration() { mDuration = null; }

    // overriden methods -------------------------------------------------------

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mViewWidth = getWidth();
        mViewHeight = getHeight();
        int smallerMetric = mViewWidth < mViewHeight ? mViewWidth : mViewHeight;
        mClockRadius = smallerMetric/2 - smallerMetric/2 * 20/100; // smallestMetric - 20%
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(CLOCK_COLOR);
        canvas.drawCircle(mViewWidth / 2, mViewHeight / 2, mClockRadius, mPaint);

        // Draws the timeIntervals
        drawInterval(canvas);

        // Draws the current pomodoro
        if (mDuration != null) {
            drawCurrentPomodoro(canvas);
        }

        // Draws the pointers in the current time
        Calendar currentTime = Calendar.getInstance();
        setTime(currentTime);
        drawClockPointers(mHours, mMinutes, canvas);

        drawClockBorder(canvas);
    }

    // draw methods --------------------------------------------------------------------------------

    /**
     * Draws the clock hours and minutes pointers based on the hours and minutes values passed.
     * @param hours the current hour
     * @param minutes the current minutes
     * @param canvas the canvas where the pointers will be drawn
     */
    private void drawClockPointers(int hours, int minutes, Canvas canvas) {
        // Draw hour
        double degreesAngle = calculateHourAngle(hours, minutes);
        double radiansAngle = degreesToRadians(degreesAngle);
        double hourBraceLength = mClockRadius * HOUR_POINTER_MODIFIER;
        int x1 = (int) (mViewWidth /2 + hourBraceLength * Math.sin(radiansAngle));
        int y1 = (int) (mViewHeight /2 - hourBraceLength * Math.cos(radiansAngle));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(5 * getResources().getDisplayMetrics().density);
        mPaint.setColor(POINTERS_COLOR);
        canvas.drawLine(mViewWidth / 2, mViewHeight / 2, x1, y1, mPaint);

        // Draw minutes
        degreesAngle = calculateMinutesAngle(minutes);
        radiansAngle = degreesToRadians(degreesAngle);
        double minutesBraceLength = mClockRadius * MINUTE_POINTER_MODIFIER;
        x1 = (int) (mViewWidth /2 + minutesBraceLength * Math.sin(radiansAngle));
        y1 = (int) (mViewHeight /2 - minutesBraceLength * Math.cos(radiansAngle));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(5 * getResources().getDisplayMetrics().density);
        mPaint.setColor(POINTERS_COLOR);
        canvas.drawLine(mViewWidth / 2, mViewHeight / 2, x1, y1, mPaint);

        // Draw central circle
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(POINTERS_COLOR);
        canvas.drawCircle(mViewWidth / 2, mViewHeight / 2, 5 * getResources().getDisplayMetrics().density, mPaint);
    }

    /**
     * Draws an interval of executed tasks
     * @param canvas the canvas where the interval will be drawn
     */
    private void drawInterval(Canvas canvas) {
        for (Duration duration : mIntervals) {
            Calendar startTime = duration.getStartTime();
            Calendar endTime = duration.getEndTime();

            int x1 = mViewWidth /2 - mClockRadius /2;
            int y1 = mViewHeight /2 - mClockRadius /2;
            int x2 = mViewWidth /2 + mClockRadius /2;
            int y2 = mViewHeight /2 + mClockRadius /2;
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeCap(Paint.Cap.BUTT);
            mPaint.setStrokeWidth(15 * getResources().getDisplayMetrics().density);
            mPaint.setColor(INTERVALS_COLOR);
            int startAngle = (int) (calculateHourAngle(startTime.get(Calendar.HOUR), startTime.get(Calendar.MINUTE)));
            int endAngle = (int) (calculateHourAngle(endTime.get(Calendar.HOUR), endTime.get(Calendar.MINUTE)));
            if (endAngle < startAngle) {
                endAngle += startAngle;
            }
            canvas.drawArc(new RectF(x1, y1, x2, y2), startAngle - 90, endAngle - startAngle, false, mPaint);
        }
    }

    /**
     * Draws the current pomodoro's time
     */
    private void drawCurrentPomodoro(Canvas canvas) {
        int rectSize = mClockRadius - POMODORO_THICKNESS;
        int x1 = mViewWidth /2 - rectSize;
        int y1 = mViewHeight /2 - rectSize;
        int x2 = mViewWidth /2 + rectSize;
        int y2 = mViewHeight /2 + rectSize;
        int startMinutes = mDuration.getStartTime().get(Calendar.MINUTE);
        int endMinutes = mDuration.getEndTime().get(Calendar.MINUTE);
        if (endMinutes < startMinutes) {
            endMinutes += startMinutes;
        }

        int startAngle = (int) calculateMinutesAngle(startMinutes);
        int endAngle = (int) calculateMinutesAngle(endMinutes);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.BUTT);
        mPaint.setStrokeWidth(POMODORO_THICKNESS * getResources().getDisplayMetrics().density);
        mPaint.setColor(POMODORO_COLOR);
        canvas.drawArc(new RectF(x1, y1, x2, y2), startAngle - 90, endAngle - startAngle, false, mPaint);
    }

    /**
     * Draws the clock border
     * @param canvas the canvas where the border will be drawn
     */
    private void drawClockBorder(Canvas canvas) {
        int rectSize = mClockRadius + CLOCK_BORDER_THICKNESS /2;
        int x1 = mViewWidth /2 - rectSize;
        int y1 = mViewHeight /2 - rectSize;
        int x2 = mViewWidth /2 + rectSize;
        int y2 = mViewHeight /2 + rectSize;
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.BUTT);
        mPaint.setStrokeWidth(CLOCK_BORDER_THICKNESS * getResources().getDisplayMetrics().density);
        mPaint.setColor(Color.WHITE);
        canvas.drawArc(new RectF(x1, y1, x2, y2), 0, 360, false, mPaint);
    }

    // utils methods -------------------------------------------------------------------------------

    /**
     * Calculates the hours pointer's angle, in degrees.
     * @param hour the current hour
     * @param minutes the current minutes
     * @return the angle of the hour pointer to be drawn
     */
    private double calculateHourAngle(int hour, int minutes) {
        double decimalMinutes = minutes / 60.0;
        double decimalHour = hour + decimalMinutes;
        return 2.0 * 180.0 * decimalHour / 12.0;
    }

    /**
     * Calculates the minutes pointer's angle, in degrees
     * @param minutes the current minutes
     * @return the angle of the minutes pointer to be drawn
     */
    private double calculateMinutesAngle(int minutes) { return 2.0 * 180.0 * minutes / 60.0; }

    /**
     * Converts a degrees angle value to its corresponding radian value.
     * @param degrees the value in degrees
     * @return the corresponding angle in radians
     */
    private double degreesToRadians(double degrees) { return (Math.PI/180) * degrees; }
}
