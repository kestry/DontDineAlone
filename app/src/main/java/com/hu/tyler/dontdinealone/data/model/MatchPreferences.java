package com.hu.tyler.dontdinealone.data.model;

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

    // Public other methods ----------------------------------------------------------------------

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

    /**
     * Checks if the preferences have a match.
     * @param other
     * @return Returns true if there is at least one matching group size preference and
     * at least one matching dining hall preference.
     */
    public boolean hasMatch(MatchPreferences other) {
        boolean hasMatch = false;

        for (int i = 0; i < groupSizePreferences.length; ++i) {
            boolean bothPrefer = groupSizePreferences[i] && other.groupSizePreferences[i];
            hasMatch |= bothPrefer;
        }
        if (!hasMatch) {
            return false;
        }
        for (int i = 0; i < diningHallPreferences.length; ++i) {
            hasMatch |= diningHallPreferences[i] == other.diningHallPreferences[i];
        }
        return hasMatch;
    }

    /**
     * Changes preferences to only be true if the other also prefers the item
     * @param other
     */
    public void conformPreferences(MatchPreferences other) {
        for (int i = 0; i < groupSizePreferences.length; ++i) {
            groupSizePreferences[i] &= other.groupSizePreferences[i];
        }
        for (int i = 0; i < diningHallPreferences.length; ++i) {
            diningHallPreferences[i] &= other.diningHallPreferences[i];
        }
    }
}
