package com.hu.tyler.dontdinealone;

import org.junit.Test;

import android.content.Intent;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.hu.tyler.dontdinealone.SUT.EntityUT;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class RegisterActivityTest {

    private static boolean intentsAreClean = true;

    @Rule
    public ActivityTestRule<RegisterActivity> myActivityTestRule
            = new ActivityTestRule<RegisterActivity>(RegisterActivity.class);
    private RegisterActivity registerActivity = null;

    @Before
    public void setUp() throws Exception {
        EntityUT.setupWithMock();

        if (intentsAreClean) {
            Intents.init();
            intentsAreClean = !intentsAreClean;
        }

        registerActivity = myActivityTestRule.getActivity();
        myActivityTestRule.launchActivity(new Intent());
    }

    @After
    public void tearDown() throws Exception {
        myActivityTestRule.finishActivity();
        registerActivity = null;
        if (!intentsAreClean) {
            Intents.release();
            intentsAreClean = !intentsAreClean;
        }
        EntityUT.teardown();
    }

    @Test
    // Currently tests to see if all UI features are displayed on the activity screen
    public void testAllViews_OnCreate_NotNull() {
        View email        = registerActivity.findViewById(R.id.xxxxReg);
        View password     = registerActivity.findViewById(R.id.xxxxPW);
        View confirmPW    = registerActivity.findViewById(R.id.xxxxPWC);
        View signIn       = registerActivity.findViewById(R.id.buttonSignin);
        View register     = registerActivity.findViewById(R.id.buttonRegister);
        View activityName = registerActivity.findViewById(R.id.textViewTitle);
        assertNotNull(email);
        assertNotNull(password);
        assertNotNull(confirmPW);
        assertNotNull(signIn);
        assertNotNull(register);
        assertNotNull(activityName);
    }

    @Test
    public void testSignInInsteadButton_OnClick_ShouldLaunchToMainActivity() {
        onView(withId(R.id.buttonSignin)).perform(click());

        // Check that we went back to MainActivity after edit
        intended(hasComponent(MainActivity.class.getName()));
    }


    @Test
    public void testRegisterButton_OnClick_ShouldLaunchToMainActivity() {
        onView(withId(R.id.buttonRegister)).perform(click());

        // Check that we went back to LobbyActivity after edit
        intended(hasComponent(MainActivity.class.getName()));
    }

}