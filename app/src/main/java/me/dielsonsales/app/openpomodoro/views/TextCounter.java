package me.dielsonsales.app.openpomodoro.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by dielson on 13/01/16.
 */
public class TextCounter extends View {
    private String mText;

    public TextCounter(Context context, AttributeSet attrs) {
        super(context, attrs);
        mText = "00:00:00";
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i("TextCounter", "Drawing text");
        super.onDraw(canvas);
        Paint strokePaint = new Paint();
        strokePaint.setAntiAlias(true);
        strokePaint.setColor(Color.BLACK);
        strokePaint.setTextAlign(Paint.Align.CENTER);
        strokePaint.setTextSize(100);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(3 * getResources().getDisplayMetrics().density);

        Paint textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(100);

        canvas.drawText(mText, getWidth()/2, getHeight(), strokePaint);
        canvas.drawText(mText, getWidth()/2, getHeight(), textPaint);
    }
}
