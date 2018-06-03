package com.hu.tyler.dontdinealone;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.EditText;

import com.hu.tyler.dontdinealone.data.Entity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;

import java.util.Map;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

public class EditProfileActivityTest {

    @Rule
    public ActivityTestRule<EditProfileActivity> EditProfileActivityTestRule =
            new ActivityTestRule<EditProfileActivity>(EditProfileActivity.class);
    private EditProfileActivity profileActivity = null;
    Instrumentation.ActivityMonitor returnToLobbyMonitor =
            getInstrumentation().addMonitor(LobbyActivity.class.getName(), null, false);


    @Before
    public void setUp() throws Exception {
        profileActivity = EditProfileActivityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        profileActivity = null;
    }

    @Test
    // Tests to see if all UI features are displayed on the Edit Profile page
    public void testOnCreate() {
        View cancelButton    = profileActivity.findViewById(R.id.buttonEditCancel);
        View okButton        = profileActivity.findViewById(R.id.buttonEditOk);
        View textAnimalSpace = profileActivity.findViewById(R.id.editTextAnimal);
        View textAnimalPrompt= profileActivity.findViewById(R.id.textView5);
        View textGenderSpace = profileActivity.findViewById(R.id.editTextGender);
        View textGenderPrompt= profileActivity.findViewById(R.id.textView4);
        View editNameSpace   = profileActivity.findViewById(R.id.editTextDisplayName);
        View editNamePrompt  = profileActivity.findViewById(R.id.textView3);
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
    public void testOKButtonOnClick(){
        onView(withId(R.id.buttonEditOk)).perform(click());
        Activity lobbyActivity = getInstrumentation().waitForMonitorWithTimeout(returnToLobbyMonitor, 5000);
        assertNotNull(lobbyActivity);
        lobbyActivity.finish();
    }

    // Verifies the Ok button brings you back to the lobby.
    @Test
    public void testCancelButtonOnClick(){
        onView(withId(R.id.buttonEditCancel)).perform(click());
        Activity lobbyActivity = getInstrumentation().waitForMonitorWithTimeout(returnToLobbyMonitor, 5000);
        assertNotNull(lobbyActivity);
        lobbyActivity.finish();
    }
    
    // Verifies text entered into fields is properly saved to the Entity class.
    @Test
    public void testSaveUserInfo(){
        String name = "Test Name";
        String gender = "Test Gender";
        String animal = "Test Animal";
        // Enters test values into text fields
        onView(withId(R.id.editTextDisplayName)).perform(replaceText(name));
        onView(withId(R.id.editTextGender)).perform(replaceText(gender));
        onView(withId(R.id.editTextAnimal)).perform(replaceText(animal));
        // Saves the entered info
        onView(withId(R.id.buttonEditOk)).perform(click());
        // Verifies the entered info was saved to the Entity class
        assertEquals(name, Entity.user.getDisplayName());
        assertEquals(gender, Entity.user.getGender());
        assertEquals(animal, Entity.user.getAnimal());
    }
}