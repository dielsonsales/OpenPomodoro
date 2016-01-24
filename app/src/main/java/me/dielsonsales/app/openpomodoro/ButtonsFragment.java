package me.dielsonsales.app.openpomodoro;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Represents the buttons in the MainActivity.
 */
public class ButtonsFragment extends Fragment {
    private ImageView mPlayButton;
    private ImageView mSkipButton;
    private ImageView mStopButton;
    private FragmentInteractionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buttons, container, false);
        mPlayButton = (ImageView) view.findViewById(R.id.play_button);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onPlayButtonClicked();
                    enableRunningButtons();
                }
            }
        });
        mSkipButton = (ImageView) view.findViewById(R.id.skip_button);
        mSkipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onSkipButtonClicked();
                }
            }
        });
        mStopButton = (ImageView) view.findViewById(R.id.stop_button);
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onStopButtonClicked();
                    disableRunningButtons();
                }
            }
        });
        return view;
    }

    /**
     * Configures the Activity to listen to this component. Any Activity that
     * attaches this Fragment must implement FragmentInteractionListener.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentInteractionListener) {
            mListener = (FragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Hides the play button and shows the skip and stop buttons.
     */
    public void enableRunningButtons() {
        mPlayButton.setVisibility(View.GONE);
        mSkipButton.setVisibility(View.VISIBLE);
        mStopButton.setVisibility(View.VISIBLE);
    }

    /**
     * Hides the skip and stop buttons and shows the play button again.
     */
    public void disableRunningButtons() {
        mPlayButton.setVisibility(View.VISIBLE);
        mSkipButton.setVisibility(View.GONE);
        mStopButton.setVisibility(View.GONE);
    }

    // Interfaces --------------------------------------------------------------

    /**
     * Interface to receive signals from this fragment. Any activity that
     * attaches this fragment must implement this interface in order to know
     * when the buttons were clicked.
     */
    public interface FragmentInteractionListener {
        void onPlayButtonClicked();
        void onSkipButtonClicked();
        void onStopButtonClicked();
    }
}
