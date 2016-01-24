package me.dielsonsales.app.openpomodoro.framework;

/**
 * Created by dielson on 24/01/16.
 */
public interface IWakeLock {
    void acquire();
    void release();
}
