package com.hu.tyler.dontdinealone.domain;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FieldValue;
import com.hu.tyler.dontdinealone.data.Documents;
import com.hu.tyler.dontdinealone.data.Entity;
import com.hu.tyler.dontdinealone.data.entity.OnlineUser;
import com.hu.tyler.dontdinealone.util.Callback;

import java.util.HashMap;
import java.util.Map;


public abstract class Queue {
    public static void queueUser(final Callback callback) {
        Documents.getInstance().getQueuedUserDocRef().set(Entity.queuedUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        updateBirthTimestamp(callback);
                    }})
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onFailure(e);
                    }});
    }

    public static void dequeUser() {
        Documents.getInstance().getQueuedUserDocRef().delete();
    }

    private static void updateBirthTimestamp(final Callback callback) {
        Documents.getInstance().getOnlineUserDocRef()
                .update(Entity.queuedUser.birthTimestampKey(), FieldValue.serverTimestamp())
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

