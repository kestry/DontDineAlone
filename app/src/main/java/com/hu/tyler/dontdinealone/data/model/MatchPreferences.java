package com.hu.tyler.dontdinealone.data.model;

import com.hu.tyler.dontdinealone.res.DatabaseKeys;

import java.util.ArrayList;
import java.util.List;

// Preferences are stored here locally. We do not store them on the Firestore for now,
// because we will be doing the matching on the client through transactions.
// However we may want some kind of persistent preference. Options we have for where we store
// preferences: OnlineUser, User, QueuedUser, or a subcollection.

// This class is also used by Group in order to match preferences.

public class MatchPreferences {

    private List<Boolean> groupSizePreferences;
    private List<Boolean> diningHallPreferences;

    public MatchPreferences() {
        groupSizePreferences = new ArrayList<>();
        diningHallPreferences = new ArrayList<>();
        setToDefault();
    }

    public MatchPreferences(MatchPreferences other) {
        copy(other);
    }

    public void setToDefault() {
        groupSizePreferences.clear();
        diningHallPreferences.clear();
        for (String groupSize : DatabaseKeys.MatchPreferenceArray.GROUP_SIZES) {
            groupSizePreferences.add(true);
        }
        for (String diningHall : DatabaseKeys.MatchPreferenceArray.DINING_HALLS) {
            diningHallPreferences.add(true);
        }
    }

    public String groupSizePreferencesKey() {return "groupSizePreferences"; }
    public List<Boolean> getGroupSizePreferences() { return groupSizePreferences; }
    public Boolean getGroupSizePreference(int index) { return groupSizePreferences.get(index); }
    public void setGroupSizePreferences(List<Boolean> groupSizePreferences) {
        this.groupSizePreferences = groupSizePreferences;
    }
    public void setGroupSizePreferencesFromArray(boolean[] groupSizePreferences) {
        for (int i = 0; i < groupSizePreferences.length; ++i) {
            this.groupSizePreferences.set(i, groupSizePreferences[i]);
        }
    }
    public void setGroupSizePreferenceAt(int index, boolean preference) {
        this.groupSizePreferences.set(index, preference);
    }

    public String diningHallPreferencesKey() {return "diningHallPreferences"; }
    public List<Boolean> getDiningHallPreferences() { return diningHallPreferences; }
    public Boolean getDiningHallPreference(int index) { return groupSizePreferences.get(index); }
    public void setDiningHallPreferences(List<Boolean> diningHallPreference) {
        this.diningHallPreferences = diningHallPreference;
    }
    public void setDiningHallPreferencesFromArray(boolean[] diningHallPreferences) {
        for (int i = 0; i < diningHallPreferences.length; ++i) {
            this.diningHallPreferences.set(i, diningHallPreferences[i]);
        }
    }
    public void setDiningHallPreferenceAt(int index, boolean preference) {

        this.diningHallPreferences.set(index, preference);
    }

    // Public other methods ----------------------------------------------------------------------

    public boolean hasChosenAGroupSizePreference() {
        boolean hasChosen = false;
        for (Boolean pref : groupSizePreferences) {
            hasChosen |= pref;
        }
        return hasChosen;
    }

    public boolean hasChosenADiningHallPreference() {
        boolean hasChosen = false;
        for (Boolean pref : diningHallPreferences) {
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

        for (int i = 0; i < groupSizePreferences.size(); ++i) {
            boolean bothPrefer = groupSizePreferences.get(i) && other.groupSizePreferences.get(i);
            hasMatch |= bothPrefer;
        }
        if (!hasMatch) {
            return false;
        }
        for (int i = 0; i < diningHallPreferences.size(); ++i) {
            hasMatch |= diningHallPreferences.get(i) == other.diningHallPreferences.get(i);
        }
        return hasMatch;
    }

    /**
     * Changes preferences to only be true if the other also prefers the item
     * @param other
     */
    public void conformPreferences(MatchPreferences other) {
        for (int i = 0; i < this.groupSizePreferences.size(); ++i) {
            this.groupSizePreferences.set(i, other.groupSizePreferences.get(i));
        }
        for (int i = 0; i < diningHallPreferences.size(); ++i) {
            this.diningHallPreferences.set(i, other.diningHallPreferences.get(i));
        }
    }

    public void copy(MatchPreferences other) {
        this.groupSizePreferences = new ArrayList<>(other.groupSizePreferences);
        this.diningHallPreferences = new ArrayList<>(other.diningHallPreferences);
    }
}
