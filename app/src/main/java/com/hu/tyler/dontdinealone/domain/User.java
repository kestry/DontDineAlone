package com.hu.tyler.dontdinealone.domain;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hu.tyler.dontdinealone.data.UserProfileRepo;
import com.hu.tyler.dontdinealone.util.Callback;
import com.hu.tyler.dontdinealone.util.NullCallback;

// This class represents a user in terms of authentication and login.

// Basically, this class is implemented so that when a user is already signed in, their info is
// loaded into the cache at the sign-in check. If there is a Firebase way of doing this or if this
// turns out unnecessary, we can do away with this class.

// Reason for Runnables: This is a quick start to separating things out from the UI until we have
// a better solution and setup.

public class User implements UserInterface {

    public UserProfileRepo repo;

    // Firebase authentication related members
    private static FirebaseAuth mFAuth;
    private static FirebaseUser mFUser;

    private NullCallback nullCallback = NullCallback.getInstance();

    // Singleton Holder that creates a single instance of this class.
    private static class UserSingletonHolder
    {
        private final static User INSTANCE = new User();
    }

    // Reference to self so there is only one instance of this class
    public static User getInstance()
    {
        return UserSingletonHolder.INSTANCE;
    }

    // Private constructor. Called once by the UserModelHolder
    private User() {
        repo = UserProfileRepo.getInstance();
        mFAuth = FirebaseAuth.getInstance();

        // Init user info
        setFUser(nullCallback);
    }

    private void setFUser(Callback callback) {
        mFUser = mFAuth.getCurrentUser();
        if (mFUser == null) {
            repo.setToDefault();
            callback.onFailure(null);
        } else {
            repo.load(callback);
        }
    }

    // Authentication and FirebaseUser Section ---------------------------

    public boolean isSignedIn(final Callback callback) {
        setFUser(callback);
        return mFUser != null;
    }

    public String getUid() {
        return mFUser.getUid();
    }

    public String getEmail() {
        return mFUser.getEmail();
    }

    public boolean isEmailVerified() {
        return mFUser.isEmailVerified();
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
                    setFUser(nullCallback);
                    repo.load(NullCallback.getInstance());
                    callback.onSuccess();
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
