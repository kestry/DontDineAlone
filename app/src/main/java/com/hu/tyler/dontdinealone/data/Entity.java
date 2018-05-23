package com.hu.tyler.dontdinealone.data;

import com.hu.tyler.dontdinealone.data.entity.AuthUser;
import com.hu.tyler.dontdinealone.data.entity.MatchPreferences;
import com.hu.tyler.dontdinealone.data.entity.OnlineUser;
import com.hu.tyler.dontdinealone.data.entity.User;

public interface Entity {
    AuthUser authUser = new AuthUser();
    OnlineUser onlineUser = new OnlineUser();
    User user = new User();
    MatchPreferences matchPreferences = new MatchPreferences();
}
