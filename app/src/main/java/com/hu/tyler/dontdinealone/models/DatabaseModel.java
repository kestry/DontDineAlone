package com.hu.tyler.dontdinealone.models;

import android.support.annotation.NonNull;

import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DatabaseModel {

    // Cloud Firestore databaseRef and docRefs
    private FirebaseFirestore mDb;
    private static Exception mException;

    // User Fields
    private static String mEmail;
    private static String mUid;

    // Our Maps
    private Map<String, Object> mProfileMap;
    private Map<String, Object> mPreferenceMap;

    // These dirty flags allows caller to decide when to remotely load and store data.
    private static boolean mHasDirtyProfile;
    private static boolean mHasDirtyPreference;

    // This is an optimal way to make singletons both lazy and thread-safe.
    // However singletons are considered bad, and so it would be good to look into other solutions:
    // - SharedPreferences, ViewModel, Room, MVP
    // source: https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom
    private static class DatabaseModelHolder {
        private final static DatabaseModel INSTANCE = new DatabaseModel();
    }

    // Reference to self so there is only one instance of this class
    public static DatabaseModel getInstance() {
        return DatabaseModelHolder.INSTANCE;

    }

    private DatabaseModel() {
        // Initialize Cloud Firestore databaseRef and docRefs
        mDb = FirebaseFirestore.getInstance();
        mException = null;

        mEmail = "";
        mUid = "";

        mHasDirtyProfile = true;
        mHasDirtyPreference = true;

        // Initialize Maps
        mProfileMap = new HashMap<String, Object>();
        mPreferenceMap = new HashMap<String, Object>();

        setLocalProfileToDefault();
    }

    public void setLocalProfileToDefault() {
        mHasDirtyProfile = true;
        mProfileMap.clear();
        mProfileMap.put(DatabaseKeys.DISPLAY_NAME, "");
        mProfileMap.put(DatabaseKeys.GENDER, "");
        mProfileMap.put(DatabaseKeys.ANIMAL, "");
    }

    public Exception getException() {
        return mException;
    }

    // Profile Section -----------------------------------------------

    public boolean hasDirtyProfile() {
        return mHasDirtyProfile;
    }

    public String getEmail() {
        return mEmail;
    }

    public String getUid() {
        return mUid;
    }

    public String getDisplayName() {
        return (String) mProfileMap.get(DatabaseKeys.DISPLAY_NAME);
    }

    public String getGender() {
        return (String) mProfileMap.get(DatabaseKeys.GENDER);
    }

    public String getAnimal() {
        return (String) mProfileMap.get(DatabaseKeys.ANIMAL);
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public void setUid(String uid) {
        mUid = uid;
    }

    public void setDisplayName(String displayName) {
        mHasDirtyProfile = true;
        mProfileMap.put(DatabaseKeys.DISPLAY_NAME, displayName);
    }

    public void setGender(String gender) {
        mHasDirtyProfile = true;
        mProfileMap.put(DatabaseKeys.GENDER, gender);
    }

    public void setAnimal(String animal) {
        mHasDirtyProfile = true;
        mProfileMap.put(DatabaseKeys.ANIMAL, animal);
    }

    public void storeProfile(final Runnable successRunnable, final Runnable failureRunnable) {
        mDb.collection(mUid).document(DatabaseDocNames.PROFILE).set(mProfileMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // This flag should set prior to the successRunnable function.
                        mHasDirtyProfile = false;
                        if (successRunnable != null) {
                            successRunnable.run();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mException = e;
                        if (failureRunnable != null) {
                            failureRunnable.run();
                        }
                    }
                });
    }

    public void loadProfile(final Runnable successRunnable, final Runnable failureRunnable) {
        mDb.collection(mUid).document(DatabaseDocNames.PROFILE).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                mProfileMap = document.getData();
                            } else {
                                // WARNING: Profile defaults unstored for performance. Store if they become needed.
                                setLocalProfileToDefault();
                            }
                            // This clean status should be set between setting the local profile to default
                            // and running the successRunnable function.
                            mHasDirtyProfile = false;
                            if (successRunnable != null) {
                               successRunnable.run();
                            }
                        } else {
                            mException = task.getException();
                            if (failureRunnable != null) {
                                failureRunnable.run();
                            }
                        }
                    }
                });
    }

    // Preference Section -----------------------------------------------

    public boolean hasDirtyPreference() {
        return mHasDirtyPreference;
    }

}

