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

/**
 * The main activity containing the visual clock. This class is charged of
 * managing the view.
 */
public class MainActivity extends AppCompatActivity implements ClockFragment.OnFragmentInteractionListener {

    private static final String TAG = "MainActivity";
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

        mIsBound = false;

        startPomodoroService();

        mCountdownText = (TextView) findViewById(R.id.countdownText);

        // Play button
        Button playButton = (Button) findViewById(R.id.play_button);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsBound) {
                    if (!mService.isRunning()) {
                        mService.startPomodoro();
                    } else {
                        Log.i(TAG, "Service is already running");
                    }

                }
            }
        });

        // Stop button
        Button stopButton = (Button) findViewById(R.id.stop_button);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsBound) {
                    if (mService.isRunning()) {
                        mService.stopPomodoro();
                    } else {
                        Log.i(TAG, "The service is not running, duh!");
                    }
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
        bindService(intent, mConnection, Context.BIND_IMPORTANT);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "Pausing activity");
    }

    /**
     * Stops the activity and unbinds it from the @PomodoroService.
     */
    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "Stopping activity");
        if (mService != null) {
            if (!mService.isRunning()) {
                stopPomodoroService();
            }
        }
        if (mIsBound) {
            unbindService(mConnection);
            mIsBound = false;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // TODO: implement this?
        throw new UnsupportedOperationException("This was not supposed to be called yet");
    }

    // Private methods ---------------------------------------------------------

    private void startPomodoroService() {
        Intent intent = new Intent(this, PomodoroService.class);
        startService(intent);
    }

    private void stopPomodoroService() {
        Intent intent = new Intent(this, PomodoroService.class);
        stopService(intent);
    }

    // Implemented classes -----------------------------------------------------

    private ServiceConnection mConnection = new ServiceConnection() {
        /**
         * Retrieves the service instance and start listening to updates.
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, "onServiceConnected");
            PomodoroService.LocalBinder binder = (PomodoroService.LocalBinder) service;
            mService = binder.getService();
            mIsBound = true;

            mService.setUpdateListener(new PomodoroService.UpdateListener() {
                @Override
                public void onUpdate(final long countDown) {
                    mCountdownText.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.i(TAG, "posting to mCountdownText");
                            mCountdownText.setText(String.valueOf(countDown));
                        }
                    });
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mIsBound = false;
        }
    };
}
