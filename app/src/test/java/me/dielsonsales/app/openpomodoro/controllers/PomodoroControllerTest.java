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
        mPomodoroController = new PomodoroController();
    }

    @Test
    public void testPomodoroDefaults() {
        assertEquals(mPomodoroController.getPomodoroCount(), 0);
        assertEquals(mPomodoroController.getPomodoroTime(), 25);
        assertEquals(mPomodoroController.getRestTime(), 5);
        assertEquals(mPomodoroController.getExtendedTime(), 3);
        assertEquals(mPomodoroController.getLongRestTime(), 20);
    }

    public void testSettingValues() {
        int POMODORO_TIME = 15;
        int REST_TIME = 7;
        int EXTENDED_TIME = 2;
        int LONG_REST_TIME = 23;
        // sets new values
        mPomodoroController.setPomodoroTime(POMODORO_TIME);
        mPomodoroController.setLongRestTime(REST_TIME);
        mPomodoroController.setExtendedTime(EXTENDED_TIME);
        mPomodoroController.setLongRestTime(LONG_REST_TIME);
        // test the values
        assertEquals(mPomodoroController.getPomodoroTime(), POMODORO_TIME);
        assertEquals(mPomodoroController.getRestTime(), REST_TIME);
        assertEquals(mPomodoroController.getExtendedTime(), EXTENDED_TIME);
        assertEquals(mPomodoroController.getLongRestTime(), LONG_REST_TIME);
    }
}
