package me.dielsonsales.app.openpomodoro.presentation;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

/**
 * The pomodoro service that contains the controller.
 */
public class PomodoroService extends Service {

    private static final String TAG = "PomodoroService";
    private final IBinder mBinder = new LocalBinder();
    private ServicePresenter mPresenter;
    private UpdateListener mUpdateListener;
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
        mPresenter = new ServicePresenter(this);
    }

    /**
     * Start counting the pomodoro cycle. This class starts the foreground
     * service and allows it to run when the activity unbinds it.
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mStartId = startId;
        mPresenter.startPomodoro();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        setUpdateListener(null);
        return super.onUnbind(intent);
    }

    // Public methods ----------------------------------------------------------

    public void skipPomodoro() {
        mPresenter.skipPomodoro();
    }

    /**
     * Stops the pomodoro and the foreground service. After calling this method,
     * the service will be destroyed after it is unbound.
     */
    public void stopPomodoro() {
        mPresenter.stopPomodoro();
        stopSelf(mStartId);
    }


    public boolean isRunning() {
        return mPresenter.isRunning();
    }

    public void updateActivity(Bundle bundle) {
        if (mUpdateListener != null) {
            mUpdateListener.onUpdate(bundle);
        }
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
