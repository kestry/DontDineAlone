package com.hu.tyler.dontdinealone.data;

import com.hu.tyler.dontdinealone.data.entity.AuthUser;
import com.hu.tyler.dontdinealone.data.model.MatchPreferences;
import com.hu.tyler.dontdinealone.data.entity.OnlineUser;
import com.hu.tyler.dontdinealone.data.entity.User;
import com.hu.tyler.dontdinealone.net.Connection;

/*
 * This is a container that holds and saves our specific user information.
 *
 * Usage:
 *     Entity.onlineUser.method();
 */
public interface Entity {
    User user = new User(); // Persistant User Data
    AuthUser authUser = new AuthUser(); // Authentication User Data
    OnlineUser onlineUser = new OnlineUser(); // Online User Data
    MatchPreferences matchPreferences = new MatchPreferences();
}
