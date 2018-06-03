package com.hu.tyler.dontdinealone.domain;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.hu.tyler.dontdinealone.data.model.Collections;
import com.hu.tyler.dontdinealone.data.model.Documents;
import com.hu.tyler.dontdinealone.data.Entity;
import com.hu.tyler.dontdinealone.res.DatabaseStatuses;
import com.hu.tyler.dontdinealone.util.Callback;

import java.util.Map;

/**
 * This class is abstract because it provides static service implementations without instantiation.
 * Usage:
 *    Class.method();
 */
public abstract class OnlineService {

    /**
     *  This function is to set the fields in OnlineUser to the values
     * from the User (which contains the profile) and from AuthUser.
     * Currently we call this function when we sign-in or check
     * the sign-in status of the user.
     */
    public static Task goOnline(final Callback callback) {
        // Fill in the onlineUser fields.
        Entity.onlineUser.setupOnlineUser(Entity.authUser, Entity.user);

        // The reason we use a map is because the serverTimestamp must be mapped to our field.
        // And so instead of doing two Firestore actions, we do the remote set in one go.
        Map<String, Object> onlineUserMap = Entity.onlineUser.toMapWithoutTimestamp();
        // We only set firstOnlineTime when we create our doc.
        onlineUserMap.put(Entity.onlineUser.firstOnlineTimeKey(), FieldValue.serverTimestamp());
        return Documents.getInstance().getOnlineUserDocRef().set(onlineUserMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
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

    public static void goOffline() {
        if (Entity.authUser.isSignedIn()) {
            DocumentReference onlineUserDocRef = Documents.getInstance().getOnlineUserDocRef();
            onlineUserDocRef.delete();
        }
    }
}
