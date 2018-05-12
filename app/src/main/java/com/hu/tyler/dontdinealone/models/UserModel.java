package com.hu.tyler.dontdinealone.models;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserModel {
    private static UserModel obj;
    private static FirebaseAuth mAuth;
    private static FirebaseUser mUser;
    private static String mEmail;
    private static Exception mException;

    private UserModel() {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mEmail = "";
        mException = null;
    }

    public static UserModel getInstance() {
        if (obj == null) {
            obj = new UserModel();
        }
        return obj;
    }

    public Exception getException() {
        return mException;
    }

    public boolean isSignedIn() {
        return mUser != null;
    }

    public String getUid() {
        return mUser.getUid();
    }

    // This may throw an Exception if firebaseUser is null (Todo: Test this?).
    public String getEmail() {
        return mEmail;
    }

    public boolean isEmailVerified() {
        return mUser.isEmailVerified();
    }

    public void signIn(String email, String password, final Runnable successRunnable, final Runnable failureRunnable) {
        mEmail = email;
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mUser = mAuth.getCurrentUser();
                    successRunnable.run();
                } else {
                    mException = task.getException();
                    failureRunnable.run();
                }
            }
        });
    }

    public void signOut() {
        mAuth.signOut();
        mUser = null;
        mException = null;
    }

    public void register(String email, String password, final Runnable successRunnable, final Runnable failureRunnable) {
        mEmail = email;
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mUser = mAuth.getCurrentUser();
                    //TODO:UNCOMMENT FOR EMAIL VERIFICATION BELOW
                    //mUser.sendEmailVerification();
                    //Log out immediately to prevent illegal sign in without email confirmation
                    obj.signOut();
                    successRunnable.run();
                } else {
                    mException = task.getException();
                    failureRunnable.run();
                }
            }
        });
    }
}
