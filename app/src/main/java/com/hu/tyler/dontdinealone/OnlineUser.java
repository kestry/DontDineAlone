package com.hu.tyler.dontdinealone;

import com.google.firebase.firestore.Exclude;


/**
 * Created by tyler on 5/15/2018.
 */

public class OnlineUser {

    private String documentId;
    private String newDoc;
    private String name;
    private String description;
    private String email;
    private int status = 0;
    private String time = "0";
    public OnlineUser(){
        //public no-arg constructor needed
    }


    public OnlineUser(String email, String name, String description, int status){
        this.name = name;
        this.email = email;
        this.description = description;
        this.status = status;
    }

    public OnlineUser(String email, String name, String description, int status, String time){
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
    public String getname(){
        return name;}

    public String getDescription(){
        return description;
    }

    public int getstatus() {
        return status;
    }

    public void setstatus(int status) {
        this.status = status;
    }
}
