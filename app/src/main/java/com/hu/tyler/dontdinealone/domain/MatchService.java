package com.hu.tyler.dontdinealone.domain;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.hu.tyler.dontdinealone.data.entity.OnlineUser;
import com.hu.tyler.dontdinealone.data.model.Collections;
import com.hu.tyler.dontdinealone.data.Entity;
import com.hu.tyler.dontdinealone.data.entity.Group;
import com.hu.tyler.dontdinealone.data.model.Documents;
import com.hu.tyler.dontdinealone.data.model.GroupFactory;
import com.hu.tyler.dontdinealone.data.model.MatchPreferences;
import com.hu.tyler.dontdinealone.res.DatabaseStatuses;
import com.hu.tyler.dontdinealone.util.Callback;


/**
 * This class is abstract because it provides static service implementations without instantiation.
 * Usage:
 *    Class.method();
 */
public abstract class MatchService {

    public static void findGroup(final Callback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final Collections collections = Collections.getInstance();
        final Documents documents = Documents.getInstance();
        final CollectionReference groupsCRef = Collections.getInstance().getGroupsCRef();

        final DocumentReference ourOnlineUserDocRef = documents.getOnlineUserDocRef();
        final OnlineUser ourUser = Entity.onlineUser;
        /* In progress */
        // With transactions we must always do all gets first before doing any write.
        // IMPORTANT: WE DO NOT WANT TO MAKE ANY APPLICATION STATE CHANGES IN TRANSACTION
        // BECAUSE WE MIGHT BE TRYING THIS FUNCTION MULTIPLE TIMES.
        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {

                // Get the groupFactory
                DocumentSnapshot groupFactoryDocSnap = transaction
                        .get(Documents.getInstance().getGroupFactoryDocRef());
                GroupFactory groupFactory = groupFactoryDocSnap.toObject(GroupFactory.class);

                MatchPreferences ourPrefs = Entity.matchPreferences;
                CollectionReference groupsCRef = Collections.getInstance().getGroupsCRef();
                // Loop variables
                Group ourGroup = null;
                // For each waiting group
                for (String groupDocId : groupFactory.getPendingGroups()) {
                    // Get the doc
                    DocumentSnapshot groupSnap = transaction
                            .get(groupsCRef.document(groupDocId));
                    Group group = groupSnap.toObject(Group.class);

                    // Check if has match
                    if (group.getMatchPreferences().hasMatch(ourPrefs)) {
                        // Found matching group
                        ourGroup = group;

                        // Check if group is almost full
                        if (ourGroup.isAlmostFull()) {
                            // Our user wants to listen for this state while we in waiting mode.
                            ourGroup.setStatus(DatabaseStatuses.Group.confirming);

                            // Again, we do not want to change local user state in the transaction.
                            // However, we do want to notify the users that the group has been
                            // filled. Two possible ways are through subscription to Cloud
                            // messaging or through a listener to their own document changes. For
                            // now we choose the latter.

                            // Update other members to confirming status.
                            for (String memberId : ourGroup.getMembers().keySet()) {
                                transaction
                                        .update(collections.getOnlineUsersCRef().document(memberId),
                                                group.statusKey(), group.getStatus());
                            }

                            // Add ourself to the group
                            ourGroup.addMember(ourUser.getDocumentId());
                            ourGroup.getMatchPreferences().conformPreferences(ourPrefs);

                        } // ends "if almost full"

                        break; // Reason: found our group so we exit loop
                    } // ends "if group found"
                } // ends "for group search"

                if (ourGroup == null) {
                    // No match found, so add ourself to a new group
                    ourGroup = groupFactory.makeGroup(ourUser, ourPrefs);
                }
                // Convert the group's gid to a documentId.
                String ourGroupDocumentId = Integer.toString(ourGroup.getGid());

                // Set the group info in the remote DB
                transaction.set(groupsCRef.document(ourGroupDocumentId), ourGroup);

                // Update the user's status and user's groupDocumentId with the group info.
                // TODO: Make this into a map and update in one go.
                transaction.update(ourOnlineUserDocRef,
                        ourUser.statusKey(), ourGroup.getStatus());
                transaction.update(ourOnlineUserDocRef,
                        ourUser.groupDocumentIdKey(), ourGroupDocumentId);
                return null;
            }

        //TODO: Update local info on transaction completion.
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // Transaction success

                    // Grab the updated user info
                    documents.getOnlineUserDocRef().get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot snap = task.getResult();
                                            if (snap.exists()) {
                                                Entity.onlineUser.set(snap.toObject(OnlineUser.class));
                                            } else {
                                                Entity.user.setToDefault();
                                            }
                                            callback.onSuccess();
                                        } else {
                                            callback.onFailure(task.getException());
                                        }
                                    }
                                });
                }
            })
                .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //Log.w(TAG, "Transaction failure.", e);
                }
            });

    }
}
