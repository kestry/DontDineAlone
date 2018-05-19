package com.hu.tyler.dontdinealone.data.unused;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.ServerTimestamp;
import com.hu.tyler.dontdinealone.res.DatabaseKeys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Preference extends DataObject {
    private List<Boolean> groupSizePreferences;
    private List<Boolean> diningHallPreferences;

    @ServerTimestamp
    private FieldValue timestamp;

    Preference() {
        groupSizePreferences = new ArrayList<>(DatabaseKeys.Preference.GROUP_SIZES.length);
        diningHallPreferences = new ArrayList<>(DatabaseKeys.Preference.DINING_HALLS.length);
        timestamp = FieldValue.serverTimestamp();

    }

    @Override
    public void setToDefault() {
        super.setToDefault();
        for (Boolean b : groupSizePreferences) {
            b = false;
        }
        for (Boolean b : diningHallPreferences) {
            b = false;
        }
        timestamp = FieldValue.serverTimestamp();
    }

    public FieldValue getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(FieldValue timestamp) {
        this.timestamp = timestamp;
    }

    public List<Boolean> getGroupSizePreferences() {
        return groupSizePreferences;
    }
    public List<Boolean> getDiningHallPreferences() {
        return diningHallPreferences;
    }

    public void setGroupSizePreferences(Boolean[] booleans) {
        groupSizePreferences = Arrays.asList(booleans);
    }
    public void setDiningHallPreferences(Boolean[] booleans) {
        diningHallPreferences = Arrays.asList(booleans);
    }
}
