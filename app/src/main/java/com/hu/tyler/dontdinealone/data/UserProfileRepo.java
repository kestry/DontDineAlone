package com.hu.tyler.dontdinealone.data;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hu.tyler.dontdinealone.res.DatabaseDocNames;
import com.hu.tyler.dontdinealone.res.DatabaseKeys;
import com.hu.tyler.dontdinealone.util.Callback;

import java.util.HashMap;
import java.util.Map;

public class UserProfileRepo extends BaseRepo {


    // Singleton Holder that creates a single instance of this class.
    private static class UserProfileRepoHolder {
        private final static UserProfileRepo INSTANCE = new UserProfileRepo();
    }

    // Reference to self so there is only one instance of this class
    public static UserProfileRepo getInstance() {
        return UserProfileRepoHolder.INSTANCE;
    }

    private UserProfileRepo() {
        super(DatabaseDocNames.PROFILE);

        setToDefault();
    }

    // Implementation Section -----------------------------------------------

    @Override
    public void setToDefault() {
        mIsDirty = true;
        mCache.clear();
        mCache.put(DatabaseKeys.Profile.DISPLAY_NAME, "");
        mCache.put(DatabaseKeys.Profile.GENDER, "");
        mCache.put(DatabaseKeys.Profile.ANIMAL, "");
    }
}