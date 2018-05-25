package com.hu.tyler.dontdinealone.domain;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
public class MatchService {

    public static void findGroup(final Callback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final Collections collections = Collections.getInstance();
        final Documents documents = Documents.getInstance();
        final CollectionReference groupsCRef = Collections.getInstance().getGroupsCRef();

        final DocumentReference ourOnlineUserDocRef = documents.getOnlineUserDocRef();
        final OnlineUser ourUser = Entity.onlineUser;
        /* In progress */
        // With transactions we must always do all gets first before doing any write.
        // IMPORTANT: WE DO NOT WANT TO MAKE ANY APPLICATION STATE CHANGES
        // BECAUSE WE MIGHT BE TRYING THIS FUNCTION MULTIPLE TIMES.
        // We do not want to be changing the OnlineUser remote doc during the transaction.
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
                    //Log.d(TAG, "Transaction success!"); // What is TAG?

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //Log.w(TAG, "Transaction failure.", e);
                }
            });

/*
    private static void getWaitingGroups(final Callback callback) {
        Collections.getInstance().getGroupsCRef()
                .whereEqualTo(Entity.group.statusKey(), DatabaseStatuses.Group.waiting)
                .orderBy(Entity.group.gidKey())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public QuerySnapshot onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (DocumentSnapshot snap : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + snap.getData());
                                Group group = snap.toObject(Group.class);

                            }
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                            callback.onFailure(task.getException());
                        }
                        return task.getResult();
                    }
                });
       */
    }
}
