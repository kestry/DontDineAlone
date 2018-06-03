package com.hu.tyler.dontdinealone.domain;

import com.google.firebase.firestore.DocumentReference;
import com.hu.tyler.dontdinealone.data.model.Documents;
import com.hu.tyler.dontdinealone.data.Entity;
import com.hu.tyler.dontdinealone.res.DatabaseStatuses;
import com.hu.tyler.dontdinealone.util.Callback;

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
        DocumentReference onlineUserDocRef = Documents.getInstance().getOnlineUserDocRef();

        UserStatusService.updateEverywhere(DatabaseStatuses.User.QUEUED);
        TimestampService.updateRemoteTimestamp(onlineUserDocRef,
                Entity.onlineUser.firstQueuedTimeKey());
    }

    public static void leaveQueue() {
        DocumentReference onlineUserDocRef = Documents.getInstance().getOnlineUserDocRef();
        UserStatusService.updateEverywhere(DatabaseStatuses.User.ONLINE);
    }
    
}

