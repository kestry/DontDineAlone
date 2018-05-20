package com.hu.tyler.dontdinealone.data;

import com.hu.tyler.dontdinealone.data.entity.AuthUser;
import com.hu.tyler.dontdinealone.data.entity.OnlineUser;
import com.hu.tyler.dontdinealone.data.entity.QueuedUser;
import com.hu.tyler.dontdinealone.data.entity.User;

public interface Entity {
    AuthUser authUser = new AuthUser();
    OnlineUser onlineUser = new OnlineUser();
    QueuedUser queuedUser = new QueuedUser();
    User user = new User();
}
