package com.iss.ft03se.photolearn.Models;

import java.io.Serializable;
import java.util.Date;


public abstract class Title implements Serializable {

    private String userID;

    private String title;

    private String sessionID;

    private String titleID;

    private Date timeStamp;

    public String getTitleID() {
        return titleID;
    }

    public String getUserID() {
        return userID;
    }

    public String getTitle() {
        return title;
    }

    public String getSessionID() {
        return sessionID;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }


    public Title() {

    }

    public Title(String titleID, String userID, String title, String sessionID) {
        this.userID = userID;
        this.title = title;
        this.sessionID = sessionID;
        this.titleID = titleID;
        this.timeStamp = new Date(System.currentTimeMillis());
    }


}
