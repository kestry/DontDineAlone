package com.hu.tyler.dontdinealone;

import android.content.Intent;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.hu.tyler.dontdinealone.SUT.EntityUT;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class MainActivityTest {

    private boolean didSutSetup = false;
    private boolean intentsAreClean = true;

    // We want to setup SUT before activity launches
    private class MyActivityTestRule extends ActivityTestRule<MainActivity> {
        MyActivityTestRule() {
            super(MainActivity.class);
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
    private MainActivity mainActivity = null;

    @Before
    public void setUp() throws Exception {
        mainActivity = myActivityTestRule.getActivity();
        myActivityTestRule.launchActivity(new Intent());
    }

    @After
    public void tearDown() throws Exception {
        mainActivity = null;
    }

    @Test
    // Currently tests to see if all UI features are displayed on the activity screen
    public void testAllViews_OnCreate_NotNull() {
        View viewTitle = mainActivity.findViewById(R.id.textViewTitle);
        View editEmail = mainActivity.findViewById(R.id.editTextEmail);
        View editPassword = mainActivity.findViewById(R.id.editTextPW);
        View login     = mainActivity.findViewById(R.id.buttonLogin);
        assertNotNull(viewTitle);
        assertNotNull(editEmail);
        assertNotNull(editPassword);
        assertNotNull(login);
    }
}