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

}