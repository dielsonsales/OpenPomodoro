package me.dielsonsales.app.openpomodoro.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import me.dielsonsales.app.openpomodoro.framework.INotification;
import me.dielsonsales.app.openpomodoro.framework.ISoundPlayer;
import me.dielsonsales.app.openpomodoro.framework.IVibrator;
import me.dielsonsales.app.openpomodoro.domain.Pomodoro;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class PomodoroControllerTest {

    private static final String TAG = "PomodoroControllerTest";
    private PomodoroController mPomodoroController;

    @Before
    public void setUp() {
        mPomodoroController = new PomodoroController(
                new MockSoundManager(),
                new MockVibrator(),
                new MockNotificationManager());
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
        assertEquals(mPomodoroController.getWorkTime(), POMODORO_TIME);
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
        assertEquals(mPomodoroController.isRunning(), false);
        assertEquals(mPomodoroController.getCurrentPomodoroType(), Pomodoro.PomodoroType.NONE);
        // start counting
        try {
            mPomodoroController.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(mPomodoroController.getCurrentPomodoroType(), Pomodoro.PomodoroType.WORK);
        assertEquals(mPomodoroController.isRunning(), true);

        // automatic skip, should keep the interval time the same
        for (int i = 0; i < 3; i++) {
            mPomodoroController.skip(false);
            assertEquals(mPomodoroController.getCurrentPomodoroType(), Pomodoro.PomodoroType.WORK);
        }

        // user skip
        for (int i = 0; i < 3; i++) {
            assertEquals(mPomodoroController.getPomodoroCount(), i + 1);
            assertEquals(mPomodoroController.isRunning(), true);
            mPomodoroController.skip(true);
            assertEquals(mPomodoroController.getCurrentPomodoroType(), Pomodoro.PomodoroType.REST);
            mPomodoroController.skip(true);
            assertEquals(mPomodoroController.getCurrentPomodoroType(), Pomodoro.PomodoroType.WORK);
        }
        mPomodoroController.skip(true); // now it's the long rest
        assertEquals(mPomodoroController.getCurrentPomodoroType(), Pomodoro.PomodoroType.LONG_REST);
        assertEquals(mPomodoroController.getPomodoroCount(), 0);

        mPomodoroController.skip(true);
        assertEquals(mPomodoroController.getCurrentPomodoroType(), Pomodoro.PomodoroType.WORK);
        assertEquals(mPomodoroController.getPomodoroCount(), 1);

        // stop everything
        mPomodoroController.stop();
        assertEquals(mPomodoroController.isRunning(), false);
        assertEquals(mPomodoroController.getCurrentPomodoroType(), Pomodoro.PomodoroType.NONE);
    }

     private void setPomodoroValues() {
         mPomodoroController.setPomodoroTime(25 * 60);
         mPomodoroController.setRestTime(5 * 60);
         mPomodoroController.setLongRestTime(20 * 60);
         mPomodoroController.setExtendedTime(20 * 60);
     }

    // Private classes ---------------------------------------------------------

    private class MockNotificationManager implements INotification {
        @Override
        public void showNotification(NotificationType notificationType) {
            // do nothing
        }
        @Override
        public void hideNotification() {
            // do nothing
        }
    }

    private class MockSoundManager implements ISoundPlayer {
        @Override
        public void setSoundAllowed(boolean soundAllowed) {
            // do nothing
        }
        @Override
        public boolean isSoundAllowed() {
            return false;
        }
        @Override
        public void playTicTacSound() {
            // do nothing
        }
        @Override
        public void playAlarm() {
            // do nothing
        }
        @Override
        public void playBell() {
            // do nothing
        }
    }

    private class MockVibrator implements IVibrator {
        @Override
        public void setVibrationAllowed(boolean vibrationAllowed) {
            // do nothing
        }
        @Override
        public boolean isVibrationAllowed() {
            return false;
        }
        @Override
        public void vibrate() {
            // do nothing
        }
    }
}
