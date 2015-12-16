package me.dielsonsales.app.openpomodoro.components;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

import me.dielsonsales.app.openpomodoro.MainActivity;

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

    @SmallTest
    public void testColors() {
        assertEquals(mClockCanvas.POMODORO_COLOR, Color.YELLOW);
    }

}
