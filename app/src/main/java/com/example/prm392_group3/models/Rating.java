package com.example.prm392_group3.models;

public class Rating {
    private String ratingId, userId, bikeId, description, date;
    private float rating;
    private User user;

    public Rating() {
    }

    public Rating(String ratingId, String userId, String bikeId, String description, String date, float rating) {
        this.ratingId = ratingId;
        this.userId = userId;
        this.bikeId = bikeId;
        this.description = description;
        this.date = date;
        this.rating = rating;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRatingId() {
        return ratingId;
    }

    public void setRatingId(String ratingId) {
        this.ratingId = ratingId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBikeId() {
        return bikeId;
    }

    public void setBikeId(String bikeId) {
        this.bikeId = bikeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
