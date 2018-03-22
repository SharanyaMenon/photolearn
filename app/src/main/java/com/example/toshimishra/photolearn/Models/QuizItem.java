package com.example.toshimishra.photolearn.Models;


import java.io.Serializable;

public class QuizItem extends Item implements Serializable {

    private String question;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private int answer;
    private String ansExp; //For explanation of the option

    public QuizItem() {
        super();
    }

    public QuizItem(String titleID, String itemID, String photoURL, String question,
                    String option1, String option2, String option3, String option4,
                    int answer, String ansExp) {
        super(titleID, photoURL, itemID);

        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;

        this.answer = answer;
        this.ansExp = ansExp;
        this.question = question;

    }

    public String getQuestion() {
        return question;
    }

    public String getOption1() {
        return option1;
    }

    public String getOption2() {
        return option2;
    }

    public String getOption3() {
        return option3;
    }

    public String getOption4() {
        return option4;
    }

    public int getAnswer() {
        return answer;
    }

    public String getAnsExp() {
        return ansExp;
    }


}