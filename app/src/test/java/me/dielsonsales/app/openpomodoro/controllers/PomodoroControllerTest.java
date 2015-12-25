package me.dielsonsales.app.openpomodoro.controllers;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by dielson on 21/12/15.
 */
public class PomodoroControllerTest {

    private PomodoroController mPomodoroController;

    @Before
    public void setUp() {
//        mPomodoroController = new PomodoroController();
    }

    /**
     * Tests if the default values are set correctly in the constructor.
     */
    @Test
    public void testPomodoroDefaults() {
        assertEquals(mPomodoroController.getPomodoroCount(), 0);
        assertEquals(mPomodoroController.isRunning(), false);
        assertEquals(mPomodoroController.getPomodoroTime(), 25);
        assertEquals(mPomodoroController.getRestTime(), 5);
        assertEquals(mPomodoroController.getExtendedTime(), 3);
        assertEquals(mPomodoroController.getLongRestTime(), 20);
    }

    /**
     * Test all the getters and setters
     */
    @Test
    public void testSettingValues() {
        int POMODORO_TIME = 15;
        int REST_TIME = 7;
        int EXTENDED_TIME = 2;
        int LONG_REST_TIME = 23;
        // sets new values
        mPomodoroController.setPomodoroTime(POMODORO_TIME);
        mPomodoroController.setRestTime(REST_TIME);
        mPomodoroController.setExtendedTime(EXTENDED_TIME);
        mPomodoroController.setLongRestTime(LONG_REST_TIME);
        // test the values
        assertEquals(mPomodoroController.getPomodoroTime(), POMODORO_TIME);
        assertEquals(mPomodoroController.getRestTime(), REST_TIME);
        assertEquals(mPomodoroController.getExtendedTime(), EXTENDED_TIME);
        assertEquals(mPomodoroController.getLongRestTime(), LONG_REST_TIME);
    }

    /**
     * Tests the start, skip and stop functions.
     */
    @Test
    public void testRunning() {
        assertEquals(mPomodoroController.getCurrentIntervalType(), PomodoroController.IntervalType.POMODORO);
        assertEquals(mPomodoroController.isRunning(), false);
        // start counting
        mPomodoroController.start();
        assertEquals(mPomodoroController.getCurrentIntervalType(), PomodoroController.IntervalType.POMODORO);
        assertEquals(mPomodoroController.isRunning(), true);
        for (int i = 0; i < 3; i++) {
            assertEquals(mPomodoroController.getPomodoroCount(), i + 1);
            assertEquals(mPomodoroController.isRunning(), true);
            mPomodoroController.skip();
            assertEquals(mPomodoroController.getCurrentIntervalType(), PomodoroController.IntervalType.REST);
            mPomodoroController.skip();
            assertEquals(mPomodoroController.getCurrentIntervalType(), PomodoroController.IntervalType.POMODORO);
        }
        mPomodoroController.skip(); // now it's the long rest
        assertEquals(mPomodoroController.getCurrentIntervalType(), PomodoroController.IntervalType.LONG_REST);
        assertEquals(mPomodoroController.getPomodoroCount(), 0);

        mPomodoroController.skip();
        assertEquals(mPomodoroController.getCurrentIntervalType(), PomodoroController.IntervalType.POMODORO);
        assertEquals(mPomodoroController.getPomodoroCount(), 1);

        // stop everything
        mPomodoroController.stop();
        assertEquals(mPomodoroController.isRunning(), false);
        assertEquals(mPomodoroController.getCurrentIntervalType(), PomodoroController.IntervalType.POMODORO);
    }
}
