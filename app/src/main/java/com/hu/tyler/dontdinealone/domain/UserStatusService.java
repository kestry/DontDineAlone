package com.hu.tyler.dontdinealone.domain;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.hu.tyler.dontdinealone.data.Entity;
import com.hu.tyler.dontdinealone.data.model.Documents;
import com.hu.tyler.dontdinealone.util.Callback;

/**
 * This class is abstract because it provides static service implementations without instantiation.
 * Usage:
 *    Class.method();
 */
public abstract class UserStatusService {
    /**
     * Updates the local and remote status asynchronously.
     */
    public static void updateEverywhere(String status) {
        Entity.onlineUser.setStatus(status);
        Documents.getInstance().getOnlineUserDocRef()
                .update(Entity.onlineUser.statusKey(), Entity.onlineUser.getStatus());
    }

    /**
     * Updates the local and remote status with a callback.
     */
    public static void updateEverywhere(DocumentReference docRef,
                                     final String status,
                                     final Callback callback) {
        docRef.update(Entity.onlineUser.statusKey(), status)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Entity.onlineUser.setStatus(status);
                        callback.onSuccess();
                    }})
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFailure(e);
                    }});

    }

    /* TODO: Deletion of both uncommented functions below are pending.
     * Should be done once MatchService is ok.
    public static void updateGroupStatus(DocumentReference docRef,
                                        String status) {
        docRef.update(Entity.onlineUser.statusKey(), status);
    }
    */
    /**
     * Updates the local and remote status with a callback.

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
    */
}
