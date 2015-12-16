package me.dielsonsales.app.openpomodoro.components;

import android.graphics.Color;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.app.AppCompatActivity;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Test;
import org.junit.runner.RunWith;

import me.dielsonsales.app.openpomodoro.MainActivity;

@RunWith(AndroidJUnit4.class)
public class ClockCanvasTest extends ActivityInstrumentationTestCase2<MainActivity> {

    AppCompatActivity mActivity;
    ClockCanvas mClockCanvas;

    public ClockCanvasTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        setActivityInitialTouchMode(true);
        mActivity = getActivity();
        mClockCanvas = (ClockCanvas) mActivity.findViewById(me.dielsonsales.app.openpomodoro.R.id.clockCanvas);
        testPreconditions();
    }

    public void testPreconditions() {
        assertNotNull("Activity is null", mActivity);
        assertNotNull("ClockCanvas is null", mClockCanvas);
    }

    @Test
    public void testColors() {
        assertEquals(mClockCanvas.POMODORO_COLOR, Color.YELLOW);
    }

}
