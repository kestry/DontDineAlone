package com.hu.tyler.dontdinealone.data;

import com.google.firebase.firestore.FieldValue;
import com.hu.tyler.dontdinealone.res.DatabaseDocNames;
import com.hu.tyler.dontdinealone.res.DatabaseKeys;


import static java.lang.Boolean.FALSE;


public class UserMatchInfoRepo extends BaseRepo {

    // Singleton Holder that creates a single instance of this class.
    private static class UserMatchRepoHolder {
        private final static UserMatchInfoRepo INSTANCE = new UserMatchInfoRepo();
    }

    // Reference to self so there is only one instance of this class
    public static UserMatchInfoRepo getInstance() {
        return UserMatchRepoHolder.INSTANCE;
    }

    private UserMatchInfoRepo() {
        super(DatabaseDocNames.PREFERENCE);

        setToDefault();
    }

    // Implementation Section -----------------------------------------------

    @Override
    public void setToDefault() {
        mIsDirty = true;
        mCache.clear();
        for (int i = 0; i < DatabaseKeys.Preference.GROUP_SIZES.length; i++) {
            mCache.put(DatabaseKeys.Preference.GROUP_SIZES[i], FALSE);
        }
        for (int i = 0; i < DatabaseKeys.Preference.DINING_HALLS.length; i++) {
            mCache.put(DatabaseKeys.Preference.DINING_HALLS[i], FALSE);
        }
        mCache.put("timestamp", FieldValue.serverTimestamp());
    }
}
