package com.hu.tyler.dontdinealone.domain;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Collections {
    private FirebaseFirestore mDb;
    private String mUid;

    // Singleton Holder that creates a single instance of this class.
    private static class CollectionsHolder {
        private final static Collections INSTANCE = new Collections();
    }

    // Reference to self so there is only one instance of this class
    public static Collections getInstance() {
        return CollectionsHolder.INSTANCE;
    }

    private Collections() {
        mDb = FirebaseFirestore.getInstance();
        mUid = "";
    }

    public String getUid() {
        return mUid;
    }

    public void setUid(String uid) {
        mUid = uid;
    }

    // For persistent user info
    public CollectionReference getUsersCRef() {
        return mDb.collection("Users");
    }

    // For online users
    public CollectionReference getOnlineCRef() {
        return mDb.collection("Online");
    }

    // For queued users
    public CollectionReference getQueueCRef() {
        return mDb.collection("Queue");
    }
}
