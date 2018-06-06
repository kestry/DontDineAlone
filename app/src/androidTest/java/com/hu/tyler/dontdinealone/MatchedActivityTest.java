package com.hu.tyler.dontdinealone;

import android.content.Intent;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.hu.tyler.dontdinealone.SUT.EntityUT;
import com.hu.tyler.dontdinealone.net.Connection;
import com.hu.tyler.dontdinealone.net.Session;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

// Untested
public class MatchedActivityTest {

    private static boolean intentsAreClean = true;

    @Rule
    public ActivityTestRule<MatchedActivity> myActivityTestRule
            = new ActivityTestRule<MatchedActivity>(MatchedActivity.class);
    private MatchedActivity testActivity = null;

    @Before
    public void setUp() throws Exception {
        EntityUT.setupWithMock();

        if (intentsAreClean) {
            Intents.init();
            intentsAreClean = !intentsAreClean;
        }

        testActivity = myActivityTestRule.getActivity();
        myActivityTestRule.launchActivity(new Intent());
    }

    @After
    public void tearDown() throws Exception {
        myActivityTestRule.finishActivity();
        testActivity = null;
        if (!intentsAreClean) {
            Intents.release();
            intentsAreClean = !intentsAreClean;
        }
        EntityUT.teardown();
    }
}
