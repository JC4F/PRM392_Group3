package com.example.prm392_group3.models;

public class Comment {
    String id;


    String UserId;
    String UserName;


    String CommentContent;

    String CreatedTime;
    String NewsId;


    public Comment(String id, String userId, String commentContent, String createdTime, String userName, String newsId) {
        this.id = id;
        UserId = userId;
        CommentContent = commentContent;
        CreatedTime = createdTime;
        UserName = userName;
        NewsId = newsId;

    }

    public String getNewsId() {
        return NewsId;
    }

    public void setNewsId(String newsId) {
        NewsId = newsId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getCommentContent() {
        return CommentContent;
    }

    public void setCommentContent(String commentContent) {
        CommentContent = commentContent;
    }

    public String getCreatedTime() {
        return CreatedTime;
    }

    public void setCreatedTime(String createdTime) {
        CreatedTime = createdTime;
    }

    public Comment() {
    }

}
