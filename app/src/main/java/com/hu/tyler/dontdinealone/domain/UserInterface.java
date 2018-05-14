package com.hu.tyler.dontdinealone.domain;

import com.hu.tyler.dontdinealone.util.Callback;

public interface UserInterface {
    public String getUid();
    public String getEmail();
    public boolean isEmailVerified();
    public boolean isSignedIn();
    public void register(String email, String password, final Callback callback);
    public void signIn(String email, String password, final Callback callback);
    public void signOut();

}
