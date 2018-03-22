package com.example.toshimishra.photolearn.Models;


import java.io.Serializable;

public class Item implements Serializable {

    private String itemID;

    private String titleID;

    private String photoURL;

    public Item() {
    }

    public Item(String titleID, String photoURL, String itemID) {
        this.titleID = titleID;
        this.photoURL = photoURL;
        this.itemID = itemID;
    }

    public String getItemID() {
        return itemID;
    }

    public String getPhotoURL() {
        return photoURL;
    }
}
