package me.dielsonsales.app.openpomodoro.android;

public interface IVibrator {
    void setVibrationAllowed(boolean vibrationAllowed);
    boolean isVibrationAllowed();
    void vibrate();
}
