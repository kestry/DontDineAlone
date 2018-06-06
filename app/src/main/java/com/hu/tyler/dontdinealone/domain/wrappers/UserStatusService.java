package com.hu.tyler.dontdinealone.domain.wrappers;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.hu.tyler.dontdinealone.data.Entity;
import com.hu.tyler.dontdinealone.data.model.Documents;
import com.hu.tyler.dontdinealone.res.DatabaseStatuses;
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
    public static void update(String status) {
        Entity.onlineUser.setStatus(status);
        Documents.getInstance().getOnlineUserDocRef()
                .update(Entity.onlineUser.statusKey(), Entity.onlineUser.getStatus());
    }

    /**
     * Updates the local and remote status with a callback.
     */
    public static void update(DocumentReference docRef,
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

    public static void updateToQueued() {
        // Set user to queued -- but we do not leave the Online collection of users.
        DocumentReference onlineUserDocRef = Documents.getInstance().getOnlineUserDocRef();

        UserStatusService.update(DatabaseStatuses.User.QUEUED);
        TimestampService.updateRemoteTimestamp(onlineUserDocRef,
                Entity.onlineUser.firstQueuedTimeKey());
    }

    public static void updateToOnline() {
        DocumentReference onlineUserDocRef = Documents.getInstance().getOnlineUserDocRef();
        UserStatusService.update(DatabaseStatuses.User.ONLINE);
    }

    public static void updateToMatch() {
        DocumentReference onlineUserDocRef = Documents.getInstance().getOnlineUserDocRef();
        UserStatusService.update(DatabaseStatuses.User.MATCHED);
    }
}
