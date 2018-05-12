package com.hu.tyler.dontdinealone.models;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

public class UserModel {

    private DatabaseModel mDb;

    // Firebase authentication related members
    private static FirebaseAuth mFAuth;
    private static FirebaseUser mFUser;

    // Communicates exceptions back to the view
    private static Exception mException;

    // This is an optimal way to make singletons both lazy and thread-safe.
    // However singletons are considered bad, and so it would be good to look into other solutions:
    // - SharedPreferences, ViewModel, Room, MVP
    // source: https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom
    private static class UserModelHolder
    {
        private final static UserModel INSTANCE = new UserModel();
    }

    // Reference to self so there is only one instance of this class
    public static UserModel getInstance()
    {
        return UserModelHolder.INSTANCE;
    }

    // Private constructor. Called once by the UserModelHolder
    private UserModel() {
        mDb = DatabaseModel.getInstance();
        // Init authentication
        mFAuth = FirebaseAuth.getInstance();

        // Init user info
        setFUser();
        mException = null;
    }

    private void setFUser() {
        mFUser = mFAuth.getCurrentUser();
        if (mFUser == null) {
            mDb.setEmail("");
            mDb.setUid("");
            mDb.setLocalProfileToDefault();
        } else {
            mDb.setEmail(mFUser.getEmail());
            mDb.setUid(mFUser.getUid());
            mDb.loadProfile(null, null);
        }
    }

    // Authentication and FirebaseUser Section ---------------------------

    public Exception getException() {
        return mException;
    }

    public boolean isSignedIn() {
        setFUser();
        return mFUser != null;
    }

    public String getUid() {
        return mDb.getUid();
    }

    // This may throw an Exception if firebaseUser is null (Todo: Test this?).
    public String getEmail() {
        return mDb.getEmail();
    }

    public boolean isEmailVerified() {
        return mFUser.isEmailVerified();
    }

    public void signIn(String email, String password, final Runnable successRunnable, final Runnable failureRunnable) {
        mDb.setEmail(email);
        mFAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    setFUser();
                    mDb.loadProfile(null, null);
                    if (successRunnable != null) {
                        successRunnable.run();
                    }
                } else {
                    mException = task.getException();
                    if (failureRunnable != null) {
                        failureRunnable.run();
                    }
                }
            }
        });
    }

    public void signOut() {
        mFAuth.signOut();
        // Need to manually set null, since we do not constantly get current user.
        // May need to change if we begin to multithread?
        setFUser();
        mException = null;
    }

    public void register(String email, String password, final Runnable successRunnable, final Runnable failureRunnable) {
        mFAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    setFUser();
                    //TODO:UNCOMMENT FOR EMAIL VERIFICATION BELOW
                    //mUser.sendEmailVerification();
                    //Log out immediately to prevent illegal sign in without email confirmation
                    signOut();
                    if (successRunnable != null) {
                        successRunnable.run();
                    }
                } else {
                    if (failureRunnable != null) {
                        mException = task.getException();
                        failureRunnable.run();
                    }
                }
            }
        });
    }


}
