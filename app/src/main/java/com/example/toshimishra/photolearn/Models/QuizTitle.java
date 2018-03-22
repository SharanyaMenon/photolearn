package com.example.toshimishra.photolearn.Models;


import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class QuizTitle extends Title implements Serializable {
    public QuizTitle() {
        super();
    }

    public QuizTitle(String titleID, String userID, String title, String sessionID) {
        super(titleID, userID, title, sessionID);
    }

    public static int generateScore(List<QuizItem> quizItemList, HashMap<String, Integer> answers) {
        int score = 0;
        for (int i = 0; i < answers.size(); i++) {
            QuizItem quiz = quizItemList.get(i);
            if (answers.get(quiz.getItemID()) == quiz.getAnswer())
                score++;
        }
        return score;
    }

}
