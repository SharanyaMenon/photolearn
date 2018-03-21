package com.example.toshimishra.photolearn.Models;


public class Item {

    private String itemID;

    private String titleID;

    private String photoURL;

    public Item() {
    }

    public Item(String titleID, String photoURL, String itemID) {
        this.titleID = titleID;
        this.photoURL = photoURL;
        this.itemID = itemID;

        //TODO update ID
    }
    public void update(String photoURL){
        this.photoURL = photoURL;
    }


    public String getItemID() {
        return itemID;
    }

    public String getPhotoURL() {
        return photoURL;
    }
}
