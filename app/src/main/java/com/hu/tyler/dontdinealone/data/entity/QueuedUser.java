package com.hu.tyler.dontdinealone.data.entity;

import com.google.firebase.firestore.FieldValue;
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
    private Date birthTimestamp;

    public QueuedUser() {
        groupSizePreferences = new ArrayList<>(DatabaseKeys.Preference.GROUP_SIZES.length);
        diningHallPreferences = new ArrayList<>(DatabaseKeys.Preference.DINING_HALLS.length);
        status = DatabaseStatuses.User.queued;
        birthTimestamp = new Date();
    }

    public String birthTimestampKey() {
        return "birthTimestamp";
    }
    public Date getBirthTimestamp() {
        return birthTimestamp;
    }
    // No setter becauase we only allow the server to timestamp

    public String groupSizePreferencesKey() { return "groupSizePreferences"; }
    public List<Boolean> getGroupSizePreferences() {
        return groupSizePreferences;
    }
    public void setGroupSizePreferences(List<Boolean> groupSizePreferences) {
        this.groupSizePreferences = groupSizePreferences;
    }
    public void setGroupSizePreferencesFromArray(boolean[] booleans) {
        for (int i = 0; i < groupSizePreferences.size(); ++i) {
            groupSizePreferences.set(i, booleans[i]);
        }
    }

    public String diningHallPreferencesKey() {
        return "diningHallPreferences";
    }
    public List<Boolean> getDiningHallPreferences() {
        return diningHallPreferences;
    }
    public void setDiningHallPreferences(List<Boolean> groupSizePreferences) {
        this.diningHallPreferences = diningHallPreferences;
    }
    public void setDiningHallPreferencesFromArray(boolean[] booleans) {
        for (int i = 0; i < groupSizePreferences.size(); ++i) {
            this.diningHallPreferences.set(i, booleans[i]);
        }
    }

    public String statusKey() { return "status"; }
    public String getStatus() {
        return status;
    }
    public void setStatus (String status) {
        this.status = status;
    }

}
