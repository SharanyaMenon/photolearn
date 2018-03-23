package com.iss.ft03se.photolearn.Utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUploadUtility {

    private static final String DIRECTORY = "/fromCamera";

    public String saveImage(Bitmap bitmap, Context context) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bytes);
        File directory = new File(Environment.getExternalStorageDirectory() + DIRECTORY);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        try {
            File file = new File(directory, System.currentTimeMillis() + ".jpg");
            file.createNewFile();
            FileOutputStream fo = new FileOutputStream(file);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(context,
                    new String[]{file.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("ImageUploadUtility", "saveImage| File is saved in location" + file.getAbsolutePath());

            return file.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    public Uri getUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String imagePath = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(imagePath);
    }


    public void selectImage(boolean isPermitted, Context context, final ImageCallback callback) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Select Action");

        String[] pictureDialogItems = {Constants.SELECT_PHOTO_FROM_GALLERY, Constants.CAPTURE_PHOTO_FROM_CAMERA};
        if (!isPermitted) {
            pictureDialogItems = new String[1];
            pictureDialogItems[0] = Constants.SELECT_PHOTO_FROM_GALLERY;
        }

        if (isPermitted) {
            pictureDialogItems = new String[2];
            pictureDialogItems[0] = Constants.SELECT_PHOTO_FROM_GALLERY;
            pictureDialogItems[1] = Constants.CAPTURE_PHOTO_FROM_CAMERA;
        }

        dialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int imageSource) {
                        switch (imageSource) {
                            case 0:
                                Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                callback.onImageCallback(galleryIntent, Constants.GALLERY);
                                break;
                            case 1:
                                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                                callback.onImageCallback(cameraIntent, Constants.CAMERA);
                                break;
                        }
                    }
                });
        dialog.show();
    }


}
