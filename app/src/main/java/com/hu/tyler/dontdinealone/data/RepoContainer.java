package com.hu.tyler.dontdinealone.data;

import com.hu.tyler.dontdinealone.data.CacheContainer;

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

public interface RepoContainer {
    Repo profileRepo = new Repo(CacheContainer.myProfileCache);
    Repo preferenceRepo = new Repo(CacheContainer.myPreferenceCache);
}
