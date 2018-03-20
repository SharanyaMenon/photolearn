package com.example.toshimishra.photolearn.Models;

import com.google.firebase.auth.FirebaseAuth;


public class User {
    private String userID;

    private String name;

    User() {
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    }

}
