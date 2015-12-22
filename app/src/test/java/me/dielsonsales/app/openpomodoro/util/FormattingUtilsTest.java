package me.dielsonsales.app.openpomodoro.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by dielson on 22/12/15.
 */
public class FormattingUtilsTest {

    @Test
    public void testGetDisplayTime() {
        String expected = "00:01:00";
        String result = FormattingUtils.getDisplayTime(60);
        assertEquals(expected, result);

        expected = "01:00:00";
        result = FormattingUtils.getDisplayTime(3600);
        assertEquals(expected, result);

        expected = "00:01:30";
        result = FormattingUtils.getDisplayTime(60 + 30);
        assertEquals(expected, result);

        expected = "01:30:30";
        result = FormattingUtils.getDisplayTime(3600 + 30 * 60 + 30);
        assertEquals(expected, result);

        expected = "02:06:50";
        result = FormattingUtils.getDisplayTime(2 * 3600 + 6 * 60 + 50);
        assertEquals(expected, result);

        expected = "00:07:00";
        result = FormattingUtils.getDisplayTime(7 * 60);
        assertEquals(expected, result);
    }
}
