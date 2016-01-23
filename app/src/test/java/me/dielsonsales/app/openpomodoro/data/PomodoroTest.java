package me.dielsonsales.app.openpomodoro.data;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;

/**
 * Created by dielson on 22/01/16.
 */
public class PomodoroTest {
    @Test
    public void testToString() {
        Calendar currentTime = Calendar.getInstance();
        currentTime.set(Calendar.HOUR_OF_DAY, 7);
        currentTime.set(Calendar.MINUTE, 56);
        currentTime.set(Calendar.SECOND, 23);
        Pomodoro.PomodoroType pomodoroType = Pomodoro.PomodoroType.WORK;
        int initialTime = 60 * 25;
        Pomodoro pomodoro = new Pomodoro(currentTime, pomodoroType, initialTime);
        String expectedValue = "[Pomodoro: startedTime=07:56:23, type=WORK, remainingTime=1500s]";
        assertEquals(pomodoro.toString(), expectedValue);
    }
}
