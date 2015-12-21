package me.dielsonsales.app.openpomodoro.controllers;

/**
 * This class listens for the PomodoroController's over time
 * Created by dielson on 19/12/15.
 */
public interface PomodoroListener {
    void onMinuteLeft();
    void onTimeUpdated(long countdown);
    void onTimeFinished();
}
