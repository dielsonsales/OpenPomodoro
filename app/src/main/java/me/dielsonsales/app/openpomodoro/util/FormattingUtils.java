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
    public static String getDisplayTime(long seconds) {
        return String.format("%02d:%02d:%02d",
                TimeUnit.SECONDS.toHours(seconds),
                TimeUnit.SECONDS.toMinutes(seconds),
                TimeUnit.SECONDS.toSeconds(seconds));
    }
}
