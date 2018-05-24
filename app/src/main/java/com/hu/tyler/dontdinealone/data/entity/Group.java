package com.hu.tyler.dontdinealone.data.entity;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.ServerTimestamp;
import com.hu.tyler.dontdinealone.res.DatabaseStatuses;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Group {

    private int gid;
    private String status;
    private MatchPreferences matchPreferences;
    private ArrayList<String> members;  // Holds the member docIds. We can also add them as keys
                                        // to see them all if we would like too

    @ServerTimestamp
    private Date firstCreatedTime;

    public Group(){
        //public no-arg constructor needed
        int gid = 1;
        this.status = DatabaseStatuses.User.online;
        this.firstCreatedTime = new Date();
        this.members = new ArrayList<String>();
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
    public ArrayList<String> getMembers() { return members; }
    public void setMembers(ArrayList<String> members) { this.members = members; }

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
}
