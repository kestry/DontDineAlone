package com.hu.tyler.dontdinealone;

import android.support.test.rule.ActivityTestRule;
import android.view.View;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Rule;
import static org.junit.Assert.*;

public class EditProfileActivityTest {

    @Rule
    public ActivityTestRule<EditProfileActivity> EditProfileActivityTestRule =
            new ActivityTestRule<EditProfileActivity>(EditProfileActivity.class);
    private EditProfileActivity profileActivity = null;

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
    public void onCreate() {
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
}