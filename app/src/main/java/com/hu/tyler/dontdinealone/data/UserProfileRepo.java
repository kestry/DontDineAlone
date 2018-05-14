package com.hu.tyler.dontdinealone.data;

import com.hu.tyler.dontdinealone.res.DatabaseDocNames;
import com.hu.tyler.dontdinealone.res.DatabaseKeys;

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