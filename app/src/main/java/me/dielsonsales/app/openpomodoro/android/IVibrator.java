package me.dielsonsales.app.openpomodoro.android;

/**
 * Created by dielson on 21/01/16.
 */
public interface IVibrator {
    void setVibrationAllowed(boolean vibrationAllowed);
    boolean isVibrationAllowed();
    void vibrate();
}
