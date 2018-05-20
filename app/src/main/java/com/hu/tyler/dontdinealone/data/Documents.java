package com.hu.tyler.dontdinealone.data;

import com.google.firebase.firestore.DocumentReference;

public class Documents {
    private Collections collections;

    // Singleton Holder that creates a single instance of this class.
    private static class DocumentsHolder {
        private final static Documents INSTANCE = new Documents();
    }

    // Reference to self so there is only one instance of this class
    public static Documents getInstance() {
        return DocumentsHolder.INSTANCE;
    }

    private Documents() {
        collections = Collections.getInstance();
    }

    public DocumentReference getUserDocRef() {
        return collections.getUsersCRef().document(collections.getUid());
    }

    public DocumentReference getQueuedUserDocRef() {
        return collections.getQueuedUsersCRef().document(collections.getUid());
    }

    public DocumentReference getOnlineUserDocRef() {
        return collections.getOnlineUsersCRef().document(collections.getUid());
    }


    public DocumentReference getMatchInfoDocRef() {
        return collections.getMatchCRef().document("Info");
    }

    public DocumentReference getMatchUserDocRef() {
        return collections.getMatchCRef().document("User");
    }

    public DocumentReference getMatchGroupDocRef() {
        return collections.getMatchCRef().document("Group");
    }
}
