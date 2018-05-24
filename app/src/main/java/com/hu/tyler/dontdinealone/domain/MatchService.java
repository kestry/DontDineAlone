package com.hu.tyler.dontdinealone.domain;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.hu.tyler.dontdinealone.data.Collections;
import com.hu.tyler.dontdinealone.data.Entity;
import com.hu.tyler.dontdinealone.data.entity.Group;
import com.hu.tyler.dontdinealone.res.DatabaseStatuses;
import com.hu.tyler.dontdinealone.util.Callback;

import java.util.List;

/**
 * This class is abstract because it provides static service implementations without instantiation.
 * Usage:
 *    Class.method();
 */
public class MatchService {

    public static void findGroup(final Callback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference groupsCRef = Collections.getInstance().getGroupsCRef();
        //     - Rule, cannot change id cept with new group creation.
        // 2. Then get the
        Collections.getInstance().getGroupsCRef()
                .whereEqualTo(Entity.group.statusKey(), DatabaseStatuses.Group.waiting)
                .orderBy(Entity.group.gidKey())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot snap : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + snap.getData());
                                Group group = snap.toObject(Group.class);

                            }
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                            callback.onFailure(task.getException());
                        }
                    }
                });
/*
        // With transactions we must always do all gets first before doing any write.
        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {

                List<DocumentSnapshot> snaps = queryDocumentSnapshots.getDocuments();
                QuerySnapshot snapshot = transaction.get(groupsCRef);
                double newPopulation = snapshot.getDouble("population") + 1;
                transaction.update(sfDocRef, "population", newPopulation);

                // Success
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Transaction success!");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Transaction failure.", e);
                    }
                });

*/

    }

}
