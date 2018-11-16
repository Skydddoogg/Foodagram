package com.example.foodagramapp.foodagram.Comment;

public class Comment {

    private String user_id;
    private String content;
    private Double timestamp;

    public Comment() {

    }

    public Comment(String user_id, String content, Double timestamp) {
        this.user_id = user_id;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Double getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Double timestamp) {
        this.timestamp = timestamp;
    }
}