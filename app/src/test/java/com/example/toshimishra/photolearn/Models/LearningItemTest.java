package com.example.toshimishra.photolearn.Models;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Administrator on 2018/3/21.
 */
public class LearningItemTest {
    private LearningItem li;

    @Before
    public void setUP() {
        li = new LearningItem("li1", "t1", "URL", "photo", "GPS", "uid");
    }

    @Test
    public void getUserID() throws Exception {
        assertEquals("uid", li.getUserID());
    }


    @Test
    public void getPhotoDesc() throws Exception {
        assertEquals("photo", li.getPhotoDesc());
    }

    @Test
    public void getGps() throws Exception {
        assertEquals("GPS", li.getGps());
    }

}