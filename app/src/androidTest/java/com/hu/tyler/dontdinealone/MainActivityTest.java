package com.hu.tyler.dontdinealone;

import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule =
            new ActivityTestRule<MainActivity>(MainActivity.class);
    private MainActivity mActivity = null;

    @Before
    public void setUp() throws Exception {
        mActivity = mainActivityTestRule.getActivity();
    }

    @Test
    // Currently tests to see if all UI features are displayed on the login screen
    public void onCreate() {
        View viewTitle = mActivity.findViewById(R.id.textViewTitle);
        View editEmail = mActivity.findViewById(R.id.editTextEmail);
        View editPassword = mActivity.findViewById(R.id.editTextPW);
        View login     = mActivity.findViewById(R.id.buttonLogin);
        assertNotNull(viewTitle);
        assertNotNull(editEmail);
        assertNotNull(editPassword);
        assertNotNull(login);
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;

    }
}