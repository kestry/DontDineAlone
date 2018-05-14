package com.hu.tyler.dontdinealone.data;

import com.hu.tyler.dontdinealone.res.DatabaseDocNames;
import com.hu.tyler.dontdinealone.res.DatabaseKeys;


import static java.lang.Boolean.FALSE;


public class MatchPreferenceRepo extends BaseRepo {

    // Singleton Holder that creates a single instance of this class.
    private static class MatchPreferenceRepoHolder {
        private final static MatchPreferenceRepo INSTANCE = new MatchPreferenceRepo();
    }

    // Reference to self so there is only one instance of this class
    public static MatchPreferenceRepo getInstance() {
        return MatchPreferenceRepoHolder.INSTANCE;
    }

    private MatchPreferenceRepo() {
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
    }
}
