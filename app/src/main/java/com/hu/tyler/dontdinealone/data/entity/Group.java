package com.hu.tyler.dontdinealone.data.entity;

import com.google.firebase.firestore.ServerTimestamp;
import com.hu.tyler.dontdinealone.data.model.MatchPreferences;
import com.hu.tyler.dontdinealone.res.DatabaseKeys;
import com.hu.tyler.dontdinealone.res.DatabaseStatuses;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Boolean.FALSE;

public class Group {

    private int gid;
    private String status;
    private MatchPreferences matchPreferences;
    private int minCapacity;
    private Map<String, Boolean> members;
        // key: OnlineUser id/docId
        // value: hasConfirmed

    @ServerTimestamp
    private Date firstCreatedTime;

    public Group(){
        //public no-arg constructor needed
        this.gid = 0;
        this.status = DatabaseStatuses.Group.uninitialized;
        this.matchPreferences = null;
        this.minCapacity = 0;
        this.members = null;
        this.firstCreatedTime = null;
    }

    public Group(OnlineUser user, MatchPreferences matchPreferences, int gid){
        //public no-arg constructor needed
        this.gid = gid;
        this.status = DatabaseStatuses.Group.waiting;
        this.matchPreferences = new MatchPreferences(matchPreferences);
        // Parse match preference to update minimum group capacity.
        this.updateMinCapacity();
        this.members = new HashMap<>();
        this.members.put(user.getDocumentId(), FALSE);
        this.firstCreatedTime = new Date();
    }

    // Any function that starts with "get" will go into Firestore unless we exclude.

    public String gidKey() { return "gid"; }
    public int getGid() { return gid; }
    public void setGid(int gid) { this.gid = gid; }

    public String statusKey() { return "status"; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String matchPreferencesKey() { return "matchPreferences"; }
    public MatchPreferences getMatchPreferences() { return matchPreferences; }
    public void setMatchPreferences(String status) { this.matchPreferences = matchPreferences; }

    public String membersKey() { return "members"; }
    public Map<String, Boolean> getMembers() { return members; }
    public void setMembers(Map members) { this.members = members; }

    public String firstOnlineTimeKey() {
        return "firstOnlineTime";
    }
    public Date getFirstOnlineTime() {
        return firstCreatedTime;
    }
    // No setter because we only allow the server to timestamp

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();

        map.put(gidKey(), gid);
        map.put(statusKey(), status);
        map.put(matchPreferencesKey(), matchPreferences);
        map.put(membersKey(), members);
        return map;
    };

    public void addMember(String member) {
        members.put(member, FALSE);
    }

    public void removeMember(String member) {
        members.remove(member);
    }

    public void updateMinCapacity() {
        boolean[] groupSizePrefs = matchPreferences.getGroupSizePreferences();
        for (int i = 0; i < groupSizePrefs.length; ++i) {
            if (groupSizePrefs[i]) {
                minCapacity = Integer.parseInt(DatabaseKeys.Preference.GROUP_SIZES[i]);
                return; // Reason: we found the min size so we are done.
            }
        }
    }

    public boolean isFull() {
        return minCapacity <= members.size();
    }

    public boolean isAlmostFull() {
        return minCapacity <= members.size() - 1;
    }
}
