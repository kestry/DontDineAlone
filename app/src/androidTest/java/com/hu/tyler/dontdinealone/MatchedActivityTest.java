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

import static org.junit.Assert.assertNotNull;

public class MatchedActivityTest {

    private boolean didSutSetup = false;
    private boolean intentsAreClean = true;

    // We want to setup SUT before activity launches
    private class MyActivityTestRule extends ActivityTestRule<MatchedActivity> {
        MyActivityTestRule() {
            super(MatchedActivity.class);
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
    private MatchedActivity matchedActivity = null;

    @Before
    public void setUp() throws Exception {
        matchedActivity = myActivityTestRule.getActivity();
        myActivityTestRule.launchActivity(new Intent());
    }

    @After
    public void tearDown() throws Exception {
        matchedActivity = null;
    }

    @Test
    // Currently tests to see if all UI features are displayed on the activity screen
    public void testAllViews_OnCreate_NotNull() {

    }

}
