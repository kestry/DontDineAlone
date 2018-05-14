package com.hu.tyler.dontdinealone.util;

public interface Callback {
    void onSuccess();
    void onFailure(Exception e);
}
