package me.dielsonsales.app.openpomodoro;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.dielsonsales.app.openpomodoro.util.Duration;
import me.dielsonsales.app.openpomodoro.views.ClockCanvas;

public class ClockFragment extends Fragment {

    private static final String TAG = "ClockFragment";
    private ClockCanvas mClockCanvas;

    public ClockFragment() {
        // Required empty public constructor
    }

    // Fragment methods --------------------------------------------------------

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_clock, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mClockCanvas = (ClockCanvas) getView().findViewById(R.id.clockCanvas);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateClock();
    }

    // ClockFragment methods ---------------------------------------------------

    public Duration getDuration() { return mClockCanvas.getDuration(); }

    public void setDuration(Duration duration) { mClockCanvas.addDuration(duration); }

    /**
     * Forces the clock to be drawn again.
     */
    public void updateClock() { mClockCanvas.invalidate(); }
}
