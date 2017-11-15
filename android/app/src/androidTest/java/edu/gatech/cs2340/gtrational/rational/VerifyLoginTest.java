package edu.gatech.cs2340.gtrational.rational;

import android.content.Context;
import android.graphics.Rect;
import android.os.SystemClock;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.gatech.cs2340.gtrational.rational.controller.LoginActivity;
import edu.gatech.cs2340.gtrational.rational.controller.MainDashboardActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class VerifyLoginTest {
    @Rule
    public IntentsTestRule<LoginActivity> mActivityRule = new IntentsTestRule(LoginActivity.class);

    @Test
    public void emptyLogin() {
        onView(withId(R.id.buttonLogin)).perform(click());
        onView(withText("Please enter username")).inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))));
    }

    @Test
    public void noUsernameLogin() {
        onView(withId(R.id.editTextPassword)).perform(typeText("testpass"));
        onView(withText("Please enter username")).inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))));
    }

    @Test
    public void noPasswordLogin() {
        onView(withId(R.id.editTextUsername)).perform(typeText("testuser"));
        onView(withText("Please enter password")).inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))));
    }

    /**
     * As of now, Espresso does not have a documented way of checking if the keyboard is open;
     * therefore, it is not possible to check if the program properly closes the keyboard during
     * runtime, so there are no tests for the following segment of code which hides the keyboard:
     *
     * View focused = getCurrentFocus();
     *      if (focused != null) {
     *          InputMethodManager inputManager =(InputMethodManager)
     *                  getSystemService(Context.INPUT_METHOD_SERVICE);
     *          if (inputManager != null) {
     *              inputManager.hideSoftInputFromWindow(
     *              focused.getWindowToken(),
     *              InputMethodManager.HIDE_NOT_ALWAYS
     *          );
     *      }
     * }
     */
    @Test
    public void incorrectLogin() {
        onView(withId(R.id.editTextUsername)).perform(typeText("wronguser"));
        onView(withId(R.id.editTextPassword)).perform(typeText("wrongpass"));
        onView(withId(R.id.buttonLogin)).perform(click());
        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText("Login Failed"))).check(matches(isDisplayed()));
    }

    @Test
    public void correctLogin() {
        onView(withId(R.id.editTextUsername)).perform(typeText("testuser"));
        onView(withId(R.id.editTextPassword)).perform(typeText("testpass"));
        onView(withId(R.id.buttonLogin)).perform(click());
        intended(hasComponent(MainDashboardActivity.class.getName()));
    }
}
