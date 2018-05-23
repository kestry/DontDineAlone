package com.hu.tyler.dontdinealone.domain;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Transaction;
import com.hu.tyler.dontdinealone.util.Callback;

/**
 * This class is abstract because it provides static service implementations without instantiation.
 * Usage:
 *    Class.method();
 */
public class MatchService {

    public static void findGroup(final Callback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        /*
        final CollectionReference sfDocRef = db.collection("cities").document("SF");

        // With transactions we must always do all gets first before doing any write.
        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(sfDocRef);
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
