package me.dielsonsales.app.openpomodoro;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.Calendar;

import me.dielsonsales.app.openpomodoro.domain.Duration;
import me.dielsonsales.app.openpomodoro.presentation.BaseActivity;
import me.dielsonsales.app.openpomodoro.util.FormattingUtils;
import me.dielsonsales.app.openpomodoro.views.BorderTextView;

/**
 * The main activity containing the visual clock. This class is charged of
 * displaying and updating the view according to the data in the Service.
 */
public class MainActivity extends BaseActivity implements ButtonsFragment.FragmentInteractionListener {

    private static final String TAG = "MainActivity";
    private PomodoroService mService;
    private boolean mIsBound;

    // Fragments ---------------------------------------------------------------
    private ClockFragment mClockFragment;
    private ButtonsFragment mButtonsFragment;

    // UI Components -----------------------------------------------------------
    private BorderTextView mCountdownText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar(R.id.toolbar, R.string.title_activity_main);

        mClockFragment = (ClockFragment) getSupportFragmentManager().findFragmentById(R.id.clock_fragment);
        mCountdownText = (BorderTextView) findViewById(R.id.countdownText);
        mButtonsFragment = (ButtonsFragment) getSupportFragmentManager().findFragmentById(R.id.buttons_fragment);

        mIsBound = false;
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
            unbindService(mConnection);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                break;
            default:
                break;
        }
        return true;
    }

    // Private methods ---------------------------------------------------------

    private void startPomodoro() {
        if (!mService.isRunning()) {
            Intent intent = new Intent(this, PomodoroService.class);
            startService(intent);
            mService.stopPomodoro();
            mButtonsFragment.enableRunningButtons();
        }
    }

    private void skipPomodoro() {
        if (mService.isRunning()) {
            mService.skipPomodoro();
        }
    }

    private void stopPomodoro() {
        if (mService.isRunning()) {
            mService.stopPomodoro();
            resetClock();
            mButtonsFragment.disableRunningButtons();
        }
    }

    private void resetClock() {
        mClockFragment.setDuration(null);
        mCountdownText.setText("00:00:00");
        mClockFragment.updateClock();
    }

    // FragmentInteractionListener methods -------------------------------------

    @Override
    public void onPlayButtonClicked() {
        if (mIsBound) {
            startPomodoro();
        }
    }

    @Override
    public void onSkipButtonClicked() {
        if (mIsBound) {
            skipPomodoro();
        }
    }

    @Override
    public void onStopButtonClicked() {
        if (mIsBound) {
            stopPomodoro();
        }
    }

    /**
     * Updates the whole UI with the news parameters
     * @param bundle the updated data to display
     */
    public void updateUI(Bundle bundle) {
        int countdown = (int) bundle.getLong("countdown");
        Calendar startTime = Calendar.getInstance();
        startTime.setTimeInMillis(bundle.getLong("startTime"));
        Calendar endTime = Calendar.getInstance();
        endTime.setTimeInMillis(bundle.getLong("endTime"));
        Duration duration = new Duration(startTime, endTime);
        mCountdownText.setText(FormattingUtils.getDisplayTime(countdown));

        // I'll only update the trail duration after starting or skipping a pomodoro
        if (mClockFragment.getDuration() == null || !mClockFragment.getDuration().equals(duration)) {
            mClockFragment.setIsRest(bundle.getBoolean("isRest", false));
            mClockFragment.setDuration(duration);
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
            if (mService.isRunning()) {
                mButtonsFragment.enableRunningButtons();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mIsBound = false;
        }
    };
}
