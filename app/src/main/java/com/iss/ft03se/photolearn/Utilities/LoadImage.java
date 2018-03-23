package com.iss.ft03se.photolearn.Utilities;

/**
 * Created by toshimishra on 16/03/18.
 */


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class LoadImage extends AsyncTask<String, Void, Bitmap> {

    private int width;
    private int height;
    private ImageCallBack callBack;

    public LoadImage(ImageCallBack callBack,int width,int height) {
        this.width = width;
        this.height = height;
        this.callBack = callBack;
    }

    public interface ImageCallBack{

        void onImageLoad(Bitmap bitmap);

    }


    @Override
    protected Bitmap doInBackground(String... args) {

        try {
            return Bitmap.createScaledBitmap(BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent()),width,height,true);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {

        if (bitmap != null) {

            callBack.onImageLoad(bitmap);

        } else {

            // error
        }
    }
}