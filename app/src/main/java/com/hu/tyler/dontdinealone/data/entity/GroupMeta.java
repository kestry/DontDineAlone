package com.hu.tyler.dontdinealone.data.entity;

public class GroupMeta {
    private Integer lastGid;
    private Integer groupCount;
    private Integer waitingGroupCount;

    GroupMeta() {
        lastGid = 0; // Increment to get next gid.
        groupCount = 0;
        waitingGroupCount = 0;
    }

    GroupMeta(GroupMeta groupMeta) {
        lastGid = groupMeta.lastGid;
        groupCount = groupMeta.groupCount;
        waitingGroupCount = groupMeta.waitingGroupCount;
    }

    /**
     * Returns the lastGid that was assigned.
     * @return lastGid
     */
    public int getLastGid() { return lastGid; }

    /**
     * For copying values from database. Do not use this to develop app. Use constructor or
     * grabNextGid().
     */
    public void setLastGid(int lastGid) { lastGid = lastGid; }

    /**
     * Returns the nextGid to use.
     * @return nextGid
     */
    public int grabNextGid() { return ++lastGid; }

    public int getGroupCount() { return groupCount; }
    public void setGroupCount(int groupCount) { groupCount = groupCount; }

    public int getWaitingGroupCount() { return waitingGroupCount; }
    public void setWaitingGroupCount(int waitingGroupCount) {
        waitingGroupCount = waitingGroupCount;
    }

}
