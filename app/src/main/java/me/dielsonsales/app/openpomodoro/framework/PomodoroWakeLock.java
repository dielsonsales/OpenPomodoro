package me.dielsonsales.app.openpomodoro.framework;

import android.content.Context;
import android.os.PowerManager;

import static android.content.Context.POWER_SERVICE;
import static android.os.PowerManager.PARTIAL_WAKE_LOCK;

/**
 * Created by dielson on 24/01/16.
 */
public class PomodoroWakeLock implements IWakeLock {
    private PowerManager.WakeLock mWakeLock;

    public PomodoroWakeLock(Context context) {
        mWakeLock = ((PowerManager) context.getSystemService(POWER_SERVICE))
                .newWakeLock(PARTIAL_WAKE_LOCK, "ServiceWakeLock");
    }
    @Override
    public void acquire() {
        mWakeLock.acquire();
    }

    @Override
    public void release() {
        if (mWakeLock.isHeld()) {
            mWakeLock.release();
        }
    }
}
