package me.dielsonsales.app.openpomodoro.framework;

public interface IVibrator {
    void setVibrationAllowed(boolean vibrationAllowed);
    boolean isVibrationAllowed();
    void vibrate();
}
