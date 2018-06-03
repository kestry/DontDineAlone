package com.hu.tyler.dontdinealone.data.entity;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.ServerTimestamp;
import com.hu.tyler.dontdinealone.data.model.Collections;
import com.hu.tyler.dontdinealone.data.model.Documents;
import com.hu.tyler.dontdinealone.data.model.MatchPreferences;
import com.hu.tyler.dontdinealone.res.DatabaseStatuses;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by tyler on 5/15/2018.
 */

public class OnlineUser {

    private String documentId;
    private String chatId;
    private String newDoc; // TODO: @Tyler - could we get a comment on what this is?
    private String name;
    private String description;
    private String email;
    private String status;
    @ServerTimestamp
    private Date firstOnlineTime;
    @ServerTimestamp
    private Date firstQueuedTime;

    public OnlineUser() {
        //public no-arg constructor needed
        this.documentId = "defaultDocId"; // needs to be separately setup
        this.chatId = "0";
        this.newDoc = "0";
        this.name = "";
        this.email = "";
        this.description = "";
        this.status = DatabaseStatuses.User.ONLINE;
        this.firstOnlineTime = new Date();
        this.firstQueuedTime = new Date();
    }

    public OnlineUser(OnlineUser other) {
        copy(other);
    }

    public void copy(OnlineUser other) {
        documentId = other.documentId;
        chatId = other.chatId;
        newDoc = other.newDoc;
        name = other.name;
        description = other.description;
        email = other.email;
        status = other.status;
        firstOnlineTime = other.firstOnlineTime;
        firstQueuedTime = other.firstQueuedTime;
    }

    // Any function that starts with "get" will go into Firestore unless we exclude.

    public void setupOnlineUser(AuthUser authUser, User user) {
        documentId = authUser.getUid();
        chatId = "0";
        newDoc = "0";
        name = user.getDisplayName();
        email = authUser.getEmail();
        description = user.animal;
        status = DatabaseStatuses.User.ONLINE;
        Log.d("OnlineUser", "setupOnlineUser: documentId = " + documentId);
        Log.d("OnlineUser", "setupOnlineUser: email = " + email);

    }

    public String documentIdKey() {return "documentId"; }
    public String getDocumentId() {
        return this.documentId;
    }
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String chatIdKey() {return "chatId"; }
    public String getChatId() { return this.chatId; }
    public void setChatId(String chatId) {this.chatId = chatId; }

    public String newDocKey() {return "newDoc"; }
    public String getNewDoc() {
        return this.newDoc;
    }
    public void setNewDoc(String newDoc) {
        this.newDoc = newDoc;
    }

    //these names have to match the FireBase Parts
    public String nameKey() { return "name"; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String descriptionKey(){ return "description"; }
    public String getDescription(){ return description; }
    public void setDescription(String description) { this.description = description; }

    public String emailKey() {
        return "email";
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String statusKey() { return "status"; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }


    public String firstOnlineTimeKey() {
        return "firstOnlineTime";
    }
    public Date getFirstOnlineTime() {
        return firstOnlineTime;
    }
    // No setter because we only allow the server to timestamp

    public String firstQueuedTimeKey() {
        return "firstQueuedTime";
    }
    public Date getFirstQueuedTime() {
        return firstQueuedTime;
    }
    // No setter because we only allow the server to timestamp

    // Public Helpers ----------------------------------------------------------------------------

    public Map<String, Object> toMapWithoutTimestamp() {
        Map<String, Object> map = new HashMap<>();
        map.put(documentIdKey(), documentId);
        map.put(chatIdKey(), chatId);
        map.put(newDocKey(), newDoc);
        map.put(nameKey(), name);
        map.put(descriptionKey(), description);
        map.put(emailKey(), email);
        map.put(statusKey(), status);

        return map;
    }

    public Map<String, Object> toMapWithTimestamp() {
        Map<String, Object> map = toMapWithTimestamp();
        map.put(firstOnlineTimeKey(), FieldValue.serverTimestamp());

        return map;
    }

}
