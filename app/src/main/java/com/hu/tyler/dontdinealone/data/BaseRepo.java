package com.hu.tyler.dontdinealone.data;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hu.tyler.dontdinealone.res.DatabaseDocNames;
import com.hu.tyler.dontdinealone.util.Callback;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseRepo implements Repo {

    // Cloud Firestore databaseRef and docRefs
    protected FirebaseFirestore mDb;
    protected String mDocName;

    // Local cache
    protected Map<String, Object> mCache;

    // These dirty flags allows caller to decide when to remotely load and store data.
    protected boolean mIsDirty;

    protected BaseRepo(String docName) {
        // Initialize Cloud Firestore databaseRef
        mDb = FirebaseFirestore.getInstance();

        mDocName = docName;
        mIsDirty = true;
        mCache = new HashMap<String, Object>();

    }

    // Implementation Section -----------------------------------------------

    @Override
    public void setToDefault() {

    }

    @Override
    public Object getDocName(String key) {
        return mCache.get(key);
    }

    @Override
    public boolean isDirty() {
        return mIsDirty;
    }

    @Override
    public Object get(String key) {
        return mCache.get(key);
    }

    @Override
    public void set(String key, Object value) {
        mIsDirty = true;
        mCache.put(key, value);
    }

    @Override
    public void store(@NonNull final Callback callback) {
        mDb.collection(FirebaseAuth.getInstance().getUid()).document(mDocName).set(mCache)

                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // This flag should set prior to the callback function.
                        mIsDirty = false;
                        callback.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFailure(e);
                    }
                });
    }

    @Override
    public void load(@NonNull final Callback callback) {
        mDb.collection(FirebaseAuth.getInstance().getUid()).document(mDocName).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                mCache = document.getData();
                            } else {
                                // WARNING: Defaults unstored for performance. Store if they become needed.
                                setToDefault();
                            }
                            // This clean status should be set between setting the local cache to default
                            // and running the callback function.
                            mIsDirty = false;
                            callback.onSuccess();
                        } else {
                            callback.onFailure(task.getException());
                        }
                    }
                });
    }
}
