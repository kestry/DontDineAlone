package com.hu.tyler.dontdinealone.data.entity;

public class GroupMeta {
    private int nextGid;
    private int groupCount;
    private int waitingGroupCount;

    GroupMeta() {
        nextGid = 0;
        groupCount = 0;
    }

    public int getNextGid() {
        return nextGid;
    }

    public int getGroupCount() {
        return groupCount;
    }

    public int getWaitingGroupCount() {
        return waitingGroupCount;
    }
}
