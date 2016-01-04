package me.dielsonsales.app.openpomodoro;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import me.dielsonsales.app.openpomodoro.controllers.PomodoroController;
import me.dielsonsales.app.openpomodoro.controllers.PomodoroListener;
import me.dielsonsales.app.openpomodoro.controllers.PomodoroNotificationManager;
import me.dielsonsales.app.openpomodoro.controllers.PomodoroSoundManager;
import me.dielsonsales.app.openpomodoro.util.FormattingUtils;

/**
 * The pomodoro service that contains the controller.
 */
public class PomodoroService extends Service {

    private static final String TAG = "PomodoroService";
    private final IBinder mBinder = new LocalBinder();
    private PomodoroController mPomodoroController;
    private UpdateListener mUpdateListener;
    private PomodoroNotificationManager mNotificationManager;
    private int mStartId;

    // Getters & setters -------------------------------------------------------

    public void setUpdateListener(UpdateListener listener) {
        mUpdateListener = listener;
    }

    // Lifecycle methods -------------------------------------------------------

    /**
     * Creates the service. This is supposed to be called always by calling the
     * onBind method.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Creating service");
        mNotificationManager = new PomodoroNotificationManager(this);
        mPomodoroController = new PomodoroController(PomodoroSoundManager.getInstance(this), new PomodoroListener() {
            @Override
            public void onTimeUpdated(Bundle bundle) {
                if (mUpdateListener != null) {
                    mUpdateListener.onUpdate(bundle);
                }
            }
        });
    }

    /**
     * Start counting the pomodoro cycle. This class starts the foreground
     * service and allows it to run when the activity unbinds it.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mStartId = startId;
        try {
            startPomodoro();
        } catch (Exception e) {
            e.printStackTrace();
            stopPomodoro();
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    // Public methods ----------------------------------------------------------

    /**
     * This method is NOT supposed to be called from an activity. Instead, it
     * is automatically called in the onStartCommand method.
     */
    private void startPomodoro() throws Exception {
        updateControllerSettings();
        mPomodoroController.start();
        mNotificationManager.showNotification();
    }

    public void skipPomodoro() { mPomodoroController.skip(true); }

    /**
     * Stops the pomodoro and the foreground service. After calling this method,
     * the service will be destroyed after it is unbound.
     */
    public void stopPomodoro() {
        mPomodoroController.stop();
        mNotificationManager.hideNotification();
        stopSelf(mStartId);
    }

    /**
     * Returns whether the pomodoro is running or not.
     * @return true if the pomodoro is running
     */
    public boolean isRunning() { return mPomodoroController.isRunning(); }

    // Private methods ---------------------------------------------------------

    private void updateControllerSettings() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Set pomodoro time
        String pomodoroTimeString = preferences.getString(
                getResources().getString(R.string.pref_pomodoro_time_key),
                getResources().getString(R.string.pomodoro_time_default));
        mPomodoroController.setPomodoroTime(FormattingUtils.timeToSeconds(pomodoroTimeString));

        // Set rest time
        String restTimeString = preferences.getString(
                getResources().getString(R.string.pref_rest_time_key),
                getResources().getString(R.string.rest_time_default)
        );
        mPomodoroController.setRestTime(FormattingUtils.timeToSeconds(restTimeString));

        // Set long rest time
        String longRestTimeString = preferences.getString(
                getResources().getString(R.string.pref_long_rest_time_key),
                getResources().getString(R.string.long_rest_time_default)
        );
        mPomodoroController.setLongRestTime(FormattingUtils.timeToSeconds(longRestTimeString));

        // Set extended time
        String extendedTimeString = preferences.getString(
                getResources().getString(R.string.pref_extended_time_key),
                getResources().getString(R.string.extended_time_default)
        );
        mPomodoroController.setExtendedTime(FormattingUtils.timeToSeconds(extendedTimeString));
    }

    // Local binder ------------------------------------------------------------

    /**
     * Class used for the client binder. This class returns an instance of this
     * service for the bound activity.
     */
    public class LocalBinder extends Binder {
        PomodoroService getService() { return PomodoroService.this; }
    }

    // Service listener --------------------------------------------------------

    /**
     * Interface used to send signals to its bound activity. It allows the
     * MainActivity to register as a listener to this service.
     */
    public interface UpdateListener {
        /**
         * Sends a signal to the UI every second to update the countdown.
         * @param bundle the data to be updated
         */
        void onUpdate(Bundle bundle);
    }
}
