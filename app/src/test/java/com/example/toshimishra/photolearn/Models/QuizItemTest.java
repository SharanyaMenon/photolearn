package com.example.toshimishra.photolearn.Models;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Administrator on 2018/3/21.
 */
public class QuizItemTest {
    private QuizItem qi1;

    @Before
    public void setUP() {
        qi1 = new QuizItem("qt1", "qi1", "URL", "Where?", "a", "b", "c", "d", 2, "explain");
    }

    @Test
    public void getQuestion() throws Exception {
        assertEquals("Where?", qi1.getQuestion());
    }

    @Test
    public void getOption1() throws Exception {
        assertEquals("a", qi1.getOption1());
    }

    @Test
    public void getOption2() throws Exception {
        assertEquals("b", qi1.getOption2());
    }

    @Test
    public void getOption3() throws Exception {
        assertEquals("c", qi1.getOption3());
    }

    @Test
    public void getOption4() throws Exception {
        assertEquals("d", qi1.getOption4());
    }

    @Test
    public void getAnswer() throws Exception {
        assertEquals(2, qi1.getAnswer());
    }

    @Test
    public void getAnsExp() throws Exception {
        assertEquals("explain", qi1.getAnsExp());
    }

}