package com.iss.ft03se.photolearn.Models;

public class LearningItem extends Item {

    private String userID;

    private String photoDesc;

    private String gps;

    public LearningItem() {
        super();
    }

    public LearningItem(String itemID, String titleID, String photoURL, String photoDesc, String GPS, String userID) {
        super(titleID, photoURL, itemID);
        this.photoDesc = photoDesc;
        this.gps = GPS;
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

    public String getPhotoDesc() {
        return photoDesc;
    }

    public String getGps() {
        return gps;
    }
}
