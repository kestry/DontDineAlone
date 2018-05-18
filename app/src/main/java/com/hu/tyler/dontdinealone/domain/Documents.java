package com.hu.tyler.dontdinealone.domain;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hu.tyler.dontdinealone.res.DatabaseDocNames;

public class Documents {
    private FirebaseFirestore mDb;
    private String mUid;

    // Singleton Holder that creates a single instance of this class.
    private static class DocumentsHolder {
        private final static Documents INSTANCE = new Documents();
    }

    // Reference to self so there is only one instance of this class
    public static Documents getInstance() {
        return DocumentsHolder.INSTANCE;
    }

    private Documents() {
        mDb = FirebaseFirestore.getInstance();
        mUid = "";
    }

    public void setUid(String uid) {
        mUid = uid;
    }

    public DocumentReference getProfileDocRef() {
        return mDb.collection(mUid).document(DatabaseDocNames.PROFILE);
    }

    public DocumentReference getPreferenceDocRef() {
        return mDb.collection("Matching").document("Users").collection(mUid).document(DatabaseDocNames.PREFERENCE);
    }
}
