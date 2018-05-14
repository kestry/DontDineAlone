package com.hu.tyler.dontdinealone.data;

import com.google.firebase.firestore.FieldValue;
import com.hu.tyler.dontdinealone.res.DatabaseDocNames;
import com.hu.tyler.dontdinealone.res.DatabaseKeys;


import static java.lang.Boolean.FALSE;

/*
Matching System Design (in progress)

UserID
-Profile
-MatchInfo (Redundant for bug testing (or perf?))
    - Prefs
    -
-GroupInfo

Match
-Info
-Group
    -GroupID
        - MatchInfo
            - Prefs
            - GroupCount
-UserID
    - MatchInfo
        - Prefs
        - GroupId

Group
- MatchInfo (Redundant for bug testing (or perf?))
- Members

*/

public class UserGroupRepo extends BaseRepo {

    // Singleton Holder that creates a single instance of this class.
    private static class UserGroupRepoHolder {
        private final static UserGroupRepo INSTANCE = new UserGroupRepo();
    }

    // Reference to self so there is only one instance of this class
    public static UserGroupRepo getInstance() {
        return UserGroupRepoHolder.INSTANCE;
    }

    private UserGroupRepo() {
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
