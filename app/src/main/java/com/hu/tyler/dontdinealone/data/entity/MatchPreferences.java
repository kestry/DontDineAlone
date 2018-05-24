package com.hu.tyler.dontdinealone.data.entity;

import com.hu.tyler.dontdinealone.res.DatabaseKeys;

// Preferences are stored here locally. We do not store them on the Firestore for now,
// because we will be doing the matching on the client through transactions.
// However we may want some kind of persistent preference. Options we have for where we store
// preferences: OnlineUser, User, QueuedUser, or a subcollection.

// This class is also used by Group in order to match preferences.

public class MatchPreferences {

    private boolean[] groupSizePreferences;
    private boolean[] diningHallPreferences;

    public MatchPreferences() {
        groupSizePreferences = new boolean[DatabaseKeys.Preference.GROUP_SIZES.length];
        diningHallPreferences = new boolean[DatabaseKeys.Preference.DINING_HALLS.length];
    }

    public MatchPreferences(MatchPreferences matchPrefs) {
        System.arraycopy(matchPrefs.groupSizePreferences, 0, this.groupSizePreferences, 0,
                matchPrefs.groupSizePreferences.length);
        System.arraycopy(matchPrefs.diningHallPreferences, 0, this.diningHallPreferences, 0,
                matchPrefs.groupSizePreferences.length);
    }

    public void setToDefault() {
        for (boolean pref : groupSizePreferences) {
            pref = true;
        }
        for (boolean pref : diningHallPreferences) {
            pref = true;
        }
    }

    public boolean hasChosenAGroupSizePreference() {
        boolean hasChosen = false;
        for (boolean pref : groupSizePreferences) {
            hasChosen |= pref;
        }
        return hasChosen;
    }

    public boolean hasChosenADiningHallPreference() {
        boolean hasChosen = false;
        for (boolean pref : diningHallPreferences) {
            hasChosen |= pref;
        }
        return hasChosen;
    }

    public String groupSizePreferencesKey() {return "groupSizePreferences"; }
    public boolean[] getGroupSizePreferences() { return groupSizePreferences; }
    public boolean getGroupSizePreference(int index) { return groupSizePreferences[index]; }
    public void setGroupSizePreferences(boolean[] groupSizePreferences) {
        this.groupSizePreferences = groupSizePreferences;
    }
    public void setGroupSizePreferenceAt(int index, boolean preference) {
        this.groupSizePreferences[index] = preference;
    }

    public String diningHallPreferencesKey() {return "diningHallPreferences"; }
    public boolean[] getDiningHallPreferences() { return diningHallPreferences; }
    public boolean getDiningHallPreference(int index) { return groupSizePreferences[index]; }
    public void setDiningHallPreferences(boolean[] diningHallPreference) {
        this.diningHallPreferences = diningHallPreference;
    }
    public void setDiningHallPreferenceAt(int index, boolean preference) {
        this.diningHallPreferences[index] = preference;
    }
}
