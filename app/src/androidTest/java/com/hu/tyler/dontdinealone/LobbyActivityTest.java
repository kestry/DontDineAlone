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

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

import static org.junit.Assert.assertNotNull;

// Tyler and Jean pair programmed
public class LobbyActivityTest {

    private static boolean intentsAreClean = true;

    @Rule
    public ActivityTestRule<LobbyActivity> myActivityTestRule
            = new ActivityTestRule<LobbyActivity>(LobbyActivity.class);
    private LobbyActivity lobbyActivity = null;

    @Before
    public void setUp() throws Exception {
        EntityUT.setupWithMock();

        if (intentsAreClean) {
            Intents.init();
            intentsAreClean = !intentsAreClean;
        }

        lobbyActivity = myActivityTestRule.getActivity();
        myActivityTestRule.launchActivity(new Intent());
    }

    @After
    public void tearDown() throws Exception {
        myActivityTestRule.finishActivity();
        lobbyActivity = null;
        if (!intentsAreClean) {
            Intents.release();
            intentsAreClean = !intentsAreClean;
        }
        EntityUT.teardown();
    }

    @Test
    // Currently tests to see if all UI features are displayed on the activity screen
    public void testAllViews_OnCreate_NotNull() {
        View activityTitle = lobbyActivity.findViewById(R.id.textViewTitle);
        View editProfile = lobbyActivity.findViewById(R.id.buttonEditProfile);
        View matching = lobbyActivity.findViewById(R.id.buttonMatch);
        View logout = lobbyActivity.findViewById(R.id.buttonLogout);
        View numOnline = lobbyActivity.findViewById(R.id.onlineCount);
        assertNotNull(activityTitle);
        assertNotNull(editProfile);
        assertNotNull(matching);
        assertNotNull(logout);
        assertNotNull(numOnline);
    }

    @Test
    public void testLogoutButton_OnClick_ShouldLaunchToMainActivityAndLogoutUser() {
        onView(withId(R.id.buttonLogout)).perform(click());

        // Check that we went back to LobbyActivity after edit
        intended(hasComponent(MainActivity.class.getName()));

        assertNull(EntityUT.mockFirebaseUser);
    }

    // Verifies the Ok button brings you back to the lobby.
    @Test
    public void testEditProfileButton_OnClick_ShouldLaunchEditProfileActivity() {
        onView(withId(R.id.buttonEditProfile)).perform(click());

        // Check if we went back to LobbyActivity
        intended(hasComponent(EditProfileActivity.class.getName()));
    }
}