package com.hu.tyler.dontdinealone.data;

        import android.support.annotation.NonNull;

        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.firestore.DocumentReference;
        import com.google.firebase.firestore.DocumentSnapshot;
        import com.google.firebase.firestore.SetOptions;
        import com.hu.tyler.dontdinealone.data.Cache;
        import com.hu.tyler.dontdinealone.util.Callback;

        import java.util.Map;

public class Repo {
    // Local cache
    private Cache mCache;

    // These dirty flags allows caller to decide when to remotely load and store data.
    private boolean mIsDirty;

    public Repo(Cache cache) {
        // Initialize Cloud Firestore databaseRef
        mCache = cache;
        mIsDirty = true;
    }

    // Implementation Section -----------------------------------------------

    public void setToDefault(DocumentReference docRef, @NonNull final Callback callback) {
        mCache.setToDefault();
        store(docRef, callback);
    }

    public boolean isDirty() {
        return mIsDirty;
    }

    public Object get(String key) {
        return mCache.get(key);
    }

    public void set(String key, Object value) {
        mIsDirty = true;
        mCache.put(key, value);
    }

    public void store(DocumentReference docRef, @NonNull final Callback callback) {
        docRef.set(mCache)

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

    public void overwrite(DocumentReference docRef, @NonNull final Callback callback) {
        docRef.set(mCache)

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

    public void merge(Map<String, Object> partialMap, DocumentReference docRef, @NonNull final Callback callback) {
        docRef.set(partialMap, SetOptions.merge())

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

    public void update(Map<String, Object> partialMap, DocumentReference docRef, @NonNull final Callback callback) {
        docRef.update(partialMap)

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

    public void load(final DocumentReference docRef, @NonNull final Callback callback) {
        docRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                mCache.clear();
                                Map<String, Object> map = document.getData();
                                for (Map.Entry<String, Object> entry : map.entrySet()) {
                                    mCache.put(entry.getKey(), entry.getValue());
                                }
                            } else {
                                setToDefault(docRef, callback);
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
