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


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "creating service");
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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mStartId = startId;
        startPomodoro();
        return START_STICKY;
//        return super.onStartCommand(intent, flags, startId);
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
     * This method is not supposed to get called from a bound activity. Instead,
     * it is called when onStartCommand is called.
     */
    private void startPomodoro() {
        mPomodoroController.start();
        mNotificationManager.showNotification();
    }

    public void stopPomodoro() {
        mPomodoroController.stop();
        mNotificationManager.hideNotification();
        stopSelf(mStartId);
    }

    public boolean isRunning() {
        return mPomodoroController.isRunning();
    }

    // Local binder ------------------------------------------------------------

    /**
     * Class used for the client binder.
     */
    public class LocalBinder extends Binder {
        PomodoroService getService() { return PomodoroService.this; }
    }

    // Service listener --------------------------------------------------------

    public interface UpdateListener {
        /**
         * Sends a signal to the UI every second to update the countdown.
         * @param bundle the data to be updated
         */
        void onUpdate(Bundle bundle);
    }
}
