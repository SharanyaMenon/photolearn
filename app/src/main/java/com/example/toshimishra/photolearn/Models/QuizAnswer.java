package com.example.toshimishra.photolearn.Models;

/**
 * Created by toshimishra on 06/03/18.
 */

public class QuizAnswer {

    int optionSelcted;
    //todo timestamps

    public QuizAnswer(){

    }


    public int getOptionSelcted() {
        return optionSelcted;
    }

    public QuizAnswer(int optionSelcted){

        this.optionSelcted = optionSelcted;

    }
}
