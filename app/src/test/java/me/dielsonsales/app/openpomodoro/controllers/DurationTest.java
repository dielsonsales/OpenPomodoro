package me.dielsonsales.app.openpomodoro.controllers;

import org.junit.Test;

import java.util.Calendar;

import me.dielsonsales.app.openpomodoro.util.Duration;

import static junit.framework.Assert.assertEquals;

public class DurationTest {
    @Test
    public void testEquals() {
        Calendar startDate1 = Calendar.getInstance();
        startDate1.set(Calendar.HOUR, 5);
        Calendar endDate1 = Calendar.getInstance();
        endDate1.set(Calendar.HOUR, 7);
        Duration duration1 = new Duration(startDate1, endDate1);

        Calendar startDate2 = (Calendar) startDate1.clone();
        Calendar endDate2 = (Calendar) endDate1.clone();
        Duration duration2 = new Duration(startDate2, endDate2);

        assertEquals(duration1.equals(duration2), true);
    }
}
