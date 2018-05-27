package com.hu.tyler.dontdinealone.res;

public interface DatabaseStatuses {

    interface User {
        String online = "online";
        String queued = "queued";
        String matched = "matched"; // Matched to a group. Would "waiting" be a better term?
        String waiting = "waiting"; // Grouped but waiting on other memebers.
        String confirming = "confirming"; // Confirming after group has been filled.
                                          // TODO: Do we still want confirmation? Or just match?
        String confirmed = "confirmed";
        String dining = "dining"; // Successfully part of a dining group with all confirmed members.
        // No offline status because we delete the Online users from remote DB and set the
        // local one to default (to erase personal information).
    }

    interface Group {
        String uninitialized = "uninitialized";

        // The following two statuses are considered "pending"
        String waiting = "waiting"; // Waiting for more members
        String confirming = "confirming";

        String confirmed = "confirmed";
    }
}
