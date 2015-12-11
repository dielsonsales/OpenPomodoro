package me.dielsonsales.app.openpomodoro.components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * This class is supposed to draw a clock circle
 * Created by dielson on 02/12/15.
 */
public class ClockCanvas extends View {

    // Abstract members
    static final String TAG = "ClockCanvas";
    static final double MINUTE_LENGTH_MOD = 0.8; // minutes pointer size
    static final double HOUR_LENGTH_MOD = 0.5; // hours pointer size
    static final int CURRENT_THICKNESS = 20; // current pomodoro mark thickness
    static final int BORDER_THICKNESS = 15;
    static public int CLOCK_COLOR;
    static public int POINTERS_COLOR;
    static public int INTERVAL_COLOR;
    static public int POMODORO_COLOR;
    private static Paint mPaint;
    private int mViewWidth;
    private int mViewHeight;
    private int mClockRadius;

    // Elements of the clock
    private int mHours;
    private int mMinutes;
    private List<Interval> mIntervals;
    private Interval mCurrentPomodoro;

    // constructor ---------------------------------------------------------------------------------

    public ClockCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mIntervals =  new ArrayList<>();
        CLOCK_COLOR = Color.RED;
        POINTERS_COLOR = Color.WHITE;
        INTERVAL_COLOR = Color.GRAY;
        POINTERS_COLOR = Color.YELLOW;
    }

    // getters & setters ---------------------------------------------------------------------------

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

    public void setClockColor() {

    }

    // overriden methods ---------------------------------------------------------------------------

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

        // Draws a time interval
        Calendar startInterval = new GregorianCalendar();
        startInterval.set(Calendar.HOUR, 0);
        startInterval.set(Calendar.MINUTE, 0);
        Calendar endInterval = new GregorianCalendar();
        endInterval.set(Calendar.HOUR, 1);
        endInterval.set(Calendar.MINUTE, 0);
        drawInterval(startInterval, endInterval, canvas);

        // Draws current pomodoro
        Calendar startTime = new GregorianCalendar();
        startTime.set(Calendar.MINUTE, 30);
        int pomodoroTime = 25;
        drawCurrentPomodoro(startTime, pomodoroTime, canvas);

        // Draws the pointers in the current time
        Calendar currentTime = Calendar.getInstance();
        setTime(currentTime);
        drawClockPointers(mHours, mMinutes, canvas);

        drawClockBorder(canvas);
    }

    // private methods -----------------------------------------------------------------------------

    /**
     * Draws the clock hours and minutes pointers based on the hours and minutes values passed.
     * @param hours the current hour
     * @param minutes the current minutes
     * @param canvas the canvas where the pointers will be drawn
     */
    private void drawClockPointers(int hours, int minutes, Canvas canvas) {
        drawHour(hours, minutes, canvas);
        drawMinutes(minutes, canvas);
        drawCentralCircle(canvas);
    }

    /**
     * Draws the hour pinter in the clock
     * @param hours the current hour
     * @param minutes the current minutes
     * @param canvas the canvas where the pointer will be drawn
     */
    private void drawHour(int hours, int minutes, Canvas canvas) {
        double degreesAngle = calculateHourAngle(hours, minutes);
        double radiansAngle = degreesToRadians(degreesAngle);
        double hourBraceLength = mClockRadius * HOUR_LENGTH_MOD;
        int x1 = (int) (mViewWidth /2 + hourBraceLength * Math.sin(radiansAngle));
        int y1 = (int) (mViewHeight /2 - hourBraceLength * Math.cos(radiansAngle));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(5 * getResources().getDisplayMetrics().density);
        mPaint.setColor(POINTERS_COLOR);
        canvas.drawLine(mViewWidth / 2, mViewHeight / 2, x1, y1, mPaint);
    }

    /**
     * Draws the minutes pointer in the clock
     * @param minutes the current minutes
     * @param canvas the canvas where the pointer will be drawn
     */
    private void drawMinutes(int minutes, Canvas canvas) {
        double degreesAngle = calculateMinutesAngle(minutes);
        double radiansAngle = degreesToRadians(degreesAngle);
        double minutesBraceLength = mClockRadius * MINUTE_LENGTH_MOD;
        int x1 = (int) (mViewWidth /2 + minutesBraceLength * Math.sin(radiansAngle));
        int y1 = (int) (mViewHeight /2 - minutesBraceLength * Math.cos(radiansAngle));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(5 * getResources().getDisplayMetrics().density);
        mPaint.setColor(POINTERS_COLOR);
        canvas.drawLine(mViewWidth / 2, mViewHeight / 2, x1, y1, mPaint);
    }

    private void drawCentralCircle(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(POINTERS_COLOR);
        canvas.drawCircle(mViewWidth / 2, mViewHeight / 2, 5 * getResources().getDisplayMetrics().density, mPaint);
    }

    /**
     * Draws an interval of executed tasks
     * @param startTime the time when the task began
     * @param endTime the time when the task was finished
     * @param canvas the canvas where the interval will be drawn
     */
    private void drawInterval(Calendar startTime, Calendar endTime, Canvas canvas) {
        int x1 = mViewWidth /2 - mClockRadius /2;
        int y1 = mViewHeight /2 - mClockRadius /2;
        int x2 = mViewWidth /2 + mClockRadius /2;
        int y2 = mViewHeight /2 + mClockRadius /2;
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.BUTT);
        mPaint.setStrokeWidth(15 * getResources().getDisplayMetrics().density);
        mPaint.setColor(INTERVAL_COLOR);
        int startAngle = (int) (calculateHourAngle(startTime.get(Calendar.HOUR), startTime.get(Calendar.MINUTE)));
        int endAngle = (int) (calculateHourAngle(endTime.get(Calendar.HOUR), endTime.get(Calendar.MINUTE)));
        canvas.drawArc(new RectF(x1, y1, x2, y2), startAngle - 90, endAngle - startAngle, false, mPaint);
    }

    /**
     * Draws the current pomodoro's time
     * @param startTime
     * @param pomodoroTime the time of the pomodoro (usually 25 min.)
     * @param canvas
     */
    private void drawCurrentPomodoro(Calendar startTime, int pomodoroTime, Canvas canvas) {
        int rectSize = mClockRadius - CURRENT_THICKNESS;
        int x1 = mViewWidth /2 - rectSize;
        int y1 = mViewHeight /2 - rectSize;
        int x2 = mViewWidth /2 + rectSize;
        int y2 = mViewHeight /2 + rectSize;
        int startMinutes = startTime.get(Calendar.MINUTE);
        int endMinutes = startMinutes + pomodoroTime;

        int startAngle = (int) calculateMinutesAngle(startMinutes);
        int endAngle = (int) calculateMinutesAngle(endMinutes);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.BUTT);
        mPaint.setStrokeWidth(CURRENT_THICKNESS * getResources().getDisplayMetrics().density);
        mPaint.setColor(POMODORO_COLOR);
        canvas.drawArc(new RectF(x1, y1, x2, y2), startAngle - 90, endAngle - startAngle, false, mPaint);
    }

    /**
     * Draws the clock border
     * @param canvas the canvas where the border will be drawn
     */
    private void drawClockBorder(Canvas canvas) {
        int rectSize = mClockRadius + BORDER_THICKNESS/2;
        int x1 = mViewWidth /2 - rectSize;
        int y1 = mViewHeight /2 - rectSize;
        int x2 = mViewWidth /2 + rectSize;
        int y2 = mViewHeight /2 + rectSize;
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.BUTT);
        mPaint.setStrokeWidth(BORDER_THICKNESS * getResources().getDisplayMetrics().density);
        mPaint.setColor(Color.WHITE);
        canvas.drawArc(new RectF(x1, y1, x2, y2), 0, 360, false, mPaint);
    }

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
    private double calculateMinutesAngle(int minutes) {
        return 2.0 * 180.0 * minutes / 60.0;
    }

    private double degreesToRadians(double degrees) {
        return (Math.PI/180) * degrees;
    }

    /**
     * Represents a time interval that will be drawn as an arc in the clock.
     */
    public class Interval {
        private Calendar mStartTime;
        private Calendar mEndTime;
        public Interval(Calendar startTime, Calendar endTime) {
            mStartTime = startTime;
            mEndTime = endTime;
        }
        public Calendar getStartTime() {
            return mStartTime;
        }
        public Calendar getEndTime() {
            return mEndTime;
        }
    }
}
