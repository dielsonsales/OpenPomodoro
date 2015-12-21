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
    private boolean mBound;

    // UI Components -----------------------------------------------------------
    private TextView mCountdownText;
    private Button mPlayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.title_activity_main));
        setSupportActionBar(toolbar);

        mBound = false;

        startPomodoroService();

        mCountdownText = (TextView) findViewById(R.id.countdownText);

        mPlayButton = (Button) findViewById(R.id.play_button);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBound) {
                    mService.startPomodoro();
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
     * Stops the activity and unbinds it from the @PomodoroService.
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
        stopPomodoroService();
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
         * @param name
         * @param service
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PomodoroService.LocalBinder binder = (PomodoroService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;

            mService.setUpdateListener(new PomodoroService.UpdateListener() {
                @Override
                public void onUpdate(final long countDown) {
                    mCountdownText.post(new Runnable() {
                        @Override
                        public void run() {
                            mCountdownText.setText(String.valueOf(countDown));
                        }
                    });
//                    mCountdownText.setText(String.valueOf(countDown));
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };
}
