package me.dielsonsales.app.openpomodoro;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import me.dielsonsales.app.openpomodoro.controllers.PomodoroController;
import me.dielsonsales.app.openpomodoro.controllers.PomodoroListener;

/**
 * The pomodoro service that contains the controller.
 */
public class PomodoroService extends Service {

    private static final String TAG = "PomodoroService";
    private final IBinder mBinder = new LocalBinder();
    private PomodoroController mPomodoroController;
    private UpdateListener mUpdateListener;

    // Constructor -------------------------------------------------------------

    public PomodoroService() {
        mPomodoroController = new PomodoroController();

        mPomodoroController.setPomodoroListener(new PomodoroListener() {
            @Override
            public void onMinuteLeft() {
                Log.i(TAG, "onMinuteLeft!");
            }

            @Override
            public void onTimeUpdated(long countdown) {
                Log.i(TAG, "onTimeUpdated!");
                mUpdateListener.onUpdate(countdown);
            }

            @Override
            public void onTimeFinished() {
                // TODO: wait for user to skip
                mPomodoroController.skip();
            }
        });
    }

    // Getters & setters -------------------------------------------------------

    public void setUpdateListener(UpdateListener listener) {
        mUpdateListener = listener;
    }

    // Lifecycle methods -------------------------------------------------------

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
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

    @Override
    public boolean stopService(Intent name) {
        Log.i(TAG, "stoService");
        return super.stopService(name);
    }

    // Public methods ----------------------------------------------------------

    public void startPomodoro() {
        mPomodoroController.start();
    }

    // Local binder ------------------------------------------------------------

    /**
     * Class used for the client binder.
     */
    public class LocalBinder extends Binder {
        PomodoroService getService() {
            return PomodoroService.this;
        }
    }

    // Service listener --------------------------------------------------------

    public interface UpdateListener {
        /**
         * Sends a signal to the UI every second to update the countdown.
         * @param countDown the time left before the current period is over.
         */
        public void onUpdate(long countDown);
    }
}
