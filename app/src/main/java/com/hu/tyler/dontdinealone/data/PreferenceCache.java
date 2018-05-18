package com.hu.tyler.dontdinealone.data;

import com.google.firebase.firestore.FieldValue;
import com.hu.tyler.dontdinealone.data.Cache;
import com.hu.tyler.dontdinealone.res.DatabaseKeys;

import static java.lang.Boolean.FALSE;

public class PreferenceCache extends Cache {

    @Override
    public void setToDefault() {
        clear();
        for (int i = 0; i < DatabaseKeys.Preference.GROUP_SIZES.length; i++) {
            put(DatabaseKeys.Preference.GROUP_SIZES[i], FALSE);
        }
        for (int i = 0; i < DatabaseKeys.Preference.DINING_HALLS.length; i++) {
            put(DatabaseKeys.Preference.DINING_HALLS[i], FALSE);
        }
        put("timestamp", FieldValue.serverTimestamp());
    }
}