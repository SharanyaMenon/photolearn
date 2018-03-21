package com.example.toshimishra.photolearn;

import com.example.toshimishra.photolearn.Models.LearningItem;
import com.example.toshimishra.photolearn.Models.LearningSession;
import com.example.toshimishra.photolearn.Models.QuizAnswer;
import com.example.toshimishra.photolearn.Models.QuizItem;
import com.example.toshimishra.photolearn.Models.QuizTitle;
import com.example.toshimishra.photolearn.Utilities.State;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void addLearningSession() throws Exception {
        Date date =  new Date(System.currentTimeMillis());//date
        LearningSession learningSession = new LearningSession("key1","user1",date, 12,"code" );
        System.out.print(learningSession.getSessionID());

    }
    @Test
    public void calculateScore() throws Exception{
        ArrayList<QuizItem> questions = new ArrayList<>();
        ArrayList<QuizAnswer> answers = new ArrayList<>();
        HashMap<String,Integer> ans = new HashMap<>();
        questions.add(new QuizItem("t1","i1","url","q1",
                "1","2","3","4",
        2,"explain"));
        questions.add(new QuizItem("t2","i2","url","q2",
                "1","2","3","4",
                1,"explain"));
        questions.add(new QuizItem("t3","i3","url","q3",
                "1","2","3","4",
                3,"explain"));
        questions.add(new QuizItem("t4","i4","url","q4",
                "1","2","3","4",
                4,"explain"));
        questions.add(new QuizItem("t5","i5","url","q5",
                "1","2","3","4",
                1,"explain"));

        answers.add(new QuizAnswer(2));
        answers.add(new QuizAnswer(2));
        answers.add(new QuizAnswer(2));
        answers.add(new QuizAnswer(4));
        answers.add(new QuizAnswer(2));

        for(int i =0;i<answers.size();i++){
            ans.put(questions.get(i).getItemID(),answers.get(i).getOptionSelcted());
        }

        assertEquals(2, QuizTitle.generateScore(questions,ans));
        assertFalse(3==QuizTitle.generateScore(questions,ans));

    }
}