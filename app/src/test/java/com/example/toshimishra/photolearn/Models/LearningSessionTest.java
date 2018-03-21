package com.example.toshimishra.photolearn.Models;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2018/3/21.
 */
public class LearningSessionTest {
    private LearningSession ls1;
    Date date;
    @Before
    public void setUP(){
        date = new Date(System.currentTimeMillis());
        ls1=new LearningSession("key","id",date,1,"2");

    }
    @Test
    public void getSessionKey() throws Exception {
        assertEquals("key",ls1.getSessionKey());
    }



    @Test
    public void getUserID() throws Exception {
        assertEquals("id",ls1.getUserID());
    }

    @Test
    public void getCourseDate() throws Exception {
        assertEquals(date,ls1.getCourseDate());

    }

    @Test
    public void getCourseCode() throws Exception {
        assertEquals("2",ls1.getCourseCode());
    }



}