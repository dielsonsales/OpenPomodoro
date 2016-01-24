package me.dielsonsales.app.openpomodoro.framework;

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
