package com.example.prm392_group3.models;

public class Account {
    private int accountID;
    private String email;
    private String password;
    private String role;

    private int avatar;

    private String phone;


    public Account() {
    }
    public Account(int avatar, String email, String role, String Phone) {
        this.avatar = avatar;
        this.email = email;
        this.role = role;
        this.phone = phone;
    }


    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
