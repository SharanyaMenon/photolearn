package com.example.toshimishra.photolearn.Models;

import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Created by Administrator on 2018/3/21.
 */
public class LearningSessionTest {
    private LearningSession ls1;
    private LearningSession ls2;
    private LearningSession ls3;
    private Date date;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    String expecteddateAsDate;

    @Before
    public void setUP() {
        date = new Date(System.currentTimeMillis());
        ls1 = new LearningSession("key", "id", date, 1, "Basic Mechanical Engineering");
        ls2 = new LearningSession("key", "id", date, 20, "History");
        ls3 = new LearningSession("key", "id", date, 20, "Basic Mechanics");
        expecteddateAsDate = dateFormat.format(date);
    }

    @Test
    public void getSessionKey() throws Exception {
        assertEquals("key", ls1.getSessionKey());
    }


    @Test
    public void getUserID() throws Exception {
        assertEquals("id", ls1.getUserID());
    }

    @Test
    public void getCourseDate() throws Exception {
        assertEquals(date, ls1.getCourseDate());

    }

    @Test
    public void getCourseCode() throws Exception {
        assertEquals("Basic Mechanical Engineering", ls1.getCourseCode());
    }

    @Test
    public void testformatDate() {
        assertEquals(dateFormat.format(date), ls1.formatDate(date));
    }

    @Test
    public void testSessionId1() {
        String expectedSessionId = expecteddateAsDate + "-BME-M01";
        assertEquals(expectedSessionId, ls1.getSessionID());
    }

    @Test
    public void testSessionId2() {
        assertEquals(expecteddateAsDate, ls2.formatDate(date));
        String expectedSessionId = dateFormat.format(date) + "-History-M20";
        assertEquals(expectedSessionId, ls2.getSessionID());
    }

    @Test
    public void testSessionId3() {
        assertEquals(expecteddateAsDate, ls3.formatDate(date));
        String expectedSessionId = dateFormat.format(date) + "-BM-M20";
        assertEquals(expectedSessionId, ls3.getSessionID());
    }

}