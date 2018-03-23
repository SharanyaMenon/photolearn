package com.example.toshimishra.photolearn;

import com.example.toshimishra.photolearn.Models.QuizAnswer;
import com.example.toshimishra.photolearn.Models.QuizItem;
import com.example.toshimishra.photolearn.Models.QuizTitle;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by Administrator on 2018/3/21.
 */

public class ScoreTest {


    @Test
    public void calculateScore() throws Exception {
        ArrayList<QuizItem> questions = new ArrayList<>();
        ArrayList<QuizAnswer> answers = new ArrayList<>();
        HashMap<String, Integer> ans = new HashMap<>();
        questions.add(new QuizItem("t1", "i1", "url", "q1",
                "1", "2", "3", "4",
                2, "explain"));
        questions.add(new QuizItem("t2", "i2", "url", "q2",
                "1", "2", "3", "4",
                1, "explain"));
        questions.add(new QuizItem("t3", "i3", "url", "q3",
                "1", "2", "3", "4",
                3, "explain"));
        questions.add(new QuizItem("t4", "i4", "url", "q4",
                "1", "2", "3", "4",
                4, "explain"));
        questions.add(new QuizItem("t5", "i5", "url", "q5",
                "1", "2", "3", "4",
                1, "explain"));

        answers.add(new QuizAnswer(2));
        answers.add(new QuizAnswer(2));
        answers.add(new QuizAnswer(2));
        answers.add(new QuizAnswer(4));
        answers.add(new QuizAnswer(2));

        for (int i = 0; i < answers.size(); i++) {
            ans.put(questions.get(i).getItemID(), answers.get(i).getOptionSelcted());
        }

        assertEquals(2, QuizTitle.generateScore(questions, ans));
        assertFalse(3 == QuizTitle.generateScore(questions, ans));

    }
}
