package com.example.prm392_group3.models;

import java.util.Date;

public class News {

    private String pid;
    private Date postDate;
    private String title;
    private String hashtag;
    private String postContent;
    private String source;
    private String category;
    private String image;
    private String CreatedBy;


    /**
     * No args constructor for use in serialization
     *
     */
    public News() {
    }

    /**
     *
     * @param category
     * @param image
     * @param postContent
     * @param postDate
     * @param pid
     * @param source
     * @param title
     * @param hashtag
     */
    public News(String pid, Date postDate, String title, String hashtag, String postContent, String source, String category, String image, String CreatedBy) {
        super();
        this.pid = pid;
        this.postDate = postDate;
        this.title = title;
        this.hashtag = hashtag;
        this.postContent = postContent;
        this.source = source;
        this.category = category;
        this.image = image;
        this.CreatedBy = CreatedBy;

    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        this.CreatedBy = createdBy;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHashtag() {
        return "#"+hashtag.toUpperCase();
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getCategory() {
        return source;
    }

    public void setCategory(String source) {
        this.source = source;
    }

    public String getCategoryHashtag() {
        return category;
    }

    public void setCategoryHashtag(String category) {
        this.category = category;
    }




    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
