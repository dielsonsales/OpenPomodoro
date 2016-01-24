package me.dielsonsales.app.openpomodoro.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import me.dielsonsales.app.openpomodoro.R;

public class PomodoroPreferences implements IPreferences {

    SharedPreferences mPreferences;
    Context mContext;

    public PomodoroPreferences(Context context) {
        mContext = context;
    }

    @Override
    public void retrievePreferences() {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    @Override
    public String getPomodoroTime() {
        return mPreferences.getString(
                mContext.getResources().getString(R.string.pref_pomodoro_time_key),
                mContext.getResources().getString(R.string.pomodoro_time_default)
        );
    }

    @Override
    public String getRestTime() {
        return mPreferences.getString(
                mContext.getResources().getString(R.string.pref_rest_time_key),
                mContext.getResources().getString(R.string.rest_time_default)
        );
    }

    @Override
    public String getLongRestTime() {
        return mPreferences.getString(
                mContext.getResources().getString(R.string.pref_long_rest_time_key),
                mContext.getResources().getString(R.string.long_rest_time_default)
        );
    }

    @Override
    public String getExtendedTime() {
        return mPreferences.getString(
                mContext.getResources().getString(R.string.pref_extended_time_key),
                mContext.getResources().getString(R.string.extended_time_default)
        );
    }

    @Override
    public boolean getExtendedAllowed() {
        return mPreferences.getBoolean(
                mContext.getResources().getString(R.string.pref_auto_skip_key),
                false
        );
    }

    @Override
    public boolean getSoundAllowed() {
        return mPreferences.getBoolean(
                mContext.getResources().getString(R.string.pref_play_sound_key),
                true
        );
    }

    @Override
    public boolean getVibrationAllowed() {
        return mPreferences.getBoolean(
                mContext.getResources().getString(R.string.pref_vibration_key),
                false
        );
    }
}
