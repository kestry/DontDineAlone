package com.hu.tyler.dontdinealone;

import android.support.test.rule.ActivityTestRule;
import android.view.View;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class LobbyActivityTest {

    @Rule
    public ActivityTestRule<LobbyActivity> LobbyActivityTestRule =
            new ActivityTestRule<LobbyActivity>(LobbyActivity.class);
    private LobbyActivity lActivity = null;

    @Before
    public void setUp() throws Exception {
        lActivity = LobbyActivityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        lActivity = null;
    }

    @Test
    // Currently tests to see if all UI features are displayed on the lobby screen
    public void onCreate() {
        View activityTitle = lActivity.findViewById(R.id.textViewTitle);
        View editProfile   = lActivity.findViewById(R.id.buttonEditProfile);
        View matching      = lActivity.findViewById(R.id.buttonMatch);
        View logout        = lActivity.findViewById(R.id.buttonLogout);
        View numOnline     = lActivity.findViewById(R.id.onlineCount);
        assertNotNull(activityTitle);
        assertNotNull(editProfile);
        assertNotNull(matching);
        assertNotNull(logout);
        assertNotNull(numOnline);
    }
}