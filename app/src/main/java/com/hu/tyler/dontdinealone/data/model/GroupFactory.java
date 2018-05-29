package com.hu.tyler.dontdinealone.data.model;

import com.hu.tyler.dontdinealone.data.entity.Group;
import com.hu.tyler.dontdinealone.data.entity.OnlineUser;

import java.util.ArrayList;
import java.util.List;

public class GroupFactory {
    private String lastGid;
    //private Integer groupCount; // We don't need counts yet, but maybe in the future.
    //private Integer waitingGroupCount;
    private List<String> pendingGroups; // list of documentIds of waiting or confirming groups

    public GroupFactory() {
        lastGid = "0"; // Increment to get next gid.
        //groupCount = 0;
        //waitingGroupCount = 0;
    }

    public GroupFactory(GroupFactory groupFactory) {
        lastGid = groupFactory.lastGid;
        //groupCount = groupFactory.groupCount;
        //waitingGroupCount = groupFactory.waitingGroupCount;
        pendingGroups = new ArrayList<>();
    }

    // Public Methods ----------------------------------------------------------------------------

    public Group makeGroup(OnlineUser user, MatchPreferences matchPreferences) {
        String newGid = grabNextGid();
        pendingGroups.add(newGid);
        return new Group(user, matchPreferences, newGid);
    }

    // NOTE: Methods for Firestore use only! -----------------------------------------------------
    /**
     * Returns the lastGid that was assigned.
     * @return lastGid
     */
    public String getLastGid() { return lastGid; }

    /**
     * For copying values from database. Do not use this to develop app. Use constructor or
     * grabNextGid().
     */
    public void setLastGid(String lastGid) { this.lastGid = lastGid; }

    //public int getGroupCount() { return groupCount; }
    //public void setGroupCount(int groupCount) { groupCount = groupCount; }

    //public int getWaitingGroupCount() { return waitingGroupCount; }
    //public void setWaitingGroupCount(int waitingGroupCount) {
    //    waitingGroupCount = waitingGroupCount;
    //}

    public List<String> getPendingGroups() { return pendingGroups; }
    public void setPendingGroups(List<String> pendingGroups) {
        this.pendingGroups = pendingGroups; }

    // Private Helpers ---------------------------------------------------------------------------

    /**
     * Returns the nextGid to use. Private helper for makeGroup() function only.
     * @return nextGid
     */
    private String grabNextGid() {
        int lastGidInt = Integer.getInteger(lastGid);
        lastGid = Integer.toString(++lastGidInt);
        return lastGid;
    }


}
