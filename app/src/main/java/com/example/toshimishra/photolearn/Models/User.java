package com.example.toshimishra.photolearn.Models;

import com.google.firebase.auth.FirebaseAuth;


public class User {
    private String userID;

    private String name;

    User() {
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    }

    User(String userId, String name) {
        this.userID = userId;
        this.name = name;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
