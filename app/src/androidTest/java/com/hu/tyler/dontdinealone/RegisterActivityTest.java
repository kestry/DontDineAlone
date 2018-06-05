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

    private boolean didSutSetup = false;
    private boolean intentsAreClean = true;

    // We want to setup SUT before activity launches
    private class MyActivityTestRule extends ActivityTestRule<RegisterActivity> {
        MyActivityTestRule() {
            super(RegisterActivity.class);
        }

        @Override
        public void beforeActivityLaunched() {
            if (!didSutSetup) {
                EntityUT.setupWithMock();
                didSutSetup = true;
            }

            EntityUT.setProfileToDefault();

            if (intentsAreClean) {
                Intents.init();
                intentsAreClean = !intentsAreClean;
            }
        }

        @Override
        public void afterActivityFinished() {
            if (!intentsAreClean) {
                Intents.release();
                intentsAreClean = !intentsAreClean;
            }
            EntityUT.setProfileToDefault();
        }
    }

    @Rule
    public MyActivityTestRule myActivityTestRule = new MyActivityTestRule();
    private RegisterActivity registerActivity = null;

    @Before
    public void setUp() throws Exception {
        registerActivity = myActivityTestRule.getActivity();
        myActivityTestRule.launchActivity(new Intent());
    }

    @After
    public void tearDown() throws Exception {
        registerActivity = null;
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