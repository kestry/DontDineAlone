package com.hu.tyler.dontdinealone.domain;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.hu.tyler.dontdinealone.data.Entity;
import com.hu.tyler.dontdinealone.util.Callback;

/**
 * This class is abstract because it provides static service implementations without instantiation.
 * Usage:
 *    Class.method();
 */
public abstract class StatusService {
    /**
     * Updates the local and remote status asynchronously.
     * @param docRef
     * @param status
     */
    public static void updateStatus(DocumentReference docRef,
                                    String status) {
        docRef.update(Entity.onlineUser.statusKey(), status);
    }

    /**
     * Updates the local and remote status with a callback.
     * @param docRef
     * @param status
     * @param callback - call with NullCallback.getInstance() if you don't need to execute
     *                 code dependent on success/failure.
     */
    public static void updateStatus(DocumentReference docRef,
                                     String status,
                                     final Callback callback) {
        docRef.update(Entity.onlineUser.statusKey(), status)
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
