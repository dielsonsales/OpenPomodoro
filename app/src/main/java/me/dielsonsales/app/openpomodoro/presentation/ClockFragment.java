package me.dielsonsales.app.openpomodoro.presentation;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.dielsonsales.app.openpomodoro.R;
import me.dielsonsales.app.openpomodoro.domain.Duration;
import me.dielsonsales.app.openpomodoro.presentation.views.ClockCanvas;

public class ClockFragment extends Fragment {

    private static final String TAG = "ClockFragment";
    private ClockCanvas mClockCanvas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clock, container, false);
        mClockCanvas = (ClockCanvas) view.findViewById(R.id.clock_canvas);
        return  view;
    }

    /**
     * Every time the clock is back to the foreground, it is redrawn.
     */
    @Override
    public void onResume() {
        super.onResume();
        updateClock();
    }

    // ClockFragment methods ---------------------------------------------------

    /**
     * Returns the current trail duration
     */
    public Duration getDuration() { return mClockCanvas.getDuration(); }

    /**
     * The current pomodoro trail
     */
    public void setDuration(Duration duration) { mClockCanvas.addDuration(duration); }


    public void setIsRest(boolean isRest) {
        Context context = getContext();
        if (isRest) {
            ClockCanvas.POMODORO_COLOR = ContextCompat.getColor(context, R.color.colorAquent);
        } else {
            ClockCanvas.POMODORO_COLOR = ContextCompat.getColor(context, R.color.colorCopper);
        }
    }

    /**
     * Forces the clock to be drawn again.
     */
    public void updateClock() { mClockCanvas.invalidate(); }
}
