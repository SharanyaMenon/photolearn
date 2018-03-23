package com.example.toshimishra.photolearn.Utilities;

import android.graphics.Bitmap;
import android.util.Log;

import org.apache.commons.collections4.map.ListOrderedMap;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by toshimishra on 22/03/18.
 */

public class Images {
    private static ListOrderedMap<String, Bitmap> images = new ListOrderedMap<>();
    public static boolean isImageCached(String photoURL){
        if(images.containsKey(photoURL))
            return true;
        else
            return false;
    }
    public static Bitmap getBitmapfromURL(String photoURL){
        return images.get(photoURL);
    }

    //Keep only 100 images

    public static void addImageToCache(String url,Bitmap bmap){
        if(images.size()==50)
        {
            images.remove(0);

        }
        images.put(url,bmap);

    }

}
