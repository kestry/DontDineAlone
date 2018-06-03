package com.hu.tyler.dontdinealone;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;
import android.view.View;

import com.hu.tyler.dontdinealone.SUT.EntityUnderTest;
import com.hu.tyler.dontdinealone.SUT.TestHelperService;
import com.hu.tyler.dontdinealone.data.Entity;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static android.content.ContentValues.TAG;
import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class EditProfileActivityTest {

    // We want to setup SUT before activity launches
    private class EditProfileActivityTestRule extends ActivityTestRule<EditProfileActivity> {
        EditProfileActivityTestRule() {
            super(EditProfileActivity.class);
        }
        @Override
        public void beforeActivityLaunched() {
            EntityUnderTest.setupWithMock();
        }
    }

    @Rule
    public EditProfileActivityTestRule editProfileActivityTestRule = new EditProfileActivityTestRule();
    private EditProfileActivity editProfileActivity = null;

    Instrumentation.ActivityMonitor lobbyActivityMonitor = getInstrumentation()
            .addMonitor(LobbyActivity.class.getName(), null, false);

    @Before
    public void setUp() throws Exception {
        editProfileActivity = editProfileActivityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        editProfileActivity = null;
    }

    @Test
    // Tests to see if all UI features are displayed on the Edit Profile page
    public void testOnCreate() {
        View cancelButton    = editProfileActivity.findViewById(R.id.buttonEditCancel);
        View okButton        = editProfileActivity.findViewById(R.id.buttonEditOk);
        View textAnimalSpace = editProfileActivity.findViewById(R.id.editTextAnimal);
        View textAnimalPrompt= editProfileActivity.findViewById(R.id.textView5);
        View textGenderSpace = editProfileActivity.findViewById(R.id.editTextGender);
        View textGenderPrompt= editProfileActivity.findViewById(R.id.textView4);
        View editNameSpace   = editProfileActivity.findViewById(R.id.editTextDisplayNameInEditProfile);
        View editNamePrompt  = editProfileActivity.findViewById(R.id.textView3);
        assertNotNull(cancelButton);
        assertNotNull(okButton);
        assertNotNull(textAnimalSpace);
        assertNotNull(textAnimalPrompt);
        assertNotNull(textGenderPrompt);
        assertNotNull(textGenderSpace);
        assertNotNull(editNamePrompt);
        assertNotNull(editNameSpace);
    }

    // Verifies the Cancel button brings you back to the lobby.
    @Test
    public void testCancelButtonClick_WithEdit_ShouldLaunchLobbyActivityWithOriginalEntity(){
        // Setup
        Intents.init();

        // ... can do something here before test activity launches //
        EntityUnderTest.setProfileToDefault();

        editProfileActivityTestRule.launchActivity(new Intent());
    onView(withId(R.id.buttonEditCancel)).perform(click());

        // Check that we went back to LobbyActivity after edit
        try {
            intended(hasComponent(LobbyActivity.class.getName()));

            assertEntityStayedSame();

        } catch(Exception e) {
            Intents.release();
            throw(e);
        }

        Intents.release();
}
    // Verifies the Ok button brings you back to the lobby.
    @Test
    public void testOkButtonClick_WithoutEdit_ShouldLaunchLobbyActivityWithOriginalEntity(){
        // Setup
        Intents.init();

        // ... can do something here before test activity launches //
        EntityUnderTest.setProfileToDefault();

        editProfileActivityTestRule.launchActivity(new Intent());

        onView(withId(R.id.buttonEditOk)).perform(click());

        try {
            // Check if we went back to LobbyActivity
            intended(hasComponent(LobbyActivity.class.getName()));

            assertEntityStayedSame();

        } catch(Exception e) {
            Intents.release();
            throw(e);
        }

        // Cleanup ----------------- //

        // Reset Entity
        Intents.release();
    }

    // Verifies text entered into fields is properly saved to the Entity class.
    @Test
    public void testOkButtonClick_WithEdit_ShouldLaunchLobbyActivityWithChangedEntity(){
        // Setup ----------------- //

        Intents.init();
        // ... can do something here before test activity launches //
        EntityUnderTest.setProfileToDefault();

        // Launch ---------------- //

        editProfileActivityTestRule.launchActivity(new Intent());

        // Type in changed text
        changeText();

        // Press Ok button.
        onView(withId(R.id.buttonEditOk)).perform(click());

        try {
            // Check if we went back to LobbyActivity
            intended(hasComponent(LobbyActivity.class.getName()));

            // Checks if Entity updated to the same change values
            assertEntityWasChanged();

        } catch (Exception e) {
            EntityUnderTest.setProfileToDefault();

            Intents.release();
            throw(e);
        }

        // Cleanup ----------------- //

        // Reset Entity
        EntityUnderTest.setProfileToDefault();

        Intents.release();

    }

    @Ignore
    private static void changeText() {
        onView(withId(R.id.editTextDisplayNameInEditProfile))
            .perform(replaceText(EntityUnderTest.Values.CHANGED_DISPLAY_NAME));

        onView(withId(R.id.editTextGender))
                .perform(replaceText(EntityUnderTest.Values.CHANGED_GENDER));

        onView(withId(R.id.editTextAnimal))
                .perform(replaceText(EntityUnderTest.Values.CHANGED_ANIMAL));
    }

    @Ignore
    private static void assertEntityWasChanged() {
        TestHelperService.assertStringEquals("Entity.user.displayName",
                EntityUnderTest.Values.CHANGED_DISPLAY_NAME, Entity.user.getDisplayName());
        TestHelperService.assertStringEquals("Entity.user.gender",
                EntityUnderTest.Values.CHANGED_GENDER, Entity.user.getGender());
        TestHelperService.assertStringEquals("Entity.user.animal",
                EntityUnderTest.Values.CHANGED_ANIMAL, Entity.user.getAnimal());
    }

    @Ignore
    private static void assertEntityStayedSame() {
        TestHelperService.assertStringEquals("Entity.user.displayName",
                EntityUnderTest.Values.DEFAULT_DISPLAY_NAME, Entity.user.getDisplayName());
        TestHelperService.assertStringEquals("Entity.user.gender",
                EntityUnderTest.Values.DEFAULT_GENDER, Entity.user.getGender());
        TestHelperService.assertStringEquals("Entity.user.animal",
                EntityUnderTest.Values.DEFAULT_ANIMAL, Entity.user.getAnimal());
    }
}

