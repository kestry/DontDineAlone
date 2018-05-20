package com.hu.tyler.dontdinealone.data.entity;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.ServerTimestamp;
import com.hu.tyler.dontdinealone.res.DatabaseStatuses;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by tyler on 5/15/2018.
 */

public class OnlineUser {

    private String documentId;
    private String newDoc;
    private String name;
    private String description;
    private String email;
    private String status;
    @ServerTimestamp
    private Date birthTimestamp;
    @ServerTimestamp
    private Date lastCameOnline;

    public OnlineUser(){
        //public no-arg constructor needed
        this.name = "";
        this.email = "";
        this.description = "";
        this.status = DatabaseStatuses.User.online;
        this.birthTimestamp = new Date();
    }

    public String birthTimestampKey() {
        return "birthTimestamp";
    }
    public Date getBirthTimestamp() {
        return birthTimestamp;
    }
    // No setter becauase we only allow the server to timestamp

    public String lastCameOnlineKey() {
        return "lastCameOnline";
    }
    public Date getLastCameOnlinep() {
        return lastCameOnline;
    }
    // No setter becauase we only allow the server to timestamp

    public String emailKey() {
        return "email";
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getDocumentId() {
        return documentId;
    }
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    @Exclude
    public String getNewDoc() {
        return newDoc;
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

    public String statusKey() { return "status"; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();

        map.put(nameKey(), name);
        map.put(statusKey(), status);
        map.put(descriptionKey(), description);
        map.put(emailKey(), email);
        return map;
    };
}
