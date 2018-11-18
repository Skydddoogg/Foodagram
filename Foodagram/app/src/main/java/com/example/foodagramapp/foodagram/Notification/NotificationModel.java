package com.example.foodagramapp.foodagram.Notification;

public class NotificationModel {

    private String content, from, postId, type;
    private Double viewed, timestamp;

    public NotificationModel(String content, String from, String postId, String type, Double viewed, Double timestamp){
        this.content = content;
        this.from = from;
        this.postId = postId;
        this.type = type;
        this.viewed = viewed;
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getViewed() {
        return viewed;
    }

    public void setViewed(Double viewed) {
        this.viewed = viewed;
    }

    public Double getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Double timestamp) {
        this.timestamp = timestamp;
    }
}
