package com.example.prm392_group3.activities.orders.orders;

public class Order {
    private String resourceID;
    private String bookID;
    private String BikeID;
    private String BookingStatus;
    private int userID;
    private String bikeName;
    private String startDate;
    private String endDate;
    private int totalPrice;

    public Order() {

    }

    public Order(String resourceID, String bookID, String bikeID, String bookingStatus, int userID, String bikeName, String startDate, String endDate, int totalPrice) {
        this.resourceID = resourceID;
        this.bookID = bookID;
        BikeID = bikeID;
        BookingStatus = bookingStatus;
        this.userID = userID;
        this.bikeName = bikeName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPrice = totalPrice;
    }

    public String getResourceID() {
        return resourceID;
    }

    public void setResourceID(String resourceID) {
        this.resourceID = resourceID;
    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public String getBikeID() {
        return BikeID;
    }

    public void setBikeID(String bikeID) {
        BikeID = bikeID;
    }

    public String getBookingStatus() {
        return BookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        BookingStatus = bookingStatus;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getBikeName() {
        return bikeName;
    }

    public void setBikeName(String bikeName) {
        this.bikeName = bikeName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
