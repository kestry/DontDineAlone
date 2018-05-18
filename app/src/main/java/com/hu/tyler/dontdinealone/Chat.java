package com.hu.tyler.dontdinealone;

import com.google.firebase.firestore.Exclude;

/**
 * Created by tyler on 5/17/2018.
 */

/**
 * Created by tyler on 5/14/2018.
 */

public class Chat {
    private String documentId;
    private String title;
    private String message;
    private int priority;
    public Chat(){
        //public no-arg constructor needed
    }


    public Chat(String title, String message, int priority){
        this.title = title;
        this.message = message;
        this.priority = priority;
    }


    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }


    //these names have to match the FireBase Parts
    public String getTitle(){
        return title;}

    public String getMessage(){
        return message;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
