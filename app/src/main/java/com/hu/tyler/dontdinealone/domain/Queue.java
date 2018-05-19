package com.hu.tyler.dontdinealone.domain;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.hu.tyler.dontdinealone.OnlineUser;

import java.util.HashMap;
import java.util.Map;


public abstract class Queue {
    public static void queue(OnlineUser user, CollectionReference collectionRef) {
        Map<String, Object> data = new HashMap<>();
        data.put("timestamp", FieldValue.serverTimestamp());
        collectionRef.document(user.getDocumentId()).set(data);
    }
    public static void deque(OnlineUser user, CollectionReference collectionRef) {
        collectionRef.document(user.getDocumentId()).delete();
    }
}

