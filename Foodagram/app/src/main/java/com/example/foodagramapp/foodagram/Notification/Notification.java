package com.example.foodagramapp.foodagram.Notification;

import android.support.annotation.NonNull;

public class Notification implements Comparable<Notification>{

    private String postId;
    private String from;
    private String type;
    private String content;
    private long timestamp;
    private double viewed;
    private String pathImg;
    private String emailHash;

    public Notification(){

    }

    @Override
    public int compareTo(@NonNull Notification notification) {
        return 0;
    }

    public Notification(String postId, String from, String type, String content, long timestamp, double viewed, String pathImg, String emailHash){
        this.postId = postId;
        this.from = from;
        this.type = type;
        this.content = content;
        this.timestamp = timestamp;
        this.viewed = viewed;
        this.pathImg = pathImg;
        this.emailHash = emailHash;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getViewed() {
        return viewed;
    }

    public void setViewed(int viewed) {
        this.viewed = viewed;
    }

    public String getPathImg() {
        return pathImg;
    }

    public void setPathImg(String pathImg){
        this.pathImg = pathImg;
    }

    public String getEmailHash(){
        return emailHash;
    }

    public void setEmailHash(String emailHash) {
        this.emailHash = emailHash;
    }
}
