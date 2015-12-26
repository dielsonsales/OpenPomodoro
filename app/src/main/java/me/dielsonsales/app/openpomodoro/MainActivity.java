package me.dielsonsales.app.openpomodoro;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import me.dielsonsales.app.openpomodoro.util.FormattingUtils;

/**
 * The main activity containing the visual clock. This class is charged of
 * displaying and updating the view according to the data in the Service.
 */
public class MainActivity extends AppCompatActivity implements ClockFragment.OnFragmentInteractionListener {

    private static final String TAG = "MainActivity";
    private ClockFragment mClockFragment;
    private PomodoroService mService;
    private boolean mIsBound;

    // UI Components -----------------------------------------------------------
    private TextView mCountdownText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.title_activity_main));
        setSupportActionBar(toolbar);

        mClockFragment = (ClockFragment) getSupportFragmentManager().findFragmentById(R.id.clock_fragment);

        mIsBound = false;
        mCountdownText = (TextView) findViewById(R.id.countdownText);

        // Play button
        Button playButton = (Button) findViewById(R.id.play_button);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsBound) {
                    startPomodoro();
                }
            }
        });

        // Stop button
        Button stopButton = (Button) findViewById(R.id.stop_button);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsBound) {
                    stopPomodoro();
                }
            }
        });
    }

    /**
     * Binds the class to the service.
     */
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, PomodoroService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * Stops the activity and unbinds it from the PomodoroService.
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (mIsBound) {
            Log.i(TAG, "Unbinding service");
            unbindService(mConnection);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // TODO: implement this?
        throw new UnsupportedOperationException("This was not supposed to be called yet");
    }

    // Private methods ---------------------------------------------------------

    private void startPomodoro() {
        if (!mService.isRunning()) {
            Intent intent = new Intent(this, PomodoroService.class);
            startService(intent);
        }
    }

    private void stopPomodoro() {
        if (mService.isRunning()) {
            mService.stopPomodoro();
        }
    }

    /**
     * Updates the whole UI with the news parameters
     * @param bundle the updated data to display
     */
    public void updateUI(Bundle bundle) {
        long countdown = bundle.getLong("countdown");
        boolean isStarting = bundle.getBoolean("isStarting");
        mCountdownText.setText(FormattingUtils.getDisplayTime(countdown));
        if (isStarting) {
            Calendar startTime = Calendar.getInstance();
            Calendar endTime = (Calendar) startTime.clone();
            endTime.add(Calendar.SECOND, (int) countdown);
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            Log.i(TAG, "From " + formatter.format(startTime.getTime()) + " to " + formatter.format(endTime.getTime()));
            mClockFragment.setCurrentPomodoro(startTime, endTime);
        }
        mClockFragment.updateClock();
    }

    // Service connection ------------------------------------------------------

    private ServiceConnection mConnection = new ServiceConnection() {
        /**
         * Retrieves the service instance and start listening to updates.
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PomodoroService.LocalBinder binder = (PomodoroService.LocalBinder) service;
            mService = binder.getService();
            mIsBound = true;
            mService.setUpdateListener(new PomodoroService.UpdateListener() {
                @Override
                public void onUpdate(Bundle bundle) {
                    updateUI(bundle);
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mIsBound = false;
        }
    };
}
