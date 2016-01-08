package me.dielsonsales.app.openpomodoro.controllers;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class PomodoroControllerTest {

    private static final String TAG = "PomodoroControllerTest";
    private PomodoroController mPomodoroController;

    @Before
    public void setUp() {
        mPomodoroController = new PomodoroController(new MockSoundManager(RuntimeEnvironment.application));
    }

    /**
     * Test all the getters and setters
     */
    @Test
    public void testSettingValues() {
        int POMODORO_TIME = 15 * 60;
        int REST_TIME = 7 * 60;
        int EXTENDED_TIME = 5 * 60;
        int LONG_REST_TIME = 23 * 60;
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
     * Tests the start, skip (user) and stop functions.
     */
    @Test
    public void testRunningUserSkipping() {
        setPomodoroValues();
        assertEquals(mPomodoroController.getCurrentIntervalType(), PomodoroController.IntervalType.POMODORO);
        assertEquals(mPomodoroController.isRunning(), false);
        // start counting
        try {
            mPomodoroController.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(mPomodoroController.getCurrentIntervalType(), PomodoroController.IntervalType.POMODORO);
        assertEquals(mPomodoroController.isRunning(), true);

        // automatic skip, should keep the interval time the same
        for (int i = 0; i < 3; i++) {
            mPomodoroController.skip(false);
            assertEquals(mPomodoroController.getCurrentIntervalType(), PomodoroController.IntervalType.POMODORO);
        }

        // user skip
        for (int i = 0; i < 3; i++) {
            assertEquals(mPomodoroController.getPomodoroCount(), i + 1);
            assertEquals(mPomodoroController.isRunning(), true);
            mPomodoroController.skip(true);
            assertEquals(mPomodoroController.getCurrentIntervalType(), PomodoroController.IntervalType.REST);
            mPomodoroController.skip(true);
            assertEquals(mPomodoroController.getCurrentIntervalType(), PomodoroController.IntervalType.POMODORO);
        }
        mPomodoroController.skip(true); // now it's the long rest
        assertEquals(mPomodoroController.getCurrentIntervalType(), PomodoroController.IntervalType.LONG_REST);
        assertEquals(mPomodoroController.getPomodoroCount(), 0);

        mPomodoroController.skip(true);
        assertEquals(mPomodoroController.getCurrentIntervalType(), PomodoroController.IntervalType.POMODORO);
        assertEquals(mPomodoroController.getPomodoroCount(), 1);

        // stop everything
        mPomodoroController.stop();
        assertEquals(mPomodoroController.isRunning(), false);
        assertEquals(mPomodoroController.getCurrentIntervalType(), PomodoroController.IntervalType.POMODORO);
    }

     private void setPomodoroValues() {
         mPomodoroController.setPomodoroTime(25 * 60);
         mPomodoroController.setRestTime(5 * 60);
         mPomodoroController.setLongRestTime(20 * 60);
         mPomodoroController.setExtendedTime(20 * 60);
     }

    // Private classes ---------------------------------------------------------

    public class MockSoundManager extends PomodoroSoundManager {
        public MockSoundManager(Context context) {
            super(context);
        }

        @Override
        public void playTicTacSound() {
            // do nothing
        }
    }
}
