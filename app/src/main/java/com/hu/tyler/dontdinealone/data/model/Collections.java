package com.hu.tyler.dontdinealone.data.model;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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

    //-------------------------------------------------------------------------------------------

    // Incrememt the build number as developing so that we don't have conflicts with old builds.
    // i.e. change BUILD-2 to BUILD-3. Implement a counter if more explicit coordination is needed.
    public DocumentReference getBuildDocRef() {
        return mDb.collection("BUILDS").document("BUILD-666");
    }

    //--------------------------------------------------------------------------------------------

    /** Collections that stay the same for both live and development */

    public CollectionReference getUserMatchPreferencesCRef() {
        return getUsersCRef().document(mUid)
                .collection("Match Preferences");
    }

    public CollectionReference getUserMatchPreferencesCRef(String uid) {
        return getUsersCRef().document(uid)
                .collection("Match Preferences");
    }

    // For online users
    public CollectionReference getGroupMatchPreferencesCRef(String gid) {
        return getGroupsCRef().document(gid)
                .collection("Match Preferences");
    }

    //--------------------------------------------------------------------------------------------

    /** LIVE COLLECTIONS (Switch between Live and Development Build dBs as appropriate).

     // For meta data
     public CollectionReference getMetaCRef() { return mDb.collection("Meta"); }

    // For persistent user info
    public CollectionReference getUsersCRef() { return mDb.collection("Users"); }

    // For online users
    public CollectionReference getOnlineUsersCRef() { return mDb.collection("Online"); }

     // For groups
     public CollectionReference getGroupsCRef() { return mDb.collection("Groups"); }

     // For matched/chat
     public CollectionReference getMatchedCRef() { return mDb.collection("Matched"); }


     END of LIVE COLLECTIONS */

    //-------------------------------------------------------------------------------------------
    /** Development Build Collections. Comment out and use Live when done developing. */

    // For persistent user info
    public CollectionReference getUsersCRef() {
        return getBuildDocRef().collection("Users");
    }

    // For online users
    public CollectionReference getOnlineUsersCRef() {
        return getBuildDocRef().collection("Online");
    }

    // For groups
    public CollectionReference getGroupsCRef() {
        return getBuildDocRef().collection("Groups");
    }

    // For meta data
    public CollectionReference getSystemCRef() {
        return getBuildDocRef().collection("System");
    }

    // For matched/chat
    public CollectionReference getMatchedCRef() {
        return getBuildDocRef().collection("Matched");
    }

    /* End of Development Build Collections */

}
