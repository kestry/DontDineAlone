package com.hu.tyler.dontdinealone.domain;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.hu.tyler.dontdinealone.data.Documents;
import com.hu.tyler.dontdinealone.data.Entity;
import com.hu.tyler.dontdinealone.data.entity.OnlineUser;
import com.hu.tyler.dontdinealone.res.DatabaseKeys;
import com.hu.tyler.dontdinealone.res.DatabaseStatuses;
import com.hu.tyler.dontdinealone.util.Callback;
import com.hu.tyler.dontdinealone.util.NullCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is abstract because it provides static service implementations without instantiation.
 * Usage:
 *    Class.method();
 */
public abstract class QueueService {
    /**
     * Enters the User entity into a queued state. Then calls the match service to find a group.
     */
    public static void enterQueue(final Callback callback) {
        // Set user to queued -- but we do not leave the Online collection of users.
        StatusService.updateStatus(Documents.getInstance().getOnlineUserDocRef(),
                 DatabaseStatuses.User.queued);
        MatchService.findGroup(callback);
    }

    public static void leaveQueue() {
        StatusService.updateStatus(Documents.getInstance().getOnlineUserDocRef(),
                DatabaseStatuses.User.online);    }


}

