package me.dielsonsales.app.openpomodoro.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by dielson on 13/01/16.
 */
public class TextCounter extends View {
    private String mText;

    // Constructor -------------------------------------------------------------
    public TextCounter(Context context, AttributeSet attrs) {
        super(context, attrs);
        mText = "00:00:00";
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
        float screenDensity = getResources().getDisplayMetrics().density;
        Paint strokePaint = new Paint();
        strokePaint.setAntiAlias(true);
        strokePaint.setColor(Color.BLACK);
        strokePaint.setTextAlign(Paint.Align.CENTER);
        strokePaint.setTextSize(30 * screenDensity);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(3 * screenDensity);

        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(30 * screenDensity);

        canvas.drawText(mText, getWidth()/2, getHeight() - 10 * screenDensity, strokePaint);
        canvas.drawText(mText, getWidth()/2, getHeight() - 10 * screenDensity, textPaint);
    }
}
