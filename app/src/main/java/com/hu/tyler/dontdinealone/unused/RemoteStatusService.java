package com.hu.tyler.dontdinealone.domain;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.hu.tyler.dontdinealone.data.Entity;
import com.hu.tyler.dontdinealone.data.entity.Group;
import com.hu.tyler.dontdinealone.data.model.Collections;
import com.hu.tyler.dontdinealone.util.Callback;

/**
 * This class is abstract because it provides static service implementations without instantiation.
 * Usage:
 *    Class.method();
 */

// This class is still under consideration. 
public abstract class RemoteStatusService {
    /**
     * Updates the remote status asynchronously.
     */
    public static void updateRemoteUserStatus(DocumentReference docRef,
                                        String status) {
        docRef.update(Entity.onlineUser.statusKey(), status);
    }

    /**
     * Updates the local and remote status with a callback.
     */
    public static void updateRemoteUserStatus(DocumentReference docRef,
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

    public static void updateGroupStatus(Group group, String status) {
    }

    /**
     * Updates the local and remote status with a callback.
     */
    public static void updateGroupStatus(DocumentReference docRef,
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
