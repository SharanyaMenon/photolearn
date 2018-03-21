package com.example.toshimishra.photolearn.Models;

import com.example.toshimishra.photolearn.Models.QuizAnswer;
import com.example.toshimishra.photolearn.Models.QuizItem;
import com.example.toshimishra.photolearn.Models.QuizTitle;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by Administrator on 2018/3/21.
 */

public class QuizTitleTest{
    QuizTitle qt1,qt2;
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    @Before
    public void setUP() throws Exception{
        qt1 = new QuizTitle("qt1","user1","quiztitle1","sess1");
    }
    @Test
    public void TestQuizTitle(){
        assertEquals(qt1.getSessionID(),"sess1");
    }
    @Test
    public void TestgetTitleID() {
       assertEquals(qt1.getTitleID(),"qt1");
    }
    @Test
    public void  TestgetTitle() {

        assertEquals(qt1.getTitle(),"quiztitle1");
    }
    @Test
    public void  TestgetSessionID() {
        assertEquals(qt1.getSessionID(),"sess1");
    }
}
