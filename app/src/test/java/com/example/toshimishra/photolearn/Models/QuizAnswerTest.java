package com.example.toshimishra.photolearn.Models;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2018/3/21.
 */
public class QuizAnswerTest {
    private QuizAnswer qa;

    @Before
    public void setUP() {

        qa=new QuizAnswer(1);
    }

    @Test
    public void getOptionSelcted() throws Exception {
        assertEquals(qa.getOptionSelcted(),1);
    }




}