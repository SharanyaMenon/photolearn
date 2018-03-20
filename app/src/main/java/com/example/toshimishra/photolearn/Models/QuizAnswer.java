package com.example.toshimishra.photolearn.Models;


import java.util.Date;

public class QuizAnswer {

    private int optionSelcted;

    private Date timeStamp;


//todo timestamps

    public QuizAnswer() {

    }


    public int getOptionSelcted() {
        return optionSelcted;
    }

    public QuizAnswer(int optionSelcted) {

        this.optionSelcted = optionSelcted;

    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}
