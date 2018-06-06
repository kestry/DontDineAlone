package com.hu.tyler.dontdinealone.SUT;


import android.util.Log;

import static android.content.ContentValues.TAG;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hu.tyler.dontdinealone.data.Entity;
import com.hu.tyler.dontdinealone.data.model.Documents;

// Cody and Jean Pair Programmed
public abstract class EntityUT {

    public interface Values {
        public static final String DEFAULT_UID = "defaultTestUid";
        public static final String DEFAULT_EMAIL = "defaultTestEmail@ucsc.edu";
        public static final String DEFAULT_PASSWORD = "defaultTestPassword_123";

        public static final String INVALID_EMAIL = "invalidTestEmail";

        public static final String DEFAULT_DISPLAY_NAME = "defaultTestDisplayName";
        public static final String DEFAULT_GENDER = "defaultTestGender";
        public static final String DEFAULT_ANIMAL = "defaultTestAnimal";

        public static final String CHANGED_DISPLAY_NAME = "changedTestDisplayName";
        public static final String CHANGED_GENDER = "changedTestGender";
        public static final String CHANGED_ANIMAL = "changedTestAnimal";
    }

    public static void setProfileToDefault() {
        Entity.user.setDisplayName(EntityUT.Values.DEFAULT_DISPLAY_NAME);
        Entity.user.setGender(EntityUT.Values.DEFAULT_GENDER);
        Entity.user.setAnimal(EntityUT.Values.DEFAULT_ANIMAL);
        Documents.getInstance().getUserDocRef().set(Entity.user);
    }

    private static String mockUid = Values.DEFAULT_UID;
    private static String mockEmail = Values.DEFAULT_EMAIL;
    // We use mock instead of spy in order to encourge explicit definition of methods, so that
    // we can decide whether the real method does not rely on state and is therefore ok to use.
    @Mock
    public static FirebaseAuth mockFirebaseAuth = mock(FirebaseAuth.class);
    @Mock
    public static FirebaseUser mockFirebaseUser = mock(FirebaseUser.class);
    @Mock
    public static Task mockSuccessfulTask = mock(Task.class);
    @Mock
    public static Task mockFailedTask = mock(Task.class);
    @Mock
    public static Exception mockException = mock(Exception.class);

    /**
     * Sets the real entity under test to mock values.
     * Note that when using the mocked object, if there are unmocked methods that are used
     * it's recommended you explicitly mock the method unless very certain that the methods
     * are not dependent on their own state. If strange inconsistencies continue to occur,
     * then it's likely that app code may directly calling Firebase somewhere instead of
     * using the Entity interface.
     */
    @Ignore
    public static Task setupWithMock() {
        SaveEntityService.saveEntity();

        mockFirebaseAuth = mock(FirebaseAuth.class);
        mockFirebaseUser = mock(FirebaseUser.class);
        mockSuccessfulTask = mock(Task.class);
        mockFailedTask = mock(Task.class);
        mockException = mock(Exception.class);

        when(mockSuccessfulTask.isSuccessful()).thenReturn(true);
        when(mockSuccessfulTask.isComplete()).thenReturn(true);

        when(mockFailedTask.isSuccessful()).thenReturn(false);
        when(mockFailedTask.isComplete()).thenReturn(false);
        when(mockFailedTask.getException()).thenReturn(mockException);

        when(mockFirebaseUser.getUid()).thenReturn(mockUid);
        when(mockFirebaseUser.getEmail()).thenReturn(mockEmail);
        when(mockFirebaseUser.isEmailVerified()).thenReturn(true);
        when(mockFirebaseUser.sendEmailVerification()).thenReturn(mockSuccessfulTask);

        when(mockFirebaseAuth.getCurrentUser()).thenReturn(mockFirebaseUser);


        when(mockFirebaseAuth.createUserWithEmailAndPassword(Values.DEFAULT_EMAIL, Values.DEFAULT_EMAIL))
                .thenReturn(mockSuccessfulTask);
        when(mockFirebaseAuth.createUserWithEmailAndPassword(Values.INVALID_EMAIL, Values.DEFAULT_PASSWORD))
                .thenReturn(mockFailedTask);

        when(mockFirebaseAuth.signInWithEmailAndPassword(Values.DEFAULT_EMAIL, Values.DEFAULT_PASSWORD))
                .thenReturn(mockSuccessfulTask);
        when(mockFirebaseAuth.signInWithEmailAndPassword(Values.INVALID_EMAIL, Values.DEFAULT_PASSWORD))
                .thenReturn(mockFailedTask);

        doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                mockFirebaseUser = null;
                return null;
            }
        }).when(mockFirebaseAuth).signOut();

        // Set SUT to mock
        Task task = Entity.authUser.setFirebaseAuth(mockFirebaseAuth);
        Log.d(TAG, "setupWithMock(): userEmail = " + Entity.onlineUser.getEmail());
        setProfileToDefault();
        return task;
    }

    @Ignore
    public static void setupWithNullMockUser() {
        SaveEntityService.saveEntity();
        setupWithMock();
    }

    @Ignore
    public static void teardown() {
        mockFirebaseAuth.signOut();
        SaveEntityService.restoreEntity();
        Entity.authUser.setFirebaseAuth(FirebaseAuth.getInstance());

        mockFirebaseAuth = null;
        mockFirebaseUser = null;
        mockSuccessfulTask = null;
        mockFailedTask = null;
        mockException = null;
    }

    @Test // Test Entity CUT setup
    public void testEntity_WithMockSetup_ShouldEqualMock() {
        Log.d(TAG, "testEntity_WithMockSetup_ShouldEqualMock(): userUid = " +
                Entity.onlineUser.getDocumentId());
        Log.d(TAG, "testEntity_WithMockSetup_ShouldEqualMock(): userEmail = " +
                Entity.onlineUser.getEmail());
        assertTrue(Entity.onlineUser.getDocumentId()
                .equals(EntityUT.Values.DEFAULT_UID) && Entity.onlineUser.getEmail()
                .equals(EntityUT.Values.DEFAULT_EMAIL));
    }

}
