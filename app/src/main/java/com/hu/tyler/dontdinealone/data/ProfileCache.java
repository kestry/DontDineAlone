package com.hu.tyler.dontdinealone.data;

import com.hu.tyler.dontdinealone.data.Cache;
import com.hu.tyler.dontdinealone.res.DatabaseKeys;

public class ProfileCache extends Cache {
    @Override
    public void setToDefault() {
        clear();
        put(DatabaseKeys.Profile.DISPLAY_NAME, "");
        put(DatabaseKeys.Profile.GENDER, "");
        put(DatabaseKeys.Profile.ANIMAL, "");
    }
}
