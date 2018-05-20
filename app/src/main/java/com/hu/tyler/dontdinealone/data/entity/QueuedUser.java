package com.hu.tyler.dontdinealone.data.entity;

import com.google.firebase.firestore.ServerTimestamp;
import com.hu.tyler.dontdinealone.res.DatabaseKeys;
import com.hu.tyler.dontdinealone.res.DatabaseStatuses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class QueuedUser {
    private List<Boolean> groupSizePreferences;
    private List<Boolean> diningHallPreferences;

    private String status = DatabaseStatuses.User.queued;

    @ServerTimestamp
    private Date lastCreated;

    public QueuedUser() {
        groupSizePreferences = new ArrayList<>(DatabaseKeys.Preference.GROUP_SIZES.length);
        diningHallPreferences = new ArrayList<>(DatabaseKeys.Preference.DINING_HALLS.length);
        lastCreated = new Date();
    }

    public String getLastCreatedKey() {
        return "lastCreated";
    }
    public String getGroupSizePreferencesKey() {
        return "groupSizePreferences";
    }
    public String getDiningHallPreferencesKey() {
        return "diningHallPreferences";
    }
    public String getStatusKey() {
        return "status";
    }

    public Date getLastCreated() {
        return lastCreated;
    }

    public void setLastCreated(Date timestamp) {
        this.lastCreated = lastCreated;
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
