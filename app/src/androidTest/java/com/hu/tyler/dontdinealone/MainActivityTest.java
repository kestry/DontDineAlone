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

// Testing by Tarun
public class MainActivityTest {

    private static boolean intentsAreClean = true;

    @Rule
    public ActivityTestRule<MainActivity> myActivityTestRule
            = new ActivityTestRule<MainActivity>(MainActivity.class);
    private MainActivity mainActivity = null;

    @Before
    public void setUp() throws Exception {
        EntityUT.setupWithMock();

        if (intentsAreClean) {
            Intents.init();
            intentsAreClean = !intentsAreClean;
        }

        mainActivity = myActivityTestRule.getActivity();
        myActivityTestRule.launchActivity(new Intent());
    }

    @After
    public void tearDown() throws Exception {
        myActivityTestRule.finishActivity();
        mainActivity = null;
        if (!intentsAreClean) {
            Intents.release();
            intentsAreClean = !intentsAreClean;
        }
        EntityUT.teardown();
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