package com.hu.tyler.dontdinealone.data.entity;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hu.tyler.dontdinealone.data.Entity;
import com.hu.tyler.dontdinealone.data.model.Collections;
import com.hu.tyler.dontdinealone.data.model.Documents;
import com.hu.tyler.dontdinealone.data.model.MatchPreferences;
import com.hu.tyler.dontdinealone.domain.OnlineService;
import com.hu.tyler.dontdinealone.res.DatabaseDefaults;
import com.hu.tyler.dontdinealone.util.Callback;
import com.hu.tyler.dontdinealone.util.NullCallback;

import static android.content.ContentValues.TAG;

// This class represents a user in terms of authentication and login.

// Basically, this class is implemented so that when a user is already signed in, their info is
// loaded into the cache at the sign-in check. If there is a Firebase way of doing this or if this
// turns out unnecessary, we can do away with this class.

// Reason for Runnables: This is a quick start to separating things out from the UI until we have
// a better solution and setup.

public class AuthUser {

    private Collections collections;
    private Documents documents;

    // Firebase authentication related members
    private FirebaseAuth mFAuth;
    private FirebaseUser mFUser;

    private NullCallback nullCallback = NullCallback.getInstance();

    public AuthUser() {
        collections = Collections.getInstance();
        documents = Documents.getInstance();
        nullCallback = NullCallback.getInstance();

        // Init user info
        mFAuth = FirebaseAuth.getInstance();
        setFUser(nullCallback);
    }

    public AuthUser(AuthUser other) {
        copy(other);
    }

    public void copy (AuthUser other) {
        collections = other.collections;
        documents = other.documents;
        nullCallback = other.nullCallback;
        mFAuth = other.mFAuth;
        mFUser = other.mFUser;
    }

    public void setFirebaseAuth(FirebaseAuth firebaseAuth) {
        mFAuth = firebaseAuth;
        setFUser(nullCallback);
    }

    private void setFUser(final Callback callback) {
        mFUser = mFAuth.getCurrentUser();
        if (mFUser == null) {
            Entity.user.setToDefault();
            documents.getUserDocRef().set(Entity.user);
            loadUserMatchPreferencesAndInitOnlineUser(callback);
        } else {
            collections.setUid(mFUser.getUid());
            documents.getUserDocRef().get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                // TODO: We may want this all as a transaction
                                if (document.exists()) {
                                    Entity.user.copy(document.toObject(Entity.user.getClass()));
                                } else {
                                    Entity.user.setToDefault();
                                    documents.getUserDocRef().set(Entity.user);
                                }
                                loadUserMatchPreferencesAndInitOnlineUser(callback);

                            } else {
                                callback.onFailure(task.getException());
                            }
                        }
                    });
        }
    }

    public void loadUserMatchPreferencesAndInitOnlineUser(final Callback callback) {
        documents.getUserMatchPreferencesDocRef().get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Entity.matchPreferences
                                        .copy(document.toObject(MatchPreferences.class));
                            } else {
                                Entity.matchPreferences.setToDefault();
                                documents.getUserMatchPreferencesDocRef()
                                        .set(Entity.matchPreferences);
                            }
                            OnlineService.initOnlineUser(callback);
                        } else {
                            callback.onFailure(task.getException());
                        }
            }
        });
    }

    // Authentication and FirebaseUser Section ---------------------------

    public boolean isSignedIn(final Callback callback) {
        setFUser(callback);
        Log.d(TAG, "isSignedIn = " + (mFUser != null));
        return mFUser != null;
    }

    public String getUid() {
        return (mFUser == null) ? DatabaseDefaults.User.UID : mFUser.getUid();
    }

    public String getEmail() {
        return (mFUser == null) ? DatabaseDefaults.User.EMAIL : mFUser.getEmail();
    }

    public boolean isEmailVerified() {
        return (mFUser == null) ? false : mFUser.isEmailVerified();
    }

    public void register(String email, String password, final Callback callback) {
        mFAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    setFUser(nullCallback);
                    //TODO:UNCOMMENT FOR EMAIL VERIFICATION BELOW
                    //mUser.sendEmailVerification();
                    //Log out immediately to prevent illegal sign in without email confirmation
                    signOut();
                    callback.onSuccess();
                } else {
                    callback.onFailure(task.getException());
                }
            }
        });
    }

    public void signIn(String email, String password, final Callback callback) {
        mFAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    setFUser(callback);
                    //RepoContainer.profileRepo.load(documents.getProfileDocRef(), callback);
                } else {
                    callback.onFailure(task.getException());
                }
            }
        });
    }

    public void signOut() {
        mFAuth.signOut();
        // Need to manually set null, since we do not constantly get current user.
        // May need to change if we begin to multithread?
        setFUser(nullCallback);
    }

}
