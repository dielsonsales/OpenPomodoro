package me.dielsonsales.app.openpomodoro.android;

/**
 * Created by dielson on 21/01/16.
 */
public interface ISoundPlayer {
    void setSoundAllowed(boolean soundAllowed);
    boolean isSoundAllowed();
    void playTicTacSound();
    void playAlarm();
    void playBell();
}
