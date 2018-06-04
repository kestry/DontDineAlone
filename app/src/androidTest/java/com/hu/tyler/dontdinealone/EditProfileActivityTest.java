package com.hu.tyler.dontdinealone;

import android.content.Intent;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.hu.tyler.dontdinealone.SUT.EntityUT;
import com.hu.tyler.dontdinealone.SUT.TestHelperService;
import com.hu.tyler.dontdinealone.data.Entity;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertNotNull;

public class EditProfileActivityTest {
    private boolean didSutSetup = false;
    private boolean intentsAreClean = true;

    // We want to setup SUT before activity launches
    private class MyActivityTestRule extends ActivityTestRule<EditProfileActivity> {

        MyActivityTestRule() {
            super(EditProfileActivity.class);
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
    private EditProfileActivity editProfileActivity = null;

    @Before
    public void setUp() throws Exception {
        editProfileActivity = myActivityTestRule.getActivity();
        myActivityTestRule.launchActivity(new Intent());
    }

    @After
    public void tearDown() throws Exception {
        editProfileActivity = null;
    }

    @Test
    // Currently tests to see if all UI features are displayed on the activity screen
    public void testAllViews_OnCreate_NotNull() {
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
        changeText();

        onView(withId(R.id.buttonEditCancel)).perform(click());

        // Check that we went back to LobbyActivity after edit
        intended(hasComponent(LobbyActivity.class.getName()));

        // Check cancelling didn't affect the
        assertEntityStayedSame();
    }

    // Verifies the Ok button brings you back to the lobby.
    @Test
    public void testOkButtonClick_WithoutEdit_ShouldLaunchLobbyActivityWithOriginalEntity(){
        onView(withId(R.id.buttonEditOk)).perform(click());

        // Check if we went back to LobbyActivity
        intended(hasComponent(LobbyActivity.class.getName()));

        assertEntityStayedSame();
    }

    // Verifies text entered into fields is properly saved to the Entity class.
    @Test
    public void testOkButtonClick_WithEdit_ShouldLaunchLobbyActivityWithChangingEntity(){
        // Type in changed text
        changeText();

        // Press Ok button.
        onView(withId(R.id.buttonEditOk)).perform(click());

        // Check if we went back to LobbyActivity
        intended(hasComponent(LobbyActivity.class.getName()));

        // Checks if Entity updated to the same change values
        assertEntityWasChanged();
    }

    @Ignore
    private static void changeText() {
        onView(withId(R.id.editTextDisplayNameInEditProfile))
            .perform(replaceText(EntityUT.Values.CHANGED_DISPLAY_NAME));

        onView(withId(R.id.editTextGender))
                .perform(replaceText(EntityUT.Values.CHANGED_GENDER));

        onView(withId(R.id.editTextAnimal))
                .perform(replaceText(EntityUT.Values.CHANGED_ANIMAL));
    }

    @Ignore
    private static void assertEntityWasChanged() {
        TestHelperService.assertStringEquals("Entity.user.displayName",
                EntityUT.Values.CHANGED_DISPLAY_NAME, Entity.user.getDisplayName());
        TestHelperService.assertStringEquals("Entity.user.gender",
                EntityUT.Values.CHANGED_GENDER, Entity.user.getGender());
        TestHelperService.assertStringEquals("Entity.user.animal",
                EntityUT.Values.CHANGED_ANIMAL, Entity.user.getAnimal());
    }

    @Ignore
    private static void assertEntityStayedSame() {
        TestHelperService.assertStringEquals("Entity.user.displayName",
                EntityUT.Values.DEFAULT_DISPLAY_NAME, Entity.user.getDisplayName());
        TestHelperService.assertStringEquals("Entity.user.gender",
                EntityUT.Values.DEFAULT_GENDER, Entity.user.getGender());
        TestHelperService.assertStringEquals("Entity.user.animal",
                EntityUT.Values.DEFAULT_ANIMAL, Entity.user.getAnimal());
    }
}

