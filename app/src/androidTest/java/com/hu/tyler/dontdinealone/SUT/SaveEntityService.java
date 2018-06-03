package com.hu.tyler.dontdinealone.SUT;

import com.hu.tyler.dontdinealone.data.Entity;
import com.hu.tyler.dontdinealone.data.entity.AuthUser;
import com.hu.tyler.dontdinealone.data.entity.OnlineUser;
import com.hu.tyler.dontdinealone.data.entity.User;
import com.hu.tyler.dontdinealone.data.model.MatchPreferences;

import org.junit.Ignore;

public abstract class SaveEntityService implements Entity {
    private static User savedUser = new User(user);
    private static AuthUser savedAuthUser = new AuthUser(authUser);
    private static OnlineUser savedOnlineUser = new OnlineUser(onlineUser);
    private static MatchPreferences savedMatchPreferences = new MatchPreferences(matchPreferences);

    public static void saveEntity() {
        savedUser.copy(user);
        savedAuthUser.copy(authUser);
        savedOnlineUser.copy(onlineUser);
        savedMatchPreferences.copy(matchPreferences);
    }

    public static void restoreEntity() {
        user.copy(savedUser);
        authUser.copy(savedAuthUser);
        onlineUser.copy(savedOnlineUser);
        matchPreferences.copy(savedMatchPreferences);
    }
}
