package com.hu.tyler.dontdinealone.res;

public interface DatabaseStatuses {

    public interface User {
        String online = "online";
        String queued = "queued";
        String matched = "matched"; // Matched to a group. Would "waiting" be a better term?
        String confirming = "confirming";
        String confirmed = "confirmed";
        String dining = "dining";
        String offline = "offline";
    }

    public interface Group {
        String waiting = "waiting"; // Waiting for more members
        String confirming = "confirming";
        String confirmed = "confirmed";
    }
}
