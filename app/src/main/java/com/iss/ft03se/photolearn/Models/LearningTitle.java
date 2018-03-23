package com.iss.ft03se.photolearn.Models;

import java.io.Serializable;

public class LearningTitle extends Title implements Serializable {

    public LearningTitle() {
        super();
    }

    public LearningTitle(String titleID, String userID, String title, String sessionID) {
        super(titleID, userID, title, sessionID);
    }

}
