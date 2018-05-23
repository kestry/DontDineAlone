package com.hu.tyler.dontdinealone.domain;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.hu.tyler.dontdinealone.util.Callback;

/**
 * This class is abstract because it provides static service implementations without instantiation.
 * Usage:
 *    Class.method();
 */
public abstract class TimestampService {
    /**
     * Update the key to a new timestamp asynchronously.
     * However, does not update our local timestamp at this time.
     * @param docRef
     * @param key
     */
    public static void updateRemoteTimestamp(DocumentReference docRef, String key) {
        docRef.update(key, FieldValue.serverTimestamp());
    }

    /**
     * This function has Firestore update the key to a new timestamp with a callback.
     * However, the function does not update our local timestamp at this time.
     * @param docRef
     * @param key
     * @param callback - call with NullCallback.getInstance() if you don't need to execute
     *                 code dependent on success/failure.
     */
    public static void updateRemoteTimestamp(DocumentReference docRef,
                                              String key,
                                              final Callback callback) {
        docRef.update(key, FieldValue.serverTimestamp())
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    callback.onSuccess();
                }})
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    callback.onFailure(e);
                }});

    }

}
