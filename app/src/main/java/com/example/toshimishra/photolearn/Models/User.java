package com.example.toshimishra.photolearn.Models;

import com.google.firebase.auth.FirebaseAuth;


public class User {
    private String userID;

    private String name;

    User() {
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
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
