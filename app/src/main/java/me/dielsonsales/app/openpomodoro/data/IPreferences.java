package me.dielsonsales.app.openpomodoro.data;

/**
 * Abstracts the preferences from the higher level modules.
 *
 * Before calling any method, you must call IPreferences#retrievePreferences()
 */
public interface IPreferences {
    void retrievePreferences();
    String getPomodoroTime();
    String getRestTime();
    String getLongRestTime();
    String getExtendedTime();
    boolean getExtendedAllowed();
    boolean getSoundAllowed();
    boolean getVibrationAllowed();
}
