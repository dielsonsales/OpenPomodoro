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
    private int mStartId;

    public PomodoroService() {
        mPomodoroController = new PomodoroController();

        mPomodoroController.setPomodoroListener(new PomodoroListener() {
            @Override
            public void onMinuteLeft() {
                Log.i(TAG, "onMinuteLeft!");
            }

            @Override
            public void onTimeOver() {
                Log.i(TAG, "onTimeOver!");
            }
        });
    }

    // Lifecycle methods -------------------------------------------------------

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        // TODO: something?
        mStartId = startId;
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
}
