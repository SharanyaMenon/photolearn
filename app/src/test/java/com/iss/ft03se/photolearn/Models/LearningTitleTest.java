package com.iss.ft03se.photolearn.Models;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Administrator on 2018/3/21.
 */
public class LearningTitleTest {
    LearningTitle lt1;

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Before
    public void setUP() throws Exception {
        lt1 = new LearningTitle("qt1", "user1", "quiztitle1", "sess1");
    }

    @Test
    public void TestQuizTitle() {
        assertEquals(lt1.getSessionID(), "sess1");
    }

}