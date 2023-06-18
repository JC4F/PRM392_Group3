package com.example.prm392_group3.models;

public class Blog {
    int id;

    int accountId;

    String content;

    String blogTitle;

    String blogDescription;

    String image;

    String createdAt;

    String updatedAt;

    boolean status;

    boolean isRead;

    public Blog() {
    }

    public Blog(int id, int accountId, String content, String blogTitle, String blogDescription, String image, String createdAt, String updatedAt, boolean status, boolean isRead) {
        this.id = id;
        this.accountId = accountId;
        this.content = content;
        this.blogTitle = blogTitle;
        this.blogDescription = blogDescription;
        this.image = image;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
        this.isRead = isRead;
    }

    public boolean isIsRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getBlogDescription() {
        return blogDescription;
    }

    public void setBlogDescription(String blogDescription) {
        this.blogDescription = blogDescription;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBlogTitle() {
        return blogTitle;
    }

    public void setBlogTitle(String blogTitle) {
        this.blogTitle = blogTitle;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
