package com.hu.tyler.dontdinealone.domain;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.hu.tyler.dontdinealone.data.entity.OnlineUser;
import com.hu.tyler.dontdinealone.data.model.Collections;
import com.hu.tyler.dontdinealone.data.Entity;
import com.hu.tyler.dontdinealone.data.entity.Group;
import com.hu.tyler.dontdinealone.data.model.Documents;
import com.hu.tyler.dontdinealone.data.model.GroupFactory;
import com.hu.tyler.dontdinealone.data.model.MatchPreferences;
import com.hu.tyler.dontdinealone.res.DatabaseKeys;
import com.hu.tyler.dontdinealone.res.DatabaseStatuses;
import com.hu.tyler.dontdinealone.util.Callback;


/**
 * This class is abstract because it provides static service implementations without instantiation.
 * Usage:
 *    Class.method();
 */
public abstract class MatchService {

    private static EventListener<DocumentSnapshot> onlineUserEventListener
            = getOnlineUserNewEventListener();

    private static boolean continuingQueue = true;



    public static void findGroup(final Callback callback) {

        continuingQueue = true;

        final Collections collections = Collections.getInstance();
        final Documents documents = Documents.getInstance();
        final OnlineUser ourUser = Entity.onlineUser;
        final MatchPreferences ourUser_Prefs = Entity.matchPreferences;

        // Collection References
        final CollectionReference groupsCRef = collections.getGroupsCRef();

        // Document References
        final DocumentReference ourOnlineUserDocRef = documents.getOnlineUserDocRef();

        /* In progress */
        // With transactions we must always do all gets first before doing any write.
        // IMPORTANT: WE DO NOT WANT TO MAKE ANY APPLICATION STATE CHANGES IN TRANSACTION
        // BECAUSE WE MIGHT BE TRYING THIS FUNCTION MULTIPLE TIMES.
        FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {

                // Get the groupFactory
                GroupFactory groupFactory = null;
                DocumentSnapshot groupFactoryDocSnap = transaction
                        .get(Documents.getInstance().getGroupFactoryDocRef());
                if (groupFactoryDocSnap.exists()) {
                    groupFactory = groupFactoryDocSnap.toObject(GroupFactory.class);
                } else {
                    groupFactory = new GroupFactory();
                }

                // Variables for our group
                Group ourGroup = null;
                MatchPreferences ourGroup_Prefs = null;

                // For each group with a status of "waiting"
                for (String groupDocId : groupFactory.getPendingGroups()) {
                    // Get the doc
                    DocumentSnapshot groupSnap = transaction
                            .get(groupsCRef.document(groupDocId));
                    Group group = groupSnap.toObject(Group.class);

                    DocumentSnapshot groupMatchPreferencesSnap = transaction
                            .get(documents.getGroupMatchPreferencesDocRef(group.getGid()));
                    MatchPreferences groupMatchPreferences =
                            groupMatchPreferencesSnap.toObject(MatchPreferences.class);

                    // Check if has match
                    if (groupMatchPreferences.hasMatch(ourUser_Prefs)) {
                        // Found matching group
                        ourGroup = group;
                        ourGroup_Prefs = groupMatchPreferences;


                        // Check if group is almost full
                        if (ourGroup.isAlmostFull()) {
                            // Change our group status to confirming.
                            ourGroup.setStatus(DatabaseStatuses.Group.confirming);

                            // Again, we do not want to change local user state in the transaction.
                            // However, we do want to notify the users that the group has been
                            // filled. Two possible ways are through subscription to Cloud
                            // messaging or through a listener to their own document changes. For
                            // now we choose the latter.

                            // Update other members to confirming status.
                            for (String memberId : ourGroup.getMembers().keySet()) {
                                transaction.update(
                                        collections.getOnlineUsersCRef().document(memberId),
                                        group.statusKey(), group.getStatus());
                            }

                            // Add ourself to the group
                            ourGroup.addMember(ourUser.getDocumentId());
                            ourGroup_Prefs.conformPreferences(ourUser_Prefs);

                        }
                        break; // Reason: found our group so we exit loop
                    } // ends "if group found"
                } // ends "for group search"

                if (ourGroup == null) {
                    // No match found, so add ourself to a new group
                    ourGroup = groupFactory.makeGroup(ourUser, ourUser_Prefs);
                }

                // Save the group in the remote db.
                transaction.set(groupsCRef.document(ourGroup.getGid()), ourGroup);

                // Save the group's match preferences in the remote db.
                transaction.set(documents
                        .getGroupMatchPreferencesDocRef(ourGroup.getGid()), ourGroup_Prefs);

                // Update the user's status and user's groupDocumentId with the group info.
                // TODO: Make this into a map and update in one go.
                transaction.update(ourOnlineUserDocRef,
                        ourUser.statusKey(), ourGroup.getStatus());
                transaction.update(ourOnlineUserDocRef,
                        ourUser.groupDocumentIdKey(), ourGroup.getGid());
                return null;
            }

        //TODO: Update local info on transaction completion.
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // Transaction success
                    /* On success, we may just be done and listening.
                    // Grab the updated user info
                    documents.getOnlineUserDocRef().get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot snap = task.getResult();
                                            if (snap.exists()) {
                                                // Update our local online user object
                                                Entity.onlineUser.copy(snap.toObject(OnlineUser.class));

                                                // Start listening for our status to be changed to
                                                // "confirming" which indicates the group has been
                                                // filled.

                                            } else {
                                                callback.onFailure(new Exception("Missing OnlineUser doc"));
                                            }
                                            callback.onSuccess();
                                        } else {
                                            callback.onFailure(task.getException());
                                        }
                                    }
                                });
                   */
                }
            })
                .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // We try again.
                    if (continuingQueue) {
                        findGroup(callback);
                    }
                }
            });
    }

    private static EventListener<DocumentSnapshot> getOnlineUserNewEventListener() {
        return new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot snap, FirebaseFirestoreException e) {

                if (e != null) {
                    // We encountered an exception.
                    return;
                }

                if (snap != null && snap.exists()) {
                    // TODO: Add notification here
                    Entity.onlineUser.copy(snap.toObject(OnlineUser.class));
                    if (Entity.onlineUser.getStatus() == DatabaseStatuses.User.confirmed) {
                        return;
                    }
                } else {
                    // TODO: Current data is null. Should we throw here? Return?
                }
            }};
    }
    public static ListenerRegistration beginMatchListener() {
        return Documents.getInstance().getOnlineUserDocRef()
                .addSnapshotListener(onlineUserEventListener);
    }

    public static void leaveGroup() {
        continuingQueue = false;
        UserStatusService.updateEverywhere(DatabaseStatuses.User.online);
    }
}
