package com.hu.tyler.dontdinealone;

import com.google.firebase.firestore.Exclude;
import com.hu.tyler.dontdinealone.res.DatabaseStatuses;


/**
 * Created by tyler on 5/15/2018.
 */

public class OnlineUser {

    private String documentId;
    private String newDoc;
    private String name;
    private String description;
    private String email;
    private String status = DatabaseStatuses.OnlineUser.online;
    private String time = "0";
    public OnlineUser(){
        //public no-arg constructor needed
    }


    public OnlineUser(String email, String name, String description, String status){
        this.name = name;
        this.email = email;
        this.description = description;
        this.status = status;
    }

    public OnlineUser(String email, String name, String description, String status, String time){
        this.name = name;
        this.email = email;
        this.description = description;
        this.status = status;
        this.time = time;
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
    public String getName(){
        return name;}

    public String getDescription(){
        return description;
    }

    public String getStatus() {
        return status;
    }

    public void setstatus(String status) {
        this.status = status;
    }
}
