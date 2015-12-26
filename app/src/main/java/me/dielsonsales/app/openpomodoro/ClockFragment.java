package me.dielsonsales.app.openpomodoro;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

import me.dielsonsales.app.openpomodoro.views.ClockCanvas;

public class ClockFragment extends Fragment {

    private static final String TAG = "ClockFragment";
    private ClockCanvas mClockCanvas;

    private OnFragmentInteractionListener mListener;

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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    // ClockFragment methods ---------------------------------------------------

    public void setCurrentPomodoro(Calendar startTime, Calendar endTime) {
        mClockCanvas.addCurrentPomodoro(startTime, endTime);
    }

    /**
     * Forces the clock to be drawn again.
     */
    public void updateClock() {
        mClockCanvas.invalidate();
    }
}
