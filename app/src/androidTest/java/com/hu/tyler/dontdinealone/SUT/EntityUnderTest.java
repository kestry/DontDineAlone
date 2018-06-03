package com.hu.tyler.dontdinealone.SUT;


import android.content.Intent;
import android.util.Log;

import static android.content.ContentValues.TAG;
import static org.mockito.Mockito.*;

import org.junit.Ignore;
import org.mockito.Mock;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hu.tyler.dontdinealone.data.Entity;

public abstract class EntityUnderTest {

    public static final String FAKE_VALID_UID = "fakeValidUid";
    public static final String FAKE_VALID_EMAIL = "fakeValidEmail@ucsc.edu";
    public static final String FAKE_VALID_PASSWORD = "fakeValidPassword_123";

    public static final String FAKE_INVALID_EMAIL = "fakeInvalidEmail@ucsc.edu";

    private static String mockUid = FAKE_VALID_UID;
    private static String mockEmail = FAKE_VALID_EMAIL;

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
    public static void setupWithMock() {

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
        when(mockFirebaseAuth.createUserWithEmailAndPassword(FAKE_VALID_EMAIL, FAKE_VALID_PASSWORD))
                .thenReturn(mockSuccessfulTask);
        when(mockFirebaseAuth.signInWithEmailAndPassword(FAKE_VALID_EMAIL, FAKE_VALID_PASSWORD))
                .thenReturn(mockSuccessfulTask);

        // Mock the authentication failure cases
        when(mockFirebaseAuth.createUserWithEmailAndPassword(FAKE_INVALID_EMAIL, FAKE_VALID_PASSWORD))
                .thenReturn(mockFailedTask);
        when(mockFirebaseAuth.signInWithEmailAndPassword(FAKE_INVALID_EMAIL, FAKE_VALID_PASSWORD))
                .thenReturn(mockFailedTask);

        // Set SUT to mock
        Entity.authUser.setFirebaseAuth(mockFirebaseAuth);

        Log.d(TAG, "setupWithMock(): userEmail = " + Entity.onlineUser.getEmail());
    }

    public static void setupWithNullUser() {

    }

    public static void teardown() {
        SaveEntityService.restoreEntity();

        mockFirebaseAuth = null;
        mockFirebaseUser = null;
        mockSuccessfulTask = null;
        mockFailedTask = null;
        mockException = null;
    }


}
