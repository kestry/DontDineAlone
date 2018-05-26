package com.hu.tyler.dontdinealone.res;

public interface DatabaseStatuses {

    interface User {
        String online = "online";
        String queued = "queued";
        String matched = "matched"; // Matched to a group. Would "waiting" be a better term?
        String confirming = "confirming";
        String confirmed = "confirmed";
        String dining = "dining";
        String offline = "offline";
    }

    interface Group {
        String uninitialized = "uninitialized";

        // The following two statuses are considered "pending"
        String waiting = "waiting"; // Waiting for more members
        String confirming = "confirming";

        String confirmed = "confirmed";
    }
}
