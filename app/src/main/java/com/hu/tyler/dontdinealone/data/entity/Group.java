package com.hu.tyler.dontdinealone.data.entity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.ServerTimestamp;
import com.hu.tyler.dontdinealone.data.model.Collections;
import com.hu.tyler.dontdinealone.data.model.Documents;
import com.hu.tyler.dontdinealone.data.model.MatchPreferences;
import com.hu.tyler.dontdinealone.res.DatabaseKeys;
import com.hu.tyler.dontdinealone.res.DatabaseStatuses;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Boolean.FALSE;

public class Group {

    private String gid;
    private String status;
    //private DocumentReference matchPreferencesDocRef;
    private int minCapacity;
    private Map<String, Boolean> members;
        // key: OnlineUser id/docId
        // value: hasConfirmed

    @ServerTimestamp
    private Date firstCreatedTime;

    // This constructor should be for Firestore only.
    public Group(){
        //public no-arg constructor needed
        this.gid = "0";
        this.status = DatabaseStatuses.Group.uninitialized;
        //this.matchPreferencesDocRef = null; // needs to be setup
        this.minCapacity = 0;
        this.members = null;
        this.firstCreatedTime = null;
    }

    public Group(OnlineUser user, MatchPreferences userMatchPreferences, String gid){
        //public no-arg constructor needed
        this.gid = gid;
        this.status = DatabaseStatuses.Group.waiting;
        //this.matchPreferencesDocRef = Documents.getInstance().getGroupMatchPreferencesDocRef(gid);
        // Parse match preference to update minimum group capacity.
        this.updateMinCapacity(userMatchPreferences);
        this.members = new HashMap<>();
        this.members.put(user.getDocumentId(), FALSE);
        this.firstCreatedTime = new Date();
    }

    // Any function that starts with "get" will go into Firestore unless we exclude.

    //public DocumentReference getMatchPreferencesDocRef() {
    //    return matchPreferencesDocRef;
    //}

    public String gidKey() { return "gid"; }
    public String getGid() { return gid; }
    public void setGid(String gid) { this.gid = gid; }

    public String statusKey() { return "status"; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String membersKey() { return "members"; }
    public Map<String, Boolean> getMembers() { return members; }
    public void setMembers(Map<String, Boolean> members) { this.members = members; }

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
        map.put(membersKey(), members);
        return map;
    }

    public void addMember(String member) {
        members.put(member, FALSE);
    }

    public void removeMember(String member) {
        members.remove(member);
    }

    public boolean isFull() {
        return minCapacity <= members.size();
    }

    public boolean isAlmostFull() {
        return minCapacity <= members.size() - 1;
    }

    // Parse match preference to update minimum group capacity.
    public void updateMinCapacity(MatchPreferences matchPreferences) {
        List<Boolean> groupSizePrefs = matchPreferences.getGroupSizePreferences();
        for (int i = 0; i < groupSizePrefs.size(); ++i) {
            if (groupSizePrefs.get(i)) {
                int minCapacity = Integer.parseInt(DatabaseKeys.Preference.GROUP_SIZES[i]);
                return; // Reason: we found the min size so we are done.
            }
        }
    }
}
