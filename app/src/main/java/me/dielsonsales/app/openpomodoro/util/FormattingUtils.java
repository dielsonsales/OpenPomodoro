package me.dielsonsales.app.openpomodoro.util;

import java.util.concurrent.TimeUnit;

/**
 * Created by dielson on 22/12/15.
 */
public class FormattingUtils {
    /**
     * Converts a time in seconds to a human readable time format (hh:mm:ss)
     * @param seconds the time in seconds
     * @return a string containing a better displaying time
     */
    public static String getDisplayTime(int seconds) {
        return String.format("%02d:%02d:%02d",
                TimeUnit.SECONDS.toHours(seconds),
                TimeUnit.SECONDS.toMinutes(seconds) - TimeUnit.SECONDS.toHours(seconds) * 60,
                TimeUnit.SECONDS.toSeconds(seconds) % 60);
    }

    public static int timeToSeconds(String time) {
        String[] values = time.split(":");
        int hours = Integer.valueOf(values[0]);
        int minutes = Integer.valueOf(values[1]);
        int seconds = Integer.valueOf(values[2]);
        return seconds + minutes * 60 + hours * 3600;
    }
}
