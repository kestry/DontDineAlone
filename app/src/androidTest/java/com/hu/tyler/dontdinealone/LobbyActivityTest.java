package com.hu.tyler.dontdinealone;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;
import android.view.View;

import com.hu.tyler.dontdinealone.SUT.EntityUnderTest;
import com.hu.tyler.dontdinealone.data.Entity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.content.ContentValues.TAG;
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class LobbyActivityTest {

    // We want to setup SUT before activity launches
    private class LobbyActivityTestRule extends ActivityTestRule<LobbyActivity> {
        LobbyActivityTestRule() {
            super(LobbyActivity.class);
        }
        @Override
        public void beforeActivityLaunched() {
            EntityUnderTest.setupWithMock();
        }
    }
    @Rule
    public LobbyActivityTestRule lobbyActivityTestRule = new LobbyActivityTestRule();
    private LobbyActivity lobbyActivity = null;

    Instrumentation.ActivityMonitor editProfileActivityMonitor = getInstrumentation()
            .addMonitor(EditProfileActivity.class.getName(), null, false);

    @Before
    public void setUp() throws Exception {
        lobbyActivity = lobbyActivityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        lobbyActivity = null;
        EntityUnderTest.teardown();
    }

    @Test
    // Currently tests to see if all UI features are displayed on the lobby screen
    public void testAllView_onCreate_NotNull() {
        View activityTitle = lobbyActivity.findViewById(R.id.textViewTitle);
        View editProfile   = lobbyActivity.findViewById(R.id.buttonEditProfile);
        View matching      = lobbyActivity.findViewById(R.id.buttonMatch);
        View logout        = lobbyActivity.findViewById(R.id.buttonLogout);
        View numOnline     = lobbyActivity.findViewById(R.id.onlineCount);
        assertNotNull(activityTitle);
        assertNotNull(editProfile);
        assertNotNull(matching);
        assertNotNull(logout);
        assertNotNull(numOnline);
    }

    @Test
    public void testEditProfileButton_OnClick_ShouldLaunchEditProfileActivity() {
        assertNotNull(lobbyActivity.findViewById(R.id.buttonEditProfile));

        onView(withId(R.id.buttonEditProfile)).perform(click());

        Activity editProfileActivity = getInstrumentation()
                .waitForMonitorWithTimeout(editProfileActivityMonitor, 500);

        assertNotNull(editProfileActivity);

        editProfileActivity.finish();
    }

    @Test
    public void testEntity_WithMockSetup_ShouldEqualMock() {
        Log.d(TAG, "testUserEmail_EqualsMock(): userEmail = " + Entity.onlineUser.getEmail());
        assertTrue(
                Entity.onlineUser.getDocumentId().equals(EntityUnderTest.FAKE_VALID_UID) &&
                Entity.onlineUser.getEmail().equals(EntityUnderTest.FAKE_VALID_EMAIL));
    }

}