package com.example.toshimishra.photolearn.Models;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by toshimishra on 18/03/18.
 */

public class User {
    private String userID;
    private String name;

    User(){
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    }

}
