package me.dielsonsales.app.openpomodoro.views;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.assertion.ViewAssertions;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Before;
import org.junit.Test;

import me.dielsonsales.app.openpomodoro.MainActivity;
import me.dielsonsales.app.openpomodoro.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private MainActivity mActivity;
    public MainActivityTest() {
        super(MainActivity.class);
    }
    @Before
    public void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();
    }


    @Test
    public void testButtonsState() {
        // Checks whether the play button starts visible alone
        onView(withId(R.id.play_button)).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.skip_button)).check(ViewAssertions.matches(not(isDisplayed())));
        onView(withId(R.id.stop_button)).check(ViewAssertions.matches(not(isDisplayed())));

        // Clicks the play button
        onView(withId(R.id.play_button)).perform(ViewActions.click());

        // Checks if the play button's become invisible and the skip and stop
        // buttons are now visible.
        onView(withId(R.id.play_button)).check(ViewAssertions.matches(not(isDisplayed())));
        onView(withId(R.id.skip_button)).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.stop_button)).check(ViewAssertions.matches(isDisplayed()));

        // Clicks the skip button
        onView(withId(R.id.skip_button)).perform(ViewActions.click());

        // Checks if everything remains the same
        onView(withId(R.id.play_button)).check(ViewAssertions.matches(not(isDisplayed())));
        onView(withId(R.id.skip_button)).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.stop_button)).check(ViewAssertions.matches(isDisplayed()));

        // Clicks the stop button
        onView(withId(R.id.stop_button)).perform(ViewActions.click());

        // Checks if everything is back to the initial state
        onView(withId(R.id.play_button)).check(ViewAssertions.matches(isDisplayed()));
        onView(withId(R.id.skip_button)).check(ViewAssertions.matches(not(isDisplayed())));
        onView(withId(R.id.stop_button)).check(ViewAssertions.matches(not(isDisplayed())));
    }
}
