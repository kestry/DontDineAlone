package com.hu.tyler.dontdinealone.data.model;

import com.google.firebase.firestore.DocumentReference;

/**
 * This class contains the document references to the Cloud Firestore database for our app,
 * so that we have one spot to reference and make changes at.
 *
 * Usage Method 1: private variable
 *     private Documents documents;
 *
 *     // In the constructor or OnCreate function, we initialize it to:
 *     documents = Documents.getInstance();
 *
 *     // Then we can just call with:
 *     documents.getUserDocRef();
 *
 * Usage Method 2: direct call
 *     Documents.getInstance().getUserDocRef();
 *
 * Naming Convention of Methods:
 *     get_____DocRef();
 */
public class Documents {
    // We use collections so that we can change our database organization more easily.
    private Collections collections;

    // Singleton Holder that creates a single instance of this class.
    private static class DocumentsHolder {
        // This is the single instance of this class.
        private final static Documents INSTANCE = new Documents();
    }
    private Documents() {
        collections = Collections.getInstance();
    }

    // Set your variable with this
    public static Documents getInstance() {
        return DocumentsHolder.INSTANCE;
    }

    //--------------------------------------------------------------------------------------------

    public DocumentReference getUserDocRef() {
        return collections.getUsersCRef().document(collections.getUid());
    }

    public DocumentReference getUserDocRef(String uid) {
        return collections.getUsersCRef().document(uid);
    }

    public DocumentReference getOnlineUserDocRef() {
        return collections.getOnlineUsersCRef().document(collections.getUid());
    }

    public DocumentReference getOnlineUserDocRef(String uid) {
        return collections.getUsersCRef().document(uid);
    }

    public DocumentReference getUserMatchPreferencesDocRef() {
        return collections.getUserMatchPreferencesCRef().document("Match Preferences");
    }

    public DocumentReference getUserMatchPreferencesDocRef(String uid) {
        return collections.getUserMatchPreferencesCRef(uid).document("Match Preferences");
    }

    public DocumentReference getGroupMatchPreferencesDocRef(String gid) {
        return collections.getGroupMatchPreferencesCRef(gid).document("Match Preferences");
    }

    public DocumentReference getGroupFactoryDocRef() {
        return collections.getSystemCRef().document("GroupFactory");
    }
}
