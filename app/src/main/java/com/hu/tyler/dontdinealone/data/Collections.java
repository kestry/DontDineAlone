package com.hu.tyler.dontdinealone.data;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hu.tyler.dontdinealone.res.DatabaseKeys;

/**
 * This class contains the collection references to the Cloud Firestore database for our app,
 * so that we have one spot to reference and make changes at.
 *
 * Usage:
 *     private Collections collections;
 *
 *     In the constructor or OnCreate function, we initialize it to:
 *        collections = Collections.getInstance();
 *
 *     Then we can just call with:
 *        collections.getUserCRef();
 *
 * Naming Convention of Methods:
 *     get_____CRef();
 *
 * Developer Notes:
 *     We don't want to declare Documents in here, bad things may happen, since Documents depends
 *     on this instance.
 */
public class Collections {
    private FirebaseFirestore mDb;
    private String mUid;

    // Singleton Holder that creates a single instance of this class.
    private static class CollectionsHolder {
        // This is the single instance of this class.
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
    //public CollectionReference getUsersCRef() { return mDb.collection("Users"); }

    // TODO: DELETE THIS AND USE ABOVE ONCE WE ARE DONE DEVELOPING MAYBE?
    public CollectionReference getUsersCRef() {
        return mDb.collection("BUILDS").document("BUILD-001").collection("Users");
    }


    // For online users
    //public CollectionReference getOnlineUsersCRef() { return mDb.collection("Online"); }

    // TODO: DELETE THIS AND USE ABOVE ONCE WE ARE DONE DEVELOPING MAYBE?
    public CollectionReference getOnlineUsersCRef() {
        return mDb.collection("BUILDS").document("BUILD-001").collection("Online");
    }


    // For queued users
    public CollectionReference getQueuedUsersCRef() {
        return getMatchCRef().document("User").collection("Queue");
    }

    // For queued users
    public CollectionReference getQueuedUserGroupSizePrefCRef() {
        return getMatchCRef().document("User").collection("Queue").document(mUid).collection("Preference");
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
