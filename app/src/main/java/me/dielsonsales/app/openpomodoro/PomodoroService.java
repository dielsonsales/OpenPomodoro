package me.dielsonsales.app.openpomodoro;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import me.dielsonsales.app.openpomodoro.controllers.PomodoroController;
import me.dielsonsales.app.openpomodoro.controllers.PomodoroListener;
import me.dielsonsales.app.openpomodoro.controllers.PomodoroNotificationManager;

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
        mPomodoroController = new PomodoroController(this);
        mPomodoroController.setPomodoroListener(new PomodoroListener() {
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
        startPomodoro();
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
    private void startPomodoro() {
        mPomodoroController.start();
        mNotificationManager.showNotification();
    }

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
    public boolean isRunning() {
        return mPomodoroController.isRunning();
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
