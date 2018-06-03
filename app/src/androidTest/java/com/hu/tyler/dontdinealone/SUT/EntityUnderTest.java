package com.hu.tyler.dontdinealone.SUT;


import android.content.Intent;
import android.util.Log;

import static android.content.ContentValues.TAG;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hu.tyler.dontdinealone.data.Entity;

public abstract class EntityUnderTest {

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
        Entity.user.setDisplayName(EntityUnderTest.Values.DEFAULT_DISPLAY_NAME);
        Entity.user.setGender(EntityUnderTest.Values.DEFAULT_GENDER);
        Entity.user.setAnimal(EntityUnderTest.Values.DEFAULT_ANIMAL);

    }

    private static String mockUid = Values.DEFAULT_UID;
    private static String mockEmail = Values.DEFAULT_EMAIL;
    // We use mock instead of spy in order to encourge explicit definition of methods, so that
    // we can decide whether the real method does not rely on state and is therefore ok to use.
    @Mock
    static FirebaseAuth mockFirebaseAuth = mock(FirebaseAuth.class);
    @Mock
    static FirebaseUser mockFirebaseUser = mock(FirebaseUser.class);
    @Mock
    static Task mockSuccessfulTask = mock(Task.class);
    @Mock
    static Task mockFailedTask = mock(Task.class);
    @Mock
    static Exception mockException = mock(Exception.class);

    /**
     * Sets the real entity under test to mock values. This is
     * Note that if more methods are used in the app, then they may need to be mocked,
     * unless it is absolutely certain the methods are not dependent
     * on their own state. If strange inconsistencies continue to occur, then app code may be
     * directly calling Firebase instead of properly using the Entity interface.
     * @PostCondition - call OnlineService.initOnlineUser() when user is supposed to be online.
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

        // Mock the authentication success cases
        when(mockFirebaseAuth.createUserWithEmailAndPassword(Values.DEFAULT_EMAIL, Values.DEFAULT_EMAIL))
                .thenReturn(mockSuccessfulTask);
        when(mockFirebaseAuth.signInWithEmailAndPassword(Values.DEFAULT_EMAIL, Values.DEFAULT_PASSWORD))
                .thenReturn(mockSuccessfulTask);

        // Mock the authentication failure cases
        when(mockFirebaseAuth.createUserWithEmailAndPassword(Values.INVALID_EMAIL, Values.DEFAULT_PASSWORD))
                .thenReturn(mockFailedTask);
        when(mockFirebaseAuth.signInWithEmailAndPassword(Values.INVALID_EMAIL, Values.DEFAULT_PASSWORD))
                .thenReturn(mockFailedTask);

        // Set SUT to mock
        Task task = Entity.authUser.setFirebaseAuth(mockFirebaseAuth);
        Log.d(TAG, "setupWithMock(): userEmail = " + Entity.onlineUser.getEmail());
        return task;
    }

    public static void setupWithNullUser() {
        mockFirebaseUser = null;
        when(mockFirebaseAuth.getCurrentUser()).thenReturn(mockFirebaseUser);
    }

    public static void teardown() {
        SaveEntityService.restoreEntity();

        mockFirebaseAuth = null;
        mockFirebaseUser = null;
        mockSuccessfulTask = null;
        mockFailedTask = null;
        mockException = null;
    }

    @Test // Test Entity SUT setup
    public void testEntity_WithMockSetup_ShouldEqualMock() {
        Log.d(TAG, "testEntity_WithMockSetup_ShouldEqualMock(): userUid = " +
                Entity.onlineUser.getDocumentId());
        Log.d(TAG, "testEntity_WithMockSetup_ShouldEqualMock(): userEmail = " +
                Entity.onlineUser.getEmail());
        assertTrue(Entity.onlineUser.getDocumentId()
                .equals(EntityUnderTest.Values.DEFAULT_UID) && Entity.onlineUser.getEmail()
                .equals(EntityUnderTest.Values.DEFAULT_EMAIL));
    }

}
