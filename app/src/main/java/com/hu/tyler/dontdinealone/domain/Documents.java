package com.hu.tyler.dontdinealone.domain;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hu.tyler.dontdinealone.res.DatabaseDocNames;

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
        return collections.getQueueCRef().document(collections.getUid());
    }

    public DocumentReference getOnlineUserDocRef() {
        return collections.getOnlineCRef().document(collections.getUid());
    }
}
