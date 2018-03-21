package com.example.toshimishra.photolearn.Models;


import java.util.Date;

public class QuizAnswer {

    private int optionSelcted;

    private Date timeStamp;

    public QuizAnswer() {

    }

    public QuizAnswer(int optionSelcted) {
        this.optionSelcted = optionSelcted;
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
}
