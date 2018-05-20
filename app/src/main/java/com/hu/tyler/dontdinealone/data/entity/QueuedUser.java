package com.hu.tyler.dontdinealone.data.entity;

import com.google.firebase.firestore.ServerTimestamp;
import com.hu.tyler.dontdinealone.res.DatabaseKeys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class QueuedUser {
    private List<Boolean> groupSizePreferences;
    private List<Boolean> diningHallPreferences;

    @ServerTimestamp
    private Date timestamp;

    public QueuedUser() {
        groupSizePreferences = new ArrayList<>(DatabaseKeys.Preference.GROUP_SIZES.length);
        diningHallPreferences = new ArrayList<>(DatabaseKeys.Preference.DINING_HALLS.length);
        timestamp = new Date(0);
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public List<Boolean> getGroupSizePreferences() {
        return groupSizePreferences;
    }
    public List<Boolean> getDiningHallPreferences() {
        return diningHallPreferences;
    }

    public void setGroupSizePreferences(List<Boolean> groupSizePreferences) {
        this.groupSizePreferences = groupSizePreferences;
    }
    public void setDiningHallPreferences(List<Boolean> groupSizePreferences) {
        this.diningHallPreferences = diningHallPreferences;
    }

    public void setGroupSizePreferencesFromArray(boolean[] booleans) {
        for (int i = 0; i < groupSizePreferences.size(); ++i) {
            groupSizePreferences.set(i, booleans[i]);
        }
    }

    public void setDiningHallPreferencesFromArray(boolean[] booleans) {
        for (int i = 0; i < groupSizePreferences.size(); ++i) {
            this.diningHallPreferences.set(i, booleans[i]);
        }
    }

}
