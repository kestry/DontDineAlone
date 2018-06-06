package com.hu.tyler.dontdinealone;

import android.content.Intent;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.hu.tyler.dontdinealone.SUT.EntityUT;
import com.hu.tyler.dontdinealone.general.TestHelperService;
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

    private static boolean intentsAreClean = true;
/*
    // We want to setup SUT before activity launches
    private class MyActivityTestRule extends ActivityTestRule<EditProfileActivity> {
        MyActivityTestRule() {
            super(EditProfileActivity.class);
        }

        @Override
        public void beforeActivityLaunched() {
            EntityUT.setupWithMock();

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
            EntityUT.teardown();
        }
    }
*/
    @Rule
    public ActivityTestRule<EditProfileActivity> myActivityTestRule
        = new ActivityTestRule<EditProfileActivity>(EditProfileActivity.class);
    private EditProfileActivity testActivity = null;

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
    @Test
    // Currently tests to see if all UI features are displayed on the activity screen
    public void testAllViews_OnCreate_NotNull() {
        View cancelButton    = testActivity.findViewById(R.id.buttonEditCancel);
        View okButton        = testActivity.findViewById(R.id.buttonEditOk);
        View textAnimalSpace = testActivity.findViewById(R.id.editTextAnimal);
        View textAnimalPrompt= testActivity.findViewById(R.id.textView5);
        View textGenderSpace = testActivity.findViewById(R.id.editTextGender);
        View textGenderPrompt= testActivity.findViewById(R.id.textView4);
        View editNameSpace   = testActivity.findViewById(R.id.editTextDisplayNameInEditProfile);
        View editNamePrompt  = testActivity.findViewById(R.id.textView3);
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