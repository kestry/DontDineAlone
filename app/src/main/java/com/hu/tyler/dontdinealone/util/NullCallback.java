package com.hu.tyler.dontdinealone.util;

final public class NullCallback implements Callback {

    private static class NullCallbackHolder {
        private final static NullCallback INSTANCE = new NullCallback();
    }

    // Reference to self so there is only one instance of this class
    public static NullCallback getInstance() {
        return NullCallbackHolder.INSTANCE;
    }

    private NullCallback() { }

    // Implementation Section -----------------------------------------------

    @Override
    public void onSuccess() {}
    @Override
    public void onFailure(Exception e) {}

}
