package com.iss.ft03se.photolearn.Models;


import java.util.Date;

public class QuizAnswer {

    private int optionSelcted;

    private Date timeStamp;

    private String quizItemId;

    private String userId;

    public QuizAnswer() {

    }

    public QuizAnswer(int optionSelcted,String quizItemId, String userId ) {
        this.optionSelcted = optionSelcted;
        this.quizItemId = quizItemId;
        this.userId = userId;
    }

    public int getOptionSelcted() {
        return optionSelcted;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getQuizItemId() {
        return quizItemId;
    }

    public String getUserId() {
        return userId;
    }

}
