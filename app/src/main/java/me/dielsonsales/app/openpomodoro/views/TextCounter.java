package me.dielsonsales.app.openpomodoro.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import me.dielsonsales.app.openpomodoro.R;

/**
 * Created by dielson on 13/01/16.
 */
public class TextCounter extends View {
    private String mText;
    private Paint strokePaint;
    private Paint textPaint;
    private float mScreenDensity;

    // Constructor -------------------------------------------------------------
    public TextCounter(Context context, AttributeSet attrs) {
        super(context, attrs);
        mText = "00:00:00";
        mScreenDensity = getResources().getDisplayMetrics().density;

        strokePaint = new Paint();
        strokePaint.setAntiAlias(true);
        strokePaint.setColor(ContextCompat.getColor(getContext(), R.color.colorDarkGrey));
        strokePaint.setTextAlign(Paint.Align.CENTER);
        strokePaint.setTextSize(30 * mScreenDensity);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(3 * mScreenDensity);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(30 * mScreenDensity);
    }

    // Getters & setters -------------------------------------------------------

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(mText, getWidth()/2, getHeight() - 10 * mScreenDensity, strokePaint);
        canvas.drawText(mText, getWidth()/2, getHeight() - 10 * mScreenDensity, textPaint);
    }
}
