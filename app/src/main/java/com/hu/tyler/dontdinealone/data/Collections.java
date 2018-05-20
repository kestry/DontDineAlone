package com.hu.tyler.dontdinealone.data;

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

    // For Match system
    public CollectionReference getMatchCRef() {
        return mDb.collection("Match");
    }
    // For persistent user info
    public CollectionReference getUsersCRef() {
        return mDb.collection("Users");
    }

    // For online users
    public CollectionReference getOnlineUsersCRef() {
        return mDb.collection("Online");
    }

    // For queued users
    public CollectionReference getQueuedUsersCRef() {
        return getMatchCRef().document("User").collection("Queue");
    }

    // For pending groups
    public CollectionReference getPendingGroupsCRef() {
        return getMatchCRef().document("Group").collection("Pending");
    }

    // For confirmed groups
    public CollectionReference getConfirmedGroupsCRef() {
        return getMatchCRef().document("Group").collection("Confirmed");
    }

    // For Matched
    public CollectionReference getMatchedCRef() {
        return mDb.collection("Matched");
    }

}
