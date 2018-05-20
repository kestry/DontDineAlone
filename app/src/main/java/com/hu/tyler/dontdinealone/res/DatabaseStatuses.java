package com.hu.tyler.dontdinealone.res;

public interface DatabaseStatuses {

    public interface User {
        String online = "online";
        String queued = "queued";
        String matched = "matched";
        String confirming = "confirming";
        String confirmed = "confirmed";
        String dining = "dining";
        String offline = "offline";
    }

    public interface Group {
        String waiting = "waiting";
        String confirming = "confirming";
        String confirmed = "confirmed";
    }
}
