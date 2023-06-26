package com.example.prm392_group3.utils;

import com.example.prm392_group3.models.User;
import com.google.firebase.auth.FirebaseUser;

public class ShareUserData {
    private static ShareUserData instance;
    private User userDetail;
    private FirebaseUser userAuthenFB;

    private ShareUserData() {
        // Private constructor to prevent instantiation from outside
    }

    public static ShareUserData getInstance() {
        if (instance == null) {
            instance = new ShareUserData();
        }
        return instance;
    }

    public User getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(User value) {
        userDetail = value;
    }

    public FirebaseUser getUserAuthenFB() {
        return userAuthenFB;
    }

    public void setUserAuthenFB(FirebaseUser userAuthenFB) {
        this.userAuthenFB = userAuthenFB;
    }
}
