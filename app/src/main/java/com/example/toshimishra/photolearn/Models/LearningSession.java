package com.example.toshimishra.photolearn.Models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;


public class LearningSession implements Serializable {

    private String sessionID;

    private String userID;

    private Date courseDate;

    private String courseCode;

    private Integer moduleNumber;

    public String getSessionKey() {
        return sessionKey;
    }

    private String sessionKey;

    public LearningSession() {

    }

    public LearningSession(String sessionKey, String userID, Date date, Integer moduleNumber, String courseCode) {
        this.sessionID = formatDate(date) + "-" + formatCourseCode(courseCode) + "-M" + formatModuleNum(moduleNumber);
        this.userID = userID;
        this.courseDate = date;
        this.courseCode = courseCode;
        this.moduleNumber = moduleNumber;
        this.sessionKey = sessionKey;
    }

    public String getUserID() {
        return userID;
    }

    public Date getCourseDate() {
        return courseDate;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public Integer getModuleNumber() {
        return moduleNumber;
    }

    public String getSessionID() {
        return sessionID;
    }

    public String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(date).toString();
    }

    private String formatCourseCode(String courseCode) {

        String words[] = courseCode.split(" ");
        String formattedCourseName = "";
        if (words.length < 3) {
            if (words.length < 2) {
                formattedCourseName = words[0];
            } else {
                formattedCourseName = words[0].substring(0, 1) + words[1].substring(0, 1);
            }

        } else {
            formattedCourseName = words[0].substring(0, 1) + words[1].substring(0, 1) + words[2].substring(0, 1);
        }
        return formattedCourseName;
    }

    private String formatModuleNum(Integer moduleNumber) {
        String formattedModuleNumber = moduleNumber.toString();
        if (moduleNumber.toString().length() < 2) {
            formattedModuleNumber = "0" + moduleNumber;
        }
        return formattedModuleNumber;
    }
}
