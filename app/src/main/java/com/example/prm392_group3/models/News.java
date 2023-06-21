package com.example.prm392_group3.models;

public class News {

    private Integer pid;
    private String postDate;
    private String title;
    private String url;
    private String postContent;
    private String source;
    private String sourceUrl;
    private String image;

    /**
     * No args constructor for use in serialization
     *
     */
    public News() {
    }

    /**
     *
     * @param sourceUrl
     * @param image
     * @param postContent
     * @param postDate
     * @param pid
     * @param source
     * @param title
     * @param url
     */
    public News(Integer pid, String postDate, String title, String url, String postContent, String source, String sourceUrl, String image) {
        super();
        this.pid = pid;
        this.postDate = postDate;
        this.title = title;
        this.url = url;
        this.postContent = postContent;
        this.source = source;
        this.sourceUrl = sourceUrl;
        this.image = image;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }




    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}