package com.hu.tyler.dontdinealone.domain;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FieldValue;
import com.hu.tyler.dontdinealone.data.Documents;
import com.hu.tyler.dontdinealone.data.Entity;
import com.hu.tyler.dontdinealone.res.DatabaseStatuses;
import com.hu.tyler.dontdinealone.util.Callback;

import java.util.HashMap;
import java.util.Map;

public abstract class OnlineService {

    public static void setUserToOnline(final Callback callback) {
        Entity.onlineUser.setName(Entity.user.getDisplayName());
        Entity.onlineUser.setDescription(Entity.user.getAnimal());
        Entity.onlineUser.setEmail(Entity.authUser.getEmail());
        Entity.onlineUser.setDocumentId(Entity.authUser.getUid());
        Entity.onlineUser.setStatus(DatabaseStatuses.User.online);

        Map<String, Object> onlineUserMap = Entity.onlineUser.toMap();
        onlineUserMap.put(Entity.onlineUser.birthTimestampKey(), FieldValue.serverTimestamp());
        onlineUserMap.put(Entity.onlineUser.lastCameOnlineKey(), FieldValue.serverTimestamp());
        Documents.getInstance().getOnlineUserDocRef().set(onlineUserMap)
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

    public static void goOnline() {
        Entity.onlineUser.setStatus(DatabaseStatuses.User.online);

        Map<String, Object> onlineUpdate = new HashMap<>();
        onlineUpdate.put(Entity.onlineUser.statusKey(), DatabaseStatuses.User.online);
        onlineUpdate.put(Entity.onlineUser.lastCameOnlineKey(), FieldValue.serverTimestamp());

        Documents.getInstance().getOnlineUserDocRef().update(onlineUpdate);

    }
}
