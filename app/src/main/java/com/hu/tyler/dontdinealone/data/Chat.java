package com.hu.tyler.dontdinealone.data;

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
    private String time;
    public Chat(){
        //public no-arg constructor needed
    }

//
//    public Chat(String title, String message, int priority){
//        this.title = title;
//        this.message = message;
//        this.priority = priority;
//    }

    public Chat(String title, String message, int priority, String time){
        this.title = title;
        this.message = message;
        this.priority = priority;
        this.time = time;
    }

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time){
        this.time = time;
    }


    //these names have to match the FireBase Parts
    public String getTitle(){
        return title;}

    public void setMessage(String msg){
        this.message = msg;
        return;
    }

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
