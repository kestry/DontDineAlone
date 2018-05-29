package com.hu.tyler.dontdinealone;

import org.junit.Test;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import static org.junit.Assert.*;

public class RegisterActivityTest {

    public ActivityTestRule<RegisterActivity> RegisterActivityTestRule =
            new ActivityTestRule<RegisterActivity>(RegisterActivity.class);
    private RegisterActivity rActivity;
    @Before
    public void setUp() throws Exception {
        rActivity = RegisterActivityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        rActivity = null;
    }

    @Test
    // Tests to see if all UI features are displayed on the register page
    public void onCreate() {
        View email        = rActivity.findViewById(R.id.xxxxReg);
        View password     = rActivity.findViewById(R.id.xxxxPW);
        View confirmPW    = rActivity.findViewById(R.id.xxxxPWC);
        View signIn       = rActivity.findViewById(R.id.buttonSignin);
        View register     = rActivity.findViewById(R.id.buttonRegister);
        View activityName = rActivity.findViewById(R.id.textViewTitle);
        assertNotNull(email);
        assertNotNull(password);
        assertNotNull(confirmPW);
        assertNotNull(signIn);
        assertNotNull(register);
        assertNotNull(activityName);
    }
}